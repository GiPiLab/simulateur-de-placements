package org.gipilab.simulateurdeplacements;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListePlacementsFragment.OnFragmentInteractionListener} interface
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
        ListePlacementsFragment fragment = new ListePlacementsFragment();
        Bundle args = new Bundle();
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_liste_placements, container, false);
        updateListView(v);
        setListViewListeners(v);

        return v;
    }

    private void setListViewListeners(View v) {
        ListView listViewQuinzaine = (ListView) v.findViewById(R.id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(R.id.listViewPlacementsSansQuinzaine);

        //Click handlers
        listViewQuinzaine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListener != null) {
                    mListener.onPlacementClickedFromListePlacementsFragment((Placement) adapterView.getItemAtPosition(i));
                }
            }
        });
        listViewSansQuinzaine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mListener != null) {
                    mListener.onPlacementClickedFromListePlacementsFragment((Placement) adapterView.getItemAtPosition(i));
                }
            }
        });

        //Long click handlers
        listViewQuinzaine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.supprimerPlacement))
                        .setMessage(getString(R.string.confirmerSupprimerPlacement))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                                adapter.deleteItem(i);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
        listViewSansQuinzaine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.supprimerPlacement))
                        .setMessage(getString(R.string.confirmerSupprimerPlacement))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                                adapter.deleteItem(i);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }

    public void updateListView(View v) {
        ListView listViewQuinzaine = (ListView) v.findViewById(R.id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(R.id.listViewPlacementsSansQuinzaine);
        ArrayList<Placement> listPlacementsQuinzaine = (ArrayList) PlacementQuinzaine.listAll(PlacementQuinzaine.class);
        ArrayList<Placement> listPlacementsSansQuinzaine = (ArrayList) PlacementSansQuinzaine.listAll(PlacementSansQuinzaine.class);

        listViewQuinzaine.setAdapter(new ListePlacementsListAdapter(getContext(), listPlacementsQuinzaine));
        listViewSansQuinzaine.setAdapter(new ListePlacementsListAdapter(getContext(), listPlacementsSansQuinzaine));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
        // TODO: Update argument type and name
        void onPlacementClickedFromListePlacementsFragment(Placement placement);
    }
}
