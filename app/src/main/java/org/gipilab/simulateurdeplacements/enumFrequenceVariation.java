package org.gipilab.simulateurdeplacements;

import android.content.Context;

import org.gipilab.simulateurdeplacements.R.string;

/**
 * Created by thibault on 12/02/16.
 */
enum enumFrequenceVariation {
    MENSUELLE(string.periodiciteVariationMois),
    TRIMESTRIELLE(string.periodiciteVariationTrimestre);


    private final int ressourceId;

    enumFrequenceVariation(int id) {
        ressourceId = id;
    }

    public String toLocalizedString(Context context) {
        return context.getString(ressourceId);
    }
}





