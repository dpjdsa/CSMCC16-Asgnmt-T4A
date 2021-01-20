package core;
import java.util.Map;
import java.util.*;
import java.util.regex.*;
import java.io.*;
/** Class used to store list of airports
 * @author BD837672
 * @version 20th December 2020
 */
public class AirportList {
    private Map<String, Airport> apList;
    public int MAX;
    /**Constructor
     * 
     */
    public AirportList(int MaxIn)
    {        
        apList=new HashMap<>();
        MAX=MaxIn;
    }
    // Adds new aiport
    public void addAirport(Airport airportIn)
    {
        apList.put(airportIn.getCode(),airportIn);
    }
    // Gets size of airport list
    public int size()
    {
        return apList.size();
    }
    // Getter methods based on airport Code parameter
    public String getName(String airportCodeIn)
    {
        return apList.get(airportCodeIn).getName();
    }
    public double getLat(String airportCodeIn)
    {
        return apList.get(airportCodeIn).getLat();
    }
    public double getLon(String airportCodeIn)
    {
        return apList.get(airportCodeIn).getLon();
    }
    // Returns set of airport code keys
    public Set<String> getKeys()
    {
        return apList.keySet();
    }
    // Used for output
    public String toString()
    {
        return apList.toString();
    }
}
