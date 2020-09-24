package suza.project.crazyballs.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import suza.project.crazyballs.R;

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
        text.setText("How to play Crazy Balls?\nCatch good balls to score 7/10/14 points depending on the level.\nCatch bad balls but lose 5 points.\n");
    }

    /**
     * Exit button action.
     *
     * @param view View
     */
    @OnClick(R.id.mainMenuButton)
    public void exitButtonAction(View view) {
        HowToPlayActivity.this.finish();
    }

}
