/*
* Simulateur de placements
*
* Copyright Thibault et Gilbert Mondary, Laboratoire de Recherche pour le Développement Local (2006--)
*
* labo@gipilab.org
*
* Ce logiciel est un programme informatique servant à simuler des placements
*
*
* Ce logiciel est régi par la licence CeCILL soumise au droit français et
* respectant les principes de diffusion des logiciels libres. Vous pouvez
* utiliser, modifier et/ou redistribuer ce programme sous les conditions
* de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
* sur le site "http://www.cecill.info".
*
* En contrepartie de l'accessibilité au code source et des droits de copie,
* de modification et de redistribution accordés par cette licence, il n'est
* offert aux utilisateurs qu'une garantie limitée. Pour les mêmes raisons,
* seule une responsabilité restreinte pèse sur l'auteur du programme, le
* titulaire des droits patrimoniaux et les concédants successifs.
*
* A cet égard l'attention de l'utilisateur est attirée sur les risques
* associés au chargement, à l'utilisation, à la modification et/ou au
* développement et à la reproduction du logiciel par l'utilisateur étant
* donné sa spécificité de logiciel libre, qui peut le rendre complexe à
* manipuler et qui le réserve donc à des développeurs et des professionnels
* avertis possédant des connaissances informatiques approfondies. Les
* utilisateurs sont donc invités à charger et tester l'adéquation du
* logiciel à leurs besoins dans des conditions permettant d'assurer la
* sécurité de leurs systèmes et ou de leurs données et, plus généralement,
* à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
*
* Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
* pris connaissance de la licence CeCILL, et que vous en avez accepté les
* termes.
*
*/


package org.gipilab.simulateurdeplacements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

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
import com.google.android.material.snackbar.Snackbar;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;
import org.gipilab.simulateurdeplacements.R.string;

import java.util.ArrayList;

public class AffichePlacementActivity extends AppCompatActivity implements OnChartValueSelectedListener {


    private Placement placement;

    private void displayChart(ArrayList<Echeance> mens) {
        boolean modeQuinzaine = false;
        if (placement.getModeCalculPlacement() == enumModeCalculPlacement.QUINZAINE) {
            modeQuinzaine = true;
        }
        LineChart chart = findViewById(id.lineChart);

        if (chart == null) {
            Log.e("SIMUPLACEMENT", "NULL chart");
            return;
        }

        chart.setTouchEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setOnChartValueSelectedListener(this);

        chart.getDescription().setText(getString(string.chartAxisDescriptionEcheance));
        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxisPosition.BOTTOM);

        chart.setMarker(new MyMarkerView(getApplicationContext(), layout.my_markerview_layout));

        ArrayList<Entry> valuesValeurAcquise = new ArrayList<Entry>();
        ArrayList<Entry> valuesCapitalPlace = new ArrayList<Entry>();

        for (Echeance aMens : mens) {
            valuesValeurAcquise.add(new Entry(aMens.getIeme()-1,aMens.getValeurAcquise().floatValue()));
            valuesCapitalPlace.add(new Entry(aMens.getIeme()-1,aMens.getCapitalCourant().floatValue()));
        }


        LineDataSet dataSetValeurAcquise = new LineDataSet(valuesValeurAcquise, getString(string.chartLegendValeurAcquise));
        dataSetValeurAcquise.setAxisDependency(AxisDependency.LEFT);
        dataSetValeurAcquise.setDrawValues(false);
        dataSetValeurAcquise.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if (modeQuinzaine)
            dataSetValeurAcquise.setMode(LineDataSet.Mode.STEPPED);
        dataSetValeurAcquise.setDrawCircles(false);
        dataSetValeurAcquise.setHighLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSetValeurAcquise.setHighlightLineWidth(1.0f);

