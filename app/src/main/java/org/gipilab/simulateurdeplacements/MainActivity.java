package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;
import org.gipilab.simulateurdeplacements.R.string;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements OnDateChangedListener {

    private Calendar calendarDebut, calendarFin;
    private DateFormat dateFormat;
    private int defaultDureeLabelColor;

    private static final int calculeDureeEnMois(Calendar debut, Calendar fin) {
        int diffYear = fin.get(Calendar.YEAR) - debut.get(Calendar.YEAR);
        return diffYear * 12 + fin.get(Calendar.MONTH) - debut.get(Calendar.MONTH);

    }

    private void initDateSelectionSystem() {
        this.calendarDebut = Calendar.getInstance();
        this.calendarFin = Calendar.getInstance();

        this.calendarFin.set(this.calendarDebut.get(Calendar.YEAR), this.calendarDebut.get(Calendar.MONTH) + 12, this.calendarDebut.get(Calendar.DAY_OF_MONTH));

        this.dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        final DatePicker dp = (DatePicker) this.findViewById(id.datePicker);
        dp.init(this.calendarFin.get(Calendar.YEAR), this.calendarFin.get(Calendar.MONTH), this.calendarFin.get(Calendar.DAY_OF_MONTH), this);
        TextView tv = (TextView) this.findViewById(id.labelSelectedDateDebut);
        tv.setText(this.dateFormat.format(this.calendarDebut.getTime()));
        tv = (TextView) this.findViewById(id.labelSelectedDateFin);
        tv.setText(this.dateFormat.format(this.calendarFin.getTime()));
        tv = (TextView) this.findViewById(id.labelDuree);
        tv.setText(this.getString(string.dureeXmois, MainActivity.calculeDureeEnMois(this.calendarDebut, this.calendarFin)));
        this.defaultDureeLabelColor = tv.getCurrentTextColor();
        ToggleButton dateButton = (ToggleButton) this.findViewById(id.toggleButtonChoisirDateFin);

        dateButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton b, boolean isChecked) {

                if (isChecked) {
                    dp.updateDate(MainActivity.this.calendarFin.get(Calendar.YEAR), MainActivity.this.calendarFin.get(Calendar.MONTH), MainActivity.this.calendarFin.get(Calendar.DAY_OF_MONTH));
                } else {
                    dp.updateDate(MainActivity.this.calendarDebut.get(Calendar.YEAR), MainActivity.this.calendarDebut.get(Calendar.MONTH), MainActivity.this.calendarDebut.get(Calendar.DAY_OF_MONTH));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_main);
        this.initDateSelectionSystem();
    }

    private boolean validateInputs() {
        EditText editCapital = (EditText) this.findViewById(id.editCapital);
        EditText editTaux = (EditText) this.findViewById(id.editTaux);
        EditText editVariation = (EditText) this.findViewById(id.editVariation);

        BigDecimal capital;

        if (editCapital.getText().length() == 0) {
            Toast toast = Toast.makeText(this, string.saisissezLeCapital, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        capital = new BigDecimal(editCapital.getText().toString());

        if (capital.compareTo(BigDecimal.ZERO) <= 0) {
            Toast toast = Toast.makeText(this, string.capitalDoitEtrePositif, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (capital.compareTo(Placement.MAXCAPITAL) > 0) {
            Toast toast = Toast.makeText(this, this.getString(string.capitalDoitEtreInferieurA, Placement.MAXCAPITAL.toString()), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (editTaux.getText().length() == 0) {
            Toast toast = Toast.makeText(this, string.saisissezLeTaux, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        BigDecimal taux = new BigDecimal(editTaux.getText().toString());

        if (taux.compareTo(BigDecimal.ZERO) < 0) {
            Toast toast = Toast.makeText(this, string.tauxDoitEtrePositifOuNul, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (taux.compareTo(Placement.MAXTAUX) > 0) {
            Toast toast = Toast.makeText(this, this.getString(string.tauxDoitEtreInferieurA, Placement.MAXTAUX.toString()), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        int duree = MainActivity.calculeDureeEnMois(this.calendarDebut, this.calendarFin);
        if (duree > Placement.MAXDUREE) {
            Toast toast = Toast.makeText(this, this.getString(string.dureeDoitEtreInferieureA, Placement.MAXDUREE), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (duree <= 0) {
            Toast toast = Toast.makeText(this, string.dureeDoitEtrePositive, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (editVariation.getText().length() == 0) {
            Toast toast = Toast.makeText(this, string.saisissezLaVariationPeriodique, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        BigDecimal variation = new BigDecimal(editVariation.getText().toString());
        if (variation.compareTo(BigDecimal.ZERO) < 0 && variation.abs().compareTo(capital) > 0 && variation.compareTo(Placement.MAXVARIATION) >= 0) {
            Toast toast = Toast.makeText(this, string.variationDoitEtreInferieureAuCapital, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    public void buttonCalculerClick(@SuppressWarnings("UnusedParameters") View v) {
        if (!this.validateInputs()) {
            return;
        }

        Placement placement = new Placement();
        placement.setCapitalInitial(new BigDecimal(((EditText) this.findViewById(id.editCapital)).getText().toString()));
        placement.setVariation(new BigDecimal(((EditText) this.findViewById(id.editVariation)).getText().toString()));
        placement.setTauxAnnuel(new BigDecimal(((EditText) this.findViewById(id.editTaux)).getText().toString()).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));
        Spinner spinnerFrequence = (Spinner) this.findViewById(id.spinnerFrequenceVariation);
        placement.setDuree(MainActivity.calculeDureeEnMois(this.calendarDebut, this.calendarFin));

        placement.setDateDebut(this.calendarDebut);

        switch (spinnerFrequence.getSelectedItemPosition()) {
            case 0:
                placement.setFrequenceVariation(enumFrequenceVariation.MENSUELLE);
                break;
            case 1:
                placement.setFrequenceVariation(enumFrequenceVariation.TRIMESTRIELLE);
                break;
            default:
                throw new IllegalArgumentException("frequence variation");
        }

        Intent intent = new Intent(this, ResultTableActivity.class);
        intent.putExtra("placement", placement);
        this.startActivity(intent);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        TextView tv;
        ToggleButton toggleDateFin = (ToggleButton) this.findViewById(id.toggleButtonChoisirDateFin);
        if (toggleDateFin.isChecked()) {
            tv = (TextView) this.findViewById(id.labelSelectedDateFin);
            this.calendarFin.set(year, month, day);
            tv.setText(this.dateFormat.format(this.calendarFin.getTime()));
        } else {
            tv = (TextView) this.findViewById(id.labelSelectedDateDebut);
            this.calendarDebut.set(year, month, day);
            tv.setText(this.dateFormat.format(this.calendarDebut.getTime()));
        }

        TextView labelDuree = (TextView) this.findViewById(id.labelDuree);
        int duree = MainActivity.calculeDureeEnMois(this.calendarDebut, this.calendarFin);

        if (duree <= 0 || duree > Placement.MAXDUREE) {
            labelDuree.setTextColor(Color.RED);
        } else {
            labelDuree.setTextColor(this.defaultDureeLabelColor);
        }
        labelDuree.setText(this.getString(string.dureeXmois, duree));
    }
}
