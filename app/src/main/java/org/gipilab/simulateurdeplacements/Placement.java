package org.gipilab.simulateurdeplacements;


import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by thibault on 14/01/16.
 */
class Placement implements Serializable {

    public static final BigDecimal MAXCAPITAL = new BigDecimal("10000000000");
    public static final BigDecimal MAXTAUX = new BigDecimal("10000");
    public static final int MAXDUREE = 1000;
    public static final BigDecimal MAXVARIATION = Placement.MAXCAPITAL;

    private BigDecimal tauxAnnuel = BigDecimal.ZERO;
    private BigDecimal capitalInitial = BigDecimal.ZERO;
    private BigDecimal interetsObtenus = BigDecimal.ZERO;
    private BigDecimal variation = BigDecimal.ZERO;
    private LocalDate dateDebut = LocalDate.now();
    private LocalDate dateFin = dateDebut.plusYears(1);

    private int dureeEnMois = 12;
    private enumFrequenceVariation frequenceVariation = enumFrequenceVariation.MENSUELLE;

    static int calculeDureeEnMois(LocalDate dateDebut, LocalDate dateFin) {
        int duree = Months.monthsBetween(dateDebut, dateFin).getMonths();
        return duree;
    }

    static int calculeDureeEnQuinzaines(LocalDate dateDebut, LocalDate dateFin) {
        int duree = Days.daysBetween(dateDebut, dateFin).getDays();


        return duree;
    }

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

        LocalDate cal;


        if (this.getDateDebut() == null)
            cal = new LocalDate(LocalDate.now());
        else cal = this.getDateDebut();

        BigDecimal tauxMensuel = this.tauxAnnuel.divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);
        BigDecimal capitalPlace = this.capitalInitial;
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;

        for (int i = 1; i <= this.dureeEnMois; i++) {

            Mensualite mensualite = new Mensualite();
            mensualite.setDateMensualite(cal);

            if (i > 1 && this.variation.compareTo(BigDecimal.ZERO) != 0) {
                switch (this.frequenceVariation) {
                    case MENSUELLE:
                        mensualite.setVariation(this.variation);
                        capitalPlace = capitalPlace.add(this.variation);
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 3 == 0) {
                            mensualite.setVariation(this.variation);
                            capitalPlace = capitalPlace.add(this.variation);
                        }
                        break;
                }
            }
            mensualite.setIeme(i);
            mensualite.setCapitalInitial(this.capitalInitial);
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

            cal = cal.plusMonths(1);

        }
        this.setInteretsObtenus(interetsTotaux);

        return lesMensualites;
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

        int duree = calculeDureeEnMois(dateDebut, dateFin);

        if (duree > MAXDUREE || duree <= 0) {
            throw new InputMismatchException("duree hors bornes");
        }
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dureeEnMois = duree;
    }

    int getDureeEnMois() {
        return this.dureeEnMois;
    }

    enumFrequenceVariation getFrequenceVariation() {
        return this.frequenceVariation;
    }

    void setFrequenceVariation(enumFrequenceVariation frequenceVariation) {
        this.frequenceVariation = frequenceVariation;
    }
}
