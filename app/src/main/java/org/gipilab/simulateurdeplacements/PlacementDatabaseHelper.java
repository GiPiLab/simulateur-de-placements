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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;


public class PlacementDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "placementsDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "placements";
    private static final String FIELD_ID = "id";
    private static final String FIELD_TYPE_PLACEMENT = "typePlacement";
    private static final String FIELD_TAUX_ANNUEL = "tauxAnnuel";
    private static final String FIELD_CAPITAL_INITIAL = "capitalInitial";
    private static final String FIELD_VARIATION = "variation";
    private static final String FIELD_FREQUENCE_VARIATION = "frequenceVariation";
    private static final String FIELD_TIMESTAMP_DEBUT = "timestampDebut";
    private static final String FIELD_TIMESTAMP_FIN = "timestampFin";
    private static final String FIELD_INTERETS_OBTENUS = "interetsObtenus";
    private static final String FIELD_VALEUR_ACQUISE = "valeurAcquise";
    private static PlacementDatabaseHelper sInstance;


    public PlacementDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized PlacementDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PlacementDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + FIELD_ID + " INTEGER PRIMARY KEY, " + FIELD_TYPE_PLACEMENT + " TEXT NOT NULL, " + FIELD_TAUX_ANNUEL + " TEXT NOT NULL, " + FIELD_CAPITAL_INITIAL + " TEXT NOT NULL, "
                + FIELD_VARIATION + " TEXT NOT NULL, " + FIELD_FREQUENCE_VARIATION + " INTEGER NOT NULL, " + FIELD_TIMESTAMP_DEBUT + " INTEGER NOT NULL," + FIELD_TIMESTAMP_FIN + " INTEGER NOT NULL," +
                FIELD_INTERETS_OBTENUS + " TEXT NOT NULL," + FIELD_VALEUR_ACQUISE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }

    }


    ArrayList<Placement> getPlacementsQuinzaine() {
        ArrayList<Placement> placements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + FIELD_TYPE_PLACEMENT + "=?", new String[]{enumModeCalculPlacement.QUINZAINE.toString()});
        try {
            if (cursor.moveToFirst()) {
                PlacementQuinzaine p;
                do {
                    p = new PlacementQuinzaine();
                    p.setCapitalInitial(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_CAPITAL_INITIAL))));
                    p.setDatesPlacement(new LocalDate(cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_TIMESTAMP_DEBUT))), new LocalDate(cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_TIMESTAMP_FIN))));
                    p.setFrequenceVariation(enumFrequenceVariation.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_FREQUENCE_VARIATION))));
                    p.setVariation(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_VARIATION))));
                    p.setTauxAnnuel(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TAUX_ANNUEL))));
                    p.setValeurAcquise(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_VALEUR_ACQUISE))));
                    p.setInteretsObtenus(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_INTERETS_OBTENUS))));
                    placements.add(p);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("GIPI", "Error reading DB " + e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return placements;
    }

    ArrayList<Placement> getPlacementsSansQuinzaine() {
        ArrayList<Placement> placements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + FIELD_TYPE_PLACEMENT + "=?", new String[]{enumModeCalculPlacement.SANSQUINZAINE.toString()});
        try {
            if (cursor.moveToFirst()) {
                PlacementSansQuinzaine p;
                do {
                    p = new PlacementSansQuinzaine();
                    p.setCapitalInitial(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_CAPITAL_INITIAL))));
                    p.setDatesPlacement(new LocalDate(cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_TIMESTAMP_DEBUT))), new LocalDate(cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_TIMESTAMP_FIN))));
                    p.setFrequenceVariation(enumFrequenceVariation.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_FREQUENCE_VARIATION))));
                    p.setVariation(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_VARIATION))));
                    p.setTauxAnnuel(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_TAUX_ANNUEL))));
                    p.setValeurAcquise(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_VALEUR_ACQUISE))));
                    p.setInteretsObtenus(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_INTERETS_OBTENUS))));
                    placements.add(p);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("GIPI", "Error reading DB " + e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return placements;
    }


    boolean placementExists(Placement p) {
        SQLiteDatabase db = getReadableDatabase();
        int count = 0;

        Cursor cursor = db.rawQuery("select count(" + FIELD_ID + ") from " + TABLE_NAME + " where " +
                FIELD_CAPITAL_INITIAL + " = ? and " +
                FIELD_TAUX_ANNUEL + " = ? and " +
                FIELD_TYPE_PLACEMENT + " = ? and " +
                FIELD_FREQUENCE_VARIATION + " = ? and " +
                FIELD_VARIATION + " = ? and " +
                FIELD_TIMESTAMP_DEBUT + " = " + p.getDateDebut().toDate().getTime() + " and " +
                FIELD_TIMESTAMP_FIN + " = " + p.getDateFin().toDate().getTime(), new String[]{p.getCapitalInitial().toPlainString(), p.getTauxAnnuel().toPlainString(), p.getModeCalculPlacement().toString(),
                p.getFrequenceVariation().toString(), p.getVariation().toPlainString()});

        try {
            if (cursor.moveToFirst()) {
                do {
                    count = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("GIPI", "Exception getting ids " + e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return count > 0;
    }


    long getPlacementId(Placement p) {
        SQLiteDatabase db = getReadableDatabase();
        long id = -1;
        Cursor cursor = db.rawQuery("select " + FIELD_ID + " from " + TABLE_NAME + " where " +
                FIELD_CAPITAL_INITIAL + " = ? and " +
                FIELD_TAUX_ANNUEL + " = ? and " +
                FIELD_TYPE_PLACEMENT + " = ? and " +
                FIELD_FREQUENCE_VARIATION + " = ? and " +
                FIELD_VARIATION + " = ? and " +
                FIELD_TIMESTAMP_DEBUT + " = " + p.getDateDebut().toDate().getTime() + " and " +
                FIELD_TIMESTAMP_FIN + " = " + p.getDateFin().toDate().getTime(), new String[]{p.getCapitalInitial().toPlainString(), p.getTauxAnnuel().toPlainString(), p.getModeCalculPlacement().toString(),
                p.getFrequenceVariation().toString(), p.getVariation().toPlainString()});


        try {
            if (cursor.getCount() > 1) {
                Log.w("GIPI", "Duplicate placement found");
            }
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_ID));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("GIPI", "Exception getting ids " + e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return id;
    }

    void addPlacement(Placement p) {

        if (placementExists(p)) {
            return;
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_TYPE_PLACEMENT, p.getModeCalculPlacement().toString());
        values.put(FIELD_CAPITAL_INITIAL, p.getCapitalInitial().toPlainString());
        values.put(FIELD_FREQUENCE_VARIATION, p.getFrequenceVariation().toString());
        values.put(FIELD_TAUX_ANNUEL, p.getTauxAnnuel().toPlainString());
        values.put(FIELD_VARIATION, p.getVariation().toPlainString());
        values.put(FIELD_TIMESTAMP_DEBUT, p.getDateDebut().toDate().getTime());
        values.put(FIELD_TIMESTAMP_FIN, p.getDateFin().toDate().getTime());
        values.put(FIELD_INTERETS_OBTENUS, p.getInteretsObtenus().toPlainString());
        values.put(FIELD_VALEUR_ACQUISE, p.getValeurAcquise().toPlainString());
        db.beginTransaction();

        try {
            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("GIPI", "Exception inserting placement" + e);
        } finally {
            db.endTransaction();
        }

    }


    void deletePlacement(long placementId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, FIELD_ID + "=" + placementId, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("GIPI", "Error while deleting record " + e);
        } finally {
            db.endTransaction();
        }
    }
}
