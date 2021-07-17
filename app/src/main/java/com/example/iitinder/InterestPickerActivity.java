package com.example.iitinder;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class InterestPickerActivity extends AppCompatActivity {
    ArrayList<String> interestList;
    ArrayList<String>[] INTEREST_FINAL;
    ConstraintLayout[] interestGridLayout;
    String[] interestArray, subInterestArray;
    String[][] subMultiArray;
    Button submit;
    String LDAP, EMAIL;
    boolean isSelected[];
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_picker);

        EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        LDAP = EMAIL.substring(0, EMAIL.indexOf("@"));

        interestList = new ArrayList<>();
        interestGridLayout = new ConstraintLayout[]{findViewById(R.id.interestConstraint1), findViewById(R.id.interestConstraint2)};
        interestArray = new String[]{"Literature", "Music", "Binge Watching", "Sports", "Gaming", "Art", "Tech", "Anime", "Dance", "Movies", "Finance", "Fashion", "Travel", "Photography", "Food", "Writing", "Work/Career"};
        subMultiArray = new String[][]{
                {"Romantic Era", "Poetry", "Classical Novels", "Harry Potter", "Game of Thrones", "Fiction", "Comics", "Graphic Novels", "Mystery & Thriller"},
                {"Metal", "HipHop", "Rock", "Pop", "Indie", "Classical Music", "Alternative R&B", "Bollywood Songs", "K Pop", "Instrumental"},
                {"The Office", "The Big Bang Theory", "Breaking Bad", "How I Met Your Mother", "Money Heist", "F.R.I.E.N.D.S.", "Scam 1992", "Sherlock", "Game of Thrones", "Fleabag"},
                {"Cricket", "Football", "Basketball", "Table Tennis", "Badminton", "Swimming", "Lawn Tennis", "Volleyball", "Chess", "Athletics"},
                {"Call of Duty", "Valorant", "DOTA", "Counter Strike", "Need for Speed", "FIFA", "Fortnite", "Freefire", "Among Us"},
                {"Painting", "Origami", "Sculpting", "Modern Art", "Graffiti", "Sketching", "Quilling"},
                {"Coding", "Electronics & Robotics", "Aeromodelling", "Maths & Physics", "Energy", "BioX", "Chemistry"},
                {"One Piece", "Naruto", "AOT", "Death Note", "Dragon Ball Z", "Manga", "Studio Ghibli", "Cowboy Bepop", "Haikyuu", "Tokyo Ghoul"},
                {"Classical", "Bollywood", "Hip Hop", "Street Dance", "Folk", "Freestyle"},
                {"Hollywood", "Bollywood", "South Indian", "Animated"},
                {"Trading", "Quants", "Cryptocurrency", "Fin Frauds"},
                {"Casual", "Indian", "Western", "Trending", "Vintage", "Just Shorts & Hoodie"},
                {"Europe", "America", "Australia", "Dubai", "Asian Countries", "Domestic"},
                {"Portraits", "Fashion", "Still Life", "Nature", "Wildlife", "Landscapes"},
                {"Vegan", "Non-Veg", "Regional", "Street Food", "Pizza", "KFC", "Burgers", "Donuts", "Chinese"},
                {"Philosophy", "Fiction", "Suspense", "Adventure", "School Stories", "Sensual"},
                {"Still Exploring", "Serious Plans"}
        };

        length = interestArray.length;
        INTEREST_FINAL = new ArrayList[length];
        for (int i = 0; i < length; i++) {
            INTEREST_FINAL[i] = new ArrayList<>();
        }

        isSelected = new boolean[length];
        for (int i = 0; i < length; i++) {
            isSelected[i] = false;
        }

        submit = findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = 0;
                for (String[] arr: subMultiArray)
                    size += arr.length;

                double array[] = new double[size];
                int k = 0;

                for(int i = 0; i<INTEREST_FINAL.length; i++)
                {
                    for(int j = 0; j<subMultiArray[i].length; j++)
                    {
                        if(INTEREST_FINAL[i].contains(subMultiArray[i][j])) array[k] = 1.0;
                        k++;
                    }
                }
                ArrayList<Double> temp = new ArrayList<>();
                for(double x : array)
                {
                    temp.add(x);
                }
                /*LinkedHashMap<String, ArrayList<Double>> writetofb = new LinkedHashMap<>();
                writetofb.put(LDAP, temp);*/
                FirebaseDatabase.getInstance().getReference().child("mprefs").child(LDAP).setValue(null);
                FirebaseDatabase.getInstance().getReference().child("mprefs").child(LDAP).setValue(temp);

                int count = 0;
                for(int i = 0; i<INTEREST_FINAL.length; i++)
                {
                    if(INTEREST_FINAL[i].isEmpty()) count++;
                }
                if(count < 3) Toast.makeText(getApplicationContext(), "Please select at least 3 interests", Toast.LENGTH_LONG).show();
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP).setValue(null);
                    for(int i = 0; i<INTEREST_FINAL.length; i++)
                    {
                        if(!INTEREST_FINAL[i].isEmpty())
                        {
                            for(int j = 0; j<INTEREST_FINAL[i].size(); j++)
                            {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("interests").child(LDAP);
                                mDatabase.child(interestArray[i]).child(Integer.toString(j)).setValue(INTEREST_FINAL[i].get(j));
                            }
                        }
                    }
                    String state = getIntent().getExtras().getString("state");
                    if ((state.equals("first"))) {
                        startActivity(new Intent(InterestPickerActivity.this, MainActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        });
    }

    int id, mainPos;
    boolean[] subIsSelected;
    ArrayList<String> subList;
    Dialog subinterestDialog;
    ConstraintLayout viewContainer;
    CardView cardView;

    TextView selectedTextView;
    CardView selectedCard;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    public void onClickMainInterest(View view) {
        String idString = getResources().getResourceName(view.getId());
        try {
            id = Integer.parseInt((idString.substring(idString.length() - 2)));
        } catch (NumberFormatException e) {
            id = Integer.parseInt(String.valueOf(idString.charAt(idString.length() - 1)));
        }
        mainPos = id - 1;

        viewContainer = findViewById(view.getContext().getResources().getIdentifier("interestConstraint" + Integer.toString(id), "id", getPackageName()));
        subinterestDialog = new Dialog(this);
        subinterestDialog.setContentView(R.layout.subinterest_picker);
        TextView textView = subinterestDialog.findViewById(R.id.subInterestHeading);
        GridView gridView = subinterestDialog.findViewById(R.id.subGrid);

        textView.setText(textView.getText() + interestArray[mainPos]);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(subinterestDialog.getWindow().getAttributes());
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        subinterestDialog.show();
        subinterestDialog.getWindow().setAttributes(lp);
        Button OK = subinterestDialog.findViewById(R.id.subButtonOK);
        Button cancel = subinterestDialog.findViewById(R.id.subButtonCancel);
        ArrayList<InterestData> subInterestList = new ArrayList<>();
        for (int i = 0; i < subMultiArray[mainPos].length; i++) {
            subInterestList.add(new InterestData(subMultiArray[mainPos][i], 0, false));
        }
        InterestAdapter interestAdapter = new InterestAdapter(InterestPickerActivity.this, subInterestList);
        //recyclerView.setAdapter(interestAdapter);
        //InterestAdapter interestAdapter = new InterestAdapter(this, subMultiArray[mainPos]);
        int[] arr = {0, 0, 0};
        InterestGridAdapter interestGridAdapter = new InterestGridAdapter(InterestPickerActivity.this, subMultiArray[mainPos], arr);
        gridView.setAdapter(interestGridAdapter);
        subIsSelected = new boolean[subMultiArray[mainPos].length];
        for (int i = 0; i < subMultiArray[mainPos].length; i++) subIsSelected[i] = false;
        subList = new ArrayList<>();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTextView = view.findViewById(R.id.mainText);
                selectedCard = view.findViewById(R.id.mainCardView);
                if (!subIsSelected[position]) {
                    view.findViewById(R.id.mainCardView).setElevation(0);
                    view.findViewById(R.id.main_constraint_layout).setBackground(getDrawable(R.drawable.subinterest_btn_clicked));
                    Log.d("CLICKED", subMultiArray[mainPos][position]);
                    subList.add(subMultiArray[mainPos][position]);
                    subIsSelected[position] = true;

                } else {
                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                    view.findViewById(R.id.mainCardView).setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, metrics));
                    view.findViewById(R.id.main_constraint_layout).setBackground(getDrawable(R.drawable.subinterest_btn_unclicked));
                    view.setBackground((getDrawable(R.drawable.main_unclicked)));
                    if (!subList.isEmpty()) subList.remove(subMultiArray[mainPos][position]);
                    subIsSelected[position] = false;
                }
            }
        });

        if (!INTEREST_FINAL[mainPos].isEmpty()) {
            INTEREST_FINAL[id - 1].clear();
            subList.clear();
            for (int i = 0; i < subIsSelected.length; i++) subIsSelected[i] = false;
            isSelected[id] = false;
        }

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INTEREST_FINAL[id - 1] = subList;
                subinterestDialog.dismiss();
                isSelected[id] = true;
                if (!(INTEREST_FINAL[id - 1].isEmpty()))
                    viewContainer.setBackground(getDrawable(R.drawable.main_clicked));
                else viewContainer.setBackground(getDrawable(R.drawable.main_unclicked));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subinterestDialog.dismiss();
                subList.clear();

                for (int i = 0; i < subIsSelected.length; i++)
                    subIsSelected[i] = false;

                viewContainer.setBackground(getDrawable(R.drawable.main_unclicked));
                isSelected[id] = false;
            }
        });
    }
}