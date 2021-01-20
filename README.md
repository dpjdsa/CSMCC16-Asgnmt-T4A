# CSMCC16-Asgnmt-T4A
Assignment Task 4A:
Reads the airports file. Reads passenger records from the input csv file 
A single threaded solution which creates a mapper for the input file and a combiner to add up airmiles per passenger 
and a reducer to select the passenger with the highest airmiles.

To run:
java Task_4A.java <file>
     i.e. java Task_4A.java AComp_Passenger_data_no_error.csv

 Potential Areas for improvement:
 - Error checking and handling
 - Multi-threading
 - Partitioning of input for parallel processing
 - Synchronisation and thread-safe operations
