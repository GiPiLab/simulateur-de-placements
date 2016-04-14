package org.gipilab.simulateurdeplacements;

import android.content.Context;

import org.gipilab.simulateurdeplacements.R.string;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by thibault on 12/02/16.
 */
class Echeance {

    private int ieme;
    private LocalDate dateDebutEcheance;
    private LocalDate dateFinEcheance;
    private BigDecimal capitalInitial;
    private BigDecimal capitalCourant;
    private BigDecimal interetsObtenus;
    private BigDecimal interetsTotaux;
    private BigDecimal valeurAcquise;
    private BigDecimal variation;


    Echeance() {
        ieme = 0;
        capitalInitial = BigDecimal.ZERO;
        capitalCourant = BigDecimal.ZERO;
        interetsObtenus = BigDecimal.ZERO;
        interetsTotaux = BigDecimal.ZERO;
        valeurAcquise = BigDecimal.ZERO;
        variation = BigDecimal.ZERO;
        dateDebutEcheance = new LocalDate();
        dateFinEcheance = new LocalDate();
    }

    public String toLocalizedString(Context context) {

        if (ieme == 0) {
            return context.getString(string.echeanceVide);
        }

        DateTimeFormatter dt = DateTimeFormat.longDate();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        formatter.setMaximumFractionDigits(2);
        String output = context.getString(string.echeance, ieme, dt.print(dateDebutEcheance), dt.print(dateFinEcheance));

        output += "<br>";

        if (variation.compareTo(BigDecimal.ZERO) < 0) {
            output += context.getString(string.retraitDe, formatter.format(variation.abs()));
            output += "<br>";
        } else if (variation.compareTo(BigDecimal.ZERO) > 0) {
            output += context.getString(string.versementDe, formatter.format(variation.abs()));
            output += "<br>";
        }

        output += context.getString(string.capitalPlace, formatter.format(capitalCourant));
        output += "<br>";

        output += context.getString(string.interets, formatter.format(interetsObtenus));
        output += "<br>";

        output += context.getString(string.interetsTotaux, formatter.format(interetsTotaux));
        output += "<br>";

        output += context.getString(string.valeurAcquise, formatter.format(valeurAcquise));
        return output;
    }

    public int getIeme() {
        return ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public LocalDate getDateDebutEcheance() {
        return dateDebutEcheance;
    }

    public void setDateDebutEcheance(LocalDate dateDebutEcheance) {
        this.dateDebutEcheance = dateDebutEcheance;
    }

    public LocalDate getDateFinEcheance() {
        return dateFinEcheance;
    }

    public void setDateFinEcheance(LocalDate dateFinEcheance) {
        this.dateFinEcheance = dateFinEcheance;
    }

    public BigDecimal getCapitalInitial() {
        return capitalInitial;
    }

    public void setCapitalInitial(BigDecimal capitalInitial) {
        this.capitalInitial = capitalInitial;
    }

    public BigDecimal getCapitalCourant() {
        return capitalCourant;
    }

    public void setCapitalCourant(BigDecimal capitalCourant) {
        this.capitalCourant = capitalCourant;
    }

    public BigDecimal getInteretsObtenus() {
        return interetsObtenus;
    }

    public void setInteretsObtenus(BigDecimal interetsObtenus) {
        this.interetsObtenus = interetsObtenus;
    }

    public BigDecimal getInteretsTotaux() {
        return interetsTotaux;
    }

    public void setInteretsTotaux(BigDecimal interetsTotaux) {
        this.interetsTotaux = interetsTotaux;
    }

    public BigDecimal getValeurAcquise() {
        return valeurAcquise;
    }

    public void setValeurAcquise(BigDecimal valeurAcquise) {
        this.valeurAcquise = valeurAcquise;
    }

    public BigDecimal getVariation() {
        return variation;
    }

    public void setVariation(BigDecimal variation) {
        this.variation = variation;
    }
}
