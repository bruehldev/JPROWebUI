import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

public class Test {

    public static void main(String[] args) {
        JSONArray test = getJsonArrayFromTmdbMicroservice("Game");
        System.out.println("TestMicroService:" + test);

    }

    private static JSONArray getJsonArrayFromTmdbMicroservice(String searchQuery) {
        try {
            // TV Objects
            String URL = "http://localhost:8761/";
            java.net.URL query = new URL(URL+searchQuery);
            HttpURLConnection con = (HttpURLConnection) query.openConnection();

            // Connection
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            //Buffered Reader
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            String output;

            while ((output = br.readLine()) != null) {
                // To JSONObjects
                JSONArray obj = new JSONArray(output);
                System.out.println("getJsonArrayFromTmdbMicroservice Objekt:" + obj);
                return obj;
            }

        } catch (Exception e) {
            System.out.println("Error during getJsonArrayFromTmdbMicroservice");
            System.out.println(e);
        }
        return null;
    }
}
