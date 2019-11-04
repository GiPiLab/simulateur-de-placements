/*
 * Simulateur de placements
 *
 * Copyright Thibault et Gilbert Mondary, Laboratoire de Recherche pour le Développement Local (2006--)
 *
 * labo@gipilab.org
 *
 * Ce logiciel est un programme informatique servant à simuler des placements
 *
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 *
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée. Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme, le
 * titulaire des droits patrimoniaux et les concédants successifs.
 *
 * A cet égard l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement, à l'utilisation, à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant des connaissances informatiques approfondies. Les
 * utilisateurs sont donc invités à charger et tester l'adéquation du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 *
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 *
 */


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
