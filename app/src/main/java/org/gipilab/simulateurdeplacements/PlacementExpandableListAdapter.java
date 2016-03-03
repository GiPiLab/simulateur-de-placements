package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by thibault on 09/02/16.
 */
class PlacementExpandableListAdapter extends BaseExpandableListAdapter {

    private final ArrayList<Annualite> _annualites;
    private final Context _context;

    public PlacementExpandableListAdapter(Context context, ArrayList<Annualite> annualites) {
        _context = context;
        _annualites = annualites;
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
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandablelistview_layout_group, viewGroup, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.labelVariation);
        tv.setText(Html.fromHtml(getGroup(i).toLocalizedString(_context)));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandablelistview_layout_child, viewGroup, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.textViewExpandableChild);
        tv.setText(Html.fromHtml(getChild(i, i1).toLocalizedString(_context)));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
