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
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import java.math.BigDecimal;
import java.math.MathContext;


public class MainActivity extends AppCompatActivity implements OnDateChangedListener {

    private LocalDate dateDebut, dateFin;
    private DateTimeFormatter dateFormat;
    private int defaultDureeLabelColor;

    private String formatDuree() {
        Period duration = new Period(dateDebut, dateFin);
        return getString(string.dureeXmois, PeriodFormat.wordBased().print(duration), Placement.calculeDureeEnMois(this.dateDebut, this.dateFin));
    }

    private void initDateSelectionSystem() {
        this.dateDebut = new LocalDate(LocalDate.now());
        this.dateFin = dateDebut.plusYears(1);

        this.dateFormat = DateTimeFormat.longDate();
        final DatePicker dp = (DatePicker) this.findViewById(id.datePicker);
        dp.init(this.dateFin.getYear(), this.dateFin.getMonthOfYear() - 1, this.dateFin.getDayOfMonth(), this);
        TextView tv = (TextView) this.findViewById(id.labelSelectedDateDebut);
        tv.setText(this.dateFormat.print(this.dateDebut));
        tv = (TextView) this.findViewById(id.labelSelectedDateFin);
        tv.setText(this.dateFormat.print(this.dateFin));
        tv = (TextView) this.findViewById(id.labelDuree);

        tv.setText(formatDuree());
        this.defaultDureeLabelColor = tv.getCurrentTextColor();
        ToggleButton dateButton = (ToggleButton) this.findViewById(id.toggleButtonChoisirDateFin);

        dateButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton b, boolean isChecked) {

                if (isChecked) {
                    dp.updateDate(MainActivity.this.dateFin.getYear(), MainActivity.this.dateFin.getMonthOfYear() - 1, MainActivity.this.dateFin.getDayOfMonth());
                } else {
                    dp.updateDate(MainActivity.this.dateDebut.getYear(), MainActivity.this.dateDebut.getMonthOfYear() - 1, MainActivity.this.dateDebut.getDayOfMonth());
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

        int duree = Placement.calculeDureeEnMois(this.dateDebut, this.dateFin);
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
        placement.setDatesPlacement(dateDebut, dateFin);

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
            this.dateFin = new LocalDate(year, month + 1, day);
            tv.setText(this.dateFormat.print(this.dateFin));
        } else {
            tv = (TextView) this.findViewById(id.labelSelectedDateDebut);
            this.dateDebut = new LocalDate(year, month + 1, day);
            tv.setText(this.dateFormat.print(this.dateDebut));
        }

        TextView labelDuree = (TextView) this.findViewById(id.labelDuree);
        int duree = Placement.calculeDureeEnMois(this.dateDebut, this.dateFin);

        if (duree <= 0 || duree > Placement.MAXDUREE) {
            labelDuree.setTextColor(Color.RED);
        } else {
            labelDuree.setTextColor(this.defaultDureeLabelColor);
        }

        labelDuree.setText(formatDuree());
    }
}
