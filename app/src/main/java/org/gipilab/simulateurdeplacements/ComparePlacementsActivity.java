package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.HashMap;

public class ComparePlacementsActivity extends AppCompatActivity {

    private ArrayList<Placement> _lesPlacements;
    private HashMap<Placement, ArrayList<Echeance>> _dataToPlot;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_placements);

        Intent intent = getIntent();
        _lesPlacements = (ArrayList<Placement>) intent.getSerializableExtra("listePlacements");
        chart = (LineChart) findViewById(R.id.lineChart);
        _dataToPlot = new HashMap<Placement, ArrayList<Echeance>>();

        computeData();
        // displayChart();
    }

    private void computeData() {
        //TODO : multithread
        for (Placement p : _lesPlacements) {
            _dataToPlot.put(p, p.tableauPlacement());
        }
    }
/*
    private void displayChart() {

        boolean modeQuinzaine = false;

        if (chart == null) {
            Log.e("SIMUPLACEMENT", "NULL chart");
            return;
        }







        if (placement.getModeCalculPlacement() == enumModeCalculPlacement.QUINZAINE) {
            modeQuinzaine = true;
        }

        chart.setTouchEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        //chart.setOnChartValueSelectedListener(this);

        chart.setDescription("");
        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<String> xLabels = new ArrayList<String>();

        DateTimeFormatter dateFormat = DateTimeFormat.shortDate();


        for (Echeance aMens : mens) {
            xLabels.add(dateFormat.print(aMens.getDateDebutEcheance()));
            valuesValeurAcquise.add(new Entry(aMens.getValeurAcquise().floatValue(), aMens.getIeme() - 1));
            valuesCapitalPlace.add(new Entry(aMens.getCapitalCourant().floatValue(), aMens.getIeme() - 1));
        }

        ArrayList<Entry> valuesValeurAcquise = new ArrayList<Entry>();
        ArrayList<Entry> valuesCapitalPlace = new ArrayList<Entry>();

        LineDataSet dataSetValeurAcquise = new LineDataSet(valuesValeurAcquise, getString(R.string.chartLegendValeurAcquise));
        dataSetValeurAcquise.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetValeurAcquise.setDrawValues(false);
        dataSetValeurAcquise.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setDrawStepped(modeQuinzaine);
        dataSetValeurAcquise.setDrawCircles(false);
        dataSetValeurAcquise.setHighLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setHighlightLineWidth(1.0f);

        LineDataSet dataSetCapitalPlace = new LineDataSet(valuesCapitalPlace, getString(R.string.chartLegendCapitalPlace));
        dataSetCapitalPlace.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSetCapitalPlace.setDrawValues(false);
        dataSetCapitalPlace.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setDrawStepped(true);
        dataSetCapitalPlace.setDrawCircles(false);
        dataSetCapitalPlace.setHighLightColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setHighlightLineWidth(1.0f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetValeurAcquise);
        dataSets.add(dataSetCapitalPlace);

        LineData chartData = new LineData(xLabels, dataSets);
        chart.setData(chartData);
        chart.invalidate();


    }*/
}
