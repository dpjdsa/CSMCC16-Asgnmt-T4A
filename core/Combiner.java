package core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.*;

/**
 * An abstract Combiner class defining generic combine functionality
 *
 * Areas for improvement:
 * - Implement runnable interface for execution as a thread
 * - Replace operations on the global map object with thread-safe alternatives
 */
public abstract class Combiner {
    // Intermediate records for this combiner instance to process
    protected ConcurrentHashMap records;

    // Default constructor
    public Combiner() {}

    // Setters
    public void setRecords(ConcurrentHashMap records) {
        this.records = records;
    }

    // Execute the reduce function for each key-value pair in the intermediate results output by the mapper
    public void run() {
        Iterator iterator = records.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Double> entry = (Map.Entry) iterator.next();
            combine(entry.getKey(),(double) entry.getValue());
        }
    }

    // Abstract reduce function to the overwritten by objective-specific class
    public abstract void combine(Object key, Double value);

    // Add up the totals for each unique key
    public void EmitIntermediate2(String key, Object value) {
        // Get the add value to existing value linked to the observed key else create a new map entry with value
        double total;
        if(Job.map1.containsKey(key)) {
            total = (double) Job.map1.get(key)+(double)value;
        } else {
            total = (double) value;       
        }
        // Add the new total to the list
        Job.map1.put(key, total);       
    }
}
