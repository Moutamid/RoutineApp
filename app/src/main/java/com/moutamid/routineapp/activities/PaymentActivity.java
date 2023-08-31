package com.moutamid.routineapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.adsense.Ads;
import com.moutamid.routineapp.databinding.ActivityPaymentBinding;
import com.moutamid.routineapp.utils.Constants;

public class PaymentActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityPaymentBinding binding;
    BillingProcessor bp;
    boolean monthly = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.changeTheme(this);

        binding.toolbar.tittle.setText("Become a Pro member");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());


        if (Stash.getBoolean(Constants.LANGUAGE, true)){
            Constants.setLocale(getBaseContext(), Constants.EN);
        } else {
            Constants.setLocale(getBaseContext(), Constants.ES);
        }

        if (!Stash.getBoolean(Constants.IS_VIP)){
            Stash.put(Constants.IS_VIP, false);
            Ads.init(this);
            Ads.showInterstitial(this, this);
        }

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        binding.monthly.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));

        binding.monthly.setOnClickListener(v -> {
            monthly = true;
            binding.monthly.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
            binding.lifetime.setStrokeColor(getResources().getColor(R.color.border));
        });
        binding.lifetime.setOnClickListener(v -> {
            monthly = false;
            binding.monthly.setStrokeColor(getResources().getColor(R.color.border));
            binding.lifetime.setStrokeColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        });

        binding.upgrade.setOnClickListener(v -> {
            if (monthly){
                bp.purchase(PaymentActivity.this, Constants.VIP_MONTH);
            } else {
                bp.purchase(PaymentActivity.this, Constants.VIP_LIFE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    private void updateViews() {
        binding.text1.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.text2.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.mony1.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.mony2.setTextColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
        binding.upgrade.setBackgroundColor(Stash.getInt(Constants.COLOR, getResources().getColor(R.color.light)));
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