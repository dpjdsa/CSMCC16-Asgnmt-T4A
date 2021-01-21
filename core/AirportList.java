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
    // Adds airport to airport list
    public void addAirport(Airport airportIn)
    {
        apList.put(airportIn.getCode(),airportIn);
    }
    // Returns size of airport list
    public int size()
    {
        return apList.size();
    }
    // Getter methods based on airport code
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
    // Get the set of airport codes from airport list
    public Set<String> getKeys()
    {
        return apList.keySet();
    }
    // Check if the airport code is valid
    public boolean validCode(String codeIn)
    {
        if (apList.containsKey(codeIn)){
            return true;
        }   else {
            return false;
        }
    }
    // Return the set of all keys as a HashSet
    public HashSet<String> getKeysHashSet()
    {
        HashSet<String> set=new HashSet<String>();
        set.addAll(apList.keySet());
        return set; 
    }
    // For output
    public String toString()
    {
        return apList.toString();
    }
}
