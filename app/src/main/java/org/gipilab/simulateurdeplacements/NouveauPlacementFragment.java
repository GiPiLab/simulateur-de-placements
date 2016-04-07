package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
 * {@link NouveauPlacementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NouveauPlacementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NouveauPlacementFragment extends Fragment implements DatePicker.OnDateChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

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
        NouveauPlacementFragment fragment = new NouveauPlacementFragment();
    /*    Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDateSelectionSystem();

        Button btnCalculer = (Button) view.findViewById(R.id.buttonCalculer);
        btnCalculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateInputs()) {
                    return;
                }

                Placement placement;

                RadioGroup groupMode = (RadioGroup) getView().findViewById(R.id.radioGroupMode);
                switch (groupMode.getCheckedRadioButtonId()) {
                    case R.id.radioButtonModeQuinzaine:
                        placement = new PlacementQuinzaine();
                        break;

                    case R.id.radioButtonModeNormal:
                        placement = new PlacementSansQuinzaine();
                        break;
                    default:
                        throw new IllegalArgumentException("Erreur mode");
                }

                placement.setCapitalInitial(new BigDecimal(((EditText) getView().findViewById(R.id.editCapital)).getText().toString()));
                placement.setVariation(new BigDecimal(((EditText) getView().findViewById(R.id.editVariation)).getText().toString()));
                placement.setTauxAnnuel(new BigDecimal(((EditText) getView().findViewById(R.id.editTaux)).getText().toString()).divide(BigDecimal.valueOf(100), MathContext.DECIMAL128));
                Spinner spinnerFrequence = (Spinner) getView().findViewById(R.id.spinnerFrequenceVariation);
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
                    mListener.onPlacementValidated(placement);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nouveau_placement, container, false);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
        return getString(R.string.dureeXmois, PeriodFormat.wordBased().print(duration));
    }

    private void initDateSelectionSystem() {
        dateDebut = new LocalDate(LocalDate.now());
        dateFin = dateDebut.plusYears(1);

        dateFormat = DateTimeFormat.longDate();
        final DatePicker dp = (DatePicker) getView().findViewById(R.id.datePicker);
        dp.init(dateFin.getYear(), dateFin.getMonthOfYear() - 1, dateFin.getDayOfMonth(), this);
        dp.setMaxDate(dateDebut.plusYears(200).toDate().getTime());
        TextView tv = (TextView) getView().findViewById(R.id.labelSelectedDateDebut);
        tv.setText(dateFormat.print(dateDebut));
        tv = (TextView) getView().findViewById(R.id.labelSelectedDateFin);
        tv.setText(dateFormat.print(dateFin));

        updateLabelDuree(dateDebut, dateFin);

        RadioButton dateButtonFin = (RadioButton) getView().findViewById(R.id.radioButtonDateFin);

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

        RadioGroup modeLivret = (RadioGroup) getView().findViewById(R.id.radioGroupMode);
        modeLivret.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateLabelDuree(dateDebut, dateFin);
            }
        });
    }

    private boolean validateInputs() {
        EditText editCapital = (EditText) getView().findViewById(R.id.editCapital);
        EditText editTaux = (EditText) getView().findViewById(R.id.editTaux);
        EditText editVariation = (EditText) getView().findViewById(R.id.editVariation);

        BigDecimal capital;

        if (editCapital.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.saisissezLeCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        capital = new BigDecimal(editCapital.getText().toString());

        if (capital.compareTo(BigDecimal.ZERO) <= 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.capitalDoitEtrePositif, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        if (capital.compareTo(Placement.MAXCAPITAL) > 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.capitalDoitEtreInferieurA, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (editTaux.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.saisissezLeTaux, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        BigDecimal taux = new BigDecimal(editTaux.getText().toString());

        if (taux.compareTo(BigDecimal.ZERO) < 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.tauxDoitEtrePositifOuNul, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (taux.compareTo(Placement.MAXTAUX) > 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.tauxDoitEtreInferieurA, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        Placement p;
        RadioGroup groupMode = (RadioGroup) getView().findViewById(R.id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case R.id.radioButtonModeQuinzaine:
                p = new PlacementQuinzaine();
                break;

            case R.id.radioButtonModeNormal:
                p = new PlacementSansQuinzaine();
                break;
            default:
                throw new IllegalArgumentException("Erreur mode");
        }

        int duree = p.approximeDureeEnEcheances(dateDebut, dateFin);
        if (duree > p.getMAXECHEANCES()) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.dureeTropLongue, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (duree < 0 || dateDebut.toDate().getTime() == dateFin.toDate().getTime()) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.dureeDoitEtrePositive, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        if (editVariation.getText().length() == 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.saisissezLaVariationPeriodique, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        BigDecimal variation = new BigDecimal(editVariation.getText().toString());
        if (variation.compareTo(BigDecimal.ZERO) < 0 && variation.abs().compareTo(capital) > 0 && variation.compareTo(Placement.MAXVARIATION) >= 0) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.variationDoitEtreInferieureAuCapital, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }

    void updateLabelDuree(LocalDate dateDebut, LocalDate dateFin) {
        TextView labelDuree = (TextView) getView().findViewById(R.id.labelDuree);

        Placement p;
        RadioGroup groupMode = (RadioGroup) getView().findViewById(R.id.radioGroupMode);
        switch (groupMode.getCheckedRadioButtonId()) {
            case R.id.radioButtonModeQuinzaine:
                p = new PlacementQuinzaine();
                break;

            case R.id.radioButtonModeNormal:
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
        RadioButton buttonDateFin = (RadioButton) getView().findViewById(R.id.radioButtonDateFin);
        if (buttonDateFin.isChecked()) {
            tv = (TextView) getView().findViewById(R.id.labelSelectedDateFin);
            dateFin = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateFin));
        } else {
            tv = (TextView) getView().findViewById(R.id.labelSelectedDateDebut);
            dateDebut = new LocalDate(year, month + 1, day);
            tv.setText(dateFormat.print(dateDebut));
        }
        updateLabelDuree(dateDebut, dateFin);
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
        void onPlacementValidated(Placement placement);
    }

}
