package com.example.learningapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.learningapp.R;
import com.example.learningapp.adapters.MainViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;
    private FragmentContainerView navHostFragment;
    private NavController navController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        
        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        navHostFragment = findViewById(R.id.nav_host_fragment);
        
        // Setup NavController
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment);
        if (navHost != null) {
            navController = navHost.getNavController();
        }
        
        // Setup ViewPager2
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        
        // Sync ViewPager with BottomNavigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_home);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_pomodoro);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_settings);
                        break;
                }
            }
        });
        
        // BottomNavigation item selected listener
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_pomodoro) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_settings) {
                viewPager.setCurrentItem(2, true);
                return true;
            }
            return false;
        });
        
        // Listen to NavController destination changes
        if (navController != null) {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                // Hiển thị ViewPager và BottomNavigation cho các màn hình chính
                // Ẩn khi navigate sang màn hình khác
                int destId = destination.getId();
                if (destId == R.id.homeFragment || 
                    destId == R.id.pomodoroHubFragment ||
                    destId == R.id.settingsFragment) {
                    viewPager.setVisibility(View.VISIBLE);
                    bottomNavigation.setVisibility(View.VISIBLE);
                    navHostFragment.setVisibility(View.GONE);
                } else {
                    viewPager.setVisibility(View.GONE);
                    bottomNavigation.setVisibility(View.GONE);
                    navHostFragment.setVisibility(View.VISIBLE);
                }
            });
        }
    }
    
    public void navigateFromViewPager(int destinationId, Bundle args) {
        if (navController != null) {
            // Chuyển sang NavHostFragment để navigate
            viewPager.setVisibility(View.GONE);
            bottomNavigation.setVisibility(View.GONE);
            navHostFragment.setVisibility(View.VISIBLE);
            
            // Navigate to destination directly (not using action)
            navController.navigate(destinationId, args);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp()
    {
        return (navController != null && navController.navigateUp()) || super.onSupportNavigateUp();
    }
}

