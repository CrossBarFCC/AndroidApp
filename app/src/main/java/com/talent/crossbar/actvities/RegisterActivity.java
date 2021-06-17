package com.talent.crossbar.actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.talent.crossbar.fragments.OTPDialogFragment;
import com.talent.crossbar.R;

import static com.talent.crossbar.fragments.OTPDialogFragment.newInstance;

public class RegisterActivity extends AppCompatActivity {

    private Button continueButton;
    private EditText name,email,phone;
    private TextInputLayout emailLo, nameLo, phoneLo;
    private String RegValues = "RegValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        continueButton = findViewById(R.id.continue_register);
        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        phone = findViewById(R.id.phone_register);
        emailLo = findViewById(R.id.email_register_layout);
        nameLo = findViewById(R.id.name_register_layout);
        phoneLo = findViewById(R.id.phone_register_layout);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(validateInputs()) {

                    SharedPreferences sharedpreferences = getSharedPreferences(RegValues, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Phone", phone.getText().toString().trim());
                    editor.putString("Name", name.getText().toString().trim());
                    editor.putString("Email", email.getText().toString().trim());
                    editor.apply();
                    OTPDialogFragment otpdialog = newInstance(phone.getText().toString().trim(), name.getText().toString().trim(), email.getText().toString().trim());

                    otpdialog.show(getSupportFragmentManager(), "dialog");

                }
            }

        });
    }

    public boolean validateInputs() {

        if(TextUtils.isEmpty(name.getText())){
            name.setError("Name required");
            name.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(email.getText())){

            if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                email.setError("Input correct email address");
                email.requestFocus();
                return false;
            }

            email.setError("Email required");
            email.requestFocus();
            return false;

        }

        if(TextUtils.isEmpty(phone.getText())){
            phone.setError("Phone Number required");
            phone.requestFocus();
            return false;
        }

        return true;
    }


}