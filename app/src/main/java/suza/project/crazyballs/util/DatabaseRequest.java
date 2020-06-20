package suza.project.crazyballs.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import suza.project.crazyballs.R;

/**
 * Background worker used for sending database requests.
 * Created by lmark on 22/09/2017.
 */
public class DatabaseRequest extends AsyncTask<String, Void, Map<String, Integer>> {

    /**
     * Select all rows from the database.
     */
    public static final String REQUEST_SELECT_ALL = "select_all";

    /**
     * Insert one row to the database.
     */
    public static final String REQUEST_INSERT_UPDATE = "insert";

    private final String URL_SELECT_ALL;
    private final String URL_INSERT_UPDATE;

    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String TAG = DatabaseRequest.class.getSimpleName();

    /**
     * Activity context.
     */
    private Context context;

    /**
     * Object used for delegating finishing tasks.
     */
    private AsyncResponse responseDelegate;

    /**
     * Task exception.
     */
    private Exception e = null;

    /**
     * Default database request constructor.
     *
     * @param context Activity context.
     */
    public DatabaseRequest(Context context, AsyncResponse responseDelegate) {
        this.context = context;
        this.responseDelegate = responseDelegate;

        // Initializes URLs
        URL_SELECT_ALL = context.getResources().getString(R.string.url_select_all);
        URL_INSERT_UPDATE = context.getResources().getString(R.string.url_insert_update);
    }

    /**
     * Database request task.
     *
     * @param params First argument is type of request.
     *               Second argument is the player name(if required by request).
     *               Third arguments is the player score(if required by request).
     * @return Map containing database query result.
     */
    @Override
    protected Map<String, Integer> doInBackground(String... params) {
        // Get request type
        Log.d(TAG, "Task " + params[0] + " started.");
        String type = params[0];

        try {
            if (type.equals(REQUEST_SELECT_ALL)) {
                return handleSelectAll();
            }

            if (type.equals(REQUEST_INSERT_UPDATE)){
                return handleUpdateInsert(params[1], params[2]);
            }

        } catch (Exception e) {
            this.e = e;
        }

        return null;
    }


    @Override
    protected void onPostExecute(final Map<String, Integer> stringIntegerMap) {
        Log.d(TAG, "Task finished");

        if (responseDelegate == null) {
            return;
        }

        if (stringIntegerMap == null) {
            responseDelegate.processFinished(stringIntegerMap, this.e);

        } else {
            // Initialize sorted map
            Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int i1 = stringIntegerMap.get(o1);
                    int i2 = stringIntegerMap.get(o2);

                    if (i1 < i2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            sortedMap.putAll(stringIntegerMap);
            responseDelegate.processFinished(sortedMap, this.e);
        }
    }

    /**
     * Handles SELECT_ALL request.
     *
     * @return Returns a map of all database entries.
     */
    private Map<String, Integer> handleSelectAll() throws Exception{
        Map<String, Integer> scoreMap = null;

        // Request URL connection
        URL url = new URL(URL_SELECT_ALL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(CONNECTION_TIMEOUT);

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        // Read response
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = reader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

        // Parse Json response
        JSONArray jsonArray = new JSONArray(responseStrBuilder.toString());
        scoreMap = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            scoreMap.put(
                    jsonArray.getJSONObject(i).getString("name"),
                    Integer.valueOf(jsonArray.getJSONObject(i).getString("score"))
            );
        }

        return scoreMap;
    }

    /**
     *
     * @param name Player name.
     * @param score Player score.
     * @return Return null if update / insert is successful otherwise returns a
     *          map containing given name and score.
     */
    private Map<String, Integer> handleUpdateInsert(String name, String score) throws Exception{

        // Get POST parameters
        String data  = URLEncoder.encode("name", "UTF-8") + "=" +
                URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode("score", "UTF-8") + "=" +
                URLEncoder.encode(score, "UTF-8");

        // Open connection
        URL url = new URL(URL_INSERT_UPDATE);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(CONNECTION_TIMEOUT);

        // Write post parameters
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write( data );
        wr.flush();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        // Read response
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = reader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        if (responseStrBuilder.toString().isEmpty()) {
            // Request successfully passed
            return null;

        } else {
            // Request failed
            Map<String, Integer> scoreMap = new HashMap<>();
            scoreMap.put(name, Integer.valueOf(score));
            return scoreMap;
        }
    }

    /**
     * Interface used for delegating finishing task.
     */
    public interface AsyncResponse{

        /**
         * Method called when async task is finished.
         *
         * @param result Reusult of async task operation.
         */
        void processFinished(Map<String, Integer> result, Exception e);
    }
}
