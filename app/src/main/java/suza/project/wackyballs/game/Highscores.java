package suza.project.wackyballs.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import suza.project.wackyballs.MainActivity;
import suza.project.wackyballs.R;

/**
 * Created by Darija on 8.9.2017..
 */

public class Highscores extends Activity {

    TableLayout table;
    TableRow rowHeader, row1, row2, row3, row4, row5, row6, row7, row10;
    TextView rank, percentage, score;
    Button btn1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscoresmain);

        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);

        TextView rank = (TextView)findViewById(R.id.rank);
        rank.setText("RANK");
        TextView precentage = (TextView)findViewById(R.id.percentage);
        precentage.setText("PRECENTAGE");
        TextView score = (TextView) findViewById(R.id.rowHeader);

        rowHeader.addView(rank);
        rowHeader.addView(precentage);
        rowHeader.addView(score);

        Button btn1 = (Button)findViewById(R.id.btnHome);

        table.addView(rowHeader, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highscores.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
