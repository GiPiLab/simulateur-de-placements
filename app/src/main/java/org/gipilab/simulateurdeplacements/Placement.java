package org.gipilab.simulateurdeplacements;


import org.joda.time.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by thibault on 14/01/16.
 */
abstract class Placement implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final int MAXDUREE = 1000;
    public static final BigDecimal MAXVARIATION = Placement.MAXCAPITAL;

    protected BigDecimal tauxAnnuel = BigDecimal.ZERO;
    protected BigDecimal capitalInitial = BigDecimal.ZERO;
    protected BigDecimal interetsObtenus = BigDecimal.ZERO;
    protected BigDecimal variation = BigDecimal.ZERO;
    protected LocalDate dateDebut = LocalDate.now();
    protected LocalDate dateFin = dateDebut.plusYears(1);

    protected int duree = 12;
    protected enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;

    abstract int calculeDuree(LocalDate dateDebut, LocalDate dateFin);

    abstract ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances);


    abstract ArrayList<Echeance> tableauPlacement();


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
        return this.dateDebut;
    }

    LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * @throws InputMismatchException
     * @params dateDebut, dateFin
     */
    void setDatesPlacement(LocalDate dateDebut, LocalDate dateFin) {

        int duree = calculeDuree(dateDebut, dateFin);

        if (duree > MAXDUREE || duree <= 0) {
            throw new InputMismatchException("duree hors bornes");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.duree = duree;
    }

    int getDuree() {
        return this.duree;
    }

    enumFrequenceVariation getFrequenceVariation() {
        return this.frequenceVariation;
    }

    void setFrequenceVariation(enumFrequenceVariation frequenceVariation) {
        this.frequenceVariation = frequenceVariation;
    }
}
