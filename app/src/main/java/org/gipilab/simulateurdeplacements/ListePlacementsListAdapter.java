package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

/**
 * Created by thibault on 07/04/16.
 */
class ListePlacementsListAdapter extends BaseAdapter {

    private final TreeMap<Long, Placement> _placementIdToPlacement;
    private final Context _context;
    private final ArrayList<Placement> _lesPlacements;
    private HashSet<Long> _checkedItemsIds;

    ListePlacementsListAdapter(Context context, ArrayList<Placement> lesPlacements) {
        _context = context;
        _lesPlacements = (ArrayList<Placement>) lesPlacements.clone();
        _checkedItemsIds = new HashSet<>();
        _placementIdToPlacement = new TreeMap<>();
        for (Placement p : lesPlacements) {
            _placementIdToPlacement.put(p.getId(), p);
        }
    }

    public void deleteItemId(long itemId) {
        Placement p = _placementIdToPlacement.get(itemId);
        p.delete();
        _lesPlacements.remove(p);
        _placementIdToPlacement.remove(itemId);
        if (_checkedItemsIds.contains(itemId)) {
            _checkedItemsIds.remove(itemId);
        }

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
        return _lesPlacements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return _lesPlacements.get(i).getId();
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
            holder.description = (CheckedTextView) view1.findViewById(id.textViewItem);
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

        holder.description.setChecked(_checkedItemsIds.contains(getItemId(i)));

        return view1;
    }

    private static class ViewHolder {
        CheckedTextView description;
    }
}
