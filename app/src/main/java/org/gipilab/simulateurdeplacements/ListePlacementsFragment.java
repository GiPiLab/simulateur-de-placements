package org.gipilab.simulateurdeplacements;

import android.R.drawable;
import android.R.string;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;

import java.util.ArrayList;
import java.util.HashSet;


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
    public void onSaveInstanceState(Bundle outState) {
        if (getView() != null) {
            ListView listViewQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsQuinzaine);
            ListView listViewSansQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsSansQuinzaine);

            if (listViewQuinzaine != null && listViewSansQuinzaine != null) {
                ListePlacementsListAdapter adapterQuinzaine = (ListePlacementsListAdapter) listViewQuinzaine.getAdapter();
                ListePlacementsListAdapter adapterSansQuinzaine = (ListePlacementsListAdapter) listViewSansQuinzaine.getAdapter();

                outState.putSerializable("checkedItemStatesPlacementQuinzaine", adapterQuinzaine.getCheckedIds());
                outState.putSerializable("checkedItemStatesPlacementSansQuinzaine", adapterSansQuinzaine.getCheckedIds());
            }
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        if (savedInstanceState == null)
            return;

        if (getView() != null) {
            ListView listViewQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsQuinzaine);
            ListView listViewSansQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsSansQuinzaine);

            if (listViewQuinzaine != null && listViewSansQuinzaine != null) {

                ListePlacementsListAdapter adapterQuinzaine = (ListePlacementsListAdapter) listViewQuinzaine.getAdapter();
                ListePlacementsListAdapter adapterSansQuinzaine = (ListePlacementsListAdapter) listViewSansQuinzaine.getAdapter();


                HashSet<Long> statsQuinzaine = (HashSet<Long>) savedInstanceState.getSerializable("checkedItemStatesPlacementQuinzaine");
                HashSet<Long> statsSansQuinzaine = (HashSet<Long>) savedInstanceState.getSerializable("checkedItemStatesPlacementSansQuinzaine");
                adapterQuinzaine.setCheckedIds(statsQuinzaine);
                adapterSansQuinzaine.setCheckedIds(statsSansQuinzaine);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(layout.fragment_liste_placements, container, false);
        updateListView(v);
        setListViewListeners(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button btnAfficher = (Button) view.findViewById(id.btnAfficher);
        btnAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getView() != null) {
                    ListView listViewQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsQuinzaine);
                    ListView listViewSansQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsSansQuinzaine);

                    final ListePlacementsListAdapter adapterQuinzaine = (ListePlacementsListAdapter) listViewQuinzaine.getAdapter();
                    final ListePlacementsListAdapter adapterSansQuinzaine = (ListePlacementsListAdapter) listViewSansQuinzaine.getAdapter();

                    final HashSet<Long> checkedIdsQuinzaine = adapterQuinzaine.getCheckedIds();
                    final HashSet<Long> checkedIdsSansQuinzaine = adapterSansQuinzaine.getCheckedIds();

                    int countQuinzaine = adapterQuinzaine.getCheckedCount();
                    int countSansQuinzaine = adapterSansQuinzaine.getCheckedCount();

                    if (countQuinzaine + countSansQuinzaine != 1) {
                        Snackbar snackbar = Snackbar.make(getView(), getString(R.string.selectionnerPlacementAAfficher), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }

                    if (mListener != null) {
                        if (countQuinzaine == 1) {
                            mListener.onPlacementClickedFromListePlacementsFragment(adapterQuinzaine.getPlacementFromItemId(checkedIdsQuinzaine.iterator().next()));
                        } else if (countSansQuinzaine == 1) {
                            mListener.onPlacementClickedFromListePlacementsFragment(adapterSansQuinzaine.getPlacementFromItemId(checkedIdsSansQuinzaine.iterator().next()));
                        } else {
                            throw new IndexOutOfBoundsException();
                        }
                    }
                }
            }
        });




        Button btnCompare = (Button) view.findViewById(id.btnComparer);
        if (btnCompare != null) {
            btnCompare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getView() != null) {
                        ListView listViewQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsQuinzaine);
                        ListView listViewSansQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsSansQuinzaine);

                        if (listViewQuinzaine != null && listViewSansQuinzaine != null) {

                            final ListePlacementsListAdapter adapterQuinzaine = (ListePlacementsListAdapter) listViewQuinzaine.getAdapter();
                            final ListePlacementsListAdapter adapterSansQuinzaine = (ListePlacementsListAdapter) listViewSansQuinzaine.getAdapter();

                            final HashSet<Long> checkedIdsQuinzaine = adapterQuinzaine.getCheckedIds();
                            final HashSet<Long> checkedIdsSansQuinzaine = adapterSansQuinzaine.getCheckedIds();

                            int countQuinzaine = adapterQuinzaine.getCheckedCount();
                            int countSansQuinzaine = adapterSansQuinzaine.getCheckedCount();

                            if (countQuinzaine + countSansQuinzaine <= 1) {
                                Snackbar snackbar = Snackbar.make(getView(), getString(R.string.selectionnerPlacementsAComparer), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                return;
                            }

                            if ((countQuinzaine + countSansQuinzaine) < getResources().getInteger(R.integer.maxPlacementsToCompare)) {

                                ArrayList<Placement> placementsToCompare = new ArrayList<Placement>(countQuinzaine + countSansQuinzaine);
                                for (long checkedQuinzaine : checkedIdsQuinzaine) {
                                    placementsToCompare.add(adapterQuinzaine.getPlacementFromItemId(checkedQuinzaine));
                                }

                                for (long checkedSansQuinzaine : checkedIdsSansQuinzaine) {
                                    placementsToCompare.add(adapterSansQuinzaine.getPlacementFromItemId(checkedSansQuinzaine));
                                }

                                if (mListener != null) {
                                    mListener.onComparePlacementClickedFromListPlacementsFragment(placementsToCompare);
                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(getView(), getString(R.string.maxPlacementsToCompare, getResources().getInteger(R.integer.maxPlacementsToCompare)), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    }
                }
            });
        }

        Button btnSupprimer = (Button) view.findViewById(id.btnSupprimerPlacement);

        if (btnSupprimer != null) {
            btnSupprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getView() != null) {

                        ListView listViewQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsQuinzaine);
                        ListView listViewSansQuinzaine = (ListView) getView().findViewById(id.listViewPlacementsSansQuinzaine);

                        if (listViewQuinzaine != null && listViewSansQuinzaine != null) {

                            final ListePlacementsListAdapter adapterQuinzaine = (ListePlacementsListAdapter) listViewQuinzaine.getAdapter();
                            final ListePlacementsListAdapter adapterSansQuinzaine = (ListePlacementsListAdapter) listViewSansQuinzaine.getAdapter();

                            final HashSet<Long> checkedIdsQuinzaine = adapterQuinzaine.getCheckedIds();
                            final HashSet<Long> checkedIdsSansQuinzaine = adapterSansQuinzaine.getCheckedIds();

                            int countQuinzaine = adapterQuinzaine.getCheckedCount();
                            int countSansQuinzaine = adapterSansQuinzaine.getCheckedCount();
                            if (countQuinzaine + countSansQuinzaine > 0) {
                                new Builder(getContext())
                                        .setTitle(getString(R.string.supprimerPlacement))
                                        .setMessage(getString(R.string.confirmerSupprimerPlacement))
                                        .setPositiveButton(string.yes, new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                for (long checkedIdQuinzaine : checkedIdsQuinzaine) {
                                                    adapterQuinzaine.deleteItemId(checkedIdQuinzaine);
                                                }

                                                for (long checkedIdSansQuinzaine : checkedIdsSansQuinzaine) {

                                                    adapterSansQuinzaine.deleteItemId(checkedIdSansQuinzaine);
                                                }
                                            }
                                        })
                                        .setNegativeButton(string.no, new OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                Snackbar snackbar = Snackbar.make(getView(), R.string.selectionnerPlacementsASupprimer, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    }
                }
            });
        }
    }

    private void setListViewListeners(View v) {
        final ListView listViewQuinzaine = (ListView) v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(id.listViewPlacementsSansQuinzaine);


        //Click handlers
        listViewQuinzaine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                adapter.swapCheckedState(adapter.getItemId(i));
            }
        });
        listViewSansQuinzaine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                adapter.swapCheckedState(adapter.getItemId(i));
            }
        });

        //Long click handlers
