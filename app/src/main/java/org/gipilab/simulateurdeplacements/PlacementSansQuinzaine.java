package org.gipilab.simulateurdeplacements;

import android.content.Context;

import com.orm.SugarRecord;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibault on 03/03/16.
 */
public class PlacementSansQuinzaine extends Placement {


    public PlacementSansQuinzaine() {
        setModeCalculPlacement(enumModeCalculPlacement.SANSQUINZAINE);
    }


    static List<PlacementSansQuinzaine> getAll() {
        return SugarRecord.listAll(PlacementSansQuinzaine.class);
    }


    @Override
    int getMAXECHEANCES() {
        return 1200;
    }

    /**
     * Approche la durée de manière rapide
     *
     * @param dateDebut
     * @param dateFin
     * @return la durée approchée en mois sans le mois résiduel éventuel
     */
    @Override
    int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        return Months.monthsBetween(dateDebut, dateFin).getMonths();
    }

    @Override
    int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        int duree = Months.monthsBetween(dateDebut, dateFin).getMonths();

        LocalDate tmpDate = dateDebut.plusMonths(duree);
        int residuel = Days.daysBetween(tmpDate, dateFin).getDays() + dateDebut.getDayOfMonth() - 1;

        if (residuel >= 30) {
            duree++;
        }

        return duree;
    }

    @Override
    ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances) {
        ArrayList<Annualite> lesAnnualites = new ArrayList<>();

        if (lesEcheances.isEmpty())
            return lesAnnualites;

        Annualite annualite = new Annualite();

        annualite.setCapitalPlaceDebutAnnee(lesEcheances.get(0).getCapitalCourant());

        int iemeAnnee = 1;

        for (int i = 1; i <= lesEcheances.size(); i++) {
            Echeance mens = lesEcheances.get(i - 1);
            annualite.getEcheances().add(mens);
            annualite.setInteretsFinAnnee(annualite.getInteretsFinAnnee().add(mens.getInteretsObtenus()));

            if (mens.getDateDebutEcheance().getMonthOfYear() == 12) {
                annualite.setValeurAcquiseFinAnnee(mens.getValeurAcquise());
                annualite.setCapitalPlaceFinAnnee(mens.getCapitalCourant());
                annualite.setInteretsTotaux(mens.getInteretsTotaux());
                lesAnnualites.add(annualite);
                annualite = new Annualite();
                iemeAnnee++;
                annualite.setIeme(iemeAnnee);
                annualite.setCapitalPlaceDebutAnnee(mens.getValeurAcquise());
            }
        }
        if (!annualite.getEcheances().isEmpty()) {
            Echeance mens = lesEcheances.get(lesEcheances.size() - 1);

            annualite.setCapitalPlaceFinAnnee(mens.getCapitalCourant());
            annualite.setValeurAcquiseFinAnnee(mens.getValeurAcquise());
            annualite.setInteretsTotaux(mens.getInteretsTotaux());
            lesAnnualites.add(annualite);
        }

        return lesAnnualites;
    }


    @Override
    ArrayList<Echeance> tableauPlacement() {
        ArrayList<Echeance> lesMensualites = new ArrayList<>();

        LocalDate cal;

        if (this.getDateDebut() == null)
            cal = new LocalDate(LocalDate.now());
        else cal = this.getDateDebut();

        BigDecimal tauxJournalier = getTauxAnnuel().divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);
        BigDecimal capitalPlace = getCapitalInitial();
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;

        int i;
        int dur = getDuree();
        for (i = 1; i <= dur; i++) {

            Echeance mensualite = new Echeance();
            mensualite.setDateDebutEcheance(cal);
            mensualite.setDateFinEcheance(cal.dayOfMonth().withMaximumValue());

            if (i > 1 && getVariation().compareTo(BigDecimal.ZERO) != 0 && capitalPlace.add(getVariation()).compareTo(BigDecimal.ZERO) >= 0) {
                switch (getFrequenceVariation()) {
                    case MENSUELLE:
                        mensualite.setVariation(getVariation());
                        capitalPlace = capitalPlace.add(getVariation());
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 3 == 0) {
                            mensualite.setVariation(getVariation());
                            capitalPlace = capitalPlace.add(getVariation());
                        }
                        break;
                }
            }
            mensualite.setIeme(i);
            mensualite.setCapitalInitial(getCapitalInitial());
            mensualite.setCapitalCourant(capitalPlace);

            int nbJours = mensualite.getDateFinEcheance().getDayOfMonth() - mensualite.getDateDebutEcheance().getDayOfMonth();
            mensualite.setInteretsObtenus(capitalPlace.multiply(tauxJournalier, MathContext.DECIMAL128).multiply(BigDecimal.valueOf(nbJours)));

            mensualite.setInteretsTotaux(interetsTotaux.add(mensualite.getInteretsObtenus()));
            interetsTotaux = mensualite.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(mensualite.getInteretsObtenus());
            mensualite.setValeurAcquise(capitalPlace.add(interetsDeAnnee));


            //Capitalisation
            if (mensualite.getDateDebutEcheance().getMonthOfYear() == 12) {
                capitalPlace = capitalPlace.add(interetsDeAnnee);
                interetsDeAnnee = BigDecimal.ZERO;
            }

            if (mensualite.getCapitalCourant().compareTo(BigDecimal.ZERO) >= 0) {
                lesMensualites.add(mensualite);
            } else break;

            cal = cal.plusMonths(1).withDayOfMonth(1);

        }

        //Ajoute les jours manquants

        int daysLeft = Days.daysBetween(cal, getDateFin()).getDays();

        if (daysLeft > 0) {

            Echeance lastEcheance = new Echeance();
            lastEcheance.setIeme(i);

            if (i > 1 && getVariation().compareTo(BigDecimal.ZERO) != 0 && capitalPlace.add(getVariation()).compareTo(BigDecimal.ZERO) >= 0) {
                switch (getFrequenceVariation()) {
                    case MENSUELLE:
                        lastEcheance.setVariation(getVariation());
                        capitalPlace = capitalPlace.add(getVariation());
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 3 == 0) {
                            lastEcheance.setVariation(getVariation());
                            capitalPlace = capitalPlace.add(getVariation());
                        }
                        break;
                }
            }

            lastEcheance.setDateDebutEcheance(cal);
            lastEcheance.setDateFinEcheance(getDateFin());
            lastEcheance.setCapitalInitial(getCapitalInitial());
            lastEcheance.setCapitalCourant(capitalPlace);
            lastEcheance.setInteretsObtenus(capitalPlace.multiply(tauxJournalier, MathContext.DECIMAL128)
                    .multiply(BigDecimal.valueOf(daysLeft), MathContext.DECIMAL128));
            lastEcheance.setInteretsTotaux(interetsTotaux.add(lastEcheance.getInteretsObtenus()));
            interetsTotaux = lastEcheance.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(lastEcheance.getInteretsObtenus());
            lastEcheance.setValeurAcquise(capitalPlace.add(interetsDeAnnee));
            lesMensualites.add(lastEcheance);
        }

        this.setInteretsObtenus(interetsTotaux);
        this.setValeurAcquise(lesMensualites.get(lesMensualites.size() - 1).getValeurAcquise());

        return lesMensualites;
    }


    @Override
    String toLocalizedString(Context context) {

        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        moneyFormatter.setMaximumFractionDigits(2);
        percentFormatter.setMaximumFractionDigits(2);
        Period duration = new Period(getDateDebut(), getDateFin());

        String s = context.getString(R.string.descriptionPlacementSansLivret, moneyFormatter.format(getCapitalInitial()), percentFormatter.format(getTauxAnnuel())
                , PeriodFormat.wordBased().print(duration));


        if (getVariation().compareTo(BigDecimal.ZERO) < 0) {
            s += context.getString(R.string.avecRetraitDe, moneyFormatter.format(getVariation().abs()), getFrequenceVariation().toLocalizedString(context));
        } else if (getVariation().compareTo(BigDecimal.ZERO) > 0) {
            s += context.getString(R.string.avecVersementDe, moneyFormatter.format(getVariation()), getFrequenceVariation().toLocalizedString(context));
        }


        s += context.getString(R.string.descriptionInteretsObtenus, moneyFormatter.format(getInteretsObtenus()));
        s += context.getString(R.string.descriptionValeurAcquise, moneyFormatter.format(getValeurAcquise()));

        return s;
    }


}

