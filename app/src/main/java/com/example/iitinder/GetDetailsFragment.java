package com.example.iitinder;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;
    private String gender;

    public GetDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetDetailsFragment newInstance(String param1, String param2) {
        GetDetailsFragment fragment = new GetDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((RadioGroup) view.findViewById(R.id.radioGroup)).setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbtnMale:
                    group.findViewById(R.id.rbtnMale).setBackground(getResources().getDrawable(R.drawable.radio_checked));
                    group.findViewById(R.id.rbtnFemale).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    group.findViewById(R.id.rBtnOther).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    gender = "m";
                    break;
                case R.id.rbtnFemale:
                    group.findViewById(R.id.rbtnMale).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    group.findViewById(R.id.rbtnFemale).setBackground(getResources().getDrawable(R.drawable.radio_checked));
                    group.findViewById(R.id.rBtnOther).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    gender = "f";
                    break;
                case R.id.rBtnOther:
                    group.findViewById(R.id.rbtnMale).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    group.findViewById(R.id.rbtnFemale).setBackground(getResources().getDrawable(R.drawable.radiobutton));
                    group.findViewById(R.id.rBtnOther).setBackground(getResources().getDrawable(R.drawable.radio_checked));
                    gender = "o";
                    break;
            }
        });

        final Calendar cal = Calendar.getInstance();
        EditText editTextDate = view.findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTextDate.setText(year + " " + month + " " + dayOfMonth);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        view.findViewById(R.id.btnGetDetailsDone).setOnClickListener(v -> {
            String name, dob, phone;
            name = ((EditText) view.findViewById(R.id.editTextTextPersonName)).getText().toString();
            dob = editTextDate.getText().toString();
            phone = ((EditText) view.findViewById(R.id.editTextPhone)).getText().toString();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            // mParam1 is the ldap email, e.g. 20d990012@iitb.ac.in, but we only need the ldap part
            String ldap = mParam1.substring(0, mParam1.lastIndexOf('@'));
            mDatabase.child("data").child(ldap).child("name").setValue(name);
            mDatabase.child("data").child(ldap).child("gender").setValue(gender);
            mDatabase.child("data").child(ldap).child("dob").setValue(dob);
            mDatabase.child("map").child(phone).setValue(ldap);

            startActivity(new Intent(getActivity(), InterestPickerActivity.class));
        });
    }
}