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


import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * Found on https://gist.github.com/nesquena/c715c9b22fb873b1d259
 * and http://guides.codepath.com/android/ViewPager-with-FragmentPagerAdapter#setup-smartfragmentstatepageradapter
 */


/*
   Extension of FragmentStatePagerAdapter which intelligently caches
   all active fragments and manages the fragment lifecycles.
   Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
*/
abstract class SmartFragmentStatePagerAdapter extends FragmentPagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private final SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
