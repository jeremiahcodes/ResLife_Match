package teambool.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.security.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Bekhzod Umarov on 9/12/16.
 */

public class Pipeline {
    private static final String HOST = "http://wbj-test-bekhzod0725.c9users.io/?f=";
    private static String session_code = null;
    private static int      uid = -1;
    public static byte[] imgdata;
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

    public static int getUid() {
        return uid;
    }

    private static JSONObject _connection(String _url) {
        StringBuilder _out  = new StringBuilder();
        BufferedReader _in = null;
        JSONObject _result = null;

        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            _in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            //Log.e("ResLife_Match", e.getMessage());
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
    public static String authenticate(String username, String password) {
        String _url = HOST + "login&username="+username + "&secret=" + sha1(password);
        JSONObject result = _connection(_url);
        try {
            session_code = result.get("sessionkey").toString();
            uid = result.getInt("id");
            //getPicture();
        } catch (Throwable t) {
            t.printStackTrace();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
        return session_code;
    }
    public static JSONObject getData() {
        String _url = HOST + "match&attr="+uid+"&session="+session_code; // URL to call
        return _connection(_url);
    }

    public static JSONObject getDataFrom(String uRL) {
        String _url     = uRL; // URL to call
        return _connection(_url);
    }

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
        return getDataFrom(_url);
    }
    public static JSONObject getUserInfo(int iid) {
        String _url = HOST + "uinfo&foruid=" + iid + "&session="+session_code;
        return _connection(_url);
    }
    public static JSONObject  getProfile(int iid) {
        String _url = HOST + "profile&uid="+iid+"&session="+session_code;
        return _connection(_url);
    }
    public static JSONObject getPicture() throws Throwable {
        String _url = HOST + "img&uid=" + uid + "&session="+session_code;
        JSONObject img = _connection(_url);
        //Bitmap bmp =  BitmapFactory.decodeByteArray(data, 0, data.length);
        //imgdata = Base64.decode(img.getString("photo"), Base64.DEFAULT);
        //System.out.print("Size of data: ");
        //System.out.println(imgdata.length);
        return img;
    }
    public static Drawable getImageUid(int iid) {
        String _url = HOST + "img&uid=" + iid + "&session="+session_code;
        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(_url).getContent());
            InputStream is = (InputStream) new URL(_url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    public static Drawable getImage() {
        String _url = HOST + "img&uid=" + uid + "&session="+session_code;
        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(_url).getContent());
            InputStream is = (InputStream) new URL(_url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setPref(int catid, int subcatid, float rate) {
        String _url = HOST + "setPref&catid=" + catid + "&subcatid=" + subcatid +
                "&rate=" + rate + "&uid=" + uid + "&session=" + session_code ;
        JSONObject resp = _connection(_url);
    }
}
