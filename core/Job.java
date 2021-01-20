package core;

import java.io.File;
import java.util.*;

//import objective.AirportList;
import java.util.concurrent.*;

/**
 * MapReduce Job class
 * Coordinates the entire MapReduce process
 *
 * Features:
 * - Implements effective partitioning or "chunking" of the input to evenly distribute records across multiple mappers
 *   and reducers
 * - Passes segmented chunks to independent threads for efficient parallel processing
 * 
 */
public class Job {
    // Job configuration
    private Config config;

    // Global Threadsafe objects to store intermediate and final results
    protected static ConcurrentHashMap map,map1;
    protected static ArrayList record;
    protected static PassengerList pList;
    protected static Stack highestAmp=new Stack<String>();
    protected static Stack highestAm= new Stack<Double>();

    // Constructor
    public Job(Config config,PassengerList pListIn) {
        this.config = config;
        this.pList=pListIn;
    }

    // Run the job given the provided configuration, only requires map phase
    public void run() throws Exception {
        // Initialise the Threadsafe maps to store intermediate results
        map = new ConcurrentHashMap();
        map1 = new ConcurrentHashMap();
        // Initialise ArrayList to read in file prior to chunking up
        record = new ArrayList<String>();
        // Execute the map and phase
        map();
        System.out.println("After Map Phase, output map is: " + map);
        combine();
        System.out.println("After Combine Phase, output map is: " + map1);
        highestAmp.push("ABC1234DE5");
        highestAm.push(0.0);
        reduce();
        // Output the results to the console
        System.out.format("\nHighest Airmiles from Passenger ID:%10s  Who achieved: %,10.0f Nautical airmiles.\n",highestAmp.peek(),highestAm.peek());
    }

    // Map each provided file using an instance of the mapper specified by the job configuration
    private void map() throws Exception {
            Mapper mapper = config.getMapperInstance();
            mapper.setPList(pList);
            mapper.run();
        
    }
    // Reduce the intermediate results output by the map phase using an instance of the combiner specified by the job configuration
    private void combine() throws Exception {
        Combiner combiner = config.getCombinerInstance(map);
        combiner.run();
    }
    // Reduce the intermediate results output by the combine phase using an instance of the reducer specified by the job configuration
    private void reduce() throws Exception {
        Reducer reducer = config.getReducerInstance(map1);
        reducer.run();
    }
    //Return the results of request as a ConcurrentHashMap object
    public static ConcurrentHashMap getMap(){
        return map1;      
    }
}