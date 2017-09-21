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
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import suza.project.wackyballs.util.Score;

public class ResultActivity extends AppCompatActivity {

    private static final int TABLE_TEXT_SIZE = 30;

    @BindView(R.id.mainMenuButton)
    Button mainMenuButton;

    @BindView(R.id.table_main)
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

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

        // Configure row margin
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        int leftMargin=10;
        int topMargin=2;
        int rightMargin=10;
        int bottomMargin=2;
        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        // add all table rows
        int i = 1;
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            TableRow tbrow = new TableRow(this);
            tbrow.setLayoutParams(tableRowParams);
            //TODO Add row graphics

            // Add index cell
            TextView t1v = new TextView(this);
            t1v.setText(String.format(" %d.  ", i));
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            t1v.setTextSize(TABLE_TEXT_SIZE);
            tbrow.addView(t1v);
            i++;

            // Add name cell
            TextView t2v = new TextView(this);
            t2v.setText(String.format(" %s ", entry.getKey()));
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(TABLE_TEXT_SIZE);
            tbrow.addView(t2v);

            // Add score cell
            TextView t3v = new TextView(this);
            t3v.setText(String.format(" %s ", entry.getValue()));
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(TABLE_TEXT_SIZE);
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

        // Add input to builder
        builder.setView(input);

        // Configure builder
        builder.setTitle("GAME OVER")
                .setMessage("Final score: " + finalScore)
                .setPositiveButton("Save score", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing...
                    }
                })
                .setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Main menu click
                        ResultActivity.this.finish();
                    }
                })
                .setCancelable(false);

        // Create and show dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Override onclick listener - prevent closing when user enters empty text
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = input.getText().toString();

                // If text box is empty, do nothing
                if (inputString.trim().isEmpty()) {
                    Toast.makeText(
                            ResultActivity.this,
                            "Please enter your name.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Try to save the score
                boolean saved = Score.saveScoreLocally(
                        inputString,
                        finalScore,
                        ResultActivity.this.getPreferences(Context.MODE_PRIVATE));

                // If score is not saved, notify user
                if (!saved) {
                    Toast.makeText(
                            ResultActivity.this,
                            String.format(
                                    "Player \"%s\" already has higher score logged.",
                                    inputString),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Dismiss dialog
                dialog.dismiss();
                initializeTable();
            }
        });
    }

    /**
     * Exit button action.
     *
     * @param view View
     */
    @OnClick(R.id.mainMenuButton)
    public void exitButtonAction(View view) {
        ResultActivity.this.finish();
    }

}
