package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;

/**
 * Created by thibault on 07/04/16.
 */
class ListePlacementsListAdapter extends BaseAdapter {

    private final ArrayList<Placement> _lesPlacements;
    private final Context _context;


    ListePlacementsListAdapter(Context context, ArrayList<Placement> lesPlacements) {
        _context = context;
        _lesPlacements = lesPlacements;
    }

    public void deleteItem(int i) {
        if (i < _lesPlacements.size()) {
            _lesPlacements.get(i).delete();
            _lesPlacements.remove(i);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return _lesPlacements.size();
    }

    @Override
    public Placement getItem(int i) {
        if (i < _lesPlacements.size()) {
            return _lesPlacements.get(i);
        } else {
            Log.e("SIMUPLACEMENT", "Returning null as getItem");
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        if (i < _lesPlacements.size() && _lesPlacements.get(i) != null) {
            return _lesPlacements.get(i).getId();
        } else {
            Log.e("SIMUPLACEMENT", "Returning -1 as getItemId");
            return -1;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        ViewHolder holder;
        if (view1 == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(layout.liste_placements_item, viewGroup, false);
            holder.description = (TextView) view1.findViewById(id.textViewItem);
            view1.setTag(holder);
        } else {
            holder = (ViewHolder) view1.getTag();
        }
        holder.description.setText(Html.fromHtml(getItem(i).toLocalizedStringForListePlacementsView(_context)));

        if (i % 2 == 0) {
            holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.ripple_material_light));

        } else {
            holder.description.setBackgroundColor(ContextCompat.getColor(_context, R.color.ripple_material_dark));
        }

        return view1;
    }

    private static class ViewHolder {
        TextView description;
    }
}
