 package com.example.iitinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.iitinder.LoginSignup.First_page;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private BottomNavigationView navigationView;
    private ImageView logoutButton, matchesButton;
    private FragmentContainerView containerView;

    @Override
    protected void onStart() {
        super.onStart();
        if (current_user_id == null || !(mAuth.getCurrentUser().isEmailVerified())) {
            startActivity(new Intent(MainActivity.this, First_page.class));
            Log.d("MAIN", "STARTEDFIRSTPAGE");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();

        String EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String LDAP = EMAIL.substring(0, EMAIL.indexOf("@"));
        editor.putString("LDAP_ID", LDAP);


        setUpBottomNavigationBar();

    }

    private void setUpBottomNavigationBar() {
        navigationView.setItemRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.tinder_light)));
        navigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() != R.id.item_profile) logoutButton.setVisibility(View.GONE);
            switch (item.getItemId()) {
                case R.id.item_main: {
                    viewPager.setCurrentItem(0);
                }
                return true;
                case R.id.item_requests: {
                    viewPager.setCurrentItem(1);
                }
                return true;
                case R.id.item_chat: {
                    viewPager.setCurrentItem(2);
                }
                return true;
                case R.id.item_profile: {
                    viewPager.setCurrentItem(3);
                    logoutButton.setVisibility(View.VISIBLE);
                }
                return true;

            }
            return false;
        });

        matchesButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MatchesActivity.class));
        });
    }

    public void setUp() {
        sharedPreferences = getSharedPreferences("CREDENTIALS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getUid();
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.bottomNavigationView);

        setUpViewPager();

        logoutButton = findViewById(R.id.button_logout);
        matchesButton = findViewById(R.id.button_matches);

        logoutButton.setVisibility(View.GONE);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            editor.remove("EMAIL").apply();
            editor.remove("PASSWORD").apply();
            startActivity(new Intent(MainActivity.this, First_page.class));
            onDestroy();
        });
//        containerView = findViewById(R.id.mainFragmentContainerView);
    }

    public void setUpViewPager()
    {
        viewPager.setAdapter(new ViewPagerAdapter(this));
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch (position) {
                        case 0:
                            navigationView.setSelectedItemId(R.id.item_main);
                            break;
                        case 1:
                            navigationView.setSelectedItemId(R.id.item_requests);
                            break;
                        case 2:
                            navigationView.setSelectedItemId(R.id.item_chat);
                            break;
                        case 3:
                            navigationView.setSelectedItemId(R.id.item_profile);
                            break;
                    }
                }
            });
    }
}