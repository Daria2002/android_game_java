package suza.project.wackyballs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daria on 27.8.2017..
 */

public class ResultActivity extends AppCompatActivity{

    @BindView(R.id.btnPlayAgain)
    Button btnPlayAgain;

    @BindView(R.id.btnExitAfterGame)
    Button btnExit;

    @BindView(R.id.btnSaveResult)
    Button btnSaveResult;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
    }

    /**
     * Action performed when play again button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnPlayAgain)
    public void playAgainButtonAction(View view) {
        Intent intent = new Intent(ResultActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Action performed when exit button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnExit)
    public void exitButtonAction(View view) {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    /**
     * Action performed when save result button is pressed.
     *
     * @param view View.
     */
    @OnClick(R.id.btnSaveResult)
    public void saveResultButtonAction(View view) {
        Intent intent = new Intent(ResultActivity.this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }
}

