package org.gipilab.simulateurdeplacements;

import android.util.Log;

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
        Assert.assertEquals(new BigDecimal("12324.690410958904109589041095890411012"), a.getValeurAcquiseFinAnnee());
        Assert.assertEquals(new BigDecimal("1224.690410958904109589041095890411012"), a.getInteretsFinAnnee());
        Assert.assertEquals(new BigDecimal("1224.690410958904109589041095890411012"), a.getInteretsTotaux());
        Assert.assertEquals(12, a.getEcheances().size());

        a = annualites.get(1);
        Assert.assertEquals(2, a.getIeme());
        Assert.assertEquals(new BigDecimal("12324.690410958904109589041095890411012"), a.getCapitalPlaceDebutAnnee());
        Assert.assertEquals(new BigDecimal("13524.690410958904109589041095890411012"), a.getCapitalPlaceFinAnnee());
        Assert.assertEquals(new BigDecimal("15034.895475774066428973541002064177245"), a.getValeurAcquiseFinAnnee());
        Assert.assertEquals(new BigDecimal("1510.205064815162319384499906173766233"), a.getInteretsFinAnnee());
        Assert.assertEquals(new BigDecimal("2734.895475774066428973541002064177245"), a.getInteretsTotaux());
        Assert.assertEquals(12, a.getEcheances().size());

    }


    @Test
    public void testEstimeDuree() throws Exception{
        PlacementSansQuinzaine p=new PlacementSansQuinzaine();
        p.setCapitalInitial(new BigDecimal("10000"));
        p.setTauxAnnuel(new BigDecimal("0.12"));
        p.setFrequenceVariation(enumFrequenceVariation.MENSUELLE);
        p.setValeurAcquise(BigDecimal.ZERO);
        Assert.assertEquals(p.estimeDateFin(new LocalDate(2015,1,1),new BigDecimal("12000")),new LocalDate(2015,10,1));
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
        Assert.assertEquals(new BigDecimal("98.630136986301369863013698630136990"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("98.630136986301369863013698630136990"), mens.getInteretsTotaux());

        mens = tableau.get(1);
        Assert.assertEquals(BigDecimal.valueOf(10100), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(2, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("89.654794520547945205479452054794515"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("188.284931506849315068493150684931505"), mens.getInteretsTotaux());

        mens = tableau.get(12);
        Assert.assertEquals(new BigDecimal("12424.690410958904109589041095890411012"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(13, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("122.544891724526177519234377932069810"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("1347.235302683430287108275473822480822"), mens.getInteretsTotaux());

        mens = tableau.get(24);
        Assert.assertEquals(new BigDecimal("15134.895475774066428973541002064177245"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(25, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("149.275681404894901765218486595701480"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2884.171157178961330738759488659878725"), mens.getInteretsTotaux());

        //Le mois incomplet restant
        mens = tableau.get(25);
        Assert.assertEquals(new BigDecimal("15234.895475774066428973541002064177245"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(26, mens.getIeme());
        Assert.assertEquals(new BigDecimal("100"), mens.getVariation());
        Assert.assertEquals(new BigDecimal("15.02619827747579154638486235820028"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2899.197355456437122285144351018079005"), mens.getInteretsTotaux());

        //Test avec variation trimestrielle
        p.setFrequenceVariation(enumFrequenceVariation.TRIMESTRIELLE);
        tableau = p.tableauPlacement();

        Assert.assertEquals(26, tableau.size());

        mens = tableau.get(0);
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(1, mens.getIeme());
        Assert.assertEquals(BigDecimal.ZERO, mens.getVariation());
        Assert.assertEquals(new BigDecimal("98.630136986301369863013698630136990"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("98.630136986301369863013698630136990"), mens.getInteretsTotaux());

        mens = tableau.get(1);
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(2, mens.getIeme());
        Assert.assertEquals(BigDecimal.ZERO, mens.getVariation());
        Assert.assertEquals(new BigDecimal("88.767123287671232876712328767123291"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("187.397260273972602739726027397260281"), mens.getInteretsTotaux());

        mens = tableau.get(3);
        Assert.assertEquals(BigDecimal.valueOf(10100), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(4, mens.getIeme());
        Assert.assertEquals(BigDecimal.valueOf(100), mens.getVariation());
        Assert.assertEquals(new BigDecimal("96.295890410958904109589041095890405"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("382.323287671232876712328767123287676"), mens.getInteretsTotaux());

        mens = tableau.get(24);
        Assert.assertEquals(new BigDecimal("13343.091687596171889660349033589791782"), mens.getCapitalCourant());
        Assert.assertEquals(BigDecimal.valueOf(10000), mens.getCapitalInitial());
        Assert.assertEquals(25, mens.getIeme());
        Assert.assertEquals(BigDecimal.valueOf(100), mens.getVariation());
        Assert.assertEquals(new BigDecimal("131.603096096838955624047278139515760"), mens.getInteretsObtenus());
        Assert.assertEquals(new BigDecimal("2674.694783693010845284396311729307542"), mens.getInteretsTotaux());
    }
}