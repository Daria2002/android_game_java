package suza.project.wackyballs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Darija on 27.8.2017..
 */

public class ScoreActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.score_login);

        View mView = getLayoutInflater().inflate(R.layout.score_login, null);
        EditText mText = (EditText) findViewById(R.id.editname);
        Button mSave = (Button) mView.findViewById(R.id.btnSaveResult);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScoreActivity.this, "Result saved", Toast.LENGTH_SHORT).show();
                View highscoreView = getLayoutInflater().inflate(R.layout.highscoresmain, null);
            }
        });
/*
        Button btnStartAfterScore = (Button) findViewById(R.id.btnStartAfterScore);
        Button btnExitAfterScore = (Button) findViewById(R.id.btnExitAfterScore);

        btnStartAfterScore.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnExitAfterScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        btnExitAfterScore.setOnClickListener(new View.OnClickListener(){

        @Override
            public void onClick(View v){

            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            }
        });
*/
    }

}
