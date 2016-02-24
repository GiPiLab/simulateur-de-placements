package org.gipilab.simulateurdeplacements;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;

/**
 * Created by thibault on 14/01/16.
 */
class Placement implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final int MAXDUREE = 1000;
    public static final BigDecimal MAXVARIATION = MAXCAPITAL;

    private BigDecimal tauxAnnuel = BigDecimal.ZERO;
    private BigDecimal capitalInitial = BigDecimal.ZERO;
    private BigDecimal interetsObtenus = BigDecimal.ZERO;
    private BigDecimal variation = BigDecimal.ZERO;
    private Calendar dateDebut = Calendar.getInstance();

    private int duree = 0;
    private enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;


    static ArrayList<Annualite> mensualitesToAnnualites(ArrayList<Mensualite> lesMensualites) {
        ArrayList<Annualite> lesAnnualites = new ArrayList<>();

        if (lesMensualites.isEmpty())
            return lesAnnualites;

        Annualite annualite = new Annualite();

        annualite.setCapitalPlaceDebutAnnee(lesMensualites.get(0).getCapitalCourant());

        int iemeAnnee = 1;

        for (int i = 1; i <= lesMensualites.size(); i++) {
            Mensualite mens = lesMensualites.get(i - 1);
            annualite.getMensualites().add(mens);
            annualite.setInteretsFinAnnee(annualite.getInteretsFinAnnee().add(mens.getInteretsObtenus()));

            if (i > 1 && i % 12 == 0) {
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
        if (!annualite.getMensualites().isEmpty()) {
            Mensualite mens = lesMensualites.get(lesMensualites.size() - 1);

            annualite.setCapitalPlaceFinAnnee(mens.getCapitalCourant());
            annualite.setValeurAcquiseFinAnnee(mens.getValeurAcquise());
            annualite.setInteretsTotaux(mens.getInteretsTotaux());
            lesAnnualites.add(annualite);
        }

        return lesAnnualites;
    }


    ArrayList<Mensualite> tableauPlacement() {
        ArrayList<Mensualite> lesMensualites = new ArrayList<>();

        Calendar cal;

        if (getDateDebut() == null)
            cal = Calendar.getInstance();
        else cal = getDateDebut();

        BigDecimal tauxMensuel = getTauxAnnuel().divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);
        BigDecimal capitalPlace = getCapitalInitial();
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;

        for (int i = 1; i <= getDuree(); i++) {

            Mensualite mensualite = new Mensualite();
            mensualite.setDateMensualite(cal.getTime());

            if (i > 1 && getVariation().compareTo(BigDecimal.ZERO) != 0) {
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
            mensualite.setInteretsObtenus(capitalPlace.multiply(tauxMensuel, MathContext.DECIMAL128));
            mensualite.setInteretsTotaux(interetsTotaux.add(mensualite.getInteretsObtenus()));
            interetsTotaux = mensualite.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(mensualite.getInteretsObtenus());
            mensualite.setValeurAcquise(capitalPlace.add(interetsDeAnnee));

            if (i % 12 == 0) {
                capitalPlace = capitalPlace.add(interetsDeAnnee);
                interetsDeAnnee = BigDecimal.ZERO;
            }

            if (mensualite.getCapitalCourant().compareTo(BigDecimal.ZERO) >= 0) {
                lesMensualites.add(mensualite);
            } else break;

            cal.add(Calendar.MONTH, 1);

        }
        this.setInteretsObtenus(interetsTotaux);

        return lesMensualites;
    }

    BigDecimal getTauxAnnuel() {
        return tauxAnnuel;
    }

    void setTauxAnnuel(BigDecimal tauxAnnuel) throws InputMismatchException {
        if (tauxAnnuel.compareTo(BigDecimal.ZERO) < 0 || tauxAnnuel.compareTo(MAXTAUX) > 0) {
            throw new InputMismatchException("taux hors bornes");
        }
        this.tauxAnnuel = tauxAnnuel;
    }

    BigDecimal getCapitalInitial() {
        return capitalInitial;
    }

    void setCapitalInitial(BigDecimal capitalInitial) throws InputMismatchException {
        if (capitalInitial.compareTo(BigDecimal.ZERO) <= 0 || capitalInitial.compareTo(MAXCAPITAL) > 0) {
            throw new InputMismatchException("capital hors bornes");
        }
        this.capitalInitial = capitalInitial;
    }

    BigDecimal getInteretsObtenus() {
        return interetsObtenus;
    }

    void setInteretsObtenus(BigDecimal interetsObtenus) throws InputMismatchException {
        if (interetsObtenus.compareTo(BigDecimal.ZERO) < 0) {
            throw new InputMismatchException("interets negatifs");
        }
        this.interetsObtenus = interetsObtenus;
    }

    BigDecimal getVariation() {
        return variation;
    }

    void setVariation(BigDecimal variation) throws InputMismatchException {
        if (variation.abs().compareTo(MAXVARIATION) > 0) {
            throw new InputMismatchException("variation hors bornes");
        }
        this.variation = variation;
    }

    Calendar getDateDebut() {
        return dateDebut;
    }

    void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    int getDuree() {
        return duree;
    }

    void setDuree(int duree) throws InputMismatchException {
        if (duree <= 0 || duree > MAXDUREE) {
            throw new InputMismatchException("durée hors bornes");
        }
        this.duree = duree;
    }

    enumFrequenceVariation getFrequenceVariation() {
        return frequenceVariation;
    }

    void setFrequenceVariation(enumFrequenceVariation frequenceVariation) {
        this.frequenceVariation = frequenceVariation;
    }
}
