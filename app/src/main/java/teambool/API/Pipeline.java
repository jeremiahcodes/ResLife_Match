package teambool.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Bekhzod Umarov on 9/12/16.
 */

public class Pipeline {
    private static final String HOST = "http://wbj-test-bekhzod0725.c9users.io/?f=";
    public Pipeline() {}

    private JSONObject _connection(String _url) {
        StringBuilder _out  = new StringBuilder();
        BufferedReader _in = null;
        JSONObject _result = null;

        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            _in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e("ResLife_Match", e.getMessage());
            e.printStackTrace();
            return null;
        }

        String line;
        try {
            while ((line = _in.readLine()) != null) {
                _out.append(line);
            }
        } catch (Exception e) {
            Log.e("ResLife_Match", e.getMessage());
            e.printStackTrace();
        }

        try {
            _result = new JSONObject(_out.toString());
            return new JSONObject(_out.toString());
        } catch (Throwable t) {
            String err = "Could not parse malformed JSON: \"" + _out.toString() + "\"";
            Log.e("ResLife_Match", err);
            System.out.println(err);
            return null;
        }
    }

    public JSONObject getData() {
        String _url = HOST + "match"; // URL to call
        return _connection(_url);
    }
    public JSONObject getDataFrom(String uRL) {
        String _url     = uRL; // URL to call
        return _connection(_url);
    }

    public JSONObject getMatchesForUser(int uid) {
        String _url = HOST + "match&attr=" + uid;
        return getDataFrom(_url);
    }

    public JSONObject getCategories() {
        String _url = HOST + "category";
        return getDataFrom(_url);
    }

    public JSONObject getSubcategories(int catid) {
        String _url = HOST + "category&catid=" + catid;
        return getDataFrom(_url);
    }
}
