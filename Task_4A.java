package objective;

import core.*;

import java.util.List;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.HashMap;
/**
 * Assignment:
 * Counts the number airmiles per passenger and selects the passenger with the most airmiles.
 * A multi-threaded solution which creates a mapper for the input file and a combiner for each passenger and a reducer 
 * for finding the greatest passenger airmiles,each processed sequentially.
 *
 * To run:
 * java Task_4A.java <files>
 *     i.e. java Task_4A.java AComp_Passenger_data_no_error.csv
 *
 * Potential Areas for improvement:
 * 
 * - Error checking and handling
 * 
 * 
 */
class Task_4A
    {
    // Configure and set-up the job using command line arguments specifying input files and job-specific mapper and
    // reducer functions
    private static AirportList aList=new AirportList(30);
    public static void main(String[] args) throws Exception {
        ReadAirports();
        Config config = new Config(args, mapper.class, reducer.class, combiner.class);
        Job job = new Job(config);
        job.run();
        DisplayFlightMiles(Job.getMap());
    }
    // Read airports from file and add to airport list
    public static void ReadAirports()
    {
        String csvFile1="Top30_airports_LatLong.csv";
        BufferedReader br = null;
        String line = "";
        try {
                br = new BufferedReader(new FileReader(csvFile1));
                while((line=br.readLine())!=null){
                    if (line.length()>0){
                        String[] Field = line.split(",");
                        String name=Field[0];
                        String code=Field[1];
                        double lat=Double.parseDouble(Field[2]);
                        double lon=Double.parseDouble(Field[3]);
                        Airport airport = new Airport(name,code,lat,lon);
                        aList.addAirport(airport);
                    }
                }
                br.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }
    // Function to calculate Nautical Mile distances based on https://www.geodata.source accessed at 11.30am on 28th December 2020
    
    private static double CalcNauticalMiles(double latAIn, double lonAIn,double latBIn,double lonBIn ){
        double theta = lonAIn - lonBIn;
        double dist = Math.sin(Math.toRadians(latAIn)) * Math.sin(Math.toRadians(latBIn)) + Math.cos(Math.toRadians(latAIn)) * Math.cos(Math.toRadians(latBIn)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515*0.8684;
        return dist;
}
// Function to Display airmiles per passenger
private static void DisplayFlightMiles(ConcurrentHashMap<Object,Object> mapIn){
    for (Map.Entry<Object,Object> entry : mapIn.entrySet()){
        String key = entry.getKey().toString();
        System.out.format("Passenger ID: %-12s  Nautical AirMiles: %,8.0f\n",key,entry.getValue());
    }
}
    // FromAirportCode+Flightid count mapper:
    // Output a one for each occurrence of FromAirportCode+Flightid.
    // KEY = FromAirportCode+Flightid
    // VALUE = 1
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