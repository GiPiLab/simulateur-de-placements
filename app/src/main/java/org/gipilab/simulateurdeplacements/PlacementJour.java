package org.gipilab.simulateurdeplacements;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by thibault on 03/03/16.
 */
public class PlacementJour extends Placement {
    @Override
    int calculeDuree(LocalDate dateDebut, LocalDate dateFin) {
        return 0;
    }

    @Override
    ArrayList<Annualite> echeancesToAnnualites(ArrayList<Echeance> lesEcheances) {
        return null;
    }

    @Override
    ArrayList<Echeance> tableauPlacement() {
        return null;
    }
}
