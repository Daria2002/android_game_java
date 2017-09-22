package suza.project.wackyballs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUST_GET_SCORE = 0;

    @BindView(R.id.btnPlay)
    Button btnPlay;

    @BindView(R.id.btnExit)
    Button btnExit;

    @BindView(R.id.state_spinner)
    Spinner stateSpinner;

    @BindView(R.id.btnHighscore)
    Button btnHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Full screen application
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Configure spinner options
        String[] stateString = new String[] {
                "BasketBall", "BouncyBall", "GravityBall"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, stateString);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        stateSpinner.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQUST_GET_SCORE &&
                resultCode == Activity.RESULT_OK) {
        }
    }

    /**
     * Action performed when play button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnPlay)
    public void playButtonAction(View view) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("state", (String)stateSpinner.getSelectedItem());
        startActivityForResult(intent, MainActivity.REQUST_GET_SCORE);
    }

    /**
     * Action performed when exit button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnExit)
    public void exitButtonAction(View view) {
        finish();
        System.exit(0);
    }

    @OnClick(R.id.btnHighscore)
    public void highScoreButtonAction(View view) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        startActivity(intent);
    }
}
