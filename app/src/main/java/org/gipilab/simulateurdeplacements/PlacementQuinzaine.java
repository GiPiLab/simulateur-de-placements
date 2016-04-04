package org.gipilab.simulateurdeplacements;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by thibault on 03/03/16.
 */
public class PlacementQuinzaine extends Placement {


    PlacementQuinzaine() {
        modeCalculPlacement = enumModeCalculPlacement.QUINZAINE;
    }



    @Override
    int getMAXECHEANCES() {
        return 2400; //100 ans
    }

    private LocalDate aligneDateDebutSurQuinzaine(LocalDate dateDebut) {
        LocalDate dateDebutAlignee;

        if (dateDebut.getDayOfMonth() <= 15) {
            dateDebutAlignee = dateDebut.withDayOfMonth(16);
        } else {
            dateDebutAlignee = dateDebut.plusMonths(1).withDayOfMonth(1);
        }
        return dateDebutAlignee;
    }

    private LocalDate aligneDateFinSurQuinzaine(LocalDate dateFin) {
        LocalDate dateFinAlignee;
        if (dateFin.getDayOfMonth() > 16) {
            dateFinAlignee = dateFin.withDayOfMonth(16);
        } else {
            dateFinAlignee = dateFin.withDayOfMonth(1);
        }
        return dateFinAlignee;
    }


    /**
     * Fixe les dates en alignant sur des quinzaines
     *
     * @throws InputMismatchException
     * @params dateDebut, dateFin
     */
    @Override
    void setDatesPlacement(LocalDate dateDebut, LocalDate dateFin) {

        //  this.dateDebut = aligneDateDebutSurQuinzaine(dateDebut);
        //  this.dateFin = aligneDateFinSurQuinzaine(dateFin);

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

        int duree = calculeDureeEnEcheances(this.dateDebut, this.dateFin);

        if (duree > getMAXECHEANCES() || duree < 0) {
            throw new InputMismatchException("duree hors bornes :" + duree);
        }
        this.duree = duree;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean estAligneeSurQuinzaine(LocalDate date) {
        return (!(date.getDayOfMonth() != 1 && date.getDayOfMonth() != 16));
    }


    /**
     * Calcule de la durée en quinzaines
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return le nombre de quinzaines exacte
     */
    int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {


        LocalDate dateDebutAlignee, dateFinAlignee;

        if (!estAligneeSurQuinzaine(dateDebut)) {
            dateDebutAlignee = aligneDateDebutSurQuinzaine(dateDebut);
        } else dateDebutAlignee = dateDebut;

        if (!estAligneeSurQuinzaine(dateFin)) {
            dateFinAlignee = aligneDateFinSurQuinzaine(dateFin);
        } else dateFinAlignee = dateFin;

        long dateFinTime = dateFinAlignee.toDate().getTime();

        LocalDate currentDate = dateDebutAlignee;

        if (currentDate.toDate().getTime() >= dateFinTime) {
            return 0;
        }


        int nbQuinzaines = 0;


        while (currentDate.toDate().getTime() <= dateFinAlignee.toDate().getTime()) {
            currentDate = currentDate.plusDays(1);

            if (currentDate.getDayOfMonth() == 1 || currentDate.getDayOfMonth() == 16) {
                nbQuinzaines++;
                currentDate = currentDate.plusDays(12);
            }
        }

        return nbQuinzaines;
    }

    /**
     * Retourne rapidement une valeur approchée du nombre de quinzaines sans tenir compte de l'alignement
     * Utile pour l'affichage des labels de durée en rouge ou noir dans l'interface lors de changements de date
     *
     * @param dateDebut
     * @param dateFin
     * @return le nombre de quinzaines APPROCHE
     */
    @Override
    int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        return Months.monthsBetween(dateDebut, dateFin).getMonths() * 2;
    }


