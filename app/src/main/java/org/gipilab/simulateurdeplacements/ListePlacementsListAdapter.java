package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by thibault on 07/04/16.
 */
public class ListePlacementsListAdapter extends BaseAdapter {

    private final ArrayList<Placement> _lesPlacements;
    private final Context _context;

    public ListePlacementsListAdapter(Context context, ArrayList<Placement> lesPlacements) {
        _context = context;
        _lesPlacements = lesPlacements;
    }


    @Override
    public int getCount() {
        return _lesPlacements.size();
    }

    @Override
    public Placement getItem(int i) {
        return _lesPlacements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.liste_placements_item, viewGroup, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.textViewItem);
        tv.setText(Html.fromHtml(getItem(i).toLocalizedString(_context)));
        return view;
    }
}