        LineDataSet dataSetCapitalPlace = new LineDataSet(valuesCapitalPlace, getString(string.chartLegendCapitalPlace));
        dataSetCapitalPlace.setAxisDependency(AxisDependency.LEFT);
        dataSetCapitalPlace.setDrawValues(false);
        dataSetCapitalPlace.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setMode(LineDataSet.Mode.STEPPED);
        dataSetCapitalPlace.setDrawCircles(false);
        dataSetCapitalPlace.setHighLightColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSetCapitalPlace.setHighlightLineWidth(1.0f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSetValeurAcquise);
        dataSets.add(dataSetCapitalPlace);

        //LineData chartData = new LineData(xLabels, dataSets);
        LineData chartData = new LineData(dataSets);

        chart.setData(chartData);
        chart.invalidate();

    }


    public void btnModifierClicked(@SuppressWarnings("UnusedParameters") View v) {
        Intent data = new Intent();
        data.putExtra("placement", placement);
        setResult(RESULT_OK, data);
        finish();
    }

    public void btnBackClicked(View v){
        finish();
    }

    public void btnSaveClicked(View v) {
        PlacementDatabaseHelper dbHelper = PlacementDatabaseHelper.getInstance(this);
        if (dbHelper.placementExists(placement)) {
            Snackbar snackbar = Snackbar.make(v, string.placementDejaEnregistre, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            dbHelper.addPlacement(placement);
            setResult(RESULT_OK);
            Snackbar snackbar = Snackbar.make(v, string.placementEnregistre, Snackbar.LENGTH_SHORT);
            snackbar.show();
            v.setEnabled(false);
        }
    }

    private void displayTable(ArrayList<Annualite> annualites) {

        ExpandableListView listv = findViewById(id.listViewResult);
        if (listv == null) {
            Log.e("SIMUPLACEMENT", "Null listview");
            return;
        }
        final TableauPlacementExpandableListAdapter adapter = new TableauPlacementExpandableListAdapter(this, annualites);
        listv.setAdapter(adapter);
        listv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int child, long l) {

                LineChart chart = findViewById(id.lineChart);
                if (chart == null) {
                    Log.e("SIMUPLACEMENT", "Null chart");
                    return true;
                }
                int flatPosition = adapter.findFlatChildIndexFromGroupAndChild(group, child);
                adapter.setSelected(group, child);
                if (chart.getLineData() == null) {
                    Log.e("SIMUPLACEMENT", "Null getLineData");
                    return true;
                }

                //if (flatPosition >= 0 && flatPosition < chart.getLineData().getXValCount()) {
                if (flatPosition >= 0 && flatPosition < chart.getLineData().getEntryCount()) {
                    chart.highlightValue(flatPosition, 0);

                } else {
                    Log.e("SIMUPLACEMENT", "selection hors bornes");
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
            ImageButton btnSave = findViewById(id.buttonSavePlacement);
            if (btnSave != null) {
                btnSave.setEnabled(false);
            } else {
                Log.e("SIMUPLACEMENT", "Null button");
            }
        }
        new DisplayPlacementTask(this).execute(placement);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        ExpandableListView elv = findViewById(id.listViewResult);
        if (elv == null) {
            Log.e("SIMUPLACEMENT", "Null expandable list view");
            return;
        }
        TableauPlacementExpandableListAdapter adapter = (TableauPlacementExpandableListAdapter) elv.getExpandableListAdapter();
        if (adapter == null) {
            Log.e("SIMUPLACEMENT", "Null adapter");
            return;
        }
        Pair<Integer, Integer> groupAndChildId = adapter.findGroupAndChildFromFlatIndex((int)e.getX());
        elv.expandGroup(groupAndChildId.first);
        adapter.setSelected(groupAndChildId.first, groupAndChildId.second);

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
            TextView tvResult = findViewById(id.textViewResult);

          /*  if (getSupportActionBar() != null)
                getSupportActionBar().hide();
*/
            if (tvResult != null) {
                tvResult.setText(Html.fromHtml(placement.toLocalizedStringForDetailedView(activity)));
            } else {
                Log.e("SIMUPLACEMENT", "Null textview");
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
