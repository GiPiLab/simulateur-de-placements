package org.gipilab.simulateurdeplacements;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

    private String formatDuree() {
        Period duration = new Period(dateDebut, dateFin);
        return getString(string.dureeXmois, PeriodFormat.wordBased().print(duration));
    }

    private void initDateSelectionSystem() {
        dateDebut = new LocalDate(LocalDate.now());
        dateFin = dateDebut.plusYears(1);

        dateFormat = DateTimeFormat.longDate();
        final DatePicker dp = (DatePicker) findViewById(id.datePicker);
        dp.init(dateFin.getYear(), dateFin.getMonthOfYear() - 1, dateFin.getDayOfMonth(), this);
        dp.setMaxDate(dateDebut.plusYears(200).toDate().getTime());
        TextView tv = (TextView) findViewById(id.labelSelectedDateDebut);
        tv.setText(dateFormat.print(dateDebut));
        tv = (TextView) findViewById(id.labelSelectedDateFin);
        tv.setText(dateFormat.print(dateFin));

        updateLabelDuree(dateDebut, dateFin);

        RadioButton dateButtonFin = (RadioButton) this.findViewById(id.radioButtonDateFin);

        dateButtonFin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton b, boolean isChecked) {

                if (isChecked) {
                    dp.updateDate(dateFin.getYear(), dateFin.getMonthOfYear() - 1, dateFin.getDayOfMonth());
                } else {
                    dp.updateDate(dateDebut.getYear(), dateDebut.getMonthOfYear() - 1, dateDebut.getDayOfMonth());
                }
            }
        });

        RadioGroup modeLivret = (RadioGroup) findViewById(id.radioGroupMode);
        modeLivret.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateLabelDuree(dateDebut, dateFin);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        initDateSelectionSystem();
    }

    private boolean validateInputs() {
        EditText editCapital = (EditText) findViewById(id.editCapital);
        EditText editTaux = (EditText) findViewById(id.editTaux);
        EditText editVariation = (EditText) findViewById(id.editVariation);

        BigDecimal capital;

        if (editCapital.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.saisissezLeCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        capital = new BigDecimal(editCapital.getText().toString());

        if (capital.compareTo(BigDecimal.ZERO) <= 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.capitalDoitEtrePositif, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        if (capital.compareTo(Placement.MAXCAPITAL) > 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.capitalDoitEtreInferieurA, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (editTaux.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.saisissezLeTaux, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        BigDecimal taux = new BigDecimal(editTaux.getText().toString());

        if (taux.compareTo(BigDecimal.ZERO) < 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.tauxDoitEtrePositifOuNul, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (taux.compareTo(Placement.MAXTAUX) > 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.tauxDoitEtreInferieurA, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        Placement p;
        RadioGroup groupMode = (RadioGroup) findViewById(id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case id.radioButtonModeQuinzaine:
                p = new PlacementQuinzaine();
                break;

            case id.radioButtonModeNormal:
                p = new PlacementSansQuinzaine();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }

        int duree = p.approximeDureeEnEcheances(dateDebut, dateFin);
        if (duree > p.getMAXECHEANCES()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.dureeTropLongue, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (duree < 0 || dateDebut.toDate().getTime() == dateFin.toDate().getTime()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.dureeDoitEtrePositive, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (editVariation.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.saisissezLaVariationPeriodique, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        BigDecimal variation = new BigDecimal(editVariation.getText().toString());
        if (variation.compareTo(BigDecimal.ZERO) < 0 && variation.abs().compareTo(capital) > 0 && variation.compareTo(Placement.MAXVARIATION) >= 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), string.variationDoitEtreInferieureAuCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }


    public void buttonCalculerClick(@SuppressWarnings("UnusedParameters") View v) {
        if (!validateInputs()) {
            return;
        }

        Placement placement;

        RadioGroup groupMode = (RadioGroup) findViewById(id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case id.radioButtonModeQuinzaine:
                placement = new PlacementQuinzaine();
                break;

            case id.radioButtonModeNormal:
                placement = new PlacementSansQuinzaine();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }


        placement.setCapitalInitial(new BigDecimal(((EditText) findViewById(id.editCapital)).getText().toString()));
        placement.setVariation(new BigDecimal(((EditText) findViewById(id.editVariation)).getText().toString()));
        placement.setTauxAnnuel(new BigDecimal(((EditText) findViewById(id.editTaux)).getText().toString()).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));
        Spinner spinnerFrequence = (Spinner) findViewById(id.spinnerFrequenceVariation);
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


    void updateLabelDuree(LocalDate dateDebut, LocalDate dateFin) {
        TextView labelDuree = (TextView) findViewById(id.labelDuree);

        Placement p;
        RadioGroup groupMode = (RadioGroup) findViewById(id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case id.radioButtonModeQuinzaine:
                p = new PlacementQuinzaine();
                break;

            case id.radioButtonModeNormal:
                p = new PlacementSansQuinzaine();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }

        int dureeApprochee = p.approximeDureeEnEcheances(dateDebut, dateFin);

        if (dureeApprochee > p.getMAXECHEANCES() || dureeApprochee < 0 || this.dateDebut.toDate().getTime() == this.dateFin.toDate().getTime()) {
            labelDuree.setTextColor(Color.RED);
        } else {
            labelDuree.setTextColor(Color.BLACK);
        }
        labelDuree.setText(formatDuree());
    }


    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        TextView tv;
        RadioButton buttonDateFin = (RadioButton) findViewById(id.radioButtonDateFin);
        if (buttonDateFin.isChecked()) {
            tv = (TextView) findViewById(id.labelSelectedDateFin);
            dateFin = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateFin));
        } else {
            tv = (TextView) findViewById(id.labelSelectedDateDebut);
            dateDebut = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateDebut));
        }
        updateLabelDuree(dateDebut, dateFin);
    }
}
