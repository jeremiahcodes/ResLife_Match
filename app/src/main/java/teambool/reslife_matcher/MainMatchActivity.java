package teambool.reslife_matcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainMatchActivity extends AppCompatActivity {
    private RetrieveMatches mMatcher;
    private JSONObject matches;
    private RelativeLayout lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_match);

        lLayout = (RelativeLayout) findViewById(R.id.linear_layout_bottom_sheet);

        mMatcher = new RetrieveMatches();
        mMatcher.execute((Void) null);

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

    public class RetrieveMatches extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            matches = LoginActivity.p.getData();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mMatcher = null;

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
            mMatcher = null;
        }
    }

    void populateView() {
        try {
            System.out.println(matches.toString());
            for (int i = 0; i < matches.names().length(); ++i) {
                JSONObject match = matches.getJSONObject(matches.names().getString(i));
                TextView mTextView = new TextView(this);
                mTextView.setText(match.get("firstname").toString() + " " + match.get("lastname").toString());
                lLayout.addView(mTextView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}