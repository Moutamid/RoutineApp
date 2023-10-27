package com.moutamid.routineapp.adsense;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.moutamid.routineapp.R;

public class Ads {
    public static InterstitialAd mInterstitialAd;
    public static AdRequest adRequest;
    public static String TAG = "ADS_CHECK";

    /*
    *
      <com.google.android.gms.ads.AdView
        xmlns:ads="http://schemas.android.com/apk/res-auto"
        android:id="@+id/adView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_alignParentBottom="true"
        android:visibility="gone"
        ads:adSize="BANNER"
        ads:adUnitId="@string/AD_Banner_ID">
    </com.google.android.gms.ads.AdView>

    *
    * */

    public static void init(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
        });
        adRequest = new AdRequest.Builder().build();
    }

    public static void showBanner(AdView mAdView, ImageView adPlaceholder) {
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mAdView.setVisibility(View.GONE);
                adPlaceholder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
                adPlaceholder.setVisibility(View.GONE);
            }
        });
    }

    public static void loadIntersAD(Context context, Activity activity, Class intent) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Ad is Loading");
        progressDialog.show();

        InterstitialAd.load(context, context.getResources().getString(R.string.AD_Interstitial_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        progressDialog.dismiss();
                        showInterstitial(context, activity, intent);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                        progressDialog.dismiss();
                        showInterstitial(context, activity, intent);
                    }
                });

    }

    public static void showInterstitial(Context context, Activity activity, Class intent) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    context.startActivity(new Intent(context, intent));
                    activity.finish();
                }
            });
        } else {
            context.startActivity(new Intent(context, intent));
            activity.finish();
        }
    }

}
