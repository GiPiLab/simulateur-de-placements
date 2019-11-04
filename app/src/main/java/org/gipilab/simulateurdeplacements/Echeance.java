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
