package com.example.iitinder.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.example.iitinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PhotoChangeDialog extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_change_dialog, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.changeFromCamera);
        linearLayout.setOnClickListener(v -> {
            Log.d("CLICKED", "CHANGE-FROM-CAMERA");
        });
        getDialog().dismiss();
        return view;
    }
}
