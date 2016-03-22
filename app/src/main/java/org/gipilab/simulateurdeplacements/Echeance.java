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
        String output = context.getString(string.echeance, this.ieme, dt.print(this.getDateDebutEcheance()), dt.print(this.getDateFinEcheance()));

        output += "<br>";

        if (this.variation.compareTo(BigDecimal.ZERO) < 0) {
            output += context.getString(string.retraitDe, formatter.format(this.variation.abs()));
            output += "<br>";
        } else if (this.variation.compareTo(BigDecimal.ZERO) > 0) {
            output += context.getString(string.versementDe, formatter.format(this.variation.abs()));
            output += "<br>";
        }

        output += context.getString(string.capitalPlace, formatter.format(this.capitalCourant));
        output += "<br>";

        output += context.getString(string.interets, formatter.format(this.interetsObtenus));
        output += "<br>";

        output += context.getString(string.interetsTotaux, formatter.format(this.interetsTotaux));
        output += "<br>";

        output += context.getString(string.valeurAcquise, formatter.format(this.valeurAcquise));
        return output;
    }

    public int getIeme() {
        return this.ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public LocalDate getDateDebutEcheance() {
        return this.dateDebutEcheance;
    }

    public void setDateDebutEcheance(LocalDate dateDebutEcheance) {
        this.dateDebutEcheance = dateDebutEcheance;
    }

    public LocalDate getDateFinEcheance() {
        return this.dateFinEcheance;
    }

    public void setDateFinEcheance(LocalDate dateFinEcheance) {
        this.dateFinEcheance = dateFinEcheance;
    }

    public BigDecimal getCapitalInitial() {
        return this.capitalInitial;
    }

    public void setCapitalInitial(BigDecimal capitalInitial) {
        this.capitalInitial = capitalInitial;
    }

    public BigDecimal getCapitalCourant() {
        return this.capitalCourant;
    }

    public void setCapitalCourant(BigDecimal capitalCourant) {
        this.capitalCourant = capitalCourant;
    }

    public BigDecimal getInteretsObtenus() {
        return this.interetsObtenus;
    }

    public void setInteretsObtenus(BigDecimal interetsObtenus) {
        this.interetsObtenus = interetsObtenus;
    }

    public BigDecimal getInteretsTotaux() {
        return this.interetsTotaux;
    }

    public void setInteretsTotaux(BigDecimal interetsTotaux) {
        this.interetsTotaux = interetsTotaux;
    }

    public BigDecimal getValeurAcquise() {
        return this.valeurAcquise;
    }

    public void setValeurAcquise(BigDecimal valeurAcquise) {
        this.valeurAcquise = valeurAcquise;
    }

    public BigDecimal getVariation() {
        return this.variation;
    }

    public void setVariation(BigDecimal variation) {
        this.variation = variation;
    }
}
