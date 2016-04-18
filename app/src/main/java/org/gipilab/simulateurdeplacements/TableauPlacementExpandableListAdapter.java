package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.support.v4.util.Pair;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;

/**
 * Created by thibault on 09/02/16.
 */
class TableauPlacementExpandableListAdapter extends BaseExpandableListAdapter {

    private final ArrayList<Annualite> _annualites;
    private final Context _context;

    TableauPlacementExpandableListAdapter(Context context, ArrayList<Annualite> annualites) {
        _context = context;
        _annualites = annualites;
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
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.tableauplacementexpandablelistview_layout_group, viewGroup, false);
        }
        TextView tv = (TextView) view1.findViewById(id.labelVariation);
        tv.setText(Html.fromHtml(getGroup(i).toLocalizedString(_context)));

        return view1;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.tableauplacementexpandablelistview_layout_child, viewGroup, false);
        }
        TextView tv = (TextView) view1.findViewById(id.textViewExpandableChild);
        tv.setText(Html.fromHtml(getChild(i, i1).toLocalizedString(_context)));

        return view1;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
