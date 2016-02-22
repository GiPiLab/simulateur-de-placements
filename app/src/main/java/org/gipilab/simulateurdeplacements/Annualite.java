package org.gipilab.simulateurdeplacements;

import android.content.Context;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by thibault on 12/02/16.
 */
class Annualite {

    private int ieme;
    private ArrayList<Mensualite> mensualites;
    private BigDecimal capitalPlaceDebutAnnee;
    private BigDecimal capitalPlaceFinAnnee;
    private BigDecimal valeurAcquiseFinAnnee;
    private BigDecimal interetsFinAnnee;
    private BigDecimal interetsTotaux;


    Annualite() {
        setIeme(1);
        setMensualites(new ArrayList<Mensualite>());
        setCapitalPlaceDebutAnnee(BigDecimal.ZERO);
        setCapitalPlaceFinAnnee(BigDecimal.ZERO);
        setValeurAcquiseFinAnnee(BigDecimal.ZERO);
        setInteretsFinAnnee(BigDecimal.ZERO);
        setInteretsTotaux(BigDecimal.ZERO);
    }


    public String toLocalizedString(Context context) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(2);

        String sb = context.getString(R.string.anneeIeme, getIeme());
        sb += "<br>";
        sb += context.getString(R.string.capitalPlaceDebutAnnee, formatter.format(getCapitalPlaceDebutAnnee()));
        sb += "<br>";
        sb += context.getString(R.string.capitalPlaceFinAnnee, formatter.format(getCapitalPlaceFinAnnee()));
        sb += "<br>";
        sb += context.getString(R.string.valeurAcquiseFinAnnee, formatter.format(getValeurAcquiseFinAnnee()));
        sb += "<br>";
        sb += context.getString(R.string.interetsObtenusSurAnnee, formatter.format(getInteretsFinAnnee()));
        sb += "<br>";
        sb += context.getString(R.string.interetsTotaux, formatter.format(getInteretsTotaux()));
        return sb;
    }

    public int getIeme() {
        return ieme;
    }

    public void setIeme(int ieme) {
        this.ieme = ieme;
    }

    public ArrayList<Mensualite> getMensualites() {
        return mensualites;
    }

    public void setMensualites(ArrayList<Mensualite> mensualites) {
        this.mensualites = mensualites;
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
