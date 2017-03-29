package teambool.reslife_matcher;

import android.content.BroadcastReceiver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainMatchActivity extends AppCompatActivity implements View.OnClickListener{

    ///-------------------------- j test
    // BottomSheetBehavior variable
    private View    bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    // TextView variable
    private TextView bottomSheetHeading;

    private TextView mMatchView;
    private TextView txtName;
    private TextView txtRate;
    private TextView txtAge;
    private TextView txtMajor;
    private TextView txtInterestCats;
    private TextView txtInterests;

    private RetrieveMatches mMatcher;
    private JSONArray matches;
    private JSONArray matchProfiles;
    private JSONArray matchProfileInterests;
    private CoordinatorLayout lLayout;

    private int curItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_match);

        lLayout = (CoordinatorLayout) findViewById(R.id.activity_mainmatch);
        txtName = (TextView) findViewById(R.id.txtName); //new TextView(this);
        txtRate = (TextView) findViewById(R.id.txtMatchRate);
        txtAge  = (TextView) findViewById(R.id.txtAge);
        txtMajor = (TextView) findViewById(R.id.txtMajor);
        txtInterestCats = (TextView) findViewById(R.id.txtInterestCats);
        txtInterests= (TextView) findViewById(R.id.txtInterests);
        //follow
        //save matches from json file into array
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
    public class RetrieveMatchInterests extends AsyncTask<Void, Void, Boolean> {
        ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
        Drawable d=null;
        Bitmap bp ;
        int height;
        @Override
        protected Boolean doInBackground(Void... params) {

            JSONObject profile;
            String name;
            try{
                name = matches.getJSONObject(curItem).keys().next().toString();
                System.out.println(name);
                matchProfileInterests = new JSONArray();
                profile = LoginActivity.p.getProfile(Integer.parseInt(name));

                String p_str = profile.names().getString(0);
                JSONArray parr = profile.getJSONArray(p_str);
                System.out.println(parr.toString());
                for (int i = 0; i<parr.length(); ++i)
                    matchProfileInterests.put(parr.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            try{
                d = LoginActivity.p.getImageUid(Integer.parseInt(name));
                bp = ((BitmapDrawable) d).getBitmap();
                //height  = bp.getHeight();
            } catch(Exception e) {
                //height=0;
                d = null;
                e.printStackTrace();
            }
            return true;
        }
        ViewGroup.LayoutParams params ;

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                params = (ViewGroup.LayoutParams) profilePic.getLayoutParams();

                try {
                    for (int i = 0; i < matchProfileInterests.length(); ++i) {
                        String cat = matchProfileInterests.getJSONObject(i).getString("CAT");
                        String subcat = matchProfileInterests.getJSONObject(i).getString("SUBCAT");
                        if (i==0) {
                            txtInterestCats.setText(cat+":\n");
                            txtInterests.setText(subcat+"\n");
                        } else {
                            txtInterestCats.setText(txtInterestCats.getText() + cat + ":\n");
                            txtInterests.setText(txtInterests.getText() + subcat + "\n");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{

//                    bp.getHeight();
                    // existing height is ok as is, no need to edit it
//                    if (d == null) {
//                        params.height = 0;
//                    } else {

//                    }
//                    profilePic.setLayoutParams(params);
                    if (d==null) {
                        profilePic.setImageResource(R.mipmap.no_person);
                    } else {
                        profilePic.setImageDrawable(d);
                    }
                    //params.height = height;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                // Basically error handling for doInBackground
            }
        }

        @Override
        protected void onCancelled() {
            mMatcher = null;
        }
    }

    void populateView() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today[] = df.format(c.getTime()).split("-");

        try {
            // Apparently in Android when working with setting the fields, there is a loading time issue
            // meaning any fields that need to be updated with dynamic information need to be first stored
            // in memory as variable for faster processing before the View gets rendered.
            String name = matches.getJSONObject(curItem).keys().next().toString();
            String _date = matchProfiles.getJSONObject(curItem).getString("date");
            String major = matchProfiles.getJSONObject(curItem).getString("major");
            String date[] = _date.split("-");

            int age = Integer.parseInt(today[0]) - Integer.parseInt(date[0]);
            if (Integer.parseInt(today[1]) < Integer.parseInt(date[1])) {
                age -= 1;
            }

            txtName.setText(matchProfiles.getJSONObject(curItem).getString("name"));
            txtRate.setText(matches.getJSONObject(curItem).getString(name));
            txtAge.setText(Integer.toString(age));
            txtMajor.setText(major);

            RetrieveMatchInterests rmi = new RetrieveMatchInterests();
            rmi.execute( (Void) null);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * method to initialize the views
     */
    private void initViews() {

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));

        //bottomSheet = (View) findViewById(R.id.bottomSheetLayout);
        //bottomSheet.getLayoutParams().height = 1200;
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
    }

    /**
     * method to initialize the listeners
     */
    private int prevState;
    private Boolean hide = true;
    private void initListeners() {
        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetHeading.setText(getString(R.string.text_profile_close));
                } else {
                    bottomSheetHeading.setText(getString(R.string.text_profile));
                }

                if (newState != BottomSheetBehavior.STATE_DRAGGING) prevState = newState;

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (prevState == BottomSheetBehavior.STATE_EXPANDED)
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
//                        if (prevState == newState) {
//                            hide = !hide;
//                            if (hide) bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                        }
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
            public void onSlide(View bottomSheet, float slideOffset) {  }
        });


    }

    /**
     * onClick Listener to capture button click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
    }


    // The below part is for handling the Left and Right swipe
    private float x1,x2;
    static final int MIN_DISTANCE=150;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // Can have an animation for letting the user know
                // that something is happening when we are swiping
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2-x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (x2 > x1 && curItem > 0) {
                        // swipe left
                        curItem--;
                    } else if (x2 < x1 && curItem < matches.length()-1) {
                        curItem++;
                        // swipe right
                    }
                }
                populateView();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

}
