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

import android.content.Context;

import org.gipilab.simulateurdeplacements.R.string;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;

class PlacementQuinzaine extends Placement {


    public PlacementQuinzaine() {
        setModeCalculPlacement(enumModeCalculPlacement.QUINZAINE);
    }

    private static LocalDate aligneDateDebutSurQuinzaine(LocalDate dateDebut) {
        LocalDate dateDebutAlignee;

        if (dateDebut.getDayOfMonth() <= 15) {
            dateDebutAlignee = dateDebut.withDayOfMonth(16);
        } else {
            dateDebutAlignee = dateDebut.plusMonths(1).withDayOfMonth(1);
        }
        return dateDebutAlignee;
    }

    private static LocalDate aligneDateFinSurQuinzaine(LocalDate dateFin) {
        LocalDate dateFinAlignee;
        if (dateFin.getDayOfMonth() > 16) {
            dateFinAlignee = dateFin.withDayOfMonth(16);
        } else {
            dateFinAlignee = dateFin.withDayOfMonth(1);
        }
        return dateFinAlignee;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean estAligneeSurQuinzaine(LocalDate date) {
        return !(date.getDayOfMonth() != 1 && date.getDayOfMonth() != 16);
    }

    private static boolean derniereQuinzaineDeAnnee(LocalDate dateEcheance) {
        return dateEcheance.getMonthOfYear() == 12 && dateEcheance.getDayOfMonth() == 16;
    }

    private static LocalDate versQuinzaineSuivante(LocalDate quinzaine) {
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


    @SuppressWarnings("SameReturnValue")
    static int getMAXECHEANCES() {
        return 2400; //100 ans
    }

    /**
     * Retourne rapidement une valeur approchée du nombre de quinzaines sans tenir compte de l'alignement
     * Utile pour l'affichage des labels de durée en rouge ou noir dans l'interface lors de changements de date
     *
     * @param dateDebut
     * @param dateFin
     * @return le nombre de quinzaines APPROCHE
     */

    static int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        return Months.monthsBetween(dateDebut, dateFin).getMonths() * 2;
    }

    /**
     * Fixe les dates en alignant sur des quinzaines
     *
     * @throws InputMismatchException
     * @params dateDebut, dateFin
     */
    @Override
    void setDatesPlacement(LocalDate dateDebut, LocalDate dateFin) {

        setDateDebut(dateDebut);
        setDateFin(dateFin);

        int duree = calculeDureeEnEcheances(getDateDebut(), getDateFin());

        if (duree > getMAXECHEANCES() || duree < 0) {
            throw new InputMismatchException("duree hors bornes :" + duree);
        }
        setDuree(duree);
    }

    /**
     * Calcule de la durée en quinzaines
     *
     * @param dateDebut la date de début
     * @param dateFin   la date de fin
     * @return le nombre de quinzaines exacte
     */
    int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {


        LocalDate dateDebutAlignee;

        if (!estAligneeSurQuinzaine(dateDebut)) {
            dateDebutAlignee = aligneDateDebutSurQuinzaine(dateDebut);
        } else dateDebutAlignee = dateDebut;

        LocalDate dateFinAlignee;
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

    /**
     * Le tableau de placements en quinzaines
     *
     * @return la liste d'échéances
     */
    @Override
    ArrayList<Echeance> tableauPlacement() {
        ArrayList<Echeance> lesMensualites = new ArrayList<>();

        LocalDate cal;
        if (!estAligneeSurQuinzaine(getDateDebut())) {
            cal = aligneDateDebutSurQuinzaine(getDateDebut());
        } else {
            cal = getDateDebut();
        }

        BigDecimal tauxQuinzaine = getTauxAnnuel().divide(BigDecimal.valueOf(24), MathContext.DECIMAL128);

        BigDecimal capitalPlace = getCapitalInitial();

        //Si on a aucun interêt à cause des quinzaines
        if (getDuree() == 0) {
            Echeance mensualiteVide = new Echeance();
            mensualiteVide.setIeme(0);
            mensualiteVide.setCapitalInitial(getCapitalInitial());
            mensualiteVide.setInteretsObtenus(BigDecimal.ZERO);
            mensualiteVide.setValeurAcquise(getCapitalInitial());
            mensualiteVide.setInteretsTotaux(BigDecimal.ZERO);
            mensualiteVide.setCapitalCourant(getCapitalInitial());
            lesMensualites.add(mensualiteVide);
            return lesMensualites;
        }


        int dur = getDuree();
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;
        for (int i = 1; i <= dur; i++) {

            Echeance quinzaine = new Echeance();
            quinzaine.setDateDebutEcheance(cal);
            quinzaine.setDateFinEcheance(versQuinzaineSuivante(cal).minusDays(1));

            if (i > 1 && getVariation().compareTo(BigDecimal.ZERO) != 0 && capitalPlace.add(getVariation()).compareTo(BigDecimal.ZERO) >= 0) {
                switch (getFrequenceVariation()) {
                    case MENSUELLE:
                        if ((i - 1) % 2 == 0) {
                            quinzaine.setVariation(getVariation());
                            capitalPlace = capitalPlace.add(getVariation());
                        }
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 6 == 0) {
                            quinzaine.setVariation(getVariation());
                            capitalPlace = capitalPlace.add(getVariation());
                        }
                        break;
                    case ANNUELLE:
                        if ((i - 1) % 24 == 0) {
                            quinzaine.setVariation(getVariation());
                            capitalPlace = capitalPlace.add(getVariation());
                        }
                        break;
                }
            }
            quinzaine.setIeme(i);
            quinzaine.setCapitalInitial(getCapitalInitial());
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
        setInteretsObtenus(interetsTotaux);
        setValeurAcquise(lesMensualites.get(lesMensualites.size() - 1).getValeurAcquise());


        return lesMensualites;
    }

    @Override
    String toLocalizedStringForDetailedView(Context context) {

        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();
        DateTimeFormatter dateFormatter = DateTimeFormat.longDate();

        moneyFormatter.setMaximumFractionDigits(context.getResources().getInteger(R.integer.numDecimalMoney));
        percentFormatter.setMaximumFractionDigits(context.getResources().getInteger(R.integer.numDecimalPercent));

        Period duration = new Period(getDateDebut(), getDateFin());

        String s = context.getString(string.descriptionPlacementLivret, moneyFormatter.format(getCapitalInitial()), percentFormatter.format(getTauxAnnuel())
                , PeriodFormat.wordBased().print(duration), dateFormatter.print(getDateDebut()), dateFormatter.print(getDateFin()));


        if (getVariation().compareTo(BigDecimal.ZERO) < 0) {
            s += context.getString(string.avecRetraitDe, moneyFormatter.format(getVariation().abs()), getFrequenceVariation().toLocalizedString(context));
        } else if (getVariation().compareTo(BigDecimal.ZERO) > 0) {
            s += context.getString(string.avecVersementDe, moneyFormatter.format(getVariation()), getFrequenceVariation().toLocalizedString(context));
        }

        s += " (" + context.getString(string.descriptionInteretsObtenus, moneyFormatter.format(getInteretsObtenus())) + ", ";
        s += context.getString(string.descriptionValeurAcquise, moneyFormatter.format(getValeurAcquise())) + ")";

        return s;
    }

    @Override
    String toLocalizedStringForListePlacementsView(Context context) {

        return toLocalizedStringForDetailedView(context);
    }

    @Override
    String toLocalizedVeryShortDescription(Context context) {
        String s;
        NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
        NumberFormat percentFormatter = NumberFormat.getPercentInstance();

        moneyFormatter.setMaximumFractionDigits(context.getResources().getInteger(R.integer.numDecimalMoney));
        percentFormatter.setMaximumFractionDigits(context.getResources().getInteger(R.integer.numDecimalPercent));
        Period duration = new Period(getDateDebut(), getDateFin());

        s = context.getString(string.descriptionCourteLivrets, moneyFormatter.format(getCapitalInitial()), percentFormatter.format(getTauxAnnuel()), PeriodFormat.wordBased().print(duration));

        if (getVariation().compareTo(BigDecimal.ZERO) != 0) {
            s += context.getString(string.avecVariationSmall, moneyFormatter.format(getVariation()), getFrequenceVariation().toLocalizedString(context));
        }

        return s;
    }

}
