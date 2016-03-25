package org.gipilab.simulateurdeplacements;

import android.content.Context;

/**
 * Created by thibault on 12/02/16.
 */
enum enumFrequenceVariation {
    MENSUELLE(R.string.periodiciteVariationMois),
    TRIMESTRIELLE(R.string.periodiciteVariationTrimestre);


    private int ressourceId;

    enumFrequenceVariation(int id) {
        ressourceId = id;
    }

    public String toLocalizedString(Context context) {
        return context.getString(ressourceId);
    }
}





