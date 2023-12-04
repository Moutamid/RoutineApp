package com.moutamid.routineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.moutamid.routineapp.databinding.ActivityMainBinding;
import com.moutamid.routineapp.fragments.HomeFragment;
import com.moutamid.routineapp.fragments.InsightsFragment;
import com.moutamid.routineapp.activities.AddActivity;
import com.moutamid.routineapp.activities.SettingActivity;
import com.moutamid.routineapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityMainBinding binding;
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        int theme = Stash.getInt(Constants.THEME);
        setTheme(theme);
        setContentView(binding.getRoot());

        Constants.changeTheme(this);
        Constants.checkApp(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        Log.d("onCreate123", "onCreate: " + Constants.auth().getCurrentUser().getUid());

        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }


        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        ArrayList<String> ids = new ArrayList<>();
        ids.add(Constants.VIP_MONTH);
        ids.add(Constants.VIP_LIFE);
        bp.getSubscriptionsListingDetailsAsync(ids, new BillingProcessor.ISkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                Log.d("PURSS", "Size : " + products.size());
                for (int i = 0; i < products.size(); i++){
                    boolean isSub = products.get(i).isSubscription;
                    Stash.put(Constants.IS_VIP, isSub);
                }
            }

            @Override
            public void onSkuDetailsError(String error) {

            }
        });
        Log.d("PURSS", "init : " + bp.isInitialized());

        if (bp.isSubscribed(Constants.VIP_MONTH) || bp.isSubscribed(Constants.VIP_LIFE)){
            Stash.put(Constants.IS_VIP, true);
            binding.addLayout.setVisibility(View.GONE);
        } else {
            Stash.put(Constants.IS_VIP, false);
            showAd();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();

        binding.home.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit());
        binding.insights.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new InsightsFragment()).commit());

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
            finish();
        });
        binding.add.setOnClickListener(v -> {
            startActivity(new Intent(this, AddActivity.class));
            finish();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        }

    }

    private void showAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                binding.placeholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.add.setCardBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}