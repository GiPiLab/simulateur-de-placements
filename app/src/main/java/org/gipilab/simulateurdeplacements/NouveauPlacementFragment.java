package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NouveauPlacementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NouveauPlacementFragment extends Fragment implements OnDateChangedListener {

    private OnFragmentInteractionListener mListener;
    private LocalDate dateDebut, dateFin;
    private DateTimeFormatter dateFormat;

    public NouveauPlacementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NouveauPlacementFragment.
     */

    public static NouveauPlacementFragment newInstance() {
        /*    Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return new NouveauPlacementFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDateSelectionSystem();

        Button btnCalculer = (Button) view.findViewById(id.buttonCalculer);
        btnCalculer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (areInputsInvalid()) {
                    return;
                }

                if (getView() == null) {
                    Log.e("SIMUPLACEMENT", "Null getView");
                    return;
                }

                RadioGroup groupMode = (RadioGroup) getView().findViewById(id.radioGroupMode);
                if (groupMode == null) {
                    Log.e("SIMUPLACEMENT", "Null groupMode");
                    return;
                }
                Placement placement;
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

                placement.setCapitalInitial(new BigDecimal(((EditText) getView().findViewById(id.editCapital)).getText().toString()));
                placement.setVariation(new BigDecimal(((EditText) getView().findViewById(id.editVariation)).getText().toString()));
                placement.setTauxAnnuel(new BigDecimal(((EditText) getView().findViewById(id.editTaux)).getText().toString()).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));
                Spinner spinnerFrequence = (Spinner) getView().findViewById(id.spinnerFrequenceVariation);
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

                if (mListener != null) {
                    mListener.onPlacementRequestValidatedFromNouveauPlacementFragment(placement);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layout.fragment_nouveau_placement, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String formatDuree() {
        Period duration = new Period(dateDebut, dateFin);
        return getString(string.dureeXmois, PeriodFormat.wordBased().print(duration));
    }

    private void initDateSelectionSystem() {
        dateDebut = new LocalDate(LocalDate.now());
        dateFin = dateDebut.plusYears(1);

        dateFormat = DateTimeFormat.longDate();

        if (getView() == null) {
            Log.e("SIMUPLACEMENT", "Null getView");
            return;
        }


        final DatePicker dp = (DatePicker) getView().findViewById(id.datePicker);
        if (dp == null) {
            Log.e("SIMUPLACEMENT", "Null datePicker");
            return;
        }

        dp.init(dateFin.getYear(), dateFin.getMonthOfYear() - 1, dateFin.getDayOfMonth(), this);
        dp.setMaxDate(dateDebut.plusYears(200).toDate().getTime());
        TextView tv = (TextView) getView().findViewById(id.labelSelectedDateDebut);
        tv.setText(dateFormat.print(dateDebut));
        TextView labelDateFin = (TextView) getView().findViewById(id.labelSelectedDateFin);
        labelDateFin.setText(dateFormat.print(dateFin));

        updateLabelDuree(dateDebut, dateFin);

        RadioButton dateButtonFin = (RadioButton) getView().findViewById(id.radioButtonDateFin);

        dateButtonFin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton b, boolean isChecked) {

                if (isChecked) {
                    dp.updateDate(dateFin.getYear(), dateFin.getMonthOfYear() - 1, dateFin.getDayOfMonth());
                } else {
                    dp.updateDate(dateDebut.getYear(), dateDebut.getMonthOfYear() - 1, dateDebut.getDayOfMonth());
                }
            }
        });

        RadioGroup modeLivret = (RadioGroup) getView().findViewById(id.radioGroupMode);
        modeLivret.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateLabelDuree(dateDebut, dateFin);
            }
        });
    }

    private boolean areInputsInvalid() {
        if (getView() == null) {
            Log.e("SIMUPLACEMENT", "Null getView");
            return true;
        }

        EditText editCapital = (EditText) getView().findViewById(id.editCapital);

        EditText editTaux = (EditText) getView().findViewById(id.editTaux);

        EditText editVariation = (EditText) getView().findViewById(id.editVariation);

        if (editCapital == null || editTaux == null || editVariation == null) {
            Log.e("SIMUPLACEMENT", "Null edit field in validateInput");
            return true;
        }

        if (editCapital.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.saisissezLeCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }
        BigDecimal capital = new BigDecimal(editCapital.getText().toString());

        if (capital.compareTo(BigDecimal.ZERO) <= 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.capitalDoitEtrePositif, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }
        if (capital.compareTo(Placement.MAXCAPITAL) > 0) {
            Snackbar snackbar = Snackbar.make(getView(), getString(string.capitalDoitEtreInferieurA, Placement.MAXCAPITAL), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }

        if (editTaux.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.saisissezLeTaux, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }
        BigDecimal taux = new BigDecimal(editTaux.getText().toString());

        if (taux.compareTo(BigDecimal.ZERO) < 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.tauxDoitEtrePositifOuNul, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }

        if (taux.compareTo(Placement.MAXTAUX) > 0) {
            Snackbar snackbar = Snackbar.make(getView(), getString(string.tauxDoitEtreInferieurA, Placement.MAXTAUX), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }


        int dureeApprochee, maxEcheances;
        RadioGroup groupMode = (RadioGroup) getView().findViewById(id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case id.radioButtonModeQuinzaine:
                dureeApprochee = PlacementQuinzaine.approximeDureeEnEcheances(dateDebut, dateFin);
                maxEcheances = PlacementQuinzaine.getMAXECHEANCES();
                break;

            case id.radioButtonModeNormal:
                dureeApprochee = PlacementSansQuinzaine.approximeDureeEnEcheances(dateDebut, dateFin);
                maxEcheances = PlacementSansQuinzaine.getMAXECHEANCES();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }


        if (dureeApprochee > maxEcheances) {
            Snackbar snackbar = Snackbar.make(getView(), string.dureeTropLongue, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }

        if (dateDebut.toDate().getTime() >= dateFin.toDate().getTime()) {
            Snackbar snackbar = Snackbar.make(getView(), string.dureeDoitEtrePositive, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }

        if (editVariation.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.saisissezLaVariationPeriodique, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }
        BigDecimal variation = new BigDecimal(editVariation.getText().toString());
        if (variation.compareTo(BigDecimal.ZERO) < 0 && variation.abs().compareTo(capital) > 0 && variation.compareTo(Placement.MAXVARIATION) >= 0) {
            Snackbar snackbar = Snackbar.make(getView(), string.variationDoitEtreInferieureAuCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return true;
        }
        return false;
    }

    private void updateLabelDuree(LocalDate dateDebut, LocalDate dateFin) {

        if (getView() == null) {
            Log.e("SIMUPLACEMENT", "Null getView");
            return;
        }

        TextView labelDuree = (TextView) getView().findViewById(id.labelDuree);

        if (labelDuree == null) {
            Log.e("SIMUPLACEMENT", "Null labelDuree");
            return;
        }

        RadioGroup groupMode = (RadioGroup) getView().findViewById(id.radioGroupMode);
        int dureeApprochee, maxEcheances;
        switch (groupMode.getCheckedRadioButtonId()) {
            case id.radioButtonModeQuinzaine:
                dureeApprochee = PlacementQuinzaine.approximeDureeEnEcheances(dateDebut, dateFin);
                maxEcheances = PlacementQuinzaine.getMAXECHEANCES();
                break;

            case id.radioButtonModeNormal:
                dureeApprochee = PlacementSansQuinzaine.approximeDureeEnEcheances(dateDebut, dateFin);
                maxEcheances = PlacementSansQuinzaine.getMAXECHEANCES();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }


        if (dureeApprochee > maxEcheances || this.dateDebut.toDate().getTime() >= this.dateFin.toDate().getTime()) {
            labelDuree.setTextColor(Color.RED);
        } else {
            labelDuree.setTextColor(Color.BLACK);
        }
        labelDuree.setText(formatDuree());
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        if (getView() == null) {
            Log.e("SIMUPLACEMENT", "Null getView");
            return;
        }

        RadioButton buttonDateFin = (RadioButton) getView().findViewById(id.radioButtonDateFin);

        if (buttonDateFin == null) {
            Log.e("SIMUPLACEMENT", "Null buttonDateFin");
            return;
        }

        TextView tv;
        if (buttonDateFin.isChecked()) {
            tv = (TextView) getView().findViewById(id.labelSelectedDateFin);
            dateFin = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateFin));
        } else {
            tv = (TextView) getView().findViewById(id.labelSelectedDateDebut);
            dateDebut = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateDebut));
        }
        updateLabelDuree(dateDebut, dateFin);
    }

    public void loadPlacement(Placement p) {

        if (getView() == null) {
            Log.e("SIMUPLACEMENT", "Null getView");
            return;
        }

        DatePicker dp = (DatePicker) getView().findViewById(id.datePicker);

        if (dp == null) {
            Log.e("SIMUPLACEMENT", "Null datepicker");
            return;
        }
        RadioButton buttonDateFin = (RadioButton) getView().findViewById(id.radioButtonDateFin);
        this.dateDebut = p.getDateDebut();
        this.dateFin = p.getDateFin();

        buttonDateFin.setChecked(true);
        dp.updateDate(p.getDateFin().getYear(), p.getDateFin().getMonthOfYear() - 1, p.getDateFin().getDayOfMonth());
        updateLabelDuree(p.getDateDebut(), p.getDateFin());

        TextView dateDebut = (TextView) getView().findViewById(id.labelSelectedDateDebut);
        dateDebut.setText(dateFormat.print(p.getDateDebut()));
        TextView dateFin = (TextView) getView().findViewById(id.labelSelectedDateFin);
        dateFin.setText(dateFormat.print(p.getDateFin()));

        EditText capital = (EditText) getView().findViewById(id.editCapital);
        capital.setText(p.getCapitalInitial().toPlainString());

        EditText taux = (EditText) getView().findViewById(id.editTaux);
        taux.setText(p.getTauxAnnuel().multiply(BigDecimal.valueOf(100), MathContext.DECIMAL128).toPlainString());

        EditText variation = (EditText) getView().findViewById(id.editVariation);
        variation.setText(p.getVariation().toPlainString());

        Spinner freqVar = (Spinner) getView().findViewById(id.spinnerFrequenceVariation);

        switch (p.getFrequenceVariation()) {
            case MENSUELLE:
                freqVar.setSelection(0);
                break;
            case TRIMESTRIELLE:
                freqVar.setSelection(1);
                break;
        }

        variation.setText(p.getVariation().toPlainString());

        RadioButton buttonModeLivret = (RadioButton) getView().findViewById(id.radioButtonModeQuinzaine);
        switch (p.getModeCalculPlacement()) {
            case QUINZAINE:
                buttonModeLivret.setChecked(true);
                break;
            case SANSQUINZAINE:
                buttonModeLivret.setChecked(false);
                break;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onPlacementRequestValidatedFromNouveauPlacementFragment(Placement placement);
    }

}
