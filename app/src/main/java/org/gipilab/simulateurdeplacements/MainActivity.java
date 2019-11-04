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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.gipilab.simulateurdeplacements.ListePlacementsFragment.OnFragmentInteractionListener;
import org.gipilab.simulateurdeplacements.R.id;
import org.gipilab.simulateurdeplacements.R.layout;
import org.gipilab.simulateurdeplacements.R.string;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NouveauPlacementFragment.OnFragmentInteractionListener, OnFragmentInteractionListener {
    private static final int REQUEST_CODE_PLACEMENT_SAVED = 1;
    private static final int REQUEST_CODE_PLACEMENT_TO_MODIFY = 2;


    private long back_pressed = 0L;


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(id.container);
        if (mViewPager == null) {
            Log.e("SIMUPLACEMENT", "Null viewPager");
            return;
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case SectionsPagerAdapter.FRAGMENT_LISTE_PLACEMENTS_ID:
                        //getSupportActionBar().hide();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getCurrentFocus() != null)
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        break;
                    default:
                        //getSupportActionBar().show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        TabLayout tabLayout = findViewById(id.tabs);
        if (tabLayout == null) {
            Log.e("SIMUPLACEMENT", "Null tabLayout");
            return;
        }
        tabLayout.setupWithViewPager(mViewPager);

  /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }


   /* @Override
    ublic boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        WebviewDialogFragment frag;

        switch (id) {
            case R.id.action_licence:

                String language = getResources().getConfiguration().locale.getISO3Language();
                if (language.equals("fra") || language.equals("fre")) {
                    frag = WebviewDialogFragment.newInstance("file:///android_asset/cecill_fr.html");
                } else {
                    frag = WebviewDialogFragment.newInstance("file:///android_asset/cecill_en.html");
                }
                frag.show(fm, "licence");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(),
                    R.string.pressAgainToExit, Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }


    @Override
    public void onPlacementRequestValidatedFromNouveauPlacementFragment(Placement placement) {
        Intent intent = new Intent(this, AffichePlacementActivity.class);
        intent.putExtra("placement", placement);
        startActivityForResult(intent, REQUEST_CODE_PLACEMENT_SAVED);
    }

    @Override
    public void onPlacementClickedFromListePlacementsFragment(Placement placement) {

        Intent intent = new Intent(this, AffichePlacementActivity.class);
        intent.putExtra("placement", placement);
        intent.putExtra("enregistrable", false);
        startActivityForResult(intent, REQUEST_CODE_PLACEMENT_TO_MODIFY);
    }

    public void onComparePlacementClickedFromListPlacementsFragment(ArrayList<Placement> placements) {
        Intent intent = new Intent(this, ComparePlacementsActivity.class);
        intent.putExtra("listePlacements", placements);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PLACEMENT_SAVED) {
            if (resultCode == RESULT_OK) {
                ListePlacementsFragment frag = (ListePlacementsFragment) mSectionsPagerAdapter.getRegisteredFragment(SectionsPagerAdapter.FRAGMENT_LISTE_PLACEMENTS_ID);
                frag.updateListView(frag.getView());
            }
        } else if (requestCode == REQUEST_CODE_PLACEMENT_TO_MODIFY) {
            if (resultCode == RESULT_OK) {
                Placement p = (Placement) data.getSerializableExtra("placement");

                NouveauPlacementFragment frag = (NouveauPlacementFragment) mSectionsPagerAdapter.getRegisteredFragment(SectionsPagerAdapter.FRAGMENT_NOUVEAU_PLACEMENT_ID);
                mViewPager.setCurrentItem(SectionsPagerAdapter.FRAGMENT_NOUVEAU_PLACEMENT_ID);
                frag.loadPlacement(p);
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {

        public static final int FRAGMENT_NOUVEAU_PLACEMENT_ID = 0;
        public static final int FRAGMENT_LISTE_PLACEMENTS_ID = 1;
        public static final int FRAGMENT_PRESENTATION_ID = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case FRAGMENT_NOUVEAU_PLACEMENT_ID:
                    return NouveauPlacementFragment.newInstance();
                case FRAGMENT_LISTE_PLACEMENTS_ID:
                    return ListePlacementsFragment.newInstance();
                case FRAGMENT_PRESENTATION_ID:
                    String language = getResources().getConfiguration().locale.getISO3Language();
                    if (language.equals("fra") || language.equals("fre")) {
                        return WebviewDialogFragment.newInstance("file:///android_asset/presentation_fr.html");
                    } else {
                        return WebviewDialogFragment.newInstance("file:///android_asset/presentation_en.html");
                    }
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case FRAGMENT_NOUVEAU_PLACEMENT_ID:
                    return getString(string.ongletNouveauPlacement);
                case FRAGMENT_LISTE_PLACEMENTS_ID:
                    return getString(string.ongletPlacementsEnregistres);
                case FRAGMENT_PRESENTATION_ID:
                    return getString(string.ongletPresentation);
            }
            return null;
        }
    }
}
