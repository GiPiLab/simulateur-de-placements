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

import android.R.drawable;
import android.R.string;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog.Builder;
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
        return new ListePlacementsFragment();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getView() != null) {
            ListView listViewQuinzaine = getView().findViewById(id.listViewPlacementsQuinzaine);
            ListView listViewSansQuinzaine = getView().findViewById(id.listViewPlacementsSansQuinzaine);

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
            ListView listViewQuinzaine = getView().findViewById(id.listViewPlacementsQuinzaine);
            ListView listViewSansQuinzaine = getView().findViewById(id.listViewPlacementsSansQuinzaine);

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


        Button btnAfficher = view.findViewById(id.btnAfficher);
        btnAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getView() != null) {
                    ListView listViewQuinzaine = getView().findViewById(id.listViewPlacementsQuinzaine);
                    ListView listViewSansQuinzaine = getView().findViewById(id.listViewPlacementsSansQuinzaine);

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




        Button btnCompare = view.findViewById(id.btnComparer);
        if (btnCompare != null) {
            btnCompare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getView() != null) {
                        ListView listViewQuinzaine = getView().findViewById(id.listViewPlacementsQuinzaine);
                        ListView listViewSansQuinzaine = getView().findViewById(id.listViewPlacementsSansQuinzaine);

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

        Button btnSupprimer = view.findViewById(id.btnSupprimerPlacement);

        if (btnSupprimer != null) {
            btnSupprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getView() != null) {

                        ListView listViewQuinzaine = getView().findViewById(id.listViewPlacementsQuinzaine);
                        ListView listViewSansQuinzaine = getView().findViewById(id.listViewPlacementsSansQuinzaine);

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
        final ListView listViewQuinzaine = v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = v.findViewById(id.listViewPlacementsSansQuinzaine);


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
    }

    public void updateListView(View v) {
        ListView listViewQuinzaine = v.findViewById(id.listViewPlacementsQuinzaine);
        ListView listViewSansQuinzaine = v.findViewById(id.listViewPlacementsSansQuinzaine);

        TextView emptyView1 = v.findViewById(id.emptyListView1);
        TextView emptyView2 = v.findViewById(id.emptyListView2);

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
