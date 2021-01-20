package core;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * An abstract Mapper class defining generic map functionality
 *
 * Areas for improvements:
 *  - Operate on portions of the input in parallel rather than on entire files
 *  - Implement the runnable interface for execution as a thread
 *  
 */
public abstract class Mapper {
    // The input file for this mapper to process
    protected File file;

    // Default constructor
    public Mapper() {}

    // Set the input file for the given instance
    public void setFile(File file) {
        this.file = file;
    }

    // Execute the map function for each line of the provided file
    public void run() throws IOException {
        // Read the file
        Iterator<String> records = Config.read(this.file);
        // Call map() for each line
        while(records.hasNext())
            map(records.next());
    }

    // Abstract map function to be overwritten by objective-specific class
    public abstract void map(String value);

    // Add key, value pair to map if it is unique
    public void EmitIntermediate(Object key, Object value) {
        // Only add the key value pair if it doesn't already exist in the list.
        if(!Job.map.containsKey(key)) {
            Job.map.put(key, (double) value);
        }
    }
}