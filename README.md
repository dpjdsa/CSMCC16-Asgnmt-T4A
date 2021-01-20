# CSMCC16-Asgnmt-T4A
Assignment Task 4A:
Reads the airports file. Reads passenger records from the input csv file 
A multi-threaded solution which creates a mapper for the input file and a combiner to add up airmiles per passenger 
and a reducer to select the passenger with the highest airmiles.
Also reads the passenger records and error checks and corrects before further processing

To run:
java Task_4A.java <file>
     i.e. java Task_4A.java Top30_airports_LatLong.csv AComp_Passenger_data.csv

 After outputting diagnostics, prints passenger with the highest airmiles and the total number of
 airmiles for each passenger in turn.
