package class_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.ActivityFragment;
import fragments.ProfileFragment;
import fragments.SearchFragment;
import fragments.TestHomeFragment;

/**
 * Created by Chirag on 4/26/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TestHomeFragment tab0 = new TestHomeFragment();
                return tab0;
            case 1:
                SearchFragment tab1 = new SearchFragment();
                return tab1;
            case 2:
                SearchFragment tab2 = new SearchFragment();
                return tab2;
            case 3:
                ActivityFragment tab3 = new ActivityFragment();
                return tab3;
            case 4:
                ProfileFragment tab4 = new ProfileFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
