package com.example.iitinder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.iitinder.chatting.MainChatFragment;
import com.example.iitinder.profile.ProfileFragment;
import com.example.iitinder.requests.RequestsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RequestsFragment();
            case 1:
                return new MainChatFragment();
            case 2:
                return new ProfileFragment();
        }
        return new RequestsFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
