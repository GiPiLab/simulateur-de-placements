package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.util.ArrayList;

public class ResultTableActivity extends AppCompatActivity {


    private void displayChart(ArrayList<Mensualite> mens) {
        LineChart chart = (LineChart) findViewById(R.id.lineChart);

        chart.setTouchEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDragEnabled(true);
        chart.setPinchZoom(true);

        chart.setDescription("");
        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        ArrayList<Entry> valuesInteretsObtenus = new ArrayList<Entry>();
        ArrayList<Entry> valuesCapitalPlace = new ArrayList<Entry>();
        ArrayList<String> xLabels = new ArrayList<String>();

        Entry entry;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        for (Mensualite aMens : mens) {
            xLabels.add(dateFormat.format(aMens.getDateMensualite()));
            entry = new Entry(aMens.getInteretsObtenus().floatValue(), aMens.getIeme());
            valuesInteretsObtenus.add(entry);
            entry = new Entry(aMens.getCapitalCourant().floatValue(), aMens.getIeme());
            valuesCapitalPlace.add(entry);
        }

        LineDataSet dataSetInteretsObtenus = new LineDataSet(valuesInteretsObtenus, "Intérêts obtenus");
        dataSetInteretsObtenus.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetInteretsObtenus.setDrawValues(false);

        LineDataSet dataSetCapitalCourant = new LineDataSet(valuesCapitalPlace, "Capital placé");
        dataSetCapitalCourant.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetCapitalCourant.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //   dataSets.add(dataSetInteretsObtenus);
        dataSets.add(dataSetCapitalCourant);

        LineData chartData = new LineData(xLabels, dataSets);
        chart.setData(chartData);
        chart.invalidate();

    }

    private void displayTable(ArrayList<Mensualite> mens) {
        ArrayList<Annualite> annualites = Placement.mensualitesToAnnualites(mens);
        ExpandableListView listv = (ExpandableListView) findViewById(R.id.listViewResult);
        PlacementExpandableListAdapter adapter = new PlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_table);

        Intent intent = getIntent();
        Placement p = (Placement) intent.getSerializableExtra("placement");
        ArrayList<Mensualite> mens = p.tableauPlacement();
        displayTable(mens);
        displayChart(mens);
    }
}
