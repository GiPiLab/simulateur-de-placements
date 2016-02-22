package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    private Calendar calendarDebut, calendarFin;
    private DateFormat dateFormat;
    private int defaultDureeLabelColor;

    private void initDateSelectionSystem() {
        calendarDebut = Calendar.getInstance();
        calendarFin = Calendar.getInstance();

        calendarFin.set(calendarDebut.get(Calendar.YEAR), calendarDebut.get(Calendar.MONTH) + 12, calendarDebut.get(Calendar.DAY_OF_MONTH));

        dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        final DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        dp.init(calendarFin.get(Calendar.YEAR), calendarFin.get(Calendar.MONTH), calendarFin.get(Calendar.DAY_OF_MONTH), this);
        TextView tv = (TextView) findViewById(R.id.labelSelectedDateDebut);
        tv.setText(dateFormat.format(calendarDebut.getTime()));
        tv = (TextView) findViewById(R.id.labelSelectedDateFin);
        tv.setText(dateFormat.format(calendarFin.getTime()));
        tv = (TextView) findViewById(R.id.labelDuree);
        tv.setText(getString(R.string.dureeXmois, calculeDureeEnMois(calendarDebut, calendarFin)));
        defaultDureeLabelColor = tv.getCurrentTextColor();
        Switch dateSwitch = (Switch) findViewById(R.id.switchDateDeFin);

        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton b, boolean isChecked) {

                if (isChecked) {
                    dp.updateDate(calendarFin.get(Calendar.YEAR), calendarFin.get(Calendar.MONTH), calendarFin.get(Calendar.DAY_OF_MONTH));
                } else {
                    dp.updateDate(calendarDebut.get(Calendar.YEAR), calendarDebut.get(Calendar.MONTH), calendarDebut.get(Calendar.DAY_OF_MONTH));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDateSelectionSystem();
    }

    public boolean validateInputs() {
        EditText editCapital = (EditText) findViewById(R.id.editCapital);
        EditText editTaux = (EditText) findViewById(R.id.editTaux);
        EditText editVariation = (EditText) findViewById(R.id.editVariation);

        BigDecimal capital;

        if (editCapital.getText().length() == 0) {
            Toast toast = Toast.makeText(this, R.string.saisissezLeCapital, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            capital = new BigDecimal(editCapital.getText().toString());

            if (capital.compareTo(BigDecimal.ZERO) <= 0) {
                Toast toast = Toast.makeText(this, R.string.capitalDoitEtrePositif, Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
            if (capital.compareTo(Placement.MAXCAPITAL) > 0) {
                Toast toast = Toast.makeText(this, getString(R.string.capitalDoitEtreInferieurA, Placement.MAXCAPITAL.toString()), Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }

        if (editTaux.getText().length() == 0) {
            Toast toast = Toast.makeText(this, R.string.saisissezLeTaux, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            BigDecimal taux = new BigDecimal(editTaux.getText().toString());

            if (taux.compareTo(BigDecimal.ZERO) < 0) {
                Toast toast = Toast.makeText(this, R.string.tauxDoitEtrePositifOuNul, Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            if (taux.compareTo(Placement.MAXTAUX) > 0) {
                Toast toast = Toast.makeText(this, getString(R.string.tauxDoitEtreInferieurA, Placement.MAXTAUX.toString()), Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }

        int duree = calculeDureeEnMois(calendarDebut, calendarFin);
        if (duree > Placement.MAXDUREE) {
            Toast toast = Toast.makeText(this, getString(R.string.dureeDoitEtreInferieureA, Placement.MAXDUREE), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (duree <= 0) {
            Toast toast = Toast.makeText(this, R.string.dureeDoitEtrePositive, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (editVariation.getText().length() == 0) {
            Toast toast = Toast.makeText(this, R.string.saisissezLaVariationPeriodique, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            BigDecimal variation = new BigDecimal(editVariation.getText().toString());
            if (variation.compareTo(BigDecimal.ZERO) < 0 && variation.abs().compareTo(capital) > 0 && variation.compareTo(Placement.MAXVARIATION) >= 0) {
                Toast toast = Toast.makeText(this, R.string.variationDoitEtreInferieureAuCapital, Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        }
        return true;
    }


    public void buttonCalculerClick(@SuppressWarnings("UnusedParameters") View v) {
        if (!validateInputs()) {
            return;
        }

        Placement placement = new Placement();
        placement.setCapitalInitial(new BigDecimal(((EditText) findViewById(R.id.editCapital)).getText().toString()));
        placement.setVariation(new BigDecimal(((EditText) findViewById(R.id.editVariation)).getText().toString()));
        placement.setTauxAnnuel(new BigDecimal(((EditText) findViewById(R.id.editTaux)).getText().toString()).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));
        Spinner spinnerFrequence = (Spinner) findViewById(R.id.spinnerFrequenceVariation);
        placement.setDuree(calculeDureeEnMois(calendarDebut, calendarFin));

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        placement.setDateDebut(cal);

        switch (spinnerFrequence.getSelectedItemPosition()) {
            case 0:
                placement.setFrequenceVariation(enumFrequenceVariation.MENSUELLE);
                break;
            case 1:
                placement.setFrequenceVariation(enumFrequenceVariation.TRIMESTRIELLE);
                break;
        }

        Intent intent = new Intent(this, ResultTableActivity.class);
        intent.putExtra("placement", placement);
        startActivity(intent);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        TextView tv;
        Switch switchDateFin = (Switch) findViewById(R.id.switchDateDeFin);
        if (!switchDateFin.isChecked()) {
            tv = (TextView) findViewById(R.id.labelSelectedDateDebut);
            calendarDebut.set(year, month, day);
            tv.setText(dateFormat.format(calendarDebut.getTime()));
        } else {
            tv = (TextView) findViewById(R.id.labelSelectedDateFin);
            calendarFin.set(year, month, day);
            tv.setText(dateFormat.format(calendarFin.getTime()));
        }

        TextView labelDuree = (TextView) findViewById(R.id.labelDuree);
        int duree = calculeDureeEnMois(calendarDebut, calendarFin);

        if (duree <= 0 || duree > Placement.MAXDUREE) {
            labelDuree.setTextColor(Color.RED);
        } else {
            labelDuree.setTextColor(defaultDureeLabelColor);
        }
        labelDuree.setText(getString(R.string.dureeXmois, duree));
    }

    private int calculeDureeEnMois(Calendar debut, Calendar fin) {
        int diffYear = fin.get(Calendar.YEAR) - debut.get(Calendar.YEAR);
        return (diffYear * 12 + fin.get(Calendar.MONTH) - debut.get(Calendar.MONTH));

    }
}
