package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * Modified to make search case insensitive.
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
            aValue = aValue.toLowerCase();                  //Added for lowerCase MUST REMEMBER STRING IMMUTABILITY!!!
//            System.out.println(value);                    //Added for debug
//            System.out.println(aValue);                   //Added for debug
            if (aValue.contains(value.toLowerCase())) {     //Added for lowerCase
//            if (aValue.contains(value)) {                 /Replaced with above line to make case insensitive
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Returns results of searching all values in allJobs data that contain the search term
     *
     * For example, searching for "ing", "Ing", or "ING" will include results
     * with any value that includes "ing" (e.g. Spring, Things, Non-coding, etc.)
     *
     * @param value Value of the field to search for
     * @return jobsByValue List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value){

        loadData();                                                             // load data, if not already loaded

        ArrayList<HashMap<String, String>> jobsByValue = new ArrayList<>();     // populate with matched values
        String tempValue = "";                                                  // holds temporary HashMap string values; used in comparison to search value
        boolean foundValue;                                                     // true when search value found in HashMap row

        for (HashMap<String, String> row : allJobs){
            foundValue = false;
            for (String r : row.values()){
                tempValue = r.toLowerCase();
                if (tempValue.contains(value.toLowerCase())){
                    foundValue = true;
                }
            }
            if (foundValue) {
                jobsByValue.add(row);
            }
        }
        return jobsByValue;
    }


    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
