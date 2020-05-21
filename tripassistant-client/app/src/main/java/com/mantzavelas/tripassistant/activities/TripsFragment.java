package com.mantzavelas.tripassistant.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mantzavelas.tripassistant.R;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TripPagesStrategies strategies;

    public TripsFragment() {
        super();
        strategies = new TripPagesStrategies();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.trip_tabs);
        viewPager = view.findViewById(R.id.trip_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (TripPages pageName : TripPages.values()) {
            TripPageStrategy strategy = strategies.getTripPageStrategy(pageName.getName());
            if (strategy != null) {
                adapter.addFragment(strategy.getFragmentPage().getTitle(), strategy.getFragmentPage());
            }
        }
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<String> fragmentTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragmentTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(String title, Fragment fragment) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }
    }
}
