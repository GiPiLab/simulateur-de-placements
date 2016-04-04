package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.gipilab.simulateurdeplacements.R.string;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class PlacementDetailsActivity extends AppCompatActivity {


    Placement p;

    private void displayChart(ArrayList<Echeance> mens) {
        LineChart chart = (LineChart) findViewById(R.id.lineChart);

        chart.setTouchEnabled(true);

        chart.setDescription("");
        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<Entry> valuesValeurAcquise = new ArrayList<Entry>();
        ArrayList<Entry> valuesCapitalPlace = new ArrayList<Entry>();
        ArrayList<String> xLabels = new ArrayList<String>();

        DateTimeFormatter dateFormat = DateTimeFormat.shortDate();

        for (Echeance aMens : mens) {
            xLabels.add(dateFormat.print(aMens.getDateDebutEcheance()));
            valuesValeurAcquise.add(new Entry(aMens.getValeurAcquise().floatValue(), aMens.getIeme() - 1));
            valuesCapitalPlace.add(new Entry(aMens.getCapitalCourant().floatValue(), aMens.getIeme() - 1));
        }

        LineDataSet dataSetValeurAcquise = new LineDataSet(valuesValeurAcquise, this.getString(string.chartLegendValeurAcquise));
        dataSetValeurAcquise.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetValeurAcquise.setDrawValues(false);
        dataSetValeurAcquise.setColor(Color.BLUE, 128);
        dataSetValeurAcquise.setCircleColor(Color.BLUE);
        dataSetValeurAcquise.setDrawCircles(false);

        LineDataSet dataSetCapitalPlace = new LineDataSet(valuesCapitalPlace, this.getString(string.chartLegendCapitalPlace));
        dataSetCapitalPlace.setAxisDependency(YAxis.AxisDependency.LEFT);
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

    public void btnSaveClicked(View v) {
        p.save();
    }

    private void displayTable(ArrayList<Echeance> mens) {
        ArrayList<Annualite> annualites = this.p.echeancesToAnnualites(mens);
        ExpandableListView listv = (ExpandableListView) findViewById(R.id.listViewResult);
        TableauPlacementExpandableListAdapter adapter = new TableauPlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_result_table);

        Intent intent = this.getIntent();
        this.p = (Placement) intent.getSerializableExtra("placement");
        ArrayList<Echeance> mens = this.p.tableauPlacement();
        this.displayTable(mens);
        this.displayChart(mens);

        TextView tvResult = (TextView) this.findViewById(R.id.textViewResult);
        tvResult.setText(this.p.toLocalizedString(this));
    }
}
