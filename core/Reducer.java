package core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An abstract Reducer class defining generic reduce functionality
 *
 * Areas for improvement:
 * - Implement runnable interface for execution as a thread
 * - Replace operations on the global map object with thread-safe alternatives
 */
public abstract class Reducer {
    // Intermediate records for this reducer instance to process
    protected Map records;

    // Default constructor
    public Reducer() {}

    // Setters
    public void setRecords(Map records) {
        this.records = records;
    }

    // Execute the reduce function for each key-value pair in the intermediate results output by the mapper
    public void run() {
        Iterator iterator = records.entrySet().iterator();
        while(iterator.hasNext()) {
            //Map.Entry<String, List<Object>> entry = (Map.Entry) iterator.next();
            Map.Entry<String, Double> entry = (Map.Entry) iterator.next();
            reduce(entry.getKey(), (double) entry.getValue());
        }
    }

    // Abstract reduce function to the overwritten by objective-specific class
    public abstract void reduce(Object key, Double value);

    // if value greater than top of stack, replace top of stack with key and value
    public void Emit(Object key, Double value) {
        if (value>((double) Job.highestAm.peek())){
            Job.highestAm.pop();
            Job.highestAm.push(value);
            Job.highestAmp.pop();
            Job.highestAmp.push(key);
        }
    }
}