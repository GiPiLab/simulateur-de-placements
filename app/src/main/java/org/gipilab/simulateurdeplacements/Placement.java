package org.gipilab.simulateurdeplacements;


import android.content.Context;

import com.orm.SugarRecord;

import org.joda.time.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by thibault on 14/01/16.
 * Was abstract but reverted to concrete class in order to work with orm
 */


class Placement extends SugarRecord implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final BigDecimal MAXVARIATION = Placement.MAXCAPITAL;
    protected BigDecimal tauxAnnuel = BigDecimal.ZERO;
    protected BigDecimal capitalInitial = BigDecimal.ZERO;
    protected BigDecimal interetsObtenus = BigDecimal.ZERO;
    protected BigDecimal variation = BigDecimal.ZERO;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected int duree;
    protected enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;
    protected BigDecimal valeurAcquise = BigDecimal.ZERO;
    protected enumModeCalculPlacement modeCalculPlacement;

    public Placement() {

    }

    static List<Placement> getAll() {
        return listAll(Placement.class);
    }

    static List<Placement> getAllPlacementQuinzaine() {
        return null;
        //  return new Select().from(Placement.class).where("modeCalculPlacement=", enumModeCalculPlacement.QUINZAINE).execute();
    }

    static List<Placement> getAllPlacementSansQuinzaine() {
        return null;
        //return new Select().from(Placement.class).where("modeCalculPlacement=", enumModeCalculPlacement.SANSQUINZAINE).execute();
    }

    enumModeCalculPlacement getModeCalculPlacement() {
        return modeCalculPlacement;
    }

    int getMAXECHEANCES() {
        return -1;
    }

    int approximeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        return -1;
    }

    int calculeDureeEnEcheances(LocalDate dateDebut, LocalDate dateFin) {
        return -1;
    }

    ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances) {
        return null;
    }

    ArrayList<Echeance> tableauPlacement() {
        return null;
    }

    String toLocalizedString(Context context) {
        return null;
    }

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

        int duree = calculeDureeEnEcheances(dateDebut, dateFin);

        if (duree > getMAXECHEANCES() || duree < 0 || dateDebut.toDate().getTime() == dateFin.toDate().getTime()) {
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

    BigDecimal getValeurAcquise() {
        return valeurAcquise;
    }

    void setValeurAcquise(BigDecimal valeurAcquise) {
        this.valeurAcquise = valeurAcquise;
    }
}
