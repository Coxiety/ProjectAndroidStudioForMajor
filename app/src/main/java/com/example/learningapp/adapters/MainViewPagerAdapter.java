package com.example.learningapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.learningapp.fragments.HomeFragment;
import com.example.learningapp.fragments.SettingsFragment;
import com.example.learningapp.fragments.pomodoro.PomodoroHubFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {
    
    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new PomodoroHubFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new HomeFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return 3; // Home, Pomodoro, Settings
    }
}
