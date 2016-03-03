package org.gipilab.simulateurdeplacements;

import android.content.Context;

import org.gipilab.simulateurdeplacements.R.string;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by thibault on 12/02/16.
 */
final class Annualite {

    private int ieme;
    private ArrayList<Echeance> echeances;
    private BigDecimal capitalPlaceDebutAnnee;
    private BigDecimal capitalPlaceFinAnnee;
    private BigDecimal valeurAcquiseFinAnnee;
    private BigDecimal interetsFinAnnee;
    private BigDecimal interetsTotaux;


    Annualite() {
        ieme = 1;
        echeances = new ArrayList<Echeance>();
        capitalPlaceDebutAnnee = BigDecimal.ZERO;
        capitalPlaceFinAnnee = BigDecimal.ZERO;
        valeurAcquiseFinAnnee = BigDecimal.ZERO;
        interetsFinAnnee = BigDecimal.ZERO;
        interetsTotaux = BigDecimal.ZERO;
    }


    public String toLocalizedString(Context context) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);

        String sb = context.getString(string.anneeIeme, this.ieme);
        sb += "<br>";
        sb += context.getString(string.capitalPlaceDebutAnnee, formatter.format(this.capitalPlaceDebutAnnee));
        sb += "<br>";
        sb += context.getString(string.capitalPlaceFinAnnee, formatter.format(this.capitalPlaceFinAnnee));
        sb += "<br>";
        sb += context.getString(string.valeurAcquiseFinAnnee, formatter.format(this.valeurAcquiseFinAnnee));
        sb += "<br>";
        sb += context.getString(string.interetsObtenusSurAnnee, formatter.format(this.interetsFinAnnee));
        sb += "<br>";
        sb += context.getString(string.interetsTotaux, formatter.format(this.interetsTotaux));
        return sb;
    }

    public int getIeme() {
        return this.ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public ArrayList<Echeance> getEcheances() {
        return this.echeances;
    }

    public void setEcheances(ArrayList<Echeance> echeances) {
        this.echeances = echeances;
    }

    public BigDecimal getCapitalPlaceDebutAnnee() {
        return this.capitalPlaceDebutAnnee;
    }

    public void setCapitalPlaceDebutAnnee(BigDecimal capitalPlaceDebutAnnee) {
        this.capitalPlaceDebutAnnee = capitalPlaceDebutAnnee;
    }

    public BigDecimal getCapitalPlaceFinAnnee() {
        return this.capitalPlaceFinAnnee;
    }

    public void setCapitalPlaceFinAnnee(BigDecimal capitalPlaceFinAnnee) {
        this.capitalPlaceFinAnnee = capitalPlaceFinAnnee;
    }

    public BigDecimal getValeurAcquiseFinAnnee() {
        return this.valeurAcquiseFinAnnee;
    }

    public void setValeurAcquiseFinAnnee(BigDecimal valeurAcquiseFinAnnee) {
        this.valeurAcquiseFinAnnee = valeurAcquiseFinAnnee;
    }

    public BigDecimal getInteretsFinAnnee() {
        return this.interetsFinAnnee;
    }

    public void setInteretsFinAnnee(BigDecimal interetsFinAnnee) {
        this.interetsFinAnnee = interetsFinAnnee;
    }

    public BigDecimal getInteretsTotaux() {
        return this.interetsTotaux;
    }

    public void setInteretsTotaux(BigDecimal interetsTotaux) {
        this.interetsTotaux = interetsTotaux;
    }
}
