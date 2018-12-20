import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;

/**
 * Created by Daniel on 11/7/2018.
 */
public class TMDBController {

    // Get your own key here https://www.themoviedb.org/account/signup
    // INSERT KEY HERE
    private String tmdbKey= "";

    /** Build connection to tmdb and return JSONObject
     *
     * @param request
     * @return
     * @throws Exception
     */
    static JSONObject downloadJsonObject(String request) throws JSONException {
        // Build connection
        try {
            // TV Objects
            URL query = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) query.openConnection();

            // Connection
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            //Buffered Reader
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String output;

            // To JSONObjects
            while ((output = br.readLine()) != null) {
                JSONObject obj = new JSONObject(output);
                return obj;
            }

        } catch (Exception e) {
            System.out.println("Error during downloadJsonObject");
            System.out.println(e);
        }
        return null;
    }

    /** Unfinished temp test method
     *
     * @return
     */
    public JSONArray retrieveJSONArray() {
        JSONObject JSONSearch = downloadJsonObject("https://api.themoviedb.org/3/search/tv?api_key="+tmdbKey+"&query=Game+of+Thrones");
        JSONArray SearchArr = JSONSearch.getJSONArray("results");

        for (int i = 0; i < SearchArr.length(); i++) {
            /* TODO Test this
            System.out.println(SearchArr.getJSONObject(i).getString("name"));
            System.out.println(SearchArr.getJSONObject(i).getInt("id"));
            */
            int id = SearchArr.getJSONObject(i).getInt("id");
            JSONObject JSONTV = downloadJsonObject("http://api.themoviedb.org/3/tv/" + id + "?api_key="+tmdbKey);
            try {
                JSONObject JSONEpisode = JSONTV.getJSONObject("last_episode_to_air");
                JSONObject JSONEpisode2 = JSONTV.getJSONObject("next_episode_to_air");
                /* TODO Test this
                System.out.println(JSONEpisode.getString("air_date"));
                System.out.println(JSONEpisode2.getString("air_date"));
                */
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(SearchArr.getJSONObject(i).getString("name") + " nicht bekannt");
            }
        }
        return SearchArr;
    }

    /**
     *
     * @param query
     * @return
     */
    public JSONObject queryJSONObject(String query) {

        // Do search
        JSONObject JSONResult = downloadJsonObject("https://api.themoviedb.org/3/search/tv?api_key="+tmdbKey+"&query=" + query);
        return JSONResult;

    }

    public JSONArray resultJSONArray(String query) {

        // Do search
        JSONObject JSONSearch = downloadJsonObject("https://api.themoviedb.org/3/search/tv?api_key="+tmdbKey+"&query=" + query);
        JSONArray SearchArr = JSONSearch.getJSONArray("results");
        return SearchArr;

    }

    public JSONObject downloadResultJSON(int id) {

        JSONObject JSONTV = downloadJsonObject("http://api.themoviedb.org/3/tv/" + id + "?api_key="+tmdbKey);
        JSONObject resultJSON = JSONTV.getJSONObject("last_episode_to_air");

        return resultJSON;

    }

    /**
     * After retrieving id, search for next_episode_to_air of id
     * @param id primary key for each series
     * @return
     */
    public JSONObject getNextEpAr(int id) {
        JSONObject JSONTV = downloadJsonObject("http://api.themoviedb.org/3/tv/" + id + "?api_key="+tmdbKey);
        JSONObject resultJSON = JSONTV;
        try {
            resultJSON = JSONTV.getJSONObject("next_episode_to_air");
        } catch (Exception e) {
            System.out.println(e);
        }
        return resultJSON;

    }


}
