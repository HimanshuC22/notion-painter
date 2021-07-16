package com.example.iitinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordEntryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "PasswordEntryFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PasswordEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasswordEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PasswordEntryFragment newInstance(String param1, String param2) {
        PasswordEntryFragment fragment = new PasswordEntryFragment();
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
        return inflater.inflate(R.layout.fragment_password_entry, container, false);
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("DETAILS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        view.findViewById(R.id.btnPasswordEntryNext).setOnClickListener(v -> {
            String passwd = ((EditText) view.findViewById(R.id.editTextTextPassword)).getText().toString();
            String confirm = ((EditText) view.findViewById(R.id.editTextTextPassword2)).getText().toString();
            if(passwd.equals(confirm) && (!(passwd.isEmpty()) && !(confirm.isEmpty())))
            {
                editor.putString("PASSWORD", passwd);
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, com.example.iitinder.CreateUserFragment.newInstance(mParam1, passwd))
                        .addToBackStack(null)
                        .commit();
            }
            else
            {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                ((EditText)view.findViewById(R.id.editTextTextPassword)).setText("");
                ((EditText)view.findViewById(R.id.editTextTextPassword2)).setText("");
            }
        });
    }
}