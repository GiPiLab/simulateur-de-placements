package org.gipilab.simulateurdeplacements;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by thibault on 22/02/16.
 */
public class PlacementTest {

    @Test
    public void testMensualitesToAnnualites() throws Exception {
        PlacementSansQuinzaine p = new PlacementSansQuinzaine();
        p.setCapitalInitial(BigDecimal.valueOf(10000));
        p.setTauxAnnuel(new BigDecimal("0.12"));
        p.setFrequenceVariation(enumFrequenceVariation.MENSUELLE);
        p.setVariation(BigDecimal.valueOf(100));
        p.setDatesPlacement(new LocalDate(2015, 1, 1), new LocalDate(2017, 2, 1));

        ArrayList<Echeance> tableau = p.tableauPlacement();
        ArrayList<Annualite> annualites = p.echeancesToAnnualites(tableau);
        Assert.assertEquals(3, annualites.size());
        Annualite a = annualites.get(0);
        Assert.assertEquals(1, a.getIeme());
        Assert.assertEquals(new BigDecimal("10000"), a.getCapitalPlaceDebutAnnee());
        Assert.assertEquals(new BigDecimal("11100"), a.getCapitalPlaceFinAnnee());
        Assert.assertEquals(new BigDecimal("12366.00"), a.getValeurAcquiseFinAnnee());
        Assert.assertEquals(new BigDecimal("1266.00"), a.getInteretsFinAnnee());
        Assert.assertEquals(new BigDecimal("1266.00"), a.getInteretsTotaux());
        Assert.assertEquals(12, a.getEcheances().size());

        a = annualites.get(1);
        Assert.assertEquals(2, a.getIeme());
        Assert.assertEquals(new BigDecimal("12366.00"), a.getCapitalPlaceDebutAnnee());
        Assert.assertEquals(new BigDecimal("13566.00"), a.getCapitalPlaceFinAnnee());
        Assert.assertEquals(new BigDecimal("15127.9200"), a.getValeurAcquiseFinAnnee());
        Assert.assertEquals(new BigDecimal("1561.9200"), a.getInteretsFinAnnee());
        Assert.assertEquals(new BigDecimal("2827.9200"), a.getInteretsTotaux());
        Assert.assertEquals(12, a.getEcheances().size());

        a = annualites.get(2);
        Assert.assertEquals(3, a.getIeme());
        Assert.assertEquals(new BigDecimal("15127.9200"), a.getCapitalPlaceDebutAnnee());
        Assert.assertEquals(new BigDecimal("15227.9200"), a.getCapitalPlaceFinAnnee());
        Assert.assertEquals(new BigDecimal("15380.199200"), a.getValeurAcquiseFinAnnee());
        Assert.assertEquals(new BigDecimal("152.279200"), a.getInteretsFinAnnee());
        Assert.assertEquals(new BigDecimal("2980.199200"), a.getInteretsTotaux());
        Assert.assertEquals(1, a.getEcheances().size());
    }

    @Test
    public void testTableauPlacementMois() throws Exception {
        PlacementSansQuinzaine p = new PlacementSansQuinzaine();
        p.setCapitalInitial(BigDecimal.valueOf(10000));
        p.setTauxAnnuel(new BigDecimal("0.12"));
        p.setFrequenceVariation(enumFrequenceVariation.MENSUELLE);
        p.setVariation(BigDecimal.valueOf(100));
        p.setDatesPlacement(new LocalDate(2015, 1, 1), new LocalDate(2017, 2, 4));

        ArrayList<Echeance> tableau = p.tableauPlacement();

        Assert.assertEquals(26, tableau.size());

        Echeance mens = tableau.get(0);
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(1, mens.getIeme());
        Assert.assertEquals(BigDecimal.ZERO, mens.getVariation());
        Assert.assertEquals(new BigDecimal("100.00"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("100.00"), mens.getInteretsTotaux());

        mens = tableau.get(1);
        Assert.assertEquals(BigDecimal.valueOf(10100), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(2, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("101.00"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("201.00"), mens.getInteretsTotaux());

        mens = tableau.get(12);
        Assert.assertEquals(new BigDecimal("12466.00"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(13, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("124.6600"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("1390.6600"), mens.getInteretsTotaux());

        mens = tableau.get(24);
        Assert.assertEquals(new BigDecimal("15227.9200"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(25, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("152.279200"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2980.199200"), mens.getInteretsTotaux());

        //Le mois incomplet restant
        mens = tableau.get(25);
        Assert.assertEquals(new BigDecimal("15227.9200"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(26, mens.getIeme());
        Assert.assertEquals(new BigDecimal("0"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("15.01931835616438356164383561643836"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2995.21851835616438356164383561643836"), mens.getInteretsTotaux());

        //Test avec variation trimestrielle
        p.setFrequenceVariation(enumFrequenceVariation.TRIMESTRIELLE);
        tableau = p.tableauPlacement();

        Assert.assertEquals(26, tableau.size());

        mens = tableau.get(0);
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(1, mens.getIeme());
        Assert.assertEquals(BigDecimal.ZERO, mens.getVariation());
        Assert.assertEquals(new BigDecimal("100.00"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("100.00"), mens.getInteretsTotaux());

        mens = tableau.get(1);
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(2, mens.getIeme());
        Assert.assertEquals(BigDecimal.ZERO, mens.getVariation());
        Assert.assertEquals(new BigDecimal("100.00"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("200.00"), mens.getInteretsTotaux());

        mens = tableau.get(3);
        Assert.assertEquals(BigDecimal.valueOf(10100), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(4, mens.getIeme());
        Assert.assertEquals(BigDecimal.valueOf(100), mens.getVariation());
        Assert.assertEquals(new BigDecimal("101.00"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("401.00"), mens.getInteretsTotaux());

        mens = tableau.get(24);
        Assert.assertEquals(new BigDecimal("13430.1600"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(25, mens.getIeme());
        Assert.assertEquals(BigDecimal.valueOf(100), mens.getVariation());
        Assert.assertEquals(new BigDecimal("134.301600"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2764.461600"), mens.getInteretsTotaux());
    }
}