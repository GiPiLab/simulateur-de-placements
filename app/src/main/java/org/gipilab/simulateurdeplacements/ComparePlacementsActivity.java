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

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class ComparePlacementsActivity extends AppCompatActivity {// implements OnChartValueSelectedListener {

    private ArrayList<Placement> _lesPlacements;
    private HashMap<Placement, ArrayList<Echeance>> _dataToPlot;
    private LineChart chart;

    private long minTimeStamp, maxTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_placements);

        Intent intent = getIntent();
        _lesPlacements = (ArrayList<Placement>) intent.getSerializableExtra("listePlacements");

        if (_lesPlacements.size() > getResources().getInteger(R.integer.maxPlacementsToCompare)) {

            Toast toast = Toast.makeText(this, getString(R.string.maxPlacementsToCompare, getResources().getInteger(R.integer.maxPlacementsToCompare)), Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }


        /*if (getSupportActionBar() != null)
            getSupportActionBar().hide();
*/
        chart = findViewById(R.id.lineChart);
        _dataToPlot = new HashMap<Placement, ArrayList<Echeance>>();

        minTimeStamp = getMinTimestamp();
        maxTimeStamp = getMaxTimestamp();

        computeData();
        displayChart();
    }

    private void computeData() {
        //TODO : multithread
        for (Placement p : _lesPlacements) {
            _dataToPlot.put(p, p.tableauPlacement());
        }
    }

    private int getIndex(long timestamp) {
        return (int) ((timestamp - minTimeStamp) / 86400000);
    }

    private long getMinTimestamp() {
        long minTimeStamp = Long.MAX_VALUE;

        for (Placement p : _lesPlacements) {
            long dateDebut = p.getDateDebut().toDate().getTime();
            if (dateDebut < minTimeStamp) {
                minTimeStamp = dateDebut;
            }
        }
        return minTimeStamp;
    }

    private long getMaxTimestamp() {
        long maxTimeStamp = 0;

        for (Placement p : _lesPlacements) {
            long dateFin = p.getDateFin().toDate().getTime();
            if (dateFin > maxTimeStamp) {
                maxTimeStamp = dateFin;
            }
        }
        return maxTimeStamp;
    }

    private void displayChart() {

        boolean modeQuinzaine;

        chart.setTouchEnabled(true);
        chart.setHighlightPerTapEnabled(false);
        // chart.setOnChartValueSelectedListener(this);


        //FIXME : Legend word wrap seems to not working
        //Legend legend=chart.getLegend();
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        //legend.setWordWrapEnabled(true);

        chart.setDescription(null);

        XAxis xaxis = chart.getXAxis();
        chart.getAxisRight().setEnabled(false);

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<String> xLabels = new ArrayList<String>();

        DateTimeFormatter dateFormat = DateTimeFormat.shortDate();

        for (long currentTimeStamp = minTimeStamp; currentTimeStamp <= maxTimeStamp; currentTimeStamp += 86400000) {
            xLabels.add(dateFormat.print(new LocalDate(currentTimeStamp)));
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        int[] colors = getResources().getIntArray(R.array.colorsOfPlacementsToCompare);
        int i = 0;

        for (Placement placement : _lesPlacements) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            ArrayList<Echeance> mens = _dataToPlot.get(placement);

            for (Echeance aMens : mens) {
                int index = getIndex(aMens.getDateFinEcheance().toDate().getTime());
                values.add(new Entry(aMens.getValeurAcquise().floatValue(), index));
            }

            modeQuinzaine = placement.getModeCalculPlacement() == enumModeCalculPlacement.QUINZAINE;

            LineDataSet dataSetValeurAcquise = new LineDataSet(values, placement.toLocalizedVeryShortDescription(this));
            dataSetValeurAcquise.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSetValeurAcquise.setDrawValues(false);

            dataSetValeurAcquise.setColor(colors[i]);

            dataSetValeurAcquise.setCircleColor(colors[i]);
            if (modeQuinzaine == true)
                dataSetValeurAcquise.setMode(LineDataSet.Mode.STEPPED);
            else
                dataSetValeurAcquise.setMode(LineDataSet.Mode.LINEAR);

            dataSetValeurAcquise.setDrawCircles(false);

            dataSets.add(dataSetValeurAcquise);
            i++;
            if (i >= getResources().getInteger(R.integer.maxPlacementsToCompare)) {
                i = 0;
            }
        }

//        LineData chartData = new LineData(xLabels, dataSets);
        LineData chartData=new LineData(dataSets);
        chart.setData(chartData);

        chart.invalidate();


    }
/*

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        Log.d("GIPI", "selected");
        if (e != null) {
            Log.d("GIPI", "Selected " + e.getXIndex() + " val = " + e.getVal());
        }
    }

    @Override
    public void onNothingSelected() {

    }*/
}


