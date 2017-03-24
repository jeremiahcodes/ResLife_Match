package teambool.reslife_matcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import teambool.API.Pipeline;

public class PreferenceActivity extends AppCompatActivity {

    private Button mStartButton;    //variable for onClickListener
    private ImageButton pictureButton;
    private Spinner mSpinner;
    private JSONObject categories;
    private ArrayList<JSONObject> subCategories;
    private RetrieveFields fields;
    private Bitmap bmp = null;
    private static byte[] pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fields = new RetrieveFields();
        fields.execute((Void) null);

        setContentView(R.layout.activity_preference);

        pictureButton = (ImageButton) findViewById(R.id.pictureButton);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpload();
            }
        });
        pictureButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        pictureButton.setAdjustViewBounds(true);

        GetImageTask imgTask = new GetImageTask();
        imgTask.execute(pictureButton);



        mStartButton = (Button) findViewById(R.id.matchButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMatch();   //pass name data to new function
                //show what value name has
                ////Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
                unregisterReceiver(this);
            }
        }, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                sendBroadcast(broadcastIntent);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private static byte[] imgBin;
    public class GetImageTask extends AsyncTask<ImageView, Void, Drawable> {
        ImageView imageView;
//        private byte[] data;
        @Override
        protected Drawable doInBackground(ImageView...imageViews) {
            this.imageView = pictureButton;
//            final BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;

            try{
//                JSONObject pic = LoginActivity.p.getPicture();
//                String img = pic.getString("photo");
//                System.out.println(img);
//                imgBin = img.getBytes(StandardCharsets.UTF_8);
//                System.out.println(imgBin);
//                imgBin =  Base64.decode(imgBin, Base64.DEFAULT);
//                System.out.println(imgBin);

                //InputStream is = new ByteArrayInputStream(pic.getString("photo"));
                //BufferedInputStream bfs = new BufferedInputStream(is);
                //BitmapFactory.decodeStream(is, null, opts);
                //opts.inJustDecodeBounds = false;

//                Bitmap bmp = BitmapFactory.decode
//                Bitmap bmp = BitmapFactory.decodeByteArray(imgBin, 0, imgBin.length, opts);
//                System.out.println(imgBin);
                Drawable bmp = LoginActivity.p.getImage();
                return bmp;
                //return BitmapFactory.decodeStream(is,null, opts);
                //bfs.reset();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;


            //pictureButton.setImageBitmap(BitmapFactory.decodeByteArray(LoginActivity.p.imgdata, 0, LoginActivity.p.imgdata.length));

        }
        @Override
        protected void onPostExecute(Drawable result) {
            imageView.setImageDrawable(result);
        }

    }
    public class RetrieveFields extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void ... params) {


            categories =  LoginActivity.p.getCategories();
            subCategories = new ArrayList<>(categories.names().length());
            System.out.println(subCategories.size());
            for (int i=0; i<categories.names().length();++i) {
                subCategories.add(null);
            }
            System.out.println(subCategories.size());
            System.out.println("--------------------------");
            for (int i=0; i<categories.names().length(); ++i) {
                try {
                    int cat = categories.names().getInt(i);
                    if (cat > 0) {

                        JSONObject sc = LoginActivity.p.getSubcategories(cat);
                        if (sc != null) {
                            System.out.print("Working: ");
                            System.out.println(cat-1);
                            subCategories.add(cat - 1, sc);
                        }
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
            System.out.println("--------------------------");
            System.out.println(subCategories.size());
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            fields = null;

            if (success) {
                //finish();
                populateView();
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            fields = null;
        }
    }
    private void populateView() {
        try {

            JSONObject profile = LoginActivity.p.getProfile(LoginActivity.p.getUid());

            String p_str = profile.names().getString(0);
            JSONArray parr = profile.getJSONArray(p_str);
            System.out.println(parr.toString());
            int sportID;
            int musicID;
            int movieID;
            int bedtimeID;
            int wakeupID;
            //for (int i = 0; i < parr.length(); ++i)
            //    int cId = parr.getJSONObject(i);
                //matchProfileInterests.put(parr.get(i));

            for (int i = 0; i < subCategories.size(); ++i) {

                JSONObject subCat = subCategories.get(i);
                System.out.print(i);
                System.out.print(" : ");
                if (subCat != null) System.out.println(subCat.toString());
                else System.out.println();
                if (i == 0) mSpinner = (Spinner) findViewById(R.id.spSports);
                else if (i == 2) mSpinner = (Spinner) findViewById(R.id.spMusic);
                else if (i == 5) mSpinner = (Spinner) findViewById(R.id.spMovie);
                else if (i == 7) mSpinner = (Spinner) findViewById(R.id.spBedTime);
                else if (i == 4) mSpinner = (Spinner) findViewById(R.id.spWakeup);
                //else if (i == 1) mSpinner = (Spinner) findViewById(R.id.spShows);

                ArrayList<String> items = new ArrayList<>();

                if (subCat != null) {
                    myOnItemSelectedListener listener = new myOnItemSelectedListener();
                    listener.spinnerID = i;
                    listener.ids = new ArrayList<>();
                    listener.categories = new ArrayList<>();

                    mSpinner.setOnItemSelectedListener(listener);

                    for (int j = 0; j < subCat.names().length(); ++j) {
                        String sc = subCat.names().get(j).toString();
                        JSONObject subC = subCat.getJSONObject(sc);
                        sc = subC.getString("name");
                        items.add(sc);
                        int id = subC.getInt("id");
                        int catid = subC.getInt("catid");
                        listener.ids.add(id);
                        listener.categories.add(catid);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_item,
                            items);
                    mSpinner.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void startMatch() {
        Intent intent = new Intent(this, MainMatchActivity.class);
        startActivity(intent);
    }
    private void startUpload() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private class myOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public int spinnerID;
        public ArrayList<Integer> ids;
        public ArrayList<Integer> categories;
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
            Log.e("RESLIFE", Integer.toString(spinnerID));
            Log.e("RESLIFE", Integer.toString(i));

            int catid;
            if (spinnerID == 0) catid = 1;
            else if (spinnerID == 2) catid = 2;
            else if (spinnerID == 5) catid = 3;
            else if (spinnerID == 7) catid = 4;
            else if (spinnerID == 4) catid = 5;
            else catid = 0;

            StorePref prefs = new StorePref();
            prefs.execute(catid, ids.get(i));

            /*if (i == 0) mSpinner = (Spinner) findViewById(R.id.spSports);
            else if (i == 2) mSpinner = (Spinner) findViewById(R.id.spMusic);
            else if (i == 5) mSpinner = (Spinner) findViewById(R.id.spMovie);
            else if (i == 7) mSpinner = (Spinner) findViewById(R.id.spBedTime);
            else if (i == 4) mSpinner = (Spinner) findViewById(R.id.spWakeup);*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class StorePref extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer ... params) {
            int catid = params[0];
            int i = params[1];

            LoginActivity.p.setPref(catid, i, 0.7f);
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            fields = null;
        }
    }
}
