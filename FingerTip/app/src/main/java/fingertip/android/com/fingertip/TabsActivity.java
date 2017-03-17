package fingertip.android.com.fingertip;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import fingertip.android.com.fingertip.HomeFragment.Latest;
import fingertip.android.com.fingertip.HomeFragment.Popular;
import fingertip.android.com.fingertip.HomeFragment.TabDescription;
import fingertip.android.com.fingertip.HomeFragment.TopRated;


public class TabsActivity extends AppCompatActivity implements TabDescription.RefreshGrid {

    SmartTabLayout viewPagerTab;
    FragmentManager fm;
    ViewPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_menu_share);
        fm = getSupportFragmentManager();
        if (getResources().getBoolean(R.bool.dual_pane)) {
            tabletView();
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setDistributeEvenly(true);
        viewPagerTab.setViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Popular(), "POPULAR");
        adapter.addFragment(new TopRated(), "TOP RATED");
        adapter.addFragment(new Latest(), "LATEST");

        viewPager.setAdapter(adapter);
    }

    public void tabletView() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.details_frag, new FragmentNone());
        ft.commit();
    }

    @Override
    public void refreshFavGrid() {
        adapter.notifyDataSetChanged();
    }

    public interface UpdateableFragment {
        void update();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof UpdateableFragment) {
                ((UpdateableFragment) object).update();
            }
            //don't return POSITION_NONE, avoid fragment recreation.
            return super.getItemPosition(object);
        }
    }

}

