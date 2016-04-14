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

    private final ArrayList<Echeance> echeances;
    private int ieme;
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

        String sb = context.getString(string.anneeIeme, ieme);
        sb += "<br>";
        sb += context.getString(string.capitalPlaceDebutAnnee, formatter.format(capitalPlaceDebutAnnee));
        sb += "<br>";
        sb += context.getString(string.capitalPlaceFinAnnee, formatter.format(capitalPlaceFinAnnee));
        sb += "<br>";
        sb += context.getString(string.valeurAcquiseFinAnnee, formatter.format(valeurAcquiseFinAnnee));
        sb += "<br>";
        sb += context.getString(string.interetsObtenusSurAnnee, formatter.format(interetsFinAnnee));
        sb += "<br>";
        sb += context.getString(string.interetsTotaux, formatter.format(interetsTotaux));
        return sb;
    }

    public int getIeme() {
        return ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public ArrayList<Echeance> getEcheances() {
        return echeances;
    }

    public BigDecimal getCapitalPlaceDebutAnnee() {
        return capitalPlaceDebutAnnee;
    }

    public void setCapitalPlaceDebutAnnee(BigDecimal capitalPlaceDebutAnnee) {
        this.capitalPlaceDebutAnnee = capitalPlaceDebutAnnee;
    }

    public BigDecimal getCapitalPlaceFinAnnee() {
        return capitalPlaceFinAnnee;
    }

    public void setCapitalPlaceFinAnnee(BigDecimal capitalPlaceFinAnnee) {
        this.capitalPlaceFinAnnee = capitalPlaceFinAnnee;
    }

    public BigDecimal getValeurAcquiseFinAnnee() {
        return valeurAcquiseFinAnnee;
    }

    public void setValeurAcquiseFinAnnee(BigDecimal valeurAcquiseFinAnnee) {
        this.valeurAcquiseFinAnnee = valeurAcquiseFinAnnee;
    }

    public BigDecimal getInteretsFinAnnee() {
        return interetsFinAnnee;
    }

    public void setInteretsFinAnnee(BigDecimal interetsFinAnnee) {
        this.interetsFinAnnee = interetsFinAnnee;
    }

    public BigDecimal getInteretsTotaux() {
        return interetsTotaux;
    }

    public void setInteretsTotaux(BigDecimal interetsTotaux) {
        this.interetsTotaux = interetsTotaux;
    }
}
