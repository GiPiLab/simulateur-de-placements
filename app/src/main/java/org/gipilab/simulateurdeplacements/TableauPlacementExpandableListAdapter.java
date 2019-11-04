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
import androidx.core.util.Pair;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;

class TableauPlacementExpandableListAdapter extends BaseExpandableListAdapter {

    private final ArrayList<Annualite> _annualites;
    private final Context _context;
    private int _selectedGroup, _selectedChild;

    TableauPlacementExpandableListAdapter(Context context, ArrayList<Annualite> annualites) {
        _context = context;
        _annualites = annualites;
        _selectedChild = -1;
        _selectedGroup = -1;
    }

    int findFlatChildIndexFromGroupAndChild(int group, int child) {
        int flatIndex = 0;
        if (group >= _annualites.size()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < group; i++) {
            flatIndex += _annualites.get(i).getEcheances().size();
        }

        if (child >= _annualites.get(group).getEcheances().size()) {
            throw new IndexOutOfBoundsException();
        }
        flatIndex += child;
        return flatIndex;
    }

    Pair<Integer, Integer> findGroupAndChildFromFlatIndex(int flatIndex) {
        int foundChild = -1;
        int foundGroup = -1;
        int sizeAnnualites = _annualites.size();
        int currentIndex = 0;
        for (int group = 0; group < sizeAnnualites; group++) {
            ArrayList<Echeance> echeances = _annualites.get(group).getEcheances();
            int sizeEcheances = echeances.size();

            if (flatIndex > currentIndex + sizeEcheances) {
                currentIndex += sizeEcheances;
                continue;
            }

            for (int child = 0; child < sizeEcheances; child++) {
                if (currentIndex == flatIndex) {
                    foundChild = child;
                    break;
                }
                currentIndex++;
            }
            if (foundChild != -1) {
                foundGroup = group;
                break;
            }
        }
        //noinspection ConstantConditions
        if (foundGroup == -1 || foundChild == -1) {
            throw new ArrayIndexOutOfBoundsException(flatIndex);
        }
        return new Pair<Integer, Integer>(foundGroup, foundChild);
    }

    @Override
    public int getGroupCount() {
        return _annualites.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return _annualites.get(i).getEcheances().size();
    }

    @Override
    public Annualite getGroup(int i) {
        return _annualites.get(i);
    }

    @Override
    public Echeance getChild(int i, int i1) {
        return _annualites.get(i).getEcheances().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder holder;
        if (view1 == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.tableauplacementexpandablelistview_layout_group, viewGroup, false);
            holder.description = view1.findViewById(id.labelVariation);
            view1.setTag(holder);
        } else {
            holder = (ViewHolder) view1.getTag();
        }

        holder.description.setText(Html.fromHtml(getGroup(i).toLocalizedString(_context)));

        return view1;
    }

    @Override
    public View getChildView(int group, int child, boolean b, View view, ViewGroup viewGroup) {
        View view1 = view;

        ViewHolder holder;

        if (view1 == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.tableauplacementexpandablelistview_layout_child, viewGroup, false);
            holder.description = view1.findViewById(id.textViewExpandableChild);
            view1.setTag(holder);
        } else {
            holder = (ViewHolder) view1.getTag();
        }

        holder.description.setText(Html.fromHtml(getChild(group, child).toLocalizedString(_context)));


        if (group == _selectedGroup && child == _selectedChild) {
            holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.colorAccent));
        } else {
            if (child % 2 == 1) {
                holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.colorBackground1));
            } else {
                holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.colorBackground2));
            }
        }


        return view1;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setSelected(int group, int child) {
        Log.d("GIPI", "Selected = " + group + " - " + child);
        _selectedGroup = group;
        _selectedChild = child;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView description;
    }
}
