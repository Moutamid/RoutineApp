package com.moutamid.routineapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.fxn.stash.Stash;
import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.ActivityPaymentBinding;
import com.moutamid.routineapp.utils.Constants;

public class PaymentActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityPaymentBinding binding;
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.changeTheme(this);

        binding.toolbar.tittle.setText("Become a Pro member");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        binding.monthly.setOnClickListener(v -> {
            bp.purchase(PaymentActivity.this, Constants.VIP_MONTH);

        });
        binding.lifetime.setOnClickListener(v -> {
            bp.purchase(PaymentActivity.this, Constants.VIP_LIFE);
        });

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