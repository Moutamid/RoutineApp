package com.moutamid.routineapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.routineapp.R;
import com.moutamid.routineapp.databinding.FragmentInsightsBinding;

public class InsightsFragment extends Fragment {
    FragmentInsightsBinding binding;
    public InsightsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInsightsBinding.inflate(getLayoutInflater(), container, false);


        return binding.getRoot();
    }
}