/*
        listViewQuinzaine.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                adapter.swapCheckedState(adapter.getItemId(i));
                return true;
            }
        });


        listViewSansQuinzaine.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                ListePlacementsListAdapter adapter = (ListePlacementsListAdapter) adapterView.getAdapter();
                adapter.swapCheckedState(adapter.getItemId(i));
                return true;
            }
        });
        */
    }

    public void updateListView(View v) {
        ListView listViewQuinzaine = (ListView) v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = (ListView) v.findViewById(id.listViewPlacementsSansQuinzaine);

        TextView emptyView1 = (TextView) v.findViewById(id.emptyListView1);
        TextView emptyView2 = (TextView) v.findViewById(id.emptyListView2);

        listViewQuinzaine.setEmptyView(emptyView1);
        listViewSansQuinzaine.setEmptyView(emptyView2);

        PlacementDatabaseHelper dbHelper = PlacementDatabaseHelper.getInstance(getContext());

        ArrayList<Placement> listPlacementsQuinzaine = dbHelper.getPlacementsQuinzaine();
        ArrayList<Placement> listPlacementsSansQuinzaine = dbHelper.getPlacementsSansQuinzaine();

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

        void onComparePlacementClickedFromListPlacementsFragment(ArrayList<Placement> placements);
    }
}
