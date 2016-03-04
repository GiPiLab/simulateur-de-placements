package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class ResultTableActivity extends AppCompatActivity {


    Placement p;

    private void displayChart(ArrayList<Echeance> mens) {
        LineChart chart = (LineChart) this.findViewById(id.lineChart);

        chart.setTouchEnabled(true);

        chart.setDescription("");
        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxisPosition.BOTTOM);

        ArrayList<Entry> valuesValeurAcquise = new ArrayList<Entry>();
        ArrayList<Entry> valuesCapitalPlace = new ArrayList<Entry>();
        ArrayList<String> xLabels = new ArrayList<String>();

        DateTimeFormatter dateFormat = DateTimeFormat.shortDate();

        for (Echeance aMens : mens) {
            xLabels.add(dateFormat.print(aMens.getDateDebutEcheance()));
            valuesValeurAcquise.add(new Entry(aMens.getValeurAcquise().floatValue(), aMens.getIeme()));
            valuesCapitalPlace.add(new Entry(aMens.getCapitalCourant().floatValue(), aMens.getIeme()));
        }

        LineDataSet dataSetValeurAcquise = new LineDataSet(valuesValeurAcquise, getString(R.string.chartLegendValeurAcquise));
        dataSetValeurAcquise.setAxisDependency(AxisDependency.LEFT);
        dataSetValeurAcquise.setDrawValues(false);
        dataSetValeurAcquise.setColor(Color.BLUE, 128);
        dataSetValeurAcquise.setCircleColor(Color.BLUE);
        dataSetValeurAcquise.setDrawCircles(false);

        LineDataSet dataSetCapitalPlace = new LineDataSet(valuesCapitalPlace, getString(R.string.chartLegendCapitalPlace));
        dataSetCapitalPlace.setAxisDependency(AxisDependency.LEFT);
        dataSetCapitalPlace.setDrawValues(false);
        dataSetCapitalPlace.setColor(Color.RED, 128);
        dataSetCapitalPlace.setCircleColor(Color.RED);
        dataSetCapitalPlace.setDrawCircles(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetValeurAcquise);
        dataSets.add(dataSetCapitalPlace);

        LineData chartData = new LineData(xLabels, dataSets);
        chart.setData(chartData);
        chart.invalidate();

    }

    private void displayTable(ArrayList<Echeance> mens) {
        ArrayList<Annualite> annualites = p.echeancesToAnnualites(mens);
        ExpandableListView listv = (ExpandableListView) this.findViewById(id.listViewResult);
        PlacementExpandableListAdapter adapter = new PlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_result_table);

        Intent intent = this.getIntent();
        p = (Placement) intent.getSerializableExtra("placement");
        ArrayList<Echeance> mens = p.tableauPlacement();
        this.displayTable(mens);
        this.displayChart(mens);
    }
}
