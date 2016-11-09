package teambool.reslife_matcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import teambool.API.Pipeline;

public class PreferenceActivity extends AppCompatActivity {

    private Button mStartButton;    //variable for onClickListener
    private Spinner mSpinner;
    private JSONObject categories;
    private ArrayList<JSONObject> subCategories;
    private RetrieveFields fields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fields = new RetrieveFields();
        fields.execute((Void) null);

        setContentView(R.layout.activity_preference);

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

                    for (int j = 0; j < subCat.names().length(); ++j) {
                        String sc = subCat.names().get(j).toString();
                        JSONObject subC = subCat.getJSONObject(sc);
                        sc = subC.getString("name");
                        items.add(sc);
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
}