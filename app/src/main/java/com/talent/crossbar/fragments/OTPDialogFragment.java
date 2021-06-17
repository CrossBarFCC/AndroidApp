package com.talent.crossbar.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.talent.crossbar.R;
import com.talent.crossbar.actvities.MainActivity;
import com.talent.crossbar.utilities.Constants;
import com.talent.crossbar.utilities.PreferenceManagerCustom;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class OTPDialogFragment extends BottomSheetDialogFragment {


    private static final String TAG = "BMS";
    private OtpTextView otpTextView;
    private Button verify,resend;
    private String OTP ="",verificationId;
    LottieAnimationView verified;
    ConstraintLayout constraintLayout;
    private static String Fname,Fphone,Femail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CountDownTimer timer;
    private PreferenceManagerCustom preferenceManagerCustom;




    public static OTPDialogFragment newInstance(String phone, String name, String email) {

        Fname = name;
        Fphone = phone;
        Femail = email;
        return new OTPDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_o_t_p_dialog_list_dialog, container, false);
        v.setBackgroundColor(Color.TRANSPARENT);
        otpTextView = v.findViewById(R.id.otp_view);
        verify = v.findViewById(R.id.verify_bms);
        verified = v.findViewById(R.id.verified_bms);
        constraintLayout = v.findViewById(R.id.constraint_bms);
        resend = v.findViewById(R.id.resend_bms);
        preferenceManagerCustom = new PreferenceManagerCustom(getActivity());




       sendVerificationCode(Fphone);



        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);
        String phone = getActivity().getSharedPreferences("RegValue",Context.MODE_PRIVATE).getString("Phone","123");




        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
            }

            @Override
            public void onOTPComplete(String otp) {
               OTP = otp;
            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyCode(OTP);

            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(Fphone);
            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.AppBottomSheetDialogTheme);
    }



    private void sendVerificationCode(String number) {

        resend.setEnabled(false);

        timer = new CountDownTimer(45000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = String.valueOf(millisUntilFinished/1000)+"s";
                resend.setText(time);

            }

            @Override
            public void onFinish() {

                resend.setEnabled(true);
                resend.setText("RESEND");

            }
        }.start();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number,
                45,
                TimeUnit.SECONDS,
                getActivity(),
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            verifyCode(code);
                        }

                        else {

                            signInWithCredential(phoneAuthCredential);

                        }

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationId = newVerificationId;
                        Toast.makeText(getActivity(), "OTP Sent", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            constraintLayout.setVisibility(View.GONE);
                            verified.setVisibility(View.VISIBLE);
                            verified.playAnimation();

                            FirebaseUser user = task.getResult().getUser();

                            updateUserDetails(user);
                            AddToFirestore();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                    dismiss();

                                }
                            },2000);

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void AddToFirestore() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore database = FirebaseFirestore.getInstance();



        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,Fname );
        user.put(Constants.KEY_PHONE, Fphone);
        user.put(Constants.KEY_EMAIL, Femail);
        user.put(Constants.KEY_AUTH_ID,userId);

        database.collection(Constants.KEY_USERS_DB)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManagerCustom.putString(Constants.KEY_AUTH_ID, userId);
                    preferenceManagerCustom.putString(Constants.KEY_NAME, Fname);
                    preferenceManagerCustom.putString(Constants.KEY_PHONE, Fphone);
                    preferenceManagerCustom.putString(Constants.KEY_EMAIL,Femail);

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private void  updateUserDetails(FirebaseUser user) {

        final boolean[] bool = {true};


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Fname)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            bool[0] = false;
                        }
                    }
                });

        user.updateEmail(Femail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    bool[0] = false;
                }
            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}