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
import android.support.v7.app.AppCompatActivity;
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
    private GamePanel gamePanel;

    /**
     * Game finished listener.
     */
    private IGameFinishedListener finishedListener = new IGameFinishedListener() {

        @Override
        public void gameFinished(int score) {
            // Game is finished, do all the final things here
            Log.d(TAG, "Listener - Finishing the activity.");

            // Finish current activity, and start result activity
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

    /**
     * Action performed on game activity finish.
     *
     * @param finalScore Final game score.
     */
    private void onFinish(final int finalScore) {

        // Make finishing alert dialog
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GameActivity.this,
                    android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(GameActivity.this);
        }

        // Set edit text
        final EditText input = new EditText(this);
        input.setHint("Enter name");
        builder.setView(input);
        Looper.prepare();
        builder.setTitle("GAME OVER")
                .setMessage("Final score: " + finalScore)
                .setPositiveButton("Save score", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Save score click
                        saveScoreLocally(input.getText().toString(), finalScore);
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Main menu click
                        GameActivity.this.finish();
                    }
                })
                .show();
    }

    /**
     * Save player name and score locally.
     *
     * @param name Player name.
     * @param score Player score.
     */
    private void saveScoreLocally(String name, int score) {
        SharedPreferences sharedPref = GameActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(name, score);
        editor.commit();
    }
}
