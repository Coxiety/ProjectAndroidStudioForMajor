package com.example.learningapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.learningapp.R;

public class MainActivity extends AppCompatActivity {
    
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            
            // Define top-level destinations (no back button)
            appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).build();
            
            // Setup ActionBar with NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

