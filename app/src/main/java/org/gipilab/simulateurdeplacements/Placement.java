package org.gipilab.simulateurdeplacements;


import android.content.Context;

import com.orm.SugarRecord;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by thibault on 14/01/16.
 */


abstract class Placement extends SugarRecord implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final BigDecimal MAXVARIATION = Placement.MAXCAPITAL;
    private BigDecimal tauxAnnuel = BigDecimal.ZERO;
    private BigDecimal capitalInitial = BigDecimal.ZERO;
    private BigDecimal interetsObtenus = BigDecimal.ZERO;
    private BigDecimal variation = BigDecimal.ZERO;

    private long timestampDebut;
    private long timestampFin;



    /*private LocalDate dateDebut;
    private LocalDate dateFin;*/

    private int duree;
    private enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;
    private BigDecimal valeurAcquise = BigDecimal.ZERO;
    private enumModeCalculPlacement modeCalculPlacement;

    public Placement() {

    }

    enumModeCalculPlacement getModeCalculPlacement() {
        return modeCalculPlacement;
    }

    void setModeCalculPlacement(enumModeCalculPlacement mode) {
        this.modeCalculPlacement = mode;
    }

    abstract int getMAXECHEANCES();

    abstract int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin);

    abstract int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin);

    abstract ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances);

    abstract ArrayList<Echeance> tableauPlacement();

    abstract String toLocalizedStringForDetailedView(Context context);

    abstract String toLocalizedStringForListePlacementsView(Context context);

    BigDecimal getTauxAnnuel() {
        return this.tauxAnnuel;
    }

    /**
     * @param tauxAnnuel
     * @throws InputMismatchException
     */
    void setTauxAnnuel(BigDecimal tauxAnnuel) {
        if (tauxAnnuel.compareTo(BigDecimal.ZERO) < 0 || tauxAnnuel.compareTo(Placement.MAXTAUX) > 0) {
            throw new InputMismatchException("taux hors bornes");
        }
        this.tauxAnnuel = tauxAnnuel;
    }

    BigDecimal getCapitalInitial() {
        return this.capitalInitial;
    }

    /**
     * @param capitalInitial
     * @throws InputMismatchException
     */
    void setCapitalInitial(BigDecimal capitalInitial) {
        if (capitalInitial.compareTo(BigDecimal.ZERO) <= 0 || capitalInitial.compareTo(Placement.MAXCAPITAL) > 0) {
            throw new InputMismatchException("capital hors bornes");
        }
        this.capitalInitial = capitalInitial;
    }

    BigDecimal getInteretsObtenus() {
        return this.interetsObtenus;
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
        return this.variation;
    }

    /**
     * @param variation
     * @throws InputMismatchException
     */
    void setVariation(BigDecimal variation) {
        if (variation.abs().compareTo(Placement.MAXVARIATION) > 0) {
            throw new InputMismatchException("variation hors bornes");
        }
        this.variation = variation;
    }

    LocalDate getDateDebut() {
        return new LocalDate(this.timestampDebut);
    }

    void setDateDebut(LocalDate date) {
        this.timestampDebut = date.toDate().getTime();
    }

    LocalDate getDateFin() {
        return new LocalDate(this.timestampFin);
    }

    void setDateFin(LocalDate date) {
        timestampFin = date.toDate().getTime();
    }

    /**
     * @throws InputMismatchException
     * @params dateDebut, dateFin
     */
    void setDatesPlacement(LocalDate dateDebut, LocalDate dateFin) {

        int duree = calculeDureeEnEcheances(dateDebut, dateFin);

        if (duree > getMAXECHEANCES() || duree < 0 || dateDebut.toDate().getTime() == dateFin.toDate().getTime()) {
            throw new InputMismatchException("duree hors bornes");
        }
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        this.duree = duree;
    }

    int getDuree() {
        return this.duree;
    }

    void setDuree(int duree) {
        this.duree = duree;
    }

    enumFrequenceVariation getFrequenceVariation() {
        return this.frequenceVariation;
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
}
