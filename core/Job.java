package core;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * MapReduce Job class
 * Coordinates the entire MapReduce process
 *
 * Areas for improvement:
 * - Implement effective partitioning or "chunking" of the input to evenly distribute records across multiple mappers
 *   and reducers
 * - Pass segmented chunks to independent threads for efficient parallel processing
 * - Implement sorting
 */
public class Job {
    // Job configuration
    private Config config;

    // Global thread-safe objects to store intermediate and final results and stack for maximum airmiles passenger and 
    // number of airmiles
    protected static ConcurrentHashMap<Object,Object> map,map1;
    protected static Stack highestAmp=new Stack<String>();
    protected static Stack highestAm= new Stack<Double>();

    // Constructor
    public Job(Config config) {
        this.config = config;
    }

    // Run the job through map, combine, reduce stages given the provided configuration
    public void run() throws Exception {
        // Initialise the maps to store intermediate results
        map=new ConcurrentHashMap<Object,Object>();
        map1=new ConcurrentHashMap<Object,Object>();
        // Execute the map and reduce phases in sequence
        map();
        System.out.println("After Map Phase: " + map);
        combine();
        System.out.println("After Combine Phase: " + map1);
        highestAmp.push("ABC1234DE5");
        highestAm.push(0.0);
        reduce();
        System.out.format("\nHighest Airmiles from Passenger ID:%10s  Who achieved: %,10.0f Nautical airmiles.\n",highestAmp.peek(),highestAm.peek());
    }

    // Map each provided file using an instance of the mapper specified by the job configuration
    private void map() throws Exception {
        for(File file : config.getFiles()) {
            Mapper mapper = config.getMapperInstance(file);
            mapper.run();
        }
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
    // Return the results of request as a ConcurrentHashMap object
    public static ConcurrentHashMap<Object,Object> getMap(){
        return map1;      
    }
}