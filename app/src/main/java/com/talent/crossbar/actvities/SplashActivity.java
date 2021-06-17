package com.talent.crossbar.actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.talent.crossbar.R;
import com.talent.crossbar.utilities.Constants;
import com.talent.crossbar.utilities.PreferenceManagerCustom;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH";
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView company, motto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        image = findViewById(R.id.splash_logo);
        company = findViewById(R.id.splash_company);
        motto = findViewById(R.id.splash_motto);

        image.setAnimation(topAnim);
        company.setAnimation(bottomAnim);
        motto.setAnimation(bottomAnim);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        addToPreference(user);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent;
                if (user != null) {

                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, RegisterActivity.class);
                }
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        SplashActivity.this, image, ViewCompat.getTransitionName(image));
                startActivity(intent, options.toBundle());

            }
        },3000);

    }

    private void addToPreference(FirebaseUser user) {

        if(user==null) return;

        else {
            FirebaseFirestore base = FirebaseFirestore.getInstance();

            base.collection(Constants.KEY_USERS_DB).whereEqualTo(Constants.KEY_AUTH_ID,user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    PreferenceManagerCustom preferenceManagerCustom = new PreferenceManagerCustom(SplashActivity.this);

                                    preferenceManagerCustom.putString(Constants.KEY_AUTH_ID, document.get(Constants.KEY_AUTH_ID).toString());
                                    preferenceManagerCustom.putString(Constants.KEY_NAME, document.get(Constants.KEY_NAME).toString());
                                    preferenceManagerCustom.putString(Constants.KEY_PHONE, document.get(Constants.KEY_PHONE).toString());
                                    preferenceManagerCustom.putString(Constants.KEY_EMAIL,document.get(Constants.KEY_EMAIL).toString());

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }
                    });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}