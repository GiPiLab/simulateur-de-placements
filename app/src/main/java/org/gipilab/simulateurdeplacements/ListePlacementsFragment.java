package org.gipilab.simulateurdeplacements;

import android.R.drawable;
import android.R.string;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.orm.SugarRecord;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListePlacementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListePlacementsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ListePlacementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListePlacementsFragment.
     */
    public static ListePlacementsFragment newInstance() {
        //Bundle args = new Bundle();
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return new ListePlacementsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(layout.fragment_liste_placements, container, false);
        updateListView(v);
        setListViewListeners(v);

        return v;
    }

    private void setListViewListeners(View v) {
        ListView listViewQuinzaine = (ListView) v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(id.listViewPlacementsSansQuinzaine);

        //Click handlers
        listViewQuinzaine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListener != null) {
                    mListener.onPlacementClickedFromListePlacementsFragment((Placement) adapterView.getItemAtPosition(i));
                }
            }
        });
        listViewSansQuinzaine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListener != null) {
                    mListener.onPlacementClickedFromListePlacementsFragment((Placement) adapterView.getItemAtPosition(i));
                }
            }
        });

        //Long click handlers
        listViewQuinzaine.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new Builder(getContext())
                        .setTitle(getString(R.string.supprimerPlacement))
                        .setMessage(getString(R.string.confirmerSupprimerPlacement))
                        .setPositiveButton(string.yes, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                                adapter.deleteItem(i);
                            }
                        })
                        .setNegativeButton(string.no, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
        listViewSansQuinzaine.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new Builder(getContext())
                        .setTitle(getString(R.string.supprimerPlacement))
                        .setMessage(getString(R.string.confirmerSupprimerPlacement))
                        .setPositiveButton(string.yes, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                                adapter.deleteItem(i);
                            }
                        })
                        .setNegativeButton(string.no, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }

    public void updateListView(View v) {
        ListView listViewQuinzaine = (ListView) v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(id.listViewPlacementsSansQuinzaine);
        @SuppressWarnings("unchecked") ArrayList<Placement> listPlacementsQuinzaine = (ArrayList) SugarRecord.listAll(PlacementQuinzaine.class);
        @SuppressWarnings("unchecked") ArrayList<Placement> listPlacementsSansQuinzaine = (ArrayList) SugarRecord.listAll(PlacementSansQuinzaine.class);

        listViewQuinzaine.setAdapter(new ListePlacementsListAdapter(getContext(), listPlacementsQuinzaine));
        listViewSansQuinzaine.setAdapter(new ListePlacementsListAdapter(getContext(), listPlacementsSansQuinzaine));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onPlacementClickedFromListePlacementsFragment(Placement placement);
    }
}
