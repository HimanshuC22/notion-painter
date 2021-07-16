package com.example.iitinder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BirthdayChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BirthdayChangeFragment extends Fragment {

    public BirthdayChangeFragment() {
        // Required empty public constructor
    }

    public static BirthdayChangeFragment newInstance() {
        BirthdayChangeFragment fragment = new BirthdayChangeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_birthday_change, container, false);

    }
}