package teambool.reslife_matcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreferenceActivity extends AppCompatActivity {

    private Button mStartButton;    //variable for onClickListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void startMatch() {
        Intent intent = new Intent(this, MainMatchActivity.class);
        startActivity(intent);
    }
}
