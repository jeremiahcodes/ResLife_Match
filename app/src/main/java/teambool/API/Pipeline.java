package teambool.API;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
<<<<<<< HEAD
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
=======
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.security.*;
>>>>>>> ccaa31560a8d09aecb6e55d66b356823d4c9a5f8
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Bekhzod Umarov on 9/12/16.
 */

public class Pipeline {
    private static final String HOST = "http://wbj-test-bekhzod0725.c9users.io/?f=";
<<<<<<< HEAD
    public Pipeline() {}

    private JSONObject _connection(String _url) {
=======
    private static String session_code = null;
    private static int      uid = -1;
    public Pipeline() {}

    public static String sha1(String stringToHash) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] result = digest.digest(stringToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b: result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static JSONObject _connection(String _url) {
>>>>>>> ccaa31560a8d09aecb6e55d66b356823d4c9a5f8
        StringBuilder _out  = new StringBuilder();
        BufferedReader _in = null;
        JSONObject _result = null;

        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            _in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
<<<<<<< HEAD
            Log.e("ResLife_Match", e.getMessage());
=======
            //Log.e("ResLife_Match", e.getMessage());
>>>>>>> ccaa31560a8d09aecb6e55d66b356823d4c9a5f8
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
<<<<<<< HEAD

    public JSONObject getData() {
        String _url = HOST + "match"; // URL to call
        return _connection(_url);
    }
    public JSONObject getDataFrom(String uRL) {
=======
    public static String authenticate(String username, String password) {
        String _url = HOST + "login&username="+username + "&secret=" + sha1(password);
        JSONObject result = _connection(_url);
        try {
            session_code = result.get("sessionkey").toString();
            uid = result.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return session_code;

    }
    public static JSONObject getData() {
        String _url = HOST + "match&attr="+uid+"&session="+session_code; // URL to call
        return _connection(_url);
    }
    public static JSONObject getDataFrom(String uRL) {
>>>>>>> ccaa31560a8d09aecb6e55d66b356823d4c9a5f8
        String _url     = uRL; // URL to call
        return _connection(_url);
    }

<<<<<<< HEAD
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
=======
    public static JSONObject getMatchesForUser(int uid) {
        String _url = HOST + "match&attr=" + uid + "&session="+session_code;
        return getDataFrom(_url);
    }

    public static JSONObject getCategories() {
        String _url = HOST + "category&session="+session_code;
        return getDataFrom(_url);
    }

    public static JSONObject getSubcategories(int catid) {
        String _url = HOST + "category&catid=" + catid +"&session="+session_code;
>>>>>>> ccaa31560a8d09aecb6e55d66b356823d4c9a5f8
        return getDataFrom(_url);
    }
}
