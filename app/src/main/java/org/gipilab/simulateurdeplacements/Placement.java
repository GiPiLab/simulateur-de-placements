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

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;



abstract class Placement implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final BigDecimal MAXVARIATION = MAXCAPITAL;
    private BigDecimal tauxAnnuel = BigDecimal.ZERO;
    private BigDecimal capitalInitial = BigDecimal.ZERO;
    private BigDecimal interetsObtenus = BigDecimal.ZERO;
    private BigDecimal valeurAcquise = BigDecimal.ZERO;
    private BigDecimal variation = BigDecimal.ZERO;

    private long timestampDebut;
    private long timestampFin;



    private int duree;
    private enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;
    private enumModeCalculPlacement modeCalculPlacement;

    Placement() {

    }

    enumModeCalculPlacement getModeCalculPlacement() {
        return modeCalculPlacement;
    }

    void setModeCalculPlacement(enumModeCalculPlacement mode) {
        modeCalculPlacement = mode;
    }

    //abstract int getMAXECHEANCES();

    //abstract int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin);

    abstract int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin);

    abstract ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances);

    abstract ArrayList<Echeance> tableauPlacement();

    abstract String toLocalizedStringForDetailedView(Context context);

    abstract String toLocalizedStringForListePlacementsView(Context context);

    abstract String toLocalizedVeryShortDescription(Context context);

    BigDecimal getTauxAnnuel() {
        return tauxAnnuel;
    }

    /**
     * @param tauxAnnuel
     * @throws InputMismatchException
     */
    void setTauxAnnuel(BigDecimal tauxAnnuel) {
        if (tauxAnnuel.compareTo(BigDecimal.ZERO) < 0 || tauxAnnuel.compareTo(MAXTAUX) > 0) {
            throw new InputMismatchException("taux hors bornes");
        }
        this.tauxAnnuel = tauxAnnuel;
    }

    BigDecimal getCapitalInitial() {
        return capitalInitial;
    }

    /**
     * @param capitalInitial
     * @throws InputMismatchException
     */
    void setCapitalInitial(BigDecimal capitalInitial) {
        if (capitalInitial.compareTo(BigDecimal.ZERO) <= 0 || capitalInitial.compareTo(MAXCAPITAL) > 0) {
            throw new InputMismatchException("capital hors bornes");
        }
        this.capitalInitial = capitalInitial;
    }

    BigDecimal getInteretsObtenus() {
        return interetsObtenus;
    }

    /**
     * @param interetsObtenus
     * @throws InputMismatchException
     */
    void setInteretsObtenus(BigDecimal interetsObtenus) {
        if (interetsObtenus.compareTo(BigDecimal.ZERO) < 0) {
            throw new InputMismatchException("interets negatifs");
        }
        this.interetsObtenus = interetsObtenus;
    }

    BigDecimal getVariation() {
        return variation;
    }

    /**
     * @param variation
     * @throws InputMismatchException
     */
    void setVariation(BigDecimal variation) {
        if (variation.abs().compareTo(MAXVARIATION) > 0) {
            throw new InputMismatchException("variation hors bornes");
        }
        this.variation = variation;
    }

    LocalDate getDateDebut() {
        return new LocalDate(timestampDebut);
    }

    protected void setDateDebut(LocalDate date) {
        timestampDebut = date.toDate().getTime();
    }

    LocalDate getDateFin() {
        return new LocalDate(timestampFin);
    }

    protected void setDateFin(LocalDate date) {
        timestampFin = date.toDate().getTime();
    }

    /**
     * @throws InputMismatchException
     * @params dateDebut, dateFin
     */
    void setDatesPlacement(LocalDate dateDebut, LocalDate dateFin) {

        int duree = calculeDureeEnEcheances(dateDebut, dateFin);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        this.duree = duree;
    }

    int getDuree() {
        return duree;
    }

    void setDuree(int duree) {
        this.duree = duree;
    }

    enumFrequenceVariation getFrequenceVariation() {
        return frequenceVariation;
    }

    void setFrequenceVariation(enumFrequenceVariation frequenceVariation) {
        this.frequenceVariation = frequenceVariation;
    }

    BigDecimal getValeurAcquise() {
        return valeurAcquise;
    }

    void setValeurAcquise(BigDecimal valeurAcquise) {
        this.valeurAcquise = valeurAcquise;
    }


    LocalDate estimeDateFin(LocalDate dateDebut,BigDecimal valeurAcquiseVoulue)
    {
        boolean depasse=false;
        LocalDate dateFinEstimee=dateDebut.plusMonths(1);
        do {
            setDatesPlacement(dateDebut, dateFinEstimee);
            tableauPlacement();
            if (getValeurAcquise().compareTo(valeurAcquiseVoulue) == 0) {
                return dateFinEstimee;
            } else if (getValeurAcquise().compareTo(valeurAcquiseVoulue) < 0) {
                dateFinEstimee = dateFinEstimee.plusMonths(1);
            } else {
                depasse=true;
            }
        }while(depasse==false);
        return dateFinEstimee;
    }

}
