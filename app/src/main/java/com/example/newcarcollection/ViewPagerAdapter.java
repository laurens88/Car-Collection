package com.example.newcarcollection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.newcarcollection.fragments.profileFragment;
import com.example.newcarcollection.fragments.listFragment;
import com.example.newcarcollection.fragments.lookupFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new lookupFragment();
            case 1:
                return new listFragment();
            default:
                return new profileFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
