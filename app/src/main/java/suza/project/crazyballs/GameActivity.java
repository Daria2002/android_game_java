package suza.project.wackyballs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.state.BasketGameState;
import suza.project.wackyballs.state.GravityGameState;
import suza.project.wackyballs.state.TestGameState;
import suza.project.wackyballs.util.IGameFinishedListener;

/**
 * This class represents the main game activity. It sets the game panel
 * reference as a content view.
 */
public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    public static final String SCORE_KEY = "score";

    private GamePanel gamePanel;

    /**
     * Game finished listener.
     */
    private IGameFinishedListener finishedListener = new IGameFinishedListener() {

        @Override
        public void gameFinished(int score) {
            // Game is finished, do all the final things here
            Log.d(TAG, "Listener - Finishing the activity.");

            //GameActivity.this.setResult(Activity.RESULT_OK, new Intent().putExtra(SCORE_KEY, score));
            //GameActivity.this.finish();

            // Send score
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.putExtra(GameActivity.SCORE_KEY, score);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            GameActivity.this.startActivity(intent);
            GameActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Turn off the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Full screen application
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String state = getIntent().getStringExtra("state");
        gamePanel = new GamePanel(this);
        gamePanel.setGameFinishedListener(finishedListener);

        // Set correct game state
        switch (state) {
            case "BouncyBall":
                gamePanel.setGameState(new TestGameState(gamePanel));
                break;

            case "GravityBall":
                gamePanel.setGameState(new GravityGameState(gamePanel));
                break;

            case "BasketBall":
                gamePanel.setGameState(new BasketGameState(gamePanel));
                break;
        }

        // Set Game panel as the view
        setContentView(gamePanel);
        Log.d(TAG, "Game surface created.");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping main view...");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying main view...");
        super.onDestroy();
    }
}
