/*
package org.example;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MASTDataRetriever {

    public static void main(String[] args) {
        String apiUrl = "https://mast.stsci.edu/api/v0/invoke";
        String requestMethod = "POST";

        try {
            // Set up the HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Construct the request JSON payload
            JsonObject requestPayload = Json.createObjectBuilder()
                    .add("service", "Mast.Caom.Filtered")
                    .add("params", Json.createObjectBuilder()
                            .add("columns", "objID,ra,dec")
                            .add("filters", Json.createObjectBuilder()
                                    .add("bt", Json.createObjectBuilder()
                                            .add("min", 10)
                                            .add("max", 11))))
                    .build();

            // Send the request payload to the API
            connection.getOutputStream().write(requestPayload.toString().getBytes());

            // Read the API response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    JsonReader jsonReader = Json.createReader(reader);
                    JsonObject response = jsonReader.readObject();
                    jsonReader.close();

                    // Extract and process the data from the API response
                    JsonArray data = response.getJsonArray("data");
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject entry = data.getJsonObject(i);
                        String objID = entry.getString("objID");
                        double ra = entry.getJsonNumber("ra").doubleValue();
                        double dec = entry.getJsonNumber("dec").doubleValue();

                        // Process the retrieved data here (print, save to a file, etc.)
                        System.out.println("Object ID: " + objID + ", RA: " + ra + ", Dec: " + dec);
                    }
                }
            } else {
                System.out.println("Error: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
