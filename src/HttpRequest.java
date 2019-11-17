import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * File name: HttpRequest.java
 *
 * @version 1.0
 * Creation Date: 25/10/2019
 * Last Modification date: 2/11/2019
 * @author Ben Wickens
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Takes an url passed into it and forms a HTTP get request retrieving text from the page.
 * <br>
 * Version History
 * 1.0 - connects to a URL passed into it. Produces a connection error message if needed.
 *
 */


public class HttpRequest {
    /**
     * @param targetURL - a string containing the target URL.
     * @return A string containing the text present from the HTTP request.
     */
    public String newConnection(String targetURL) {
        String url = targetURL; 
        String results = connectionResult(url);
        return results;
    }

    /**
     * <p>This class is responsible for the connection to the given URL. To produce the string
     * which is used in other classes.</p>
     * @param targetURL - a string containing the target URL.
     * @return
     */
    private String connectionResult(String targetURL) {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder string = null;

        try {
            url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            string = new StringBuilder();
            String line = null;
            //Continually reads in each line of the input stream until end.
            while ((line = reader.readLine()) != null) {
                string.append(line);
            }
            return string.toString();
        } catch (Exception e) { //any issue with connection gives a user friendly message
            return "Error with connection";
        }
    }
}
