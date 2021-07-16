package com.example.iitinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetOtherDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetOtherDetails extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public GetOtherDetails() {

    }

    public static GetOtherDetails newInstance(String param1) {
        GetOtherDetails fragment = new GetOtherDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_other_details, container, false);
    }

    String LDAP;
    Spinner spinner;
    EditText boiEditText;
    TextView skip;
    Button next;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void setUp(View view)
    {
        LDAP = mParam1;
        next = view.findViewById(R.id.nextOtherDetails);
        boiEditText = view.findViewById(R.id.editTextBio);

    }

}