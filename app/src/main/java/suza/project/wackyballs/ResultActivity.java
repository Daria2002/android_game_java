package suza.project.wackyballs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import suza.project.wackyballs.util.Score;

public class ResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_result);

        // Full screen application
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get activity intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // Check if intent was sent
        // If intent is found display alert dialog
        if (bundle != null){
            Integer score = (Integer)intent.getExtras().get(GameActivity.SCORE_KEY);
            newScoreDialog(score);
        } else {
            initializeTable();
        }
    }

    /**
     * Initializes score table.
     */
    private void initializeTable() {

        // Get score map
        Map<String, Integer> scoreMap =
                Score.getSortedScoreMap(ResultActivity.this.getPreferences(Context.MODE_PRIVATE));

        // Get table layout - nested in multiple layouts
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_main);

        // add all table rows
        int i = 1;
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            TableRow tbrow = new TableRow(this);

            // Add index cell
            TextView t1v = new TextView(this);
            t1v.setText(String.format(" %d. ", i));
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            t1v.setTextSize(25);
            tbrow.addView(t1v);
            i++;

            // Add name cell
            TextView t2v = new TextView(this);
            t2v.setText(String.format(" %s ", entry.getKey()));
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(25);
            tbrow.addView(t2v);

            // Add score cell
            TextView t3v = new TextView(this);
            t3v.setText(String.format(" %s ", entry.getValue()));
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(25);
            tbrow.addView(t3v);

            // Add to tablelayout
            tableLayout.addView(tbrow);
        }
    }

    /**
     * Action performed on game activity finish.
     *
     * @param finalScore Final game score.
     */
    private void newScoreDialog(final int finalScore) {

        // Make finishing alert dialog
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ResultActivity.this,
                    android.R.style.Theme_DeviceDefault_Dialog);
        } else {
            builder = new AlertDialog.Builder(ResultActivity.this);
        }

        // Set edit text
        final EditText input = new EditText(this);
        input.setHint("Enter name");
        input.setTextColor(Color.WHITE);

        // Add length filter
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(8);
        input.setFilters(filterArray);

        builder.setView(input);

        builder.setTitle("GAME OVER")
                .setMessage("Final score: " + finalScore)
                .setPositiveButton("Save score", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Save score click
                        Score.saveScoreLocally(
                                input.getText().toString(),
                                finalScore,
                                ResultActivity.this.getPreferences(Context.MODE_PRIVATE));
                        initializeTable();
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Main menu click
                        ResultActivity.this.finish();
                    }
                })
                .show();
    }

}
