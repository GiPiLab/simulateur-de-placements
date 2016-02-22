package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class ResultTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_table);

        Intent intent = getIntent();
        Placement p = (Placement) intent.getSerializableExtra("placement");

        ArrayList<Annualite> annualites = Placement.mensualitesToAnnualites(p.tableauPlacement());
        ExpandableListView listv = (ExpandableListView) findViewById(R.id.listViewResult);


        PlacementExpandableListAdapter adapter = new PlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);

        /*TextView result=(TextView)findViewById(R.id.textViewResult);
        result.setText("Intérêts obtenus = "+p.interetsObtenus);*/
    }
}
