package org.gipilab.simulateurdeplacements;

import android.content.Context;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by thibault on 12/02/16.
 */
class Mensualite {

    private int ieme;
    private Date dateMensualite;
    private BigDecimal capitalInitial;
    private BigDecimal capitalCourant;
    private BigDecimal interetsObtenus;
    private BigDecimal interetsTotaux;
    private BigDecimal valeurAcquise;
    private BigDecimal variation;


    Mensualite() {
        this.setIeme(0);
        this.setCapitalInitial(BigDecimal.ZERO);
        this.setCapitalCourant(BigDecimal.ZERO);
        this.setInteretsObtenus(BigDecimal.ZERO);
        this.setInteretsTotaux(BigDecimal.ZERO);
        this.setValeurAcquise(BigDecimal.ZERO);
        this.setVariation(BigDecimal.ZERO);
        this.setDateMensualite(new Date());
    }

    public String toLocalizedString(Context context) {
        DateFormat dt = DateFormat.getDateInstance(DateFormat.LONG);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        formatter.setMaximumFractionDigits(2);
        String output = context.getString(R.string.mensualite, getIeme(), dt.format(getDateMensualite()));

        output += "<br>";

        if (this.getVariation().compareTo(BigDecimal.ZERO) < 0) {
            output += context.getString(R.string.retraitDe, formatter.format(this.getVariation().abs()));
            output += "<br>";
        } else if (this.getVariation().compareTo(BigDecimal.ZERO) > 0) {
            output += context.getString(R.string.versementDe, formatter.format(this.getVariation().abs()));
            output += "<br>";
        }

        output += context.getString(R.string.capitalPlace, formatter.format(this.getCapitalCourant()));
        output += "<br>";

        output += context.getString(R.string.interets, formatter.format(this.getInteretsObtenus()));
        output += "<br>";

        output += context.getString(R.string.interetsTotaux, formatter.format(this.getInteretsTotaux()));
        output += "<br>";

        output += context.getString(R.string.valeurAcquise, formatter.format(this.getValeurAcquise()));
        return output;
    }

    public int getIeme() {
        return ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public Date getDateMensualite() {
        return dateMensualite;
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
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
