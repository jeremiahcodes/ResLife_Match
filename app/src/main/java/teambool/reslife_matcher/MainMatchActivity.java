package teambool.reslife_matcher;

import android.content.BroadcastReceiver;
import android.support.design.widget.BottomSheetBehavior;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainMatchActivity extends AppCompatActivity implements View.OnClickListener{


    ///-------------------------- j test
    // BottomSheetBehavior variable
    private BottomSheetBehavior bottomSheetBehavior;

    // TextView variable
    private TextView bottomSheetHeading;

    // Button variables
    /*private Button expandBottomSheetButton;
    private Button collapseBottomSheetButton;
    private Button hideBottomSheetButton;
    private Button showBottomSheetDialogButton;*/

    private TextView mMatchView;
    ///-------------------------- j test


    private RetrieveMatches mMatcher;
    private JSONArray matches;
    private JSONArray matchProfiles;
    private CoordinatorLayout lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_match);


        lLayout = (CoordinatorLayout) findViewById(R.id.activity_mainmatch);

        //follow
        //save matches from json file into array
       // mMatchView = (TextView) findViewById(R.id.factTextView);
        initViews();
        initListeners();


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
            JSONObject match = LoginActivity.p.getData();
            matches = new JSONArray();
            matchProfiles = new JSONArray();
            try {
                matches = match.getJSONArray(match.names().getString(0));

                for (int i = 0; i< matches.length(); ++i) {
                    String name = matches.getJSONObject(i).keys().next().toString();
                    matchProfiles.put(LoginActivity.p.getUserInfo(Integer.parseInt(name)));
                }
            }  catch (JSONException e) {
                e.printStackTrace();
            }

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
            //System.out.println(matches.toString());
            //for (int i = 0; i < matches.names().length(); ++i) {
                //JSONArray match = matches.getJSONArray(matches.names().getString(i));
                for (int j=0; j < 1; ++j) {
                    TextView txtName = (TextView) findViewById(R.id.txtName); //new TextView(this);
                    TextView txtRate = (TextView) findViewById(R.id.txtMatchRate);
                    TextView txtAge  = (TextView) findViewById(R.id.txtAge);
                    TextView txtMajor = (TextView) findViewById(R.id.txtMajor);
                    try {
                        //JSONObject uinfo = LoginActivity.p.getUserInfo(Integer.parseInt(name));
                        String name = matches.getJSONObject(j).keys().next().toString();
                        txtName.setText(matchProfiles.getJSONObject(j).getString("name"));
                        txtRate.setText(matches.getJSONObject(j).getString(name));
                        txtAge.setText(matchProfiles.getJSONObject(j).getInt("age"));
                        txtMajor.setText(matchProfiles.getJSONObject(j).getString("major"));
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    //lLayout.addView(mTextView);
                }
            //}
        //} catch (JSONException e) {
        //    e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    ///-------------------------- j test

    /**
     * method to initialize the views
     */
    private void initViews() {

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
        /*expandBottomSheetButton = (Button) findViewById(R.id.expand_bottom_sheet_button);
        collapseBottomSheetButton = (Button) findViewById(R.id.collapse_bottom_sheet_button);
        hideBottomSheetButton = (Button) findViewById(R.id.hide_bottom_sheet_button);
        showBottomSheetDialogButton = (Button) findViewById(R.id.show_bottom_sheet_dialog_button);*/

    }


    /**
     * method to initialize the listeners
     */
    private void initListeners() {
        // register the listener for button click
        /*expandBottomSheetButton.setOnClickListener(this);
        collapseBottomSheetButton.setOnClickListener(this);
        hideBottomSheetButton.setOnClickListener(this);
        showBottomSheetDialogButton.setOnClickListener(this);*/

        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetHeading.setText(getString(R.string.text_profile_close));
                } else {
                    bottomSheetHeading.setText(getString(R.string.text_profile));
                }

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


    }

    /**
     * onClick Listener to capture button click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.collapse_bottom_sheet_button:
                // Collapsing the bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.expand_bottom_sheet_button:
                // Expanding the bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.hide_bottom_sheet_button:
                // Hiding the bottom sheet
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case R.id.show_bottom_sheet_dialog_button:

                break;

        }*/
    }






    ///-------------------------- j test
}
