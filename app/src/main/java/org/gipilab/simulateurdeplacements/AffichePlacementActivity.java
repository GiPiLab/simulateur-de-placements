package org.gipilab.simulateurdeplacements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;
import org.gipilab.simulateurdeplacements.R.string;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class AffichePlacementActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    private Placement placement;

    private void displayChart(ArrayList<Echeance> mens) {
        boolean modeQuinzaine = false;
        if (placement.getModeCalculPlacement() == enumModeCalculPlacement.QUINZAINE) {
            modeQuinzaine = true;
        }
        LineChart chart = (LineChart) findViewById(id.lineChart);

        if (chart == null) {
            Log.e("GIPIERROR", "NULL chart");
            return;
        }

        chart.setTouchEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setOnChartValueSelectedListener(this);

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
            valuesValeurAcquise.add(new Entry(aMens.getValeurAcquise().floatValue(), aMens.getIeme() - 1));
            valuesCapitalPlace.add(new Entry(aMens.getCapitalCourant().floatValue(), aMens.getIeme() - 1));
        }

        LineDataSet dataSetValeurAcquise = new LineDataSet(valuesValeurAcquise, getString(string.chartLegendValeurAcquise));
        dataSetValeurAcquise.setAxisDependency(AxisDependency.LEFT);
        dataSetValeurAcquise.setDrawValues(false);
        dataSetValeurAcquise.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setDrawStepped(modeQuinzaine);
        dataSetValeurAcquise.setDrawCircles(false);
        dataSetValeurAcquise.setHighLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setHighlightLineWidth(1.0f);

        LineDataSet dataSetCapitalPlace = new LineDataSet(valuesCapitalPlace, getString(string.chartLegendCapitalPlace));
        dataSetCapitalPlace.setAxisDependency(AxisDependency.LEFT);
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

    }

    public void btnSaveClicked(View v) {
        placement.save();
        setResult(RESULT_OK);
        Snackbar snackbar = Snackbar.make(v, string.placementEnregistre, Snackbar.LENGTH_SHORT);
        snackbar.show();
        v.setEnabled(false);
    }

    private void displayTable(ArrayList<Annualite> annualites) {

        ExpandableListView listv = (ExpandableListView) findViewById(id.listViewResult);
        if (listv == null) {
            Log.e("GIPIERROR", "Null listview");
            return;
        }
        final TableauPlacementExpandableListAdapter adapter = new TableauPlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);
        listv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int child, long l) {

                LineChart chart = (LineChart) findViewById(id.lineChart);
                int flatPosition = adapter.findFlatChildIndexFromGroupAndChild(group, child);
                Log.d("GIPI", "Flat position=" + flatPosition);
                if (flatPosition >= 0 && flatPosition < chart.getLineData().getXValCount()) {
                    chart.highlightValue(flatPosition, 0);

                } else {
                    Log.e("GIPIERROR", "selection hors bornes");
                }
                return true;
            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_affiche_placement);

        Intent intent = getIntent();
        placement = (Placement) intent.getSerializableExtra("placement");
        boolean enregistrable = intent.getBooleanExtra("enregistrable", true);
        if (!enregistrable) {
            Button btnSave = (Button) findViewById(id.buttonSavePlacement);
            if (btnSave != null) {
                btnSave.setEnabled(false);
            } else {
                Log.e("GIPIERROR", "Null button");
            }
        }
        new DisplayPlacementTask(this).execute(placement);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.d("GIPI", "Selected entry = " + e);
        ExpandableListView elv = (ExpandableListView) findViewById(id.listViewResult);
        TableauPlacementExpandableListAdapter adapter = (TableauPlacementExpandableListAdapter) elv.getExpandableListAdapter();
        if (adapter == null) {
            Log.e("GIPIERROR", "Null adapter");
            return;
        }
        Pair<Integer, Integer> groupAndChildId = adapter.findGroupAndChildFromFlatIndex(e.getXIndex());
        Log.d("GIPI", "Selected group = " + groupAndChildId.first + " Selected child = " + groupAndChildId.second);
        elv.expandGroup(groupAndChildId.first);
        elv.setSelectedChild(groupAndChildId.first, groupAndChildId.second, true);



    }

    @Override
    public void onNothingSelected() {

    }

    private class DisplayPlacementTask extends AsyncTask<Placement, Void, Pair<ArrayList<Echeance>, ArrayList<Annualite>>> {
        private final ProgressDialog progressDialog;
        private final AffichePlacementActivity activity;

        private DisplayPlacementTask(AffichePlacementActivity activity) {
            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(getString(string.patienterCalculsEnCours));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(Pair<ArrayList<Echeance>, ArrayList<Annualite>> echeancesEtAnnualites) {
            displayTable(echeancesEtAnnualites.second);
            displayChart(echeancesEtAnnualites.first);
            TextView tvResult = (TextView) findViewById(id.textViewResult);

            getSupportActionBar().hide();

            if (tvResult != null) {
                tvResult.setText(placement.toLocalizedStringForDetailedView(activity));
            } else {
                Log.e("GIPIERROR", "Null textview");
            }
            if (progressDialog.isShowing())
                progressDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Pair<ArrayList<Echeance>, ArrayList<Annualite>> doInBackground(Placement... placements) {
            progressDialog.show();
            ArrayList<Echeance> echeances = placements[0].tableauPlacement();
            ArrayList<Annualite> annualites = placements[0].echeancesToAnnualites(echeances);
            return new Pair<>(echeances, annualites);
        }
    }


}
