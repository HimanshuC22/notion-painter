package com.example.iitinder.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.iitinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageChangeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageChangeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ImageChangeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ImageChangeFragment newInstance() {
        ImageChangeFragment fragment = new ImageChangeFragment();
        return fragment;
    }

    ActivityResultLauncher<Intent> galleryImageResultLauncher, cameraImageResultLauncher;
    String LDAP_ID, APP_TAG = "IITinder";

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri imageURI = data.getData();
                            changeImage(imageURI);
                            Glide.with(getContext()).load(imageURI).into(profilePhoto);
                        }
                    }
                });
        cameraImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            changeImage(getImageUri(getContext(), bitmap));
                            profilePhoto.setImageBitmap(bitmap);
                        }
                    }
                });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String EMAIL = mAuth.getCurrentUser().getEmail();
        LDAP_ID = EMAIL.substring(0, EMAIL.indexOf("@"));
        Log.d("LDAP", LDAP_ID);
        StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("photos").child(LDAP_ID).child("profile.jpg");
        imageReference.getBytes(2 * 1024 * 1024).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePhoto.setImageBitmap(bitmap);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_change, container, false);
    }

    ImageButton changeButton;
    ImageView profilePhoto;
    Button cancel;
    StorageReference storageReference;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // defining variables
        setUp(view);

        Glide.with(getContext()).load(storageReference.child("photos").child(LDAP_ID).child("profile.jpg")).into(profilePhoto);

        // setting rounded image as the image source
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.male);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(80);
        roundedBitmapDrawable.setAntiAlias(true);
        profilePhoto.setImageDrawable(roundedBitmapDrawable);*/

        // onClickListeners for buttons
        cancel.setOnClickListener(v -> {
            getActivity().finish();
        });
        changeButton.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.profile_change_dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogThemeOther;
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.top_rounded));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            LinearLayout changeFromCamera, changeFromGallery, removePhoto;
            changeFromCamera = dialog.findViewById(R.id.changeFromCamera);
            changeFromGallery = dialog.findViewById(R.id.changeFromGallery);
            removePhoto = dialog.findViewById(R.id.removePhoto);
            dialog.show();
            // changing from gallery
            changeFromGallery.setOnClickListener(v1 -> {
                picklImageFromGallery();
                dialog.dismiss();
            });

            // changing from camera
            changeFromCamera.setOnClickListener(v1 -> {
                getImageFromCamera();
                dialog.dismiss();
            });

            // removing photo
            removePhoto.setOnClickListener(v1 -> {
                removeImage();
                dialog.dismiss();
            });
        });

    }

    public void setUp(View view)
    {
        storageReference = FirebaseStorage.getInstance().getReference();
        profilePhoto = view.findViewById(R.id.profilePicChange);
//        OK = view.findViewById(R.id.buttonOKProfile);
        cancel = view.findViewById(R.id.buttonCancelProfile);
        changeButton = view.findViewById(R.id.changeButton);
    }

    public void changeImage(Uri uri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("photos").child(LDAP_ID).child("profile.jpg");
        storageReference.putFile(uri);
        
    }

    public void picklImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryImageResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Log.e("TAG", "onActivityResult: PERMISSION GRANTED");
                    } else {
                        Log.e("TAG", "onActivityResult: PERMISSION DENIED");
                    }
                }
            });

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public void getImageFromCamera() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraImageResultLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(getContext(), "No permissions", Toast.LENGTH_LONG);
            mPermissionResult.launch(Manifest.permission.CAMERA);
        }
    }

    public void removeImage() {
        Bitmap bitmap;
        String gender = getString(R.string.USER_GENDER);
        if (gender.equals("M")) {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.male);
        } else {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.female);
        }
        profilePhoto.setImageBitmap(bitmap);
        changeImage(getImageUri(getContext(), bitmap));
    }

}