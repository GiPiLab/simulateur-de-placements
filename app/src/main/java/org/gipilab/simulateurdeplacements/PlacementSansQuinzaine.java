package org.gipilab.simulateurdeplacements;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

/**
 * Created by thibault on 03/03/16.
 */
public class PlacementSansQuinzaine extends Placement {


    @Override
    int calculeDuree(LocalDate dateDebut, LocalDate dateFin) {
        int duree = Months.monthsBetween(dateDebut, dateFin).getMonths();
        return duree;
    }

    @Override
    ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances) {
        ArrayList<Annualite> lesAnnualites = new ArrayList<>();

        if (lesEcheances.isEmpty())
            return lesAnnualites;

        Annualite annualite = new Annualite();

        annualite.setCapitalPlaceDebutAnnee(lesEcheances.get(0).getCapitalCourant());

        int iemeAnnee = 1;

        for (int i = 1; i <= lesEcheances.size(); i++) {
            Echeance mens = lesEcheances.get(i - 1);
            annualite.getEcheances().add(mens);
            annualite.setInteretsFinAnnee(annualite.getInteretsFinAnnee().add(mens.getInteretsObtenus()));

            if (i > 1 && i % 12 == 0) {
                annualite.setValeurAcquiseFinAnnee(mens.getValeurAcquise());
                annualite.setCapitalPlaceFinAnnee(mens.getCapitalCourant());
                annualite.setInteretsTotaux(mens.getInteretsTotaux());
                lesAnnualites.add(annualite);
                annualite = new Annualite();
                iemeAnnee++;
                annualite.setIeme(iemeAnnee);
                annualite.setCapitalPlaceDebutAnnee(mens.getValeurAcquise());
            }
        }
        if (!annualite.getEcheances().isEmpty()) {
            Echeance mens = lesEcheances.get(lesEcheances.size() - 1);

            annualite.setCapitalPlaceFinAnnee(mens.getCapitalCourant());
            annualite.setValeurAcquiseFinAnnee(mens.getValeurAcquise());
            annualite.setInteretsTotaux(mens.getInteretsTotaux());
            lesAnnualites.add(annualite);
        }

        return lesAnnualites;
    }


    @Override
    ArrayList<Echeance> tableauPlacement() {
        ArrayList<Echeance> lesMensualites = new ArrayList<>();

        LocalDate cal;

        if (this.getDateDebut() == null)
            cal = new LocalDate(LocalDate.now());
        else cal = this.getDateDebut();

        BigDecimal tauxMensuel = this.tauxAnnuel.divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);
        BigDecimal capitalPlace = this.capitalInitial;
        BigDecimal interetsTotaux = BigDecimal.ZERO;
        BigDecimal interetsDeAnnee = BigDecimal.ZERO;


        int i;
        for (i = 1; i <= this.duree; i++) {

            Echeance mensualite = new Echeance();
            mensualite.setDateDebutEcheance(cal);
            mensualite.setDateFinEcheance(cal.plusMonths(1).minusDays(1));

            if (i > 1 && this.variation.compareTo(BigDecimal.ZERO) != 0) {
                switch (this.frequenceVariation) {
                    case MENSUELLE:
                        mensualite.setVariation(this.variation);
                        capitalPlace = capitalPlace.add(this.variation);
                        break;

                    case TRIMESTRIELLE:
                        if ((i - 1) % 3 == 0) {
                            mensualite.setVariation(this.variation);
                            capitalPlace = capitalPlace.add(this.variation);
                        }
                        break;
                }
            }
            mensualite.setIeme(i);
            mensualite.setCapitalInitial(this.capitalInitial);
            mensualite.setCapitalCourant(capitalPlace);
            mensualite.setInteretsObtenus(capitalPlace.multiply(tauxMensuel, MathContext.DECIMAL128));
            mensualite.setInteretsTotaux(interetsTotaux.add(mensualite.getInteretsObtenus()));
            interetsTotaux = mensualite.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(mensualite.getInteretsObtenus());
            mensualite.setValeurAcquise(capitalPlace.add(interetsDeAnnee));

            if (i % 12 == 0) {
                capitalPlace = capitalPlace.add(interetsDeAnnee);
                interetsDeAnnee = BigDecimal.ZERO;
            }

            if (mensualite.getCapitalCourant().compareTo(BigDecimal.ZERO) >= 0) {
                lesMensualites.add(mensualite);
            } else break;

            cal = cal.plusMonths(1);

        }

        //Ajoute les jours manquants
        int daysLeft = Days.daysBetween(cal, dateFin).getDays();
        if (daysLeft > 0) {
            BigDecimal tauxJournalier = tauxAnnuel.divide(BigDecimal.valueOf(365), MathContext.DECIMAL128);
            Echeance lastEcheance = new Echeance();
            lastEcheance.setIeme(i);
            lastEcheance.setDateDebutEcheance(cal);
            lastEcheance.setDateFinEcheance(dateFin);
            lastEcheance.setCapitalInitial(this.capitalInitial);
            lastEcheance.setCapitalCourant(capitalPlace);
            lastEcheance.setInteretsObtenus(capitalPlace.multiply(tauxJournalier, MathContext.DECIMAL128)
                    .multiply(BigDecimal.valueOf(daysLeft), MathContext.DECIMAL128));
            lastEcheance.setInteretsTotaux(interetsTotaux.add(lastEcheance.getInteretsObtenus()));
            interetsTotaux = lastEcheance.getInteretsTotaux();
            interetsDeAnnee = interetsDeAnnee.add(lastEcheance.getInteretsObtenus());
            lastEcheance.setValeurAcquise(capitalPlace.add(interetsDeAnnee));
            lesMensualites.add(lastEcheance);
        }

        this.setInteretsObtenus(interetsTotaux);

        return lesMensualites;
    }
}
