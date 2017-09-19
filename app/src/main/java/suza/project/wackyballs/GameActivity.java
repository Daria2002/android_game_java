package suza.project.wackyballs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import suza.project.wackyballs.game.GamePanel;
import suza.project.wackyballs.state.BasketGameState;
import suza.project.wackyballs.state.GravityGameState;
import suza.project.wackyballs.state.TestGameState;

/**
 * This class represents the main game activity. It sets the game panel
 * reference as a content view.
 */
public class GameActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GamePanel gamePanel;

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
