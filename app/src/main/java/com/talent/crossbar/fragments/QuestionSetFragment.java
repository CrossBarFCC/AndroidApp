package com.talent.crossbar.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talent.crossbar.R;
import com.talent.crossbar.actvities.QuizActivity;
import com.talent.crossbar.databinding.FragmentQuestionSetBinding;
import com.talent.crossbar.utilities.Constants;

import java.time.Instant;
import java.util.HashMap;


public class QuestionSetFragment extends Fragment {

    private FragmentQuestionSetBinding binding;

    //private FirebaseDatabase firebaseDatabase = QuizActivity.firebaseDatabase;
    private String question,optionA,optionB,optionC,optionD,correct = "E",time;
    private long epochTime;



    public QuestionSetFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentQuestionSetBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.options_list, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.optionSpinner.setAdapter(adapter);

        binding.optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                correct = binding.optionSpinner.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.loader.setVisibility(View.VISIBLE);

                question = binding.questionSet.getText().toString().trim();
                optionA = binding.optionA.getText().toString().trim();
                optionB = binding.optionB.getText().toString().trim();
                optionC = binding.optionC.getText().toString().trim();
                optionD = binding.optionD.getText().toString().trim();
                time = binding.timeGiven.getText().toString().trim();



                if(validateInputs()){

                    switch (correct) {
                        case "A": {
                            correct = optionA;
                            break;
                        }
                        case "B": {
                            correct = optionB;
                            break;
                        }
                        case "C": {
                            correct = optionC;
                            break;
                        }
                        case "D": {
                            correct = optionD;
                            break;
                        }
                    }

                    long now = System.currentTimeMillis();
                    epochTime = now + (long)Double.parseDouble(time)*60000;

                    addToFirebase();


                }

                else Toast.makeText(getActivity(), "Not validated", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }



    private boolean validateInputs() {

        if(TextUtils.isEmpty(question)){
            binding.questionSet.setError("Question is required");
            binding.loader.setVisibility(View.GONE);
            return false;
        }

        if(TextUtils.isEmpty(optionA) && TextUtils.isEmpty(optionB) && TextUtils.isEmpty(optionC) && TextUtils.isEmpty(optionD)){
            binding.questionSet.setError("At least one option is required");
            binding.loader.setVisibility(View.GONE);
            return false;
        }

        if(TextUtils.isEmpty(time)){
            time = "2";     //Default time of 2 min
        }

        if(correct.equals("E")){
            Toast.makeText(getActivity(), "Please select correct option", Toast.LENGTH_SHORT).show();
            binding.loader.setVisibility(View.GONE);
            return false;
        }

        return true;

    }


    private void addToFirebase() {

        Log.d("UPOAD","Adding function called");

        DatabaseReference quizDB = FirebaseDatabase.getInstance().getReference();

        Log.d("UPOAD",quizDB.toString());

        HashMap<String, Object> questionMap = new HashMap<>();
        questionMap.put(Constants.KEY_QUESTION,question);
        questionMap.put(Constants.KEY_OPTION_A,optionA);
        questionMap.put(Constants.KEY_OPTION_B,optionB);
        questionMap.put(Constants.KEY_OPTION_C,optionC);
        questionMap.put(Constants.KEY_OPTION_D,optionD);
        questionMap.put(Constants.KEY_EPOCH_TIME,epochTime);
        questionMap.put(Constants.KEY_CORRECT,correct);
        questionMap.put(Constants.KEY_ATTEMPTED_COUNT,0);

        quizDB.child(Constants.KEY_QUESTION_DB).push().updateChildren(questionMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    binding.loader.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Question Added successfully", Toast.LENGTH_SHORT).show();
                }

                else {
                    binding.loader.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error: " +task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}