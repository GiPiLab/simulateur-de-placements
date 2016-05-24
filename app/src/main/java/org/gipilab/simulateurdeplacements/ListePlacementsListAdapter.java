package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;

/**
 * Created by thibault on 07/04/16.
 */
class ListePlacementsListAdapter extends BaseAdapter {

    private final ArrayList<Placement> _lesPlacements;
    private final Context _context;
    private ArrayList<Boolean> _checkedStates; //0 = unchecked, 1 = checked, -1 = invalid after deletion

    ListePlacementsListAdapter(Context context, ArrayList<Placement> lesPlacements) {
        _context = context;
        _lesPlacements = lesPlacements;
        _checkedStates = new ArrayList<>(lesPlacements.size());
        for (int i = 0; i < lesPlacements.size(); i++) {
            _checkedStates.add(false);
        }
    }

    public void deleteItem(int i) {
        if (i < _lesPlacements.size()) {
            _lesPlacements.get(i).delete();
            _lesPlacements.remove(i);
            _checkedStates.remove(i);
            notifyDataSetChanged();
        }
    }

    public void swapCheckedState(int item) {
        if (_checkedStates.get(item) == false) {
            _checkedStates.set(item, true);
        } else {
            _checkedStates.set(item, false);
        }
        notifyDataSetChanged();
    }

    public void setItemChecked(int item, boolean checked) {
        _checkedStates.set(item, checked);
        notifyDataSetChanged();
    }

    public boolean getCheckedState(int item) {
        return _checkedStates.get(item);
    }

    public ArrayList<Boolean> getCheckedStates() {
        return (ArrayList<Boolean>) _checkedStates.clone();
    }

    public void setCheckedStates(ArrayList<Boolean> savedCheckedStates) {
        if (savedCheckedStates.size() != _lesPlacements.size()) {
            throw new IndexOutOfBoundsException();
        }
        _checkedStates = (ArrayList<Boolean>) savedCheckedStates.clone();
        notifyDataSetChanged();
    }

    public int getCheckedCount() {
        int count = 0;

        for (boolean state : _checkedStates) {
            if (state == true)
                count++;
        }
        return count;
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

        holder.description.setChecked(_checkedStates.get(i));

        return view1;
    }

    private static class ViewHolder {
        CheckedTextView description;
    }
}
