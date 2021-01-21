package objective;

import core.*;

import java.util.List;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Date;
import java.text.*;
/**
 * Assignment:
 * @author BD837672
 * @version 20th January 2021
 * Calculates the line of sight total airmiles for each passenger and finds the passenger with the highest airmiles.
 * A multi-threaded solution which creates a mapper for the input file and a combiner for totalling airmiles for each passenger and a reducer to ,
 * select the passenger with the highest airmiles. Also error checks and corrects file input.
 *
 * To run:
 * java Task_4A.java <files>
 *     i.e. java Task_4A.java Top30_airports_LatLong.csv AComp_Passenger_data.csv
 * 
 * 
 * 
 * 
 *  
 */
class Task_4A
    {
    // Configure and set-up the job using command line arguments specifying input files and job-specific mapper,
    //  combiner and reducer functions
    private static AirportList aList=new AirportList(30);
    private static PassengerList pList=new PassengerList();
    public static void main(String[] args) throws Exception {
        ReadAndErrorCheck.run(args);
        aList=ReadAndErrorCheck.getAList();
        pList=ReadAndErrorCheck.getPList();
        Config config = new Config(mapper.class,reducer.class,combiner.class);
        Job job = new Job(config,pList);
        job.run();
        DisplayFlightMiles(Job.getMap());
    }
    
    // Function to calculate Nautical Mile distances based on https://www.geodata.source/developers/java accessed at 11.30am on 28th December 2020
    
    private static double CalcNauticalMiles(double latAIn, double lonAIn,double latBIn,double lonBIn ){
        double theta = lonAIn - lonBIn;
		double dist = Math.sin(Math.toRadians(latAIn)) * Math.sin(Math.toRadians(latBIn)) + Math.cos(Math.toRadians(latAIn)) * Math.cos(Math.toRadians(latBIn)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515*0.8684;
        return dist;
    }
    private static void DisplayFlightMiles(ConcurrentHashMap<String,Object> mapIn){
        for (Map.Entry<String,Object> entry : mapIn.entrySet()){
            String key = entry.getKey();
            System.out.format("Passenger ID: %-12s  Nautical AirMiles: %,8.0f\n",key,entry.getValue());
        }
    }
    // Flightid mapper:
    // Output nautical miles for each unique occurrence of Flightid.
    // KEY = Flightid
    // VALUE = Flight Nautical Miles
    public static class mapper extends Mapper {
        public void map(String line) {
            String[] Fields=line.split(",");
            double latA,lonA,latB,lonB;
            latA=aList.getLat(Fields[2]);
            latB=aList.getLat(Fields[3]);
            lonA=aList.getLon(Fields[2]);
            lonB=aList.getLon(Fields[3]);
            EmitIntermediate(Fields[0]+Fields[1],CalcNauticalMiles(latA, lonA, latB, lonB));
        }
    }
    // PassengerID airmiles combiner:
    // Output the airmiles for each Passenger for summation
    // KEY = PassengerID&Flightid: Selecting first 10 characters provides
    // PassengerID only
    // VALUE = Airmiles
    public static class combiner extends Combiner {
        public void combine(Object key, Double value) {
            EmitIntermediate2(key.toString().substring(0,10), value);
        }
    }
        // Passenger airmiles reducer:
    // Output the total airmiles for each passengerID and compare with
    // maximum on top of stack.
    // KEY = PassengerId
    // VALUE = Airmiles total for passenger
    public static class reducer extends Reducer {
        public void reduce(Object key, Double value) {
            Emit(key, value);
        }
    }
}
