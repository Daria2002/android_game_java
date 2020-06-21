package suza.project.crazyballs;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToPlayActivity extends AppCompatActivity {

    private static final int TABLE_TEXT_SIZE = 30;

    @BindView(R.id.mainMenuButton)
    Button mainMenuButton;

    @BindView(R.id.howToPlayText)
    TextView text;

    /**
     * Indicates thate activity is accessed from main screen.
     */
    private boolean fromMainScreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howtoplay);
        ButterKnife.bind(this);

        // Full screen application
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get activity intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        text.setTextSize(20);
        text.setText("How to play Crazy Balls?\nCatch good balls to score 10 points.\nCatch bad balls but lose 5 points.\n");
    }
}
