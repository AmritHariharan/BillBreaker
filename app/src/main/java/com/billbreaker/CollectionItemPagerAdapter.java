package com.billbreaker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollectionItemPagerAdapter extends FragmentStatePagerAdapter {

    List<String> names = new ArrayList<>();

    CollectionItemPagerAdapter(FragmentManager fm) {
        super(fm);
        names.add("yeet");
        names.add("neat");
    }

    @Override
    public Fragment getItem(int position) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ItemFragment.ARG_OBJECT_KEY, names.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
