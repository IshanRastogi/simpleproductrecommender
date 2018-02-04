package simple.product.recommender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerRecommendationSets {


    public static final String CSV_RECOMMENDATION_IMPORT_FILE_HEADER = "\"CUSTOMER_NUMBER\",\"RECOMMENDATION_ACTIVE\",\"REC1\",\"REC2\",\"REC3\",REC4\",\"REC5\",\"REC6\",\"REC7\",\"REC8\",\"REC9\",\"REC10\"" ;
    public static final int CSV_RECOMMENDATION_IMPORT_COLUMN_COUNT = 12;


    /**
     * Reads the CSV file provided and generates and returns
     * CustomerRecommendationSets to be stored in the repositories
     *
     * @param csv
     * @return
     * @throws InvalidCSVException
     */
    public static List<CustomerRecommendationSet> fromCSV(String csv) throws InvalidCSVException {
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(csv.split("\n")));

        validateFileHeader(lines.get(0));

        lines.remove(0);

        List<CustomerRecommendationSet> customerRecommendationSets = new ArrayList<CustomerRecommendationSet>();

        for (String line : lines) {

            String[] fields = line.split(",");

            validateColumnCount(fields);

            for (int i = 0; i < fields.length; i++) {
                fields[i] = fields[i].trim().replaceAll("\"", "");
            }
            long customerId = 0L;
            boolean recommendationsEnabled;
            String[] games = new String[CSV_RECOMMENDATION_IMPORT_COLUMN_COUNT - 2];

            try {
                customerId = Long.parseLong(fields[0]);
            } catch (NumberFormatException e) {

            }
            recommendationsEnabled = Boolean.parseBoolean(fields[1]);
            for (int j = 0; j < CSV_RECOMMENDATION_IMPORT_COLUMN_COUNT - 2; j++) {
                games[j] = fields[j+2];
            }

            customerRecommendationSets.add(new CustomerRecommendationSet(
               customerId,
               recommendationsEnabled,
               games
            ));

        }
        return customerRecommendationSets;
    }

    /**
     * Method to validate number of Columns in the uploaded CSV file
     * As per the current format each row should have 12 columns
     * @param fields
     * @throws InvalidCSVException
     */
    private static void validateColumnCount(String[] fields) throws InvalidCSVException {
        if(fields.length != CustomerRecommendationSets.CSV_RECOMMENDATION_IMPORT_COLUMN_COUNT){
            throw new InvalidCSVException(
                    "Unexpected Number of Columns found in CSV.\n"
                    + "Expected: " + CustomerRecommendationSets.CSV_RECOMMENDATION_IMPORT_COLUMN_COUNT + "\n"
                    + "Found: " + fields.length
            );
        }
    }

    /**
     * Method to validate that the Header row in the uploaded CSV file exists
     * and is in the format defined.
     *
     * @param fileHeader
     * @throws InvalidCSVException
     */
    private static void validateFileHeader(String fileHeader) throws InvalidCSVException{
        if(!fileHeader.equals(CustomerRecommendationSets.CSV_RECOMMENDATION_IMPORT_FILE_HEADER)){
            throw new InvalidCSVException(
                    "Unsupported CSV-Header.\n"
                    + "Expected: " + CustomerRecommendationSets.CSV_RECOMMENDATION_IMPORT_FILE_HEADER + "\n"
                    + "Found: " + fileHeader
            );
        }
    }

}