    boolean derniereQuinzaineDeAnnee(LocalDate dateEcheance) {
        return dateEcheance.getMonthOfYear() == 12 && dateEcheance.getDayOfMonth() == 16;
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
            Echeance quinzaine = lesEcheances.get(i - 1);
            annualite.getEcheances().add(quinzaine);
            annualite.setInteretsFinAnnee(annualite.getInteretsFinAnnee().add(quinzaine.getInteretsObtenus()));

            if (derniereQuinzaineDeAnnee(quinzaine.getDateDebutEcheance())) {
                annualite.setValeurAcquiseFinAnnee(quinzaine.getValeurAcquise());
                annualite.setCapitalPlaceFinAnnee(quinzaine.getCapitalCourant());
                annualite.setInteretsTotaux(quinzaine.getInteretsTotaux());
                lesAnnualites.add(annualite);
                annualite = new Annualite();
                iemeAnnee++;
                annualite.setIeme(iemeAnnee);
                annualite.setCapitalPlaceDebutAnnee(quinzaine.getValeurAcquise());
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


    private LocalDate versQuinzaineSuivante(LocalDate quinzaine) {
        LocalDate quinzaineSuivante;
        int day = quinzaine.getDayOfMonth();

        switch (day) {
            case 1:
                quinzaineSuivante = quinzaine.withDayOfMonth(16);
                break;
            case 16:
                quinzaineSuivante = quinzaine.plusMonths(1).withDayOfMonth(1);
                break;
            default:
                throw new InputMismatchException("Invalid quinzaine");
        }
        return quinzaineSuivante;
    }


    /**
     * Le tableau de placements en quinzaines
     * @return la liste d'échéances
     */
    @Override
    ArrayList<Echeance> tableauPlacement() {
        ArrayList<Echeance> lesMensualites = new ArrayList<>();

        LocalDate cal;
        if (!estAligneeSurQuinzaine(dateDebut)) {
            cal = aligneDateDebutSurQuinzaine(dateDebut);
        } else {
            cal = dateDebut;
        }

        BigDecimal tauxQuinzaine = this.tauxAnnuel.divide(BigDecimal.valueOf(24), MathContext.DECIMAL128);

        BigDecimal capitalPlace = this.capitalInitial;
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;

        //Si on a aucun interêt à cause des quinzaines
        if (this.duree == 0) {
            Echeance mensualiteVide = new Echeance();
            mensualiteVide.setIeme(0);
            mensualiteVide.setCapitalInitial(this.capitalInitial);
            mensualiteVide.setInteretsObtenus(BigDecimal.ZERO);
            mensualiteVide.setValeurAcquise(this.capitalInitial);
            mensualiteVide.setInteretsTotaux(BigDecimal.ZERO);
            mensualiteVide.setCapitalCourant(this.capitalInitial);
            lesMensualites.add(mensualiteVide);
            return lesMensualites;
        }


        int i;
        for (i = 1; i <= this.duree; i++) {

            Echeance quinzaine = new Echeance();
            quinzaine.setDateDebutEcheance(cal);
            quinzaine.setDateFinEcheance(versQuinzaineSuivante(cal).minusDays(1));

            if (i > 1 && this.variation.compareTo(BigDecimal.ZERO) != 0 && capitalPlace.add(this.variation).compareTo(BigDecimal.ZERO) >= 0) {
                switch (this.frequenceVariation) {
                    case MENSUELLE:
                        if ((i - 1) % 2 == 0) {
                            quinzaine.setVariation(this.variation);
                            capitalPlace = capitalPlace.add(this.variation);
                        }
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 6 == 0) {
                            quinzaine.setVariation(this.variation);
                            capitalPlace = capitalPlace.add(this.variation);
                        }
                        break;
                }
            }
            quinzaine.setIeme(i);
            quinzaine.setCapitalInitial(this.capitalInitial);
            quinzaine.setCapitalCourant(capitalPlace);

            quinzaine.setInteretsObtenus(capitalPlace.multiply(tauxQuinzaine, MathContext.DECIMAL128));
            quinzaine.setInteretsTotaux(interetsTotaux.add(quinzaine.getInteretsObtenus()));
            interetsTotaux = quinzaine.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(quinzaine.getInteretsObtenus());
            quinzaine.setValeurAcquise(capitalPlace.add(interetsDeAnnee));


            //Capitalisation
            if (derniereQuinzaineDeAnnee(cal)) {
                capitalPlace = capitalPlace.add(interetsDeAnnee);
                interetsDeAnnee = BigDecimal.ZERO;
            }

            if (quinzaine.getCapitalCourant().compareTo(BigDecimal.ZERO) >= 0) {
                lesMensualites.add(quinzaine);
            } else break;

            cal = versQuinzaineSuivante(cal);

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
        Period duration = new Period(dateDebut, dateFin);

        String s = context.getString(R.string.descriptionPlacementLivret, moneyFormatter.format(getCapitalInitial()), percentFormatter.format(getTauxAnnuel())
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
