package fingertip.android.com.fingertip.HomeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import fingertip.android.com.fingertip.*;

/**
 * Created by NILESH on 12-03-2017.
 */
public class Home_Feed_Fragment extends Fragment implements Tab_description.RefreshGrid  {

    private View view;
    FragmentManager fm;
    ViewPagerAdapter adapter;
    SmartTabLayout viewPagerTab;
    private ViewPager viewPager;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.home_fragment_layout, container, false);
        bundle = this.getArguments();
        fm=getActivity().getSupportFragmentManager();
        if(getResources().getBoolean(R.bool.dual_pane)){
            tabletView();
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        viewPagerTab.setDistributeEvenly(true);
        viewPagerTab.setViewPager(viewPager);
        return view;
    }

    @Override
    public void refreshFavGrid() {
        adapter.notifyDataSetChanged();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            mFragmentList.get(position).setArguments(bundle);
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
            //don't return POSITION_NONE, avoid fragment recreation.
            return super.getItemPosition(object);
        }
    }

    public void tabletView()
    {
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.details_frag, new FragmentNone());
        ft.commit();
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Popular(), "POPULAR");
        adapter.addFragment(new Top_rated(), "TOP RATED");
        adapter.addFragment(new Latest(), "LATEST");

        viewPager.setAdapter(adapter);
    }
}

