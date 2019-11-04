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
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;


class ListePlacementsListAdapter extends BaseAdapter {

    private final TreeMap<Long, Placement> _placementIdToPlacement;
    private final Context _context;
    private final ArrayList<Placement> _lesPlacements;
    PlacementDatabaseHelper dbHelper;
    private HashSet<Long> _checkedItemsIds;

    ListePlacementsListAdapter(Context context, ArrayList<Placement> lesPlacements) {
        _context = context;
        _lesPlacements = (ArrayList<Placement>) lesPlacements.clone();
        _checkedItemsIds = new HashSet<>();
        _placementIdToPlacement = new TreeMap<>();
        dbHelper = PlacementDatabaseHelper.getInstance(context);

        for (Placement p : lesPlacements) {
            _placementIdToPlacement.put(dbHelper.getPlacementId(p), p);
        }
    }

    public void deleteItemId(long itemId) {
        Placement p = _placementIdToPlacement.get(itemId);
        dbHelper.deletePlacement(itemId);
        _lesPlacements.remove(p);
        _placementIdToPlacement.remove(itemId);
        _checkedItemsIds.remove(itemId);
        notifyDataSetChanged();
    }

    public void swapCheckedState(long itemId) {
        if (_checkedItemsIds.contains(itemId)) {
            _checkedItemsIds.remove(itemId);
        } else {
            _checkedItemsIds.add(itemId);
        }
        notifyDataSetChanged();
    }


    public HashSet<Long> getCheckedIds() {
        return (HashSet<Long>) _checkedItemsIds.clone();
    }

    public void setCheckedIds(HashSet<Long> itemIdsOfCheckedItems) {
        if (itemIdsOfCheckedItems.size() > _placementIdToPlacement.size()) {
            throw new IndexOutOfBoundsException();
        }
        _checkedItemsIds = (HashSet<Long>) itemIdsOfCheckedItems.clone();
        notifyDataSetChanged();
    }

    public int getCheckedCount() {
        return _checkedItemsIds.size();
    }

    @Override
    public int getCount() {
        return _placementIdToPlacement.size();
    }

    @Override
    public Placement getItem(int i) {
        if (i < _lesPlacements.size()) {
            return _lesPlacements.get(i);
        } else return null;
    }

    public Placement getPlacementFromItemId(long itemId) {
        return _placementIdToPlacement.get(itemId);
    }

    @Override
    public long getItemId(int i) {
        //FIXME : lors de la suppression il arrive que ça soit appelé alors que _lesPlacements est vide
        if (i < _lesPlacements.size()) {
            return dbHelper.getPlacementId(_lesPlacements.get(i));
        } else return -1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder holder;
        if (view1 == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.liste_placements_item, viewGroup, false);
            holder.description = view1.findViewById(id.textViewItem);
            view1.setTag(holder);
        } else {
            holder = (ViewHolder) view1.getTag();
        }
        holder.description.setText(Html.fromHtml(getItem(i).toLocalizedStringForListePlacementsView(_context)));


        if (i % 2 == 0) {
            holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.colorBackground1));

        } else {
            holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.colorBackground2));
        }

        holder.description.setChecked(_checkedItemsIds.contains(getItemId(i)));

        return view1;
    }

    private static class ViewHolder {
        CheckedTextView description;
    }
}
