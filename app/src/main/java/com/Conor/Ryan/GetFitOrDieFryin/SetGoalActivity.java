package com.Conor.Ryan.GetFitOrDieFryin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SetGoalActivity extends AppCompatActivity {
    public static float mSeries = 0f;
    public static float mSeries1 = 0f;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ActionBar mActionBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setgoal);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        final String userId = user.getUid();

        final EditText stepGoal = findViewById(R.id.et1);
        final EditText calorieGoal = findViewById(R.id.et2);

        mToolbar = (Toolbar) findViewById(R.id.recycler_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);


        Button setgoal = findViewById(R.id.setgoal);
        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepGoal.getText().toString().length() == 0) {
                    stepGoal.setError("Set Steps Goal");
                    return;
                } else if (calorieGoal.getText().toString().length() == 0) {
                    calorieGoal.setError("Set Calorie Goal!");
                    return;
                }

                mDatabase.child("Users").child(userId).child("stepgoal").setValue(stepGoal.getText().toString());
                mDatabase.child("Users").child(userId).child("caloriegoal").setValue(calorieGoal.getText().toString());

                mDatabase.child("Users").child(userId).child("stepgoal").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mSeries = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                        Log.d("mSeries", (String.valueOf(mSeries)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                mDatabase.child("Users").child(userId).child("caloriegoal").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mSeries1 = Float.parseFloat(String.valueOf(dataSnapshot.getValue()));
                        Log.d("mSeries1", (String.valueOf(mSeries1)));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                Intent myIntent = new Intent(SetGoalActivity.this, SplashScreen.class);
                startActivity(myIntent);
            }
        });
    }
}
