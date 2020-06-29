package suza.project.crazyballs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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

    @BindView(R.id.btnHighscore)
    Button btnHighscore;

    @BindView(R.id.levelSpinner)
    Spinner levelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Full screen application
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String[] levelString = new String[] {
                "EASY", "MEDIUM", "DIFFICULT"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_style, levelString);
        levelSpinner.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        startActivityForResult(intent, MainActivity.REQUST_GET_SCORE);
    }


    /**
     * Action performed when how to play button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnHowToPlay)
    public void howToPlayButtonAction(View view) {
        Intent intent = new Intent(MainActivity.this, HowToPlayActivity.class);
        startActivity(intent);
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
