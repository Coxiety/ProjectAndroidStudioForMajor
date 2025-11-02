package com.example.learningapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.learningapp.R;
import com.example.learningapp.utils.PreferencesHelper;

public class OnboardingActivity extends AppCompatActivity {
    
    private ViewPager viewPager;
    private LinearLayout layoutIndicator;
    private Button btnSkip, btnNext;
    private PreferencesHelper preferencesHelper;
    private int currentPage = 0;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        preferencesHelper = new PreferencesHelper(this);
        
        if (preferencesHelper.isOnboardingCompleted()) {
            navigateToMain();
            return;
        }
        
        setContentView(R.layout.activity_onboarding);
        
        viewPager = findViewById(R.id.viewPager);
        layoutIndicator = findViewById(R.id.layoutIndicator);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);
        
        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter();
        viewPager.setAdapter(adapter);
        
        setupIndicators();
        setCurrentIndicator(0);
        
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setCurrentIndicator(position);
                
                if (position == 2) {
                    btnNext.setText(R.string.start_learning);
                } else {
                    btnNext.setText(R.string.next);
                }
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        
        btnNext.setOnClickListener(v -> {
            if (currentPage < 2) {
                viewPager.setCurrentItem(currentPage + 1);
            } else {
                completeOnboarding();
            }
        });
        
        btnSkip.setOnClickListener(v -> completeOnboarding());
    }
    
    private void setupIndicators() {
        for (int i = 0; i < 3; i++) {
            View indicator = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.setMargins(8, 0, 8, 0);
            indicator.setLayoutParams(params);
            indicator.setBackgroundResource(R.drawable.indicator_inactive);
            layoutIndicator.addView(indicator);
        }
    }
    
    private void setCurrentIndicator(int index) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View indicator = layoutIndicator.getChildAt(i);
            if (i == index) {
                indicator.setBackgroundResource(R.drawable.indicator_active);
            } else {
                indicator.setBackgroundResource(R.drawable.indicator_inactive);
            }
        }
    }
    
    private void completeOnboarding() {
        preferencesHelper.setOnboardingCompleted(true);
        navigateToMain();
    }
    
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                preferencesHelper.setNotificationEnabled(true);
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                preferencesHelper.setNotificationEnabled(true);
            }
        }
    }
    
    private class OnboardingPagerAdapter extends PagerAdapter {
        
        @Override
        public int getCount() {
            return 3;
        }
        
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(OnboardingActivity.this);
            View view;
            
            switch (position) {
                case 0:
                    view = inflater.inflate(R.layout.page_onboarding_goal, container, false);
                    setupGoalPage(view);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.page_onboarding_permission, container, false);
                    setupPermissionPage(view);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.page_onboarding_import, container, false);
                    setupImportPage(view);
                    break;
                default:
                    view = new View(OnboardingActivity.this);
            }
            
            container.addView(view);
            return view;
        }
        
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    
    private void setupGoalPage(View view) {
        CardView cardDrivingTest = view.findViewById(R.id.cardDrivingTest);
        CardView cardLanguage = view.findViewById(R.id.cardLanguage);
        CardView cardGeneral = view.findViewById(R.id.cardGeneral);
        
        cardDrivingTest.setOnClickListener(v -> {
            preferencesHelper.setSelectedGoal("driving_test");
            viewPager.setCurrentItem(1);
        });
        
        cardLanguage.setOnClickListener(v -> {
            preferencesHelper.setSelectedGoal("language");
            viewPager.setCurrentItem(1);
        });
        
        cardGeneral.setOnClickListener(v -> {
            preferencesHelper.setSelectedGoal("general");
            viewPager.setCurrentItem(1);
        });
    }
    
    private void setupPermissionPage(View view) {
        Button btnAllowNotification = view.findViewById(R.id.btnAllowNotification);
        btnAllowNotification.setOnClickListener(v -> {
            requestNotificationPermission();
            viewPager.setCurrentItem(2);
        });
    }
    
    private void setupImportPage(View view) {
        Button btnImportNow = view.findViewById(R.id.btnImportNow);
        btnImportNow.setOnClickListener(v -> {
            // Import functionality can be implemented here
            completeOnboarding();
        });
    }
}

