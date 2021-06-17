package com.talent.crossbar.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.talent.crossbar.R;
import com.talent.crossbar.databinding.FragmentQuizAreaBinding;
import com.talent.crossbar.models.Question;
import com.talent.crossbar.models.Scores;
import com.talent.crossbar.utilities.Constants;
import com.talent.crossbar.utilities.PreferenceManagerCustom;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizAreaFragment extends Fragment {

    private static final String TAG = "QUIZAREAFRAGMENT";
    private FragmentQuizAreaBinding binding;
    private int flag = 0;
    private String answer = "";

    private static String Key = "";
    PreferenceManagerCustom preferenceManagerCustom;
    private DatabaseReference quizDb, playerDb, scoreDb;


    public QuizAreaFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManagerCustom = new PreferenceManagerCustom(getActivity().getApplicationContext());


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentQuizAreaBinding.inflate(inflater, container, false);

        View v = binding.getRoot();

        addChildEventListener();


        binding.quizLayout.radioOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radio = v.findViewById(checkedId);
                answer = radio.getText().toString();
            }
        });

        binding.quizLayout.quizSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check mistake click
                if (answer.equals("")) {
                    Toast.makeText(getContext(), "Please select an answer first", Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.quizLayout.quizSubmit.setText("Submitted");
                binding.quizLayout.quizSubmit.setEnabled(false);
                binding.quizLayout.option1.setEnabled(false);
                binding.quizLayout.option2.setEnabled(false);
                binding.quizLayout.option3.setEnabled(false);
                binding.quizLayout.option4.setEnabled(false);


            }
        });

        return v;


    }

    private void addChildEventListener() {

        quizDb = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_QUESTION_DB);

        quizDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                flag = 0;
                binding.quizLayout.questionTimer.setTextColor(Color.parseColor("#39B479"));
                binding.quizLayout.quizSubmit.setText("Submit");
                binding.quizLayout.quizSubmit.setEnabled(true);
                binding.quizLayout.option1.setEnabled(true);
                binding.quizLayout.option2.setEnabled(true);
                binding.quizLayout.option3.setEnabled(true);
                binding.quizLayout.option4.setEnabled(true);


                Question question = snapshot.getValue(Question.class);


                if (question.getEpochTime() > System.currentTimeMillis()) {

                    Key = snapshot.getRef().getKey();

                    binding.quizLayout.quizWaiting.setVisibility(View.GONE);
                    binding.quizLayout.questionBox.setVisibility(View.VISIBLE);
                    binding.quizLayout.questionText.setText(question.getQuestion());

                    if (question.getOptionA().equals(""))
                        binding.quizLayout.option1.setVisibility(View.GONE);
                    binding.quizLayout.option1.setText(question.getOptionA());

                    if (question.getOptionB().equals(""))
                        binding.quizLayout.option2.setVisibility(View.GONE);
                    binding.quizLayout.option2.setText(question.getOptionB());

                    if (question.getOptionC().equals(""))
                        binding.quizLayout.option3.setVisibility(View.GONE);
                    binding.quizLayout.option3.setText(question.getOptionC());

                    if (question.getOptionD().equals(""))
                        binding.quizLayout.option4.setVisibility(View.GONE);
                    binding.quizLayout.option4.setText(question.getOptionD());

                    long timer = question.getEpochTime() - System.currentTimeMillis();
                    Log.d("Timer", String.valueOf(timer));

                    new CountDownTimer(timer, 1000) {

                        public void onTick(long millisUntilFinished) {
                            NumberFormat f = new DecimalFormat("00");
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            String time = f.format(min) + ":" + f.format(sec);
                            binding.quizLayout.questionTimer.setText(time);
                            if (millisUntilFinished < 15000) {
                                binding.quizLayout.questionTimer.setTextColor(Color.RED);
                            }


                        }

                        public void onFinish() {
                            Log.d(TAG, "Calling from finished: " + question.getQuestion());
                            if (flag == 0)
                                showResultAndAddToFireBase(question);

                            binding.quizLayout.questionBox.setVisibility(View.GONE);
                            binding.quizLayout.quizWaiting.setVisibility(View.VISIBLE);
                            flag = 1;

                        }
                    }.start();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showResultAndAddToFireBase(Question ques) {

        //Add to Firebase

        Log.d(TAG, "Inside show: " + ques.getQuestion());


        playerDb = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_PLAYER_DB);

        HashMap<String, Object> map = new HashMap<>();

        map.put(Constants.KEY_QUESTION, ques.getQuestion());
        map.put(Constants.KEY_CORRECT, ques.getCorrect());
        map.put(Constants.KEY_ANSWERED, answer);

        if (answer.equals(ques.getCorrect())) {
            binding.answerCorrectAnimation.setVisibility(View.VISIBLE);
            binding.answerCorrectAnimation.playAnimation();
        } else {
            binding.answerWrongAnimation.setVisibility(View.VISIBLE);
            binding.answerWrongAnimation.playAnimation();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.answerWrongAnimation.setVisibility(View.GONE);
                binding.answerCorrectAnimation.setVisibility(View.GONE);

                {
                    playerDb.child("Room1").child(preferenceManagerCustom.getString(Constants.KEY_AUTH_ID)).push().updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Logged");
                                        updateAttemptedCount();
                                        updateAnswerQueues();
                                        Log.d(TAG, "Before if: " + ques.getQuestion());
                                        if (answer.equals(ques.getCorrect())) {
                                            updateScores();
                                            Log.d(TAG, "Should call updateScore");
                                        }

                                    }

                                }
                            });
                }
            }
        }, 2000);

    }

    private void updateScores() {

        Log.d(TAG, "Update Score called");

        scoreDb = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_SCORE_DB).child("Room1");

        scoreDb.child(preferenceManagerCustom.getString(Constants.KEY_AUTH_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    updateFirstScore();
                }
                else {
                    updateCurrentScore();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateCurrentScore() {

        scoreDb.child(preferenceManagerCustom.getString(Constants.KEY_AUTH_ID)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Scores s = currentData.getValue(Scores.class);
                if (currentData.getValue() == null) {
                    Log.d(TAG, "Found null");
                    return Transaction.success(currentData);
                } else {

                    int point = s.getPoints();
                    point -= 10;
                    s.setPoints(point);
                }

                currentData.setValue(s);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                if (!committed) {
                    Toast.makeText(getActivity(), "Error updating score", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateFirstScore() {

        HashMap<String,Object> map = new HashMap<>();
        map.put(Constants.KEY_NAME,preferenceManagerCustom.getString(Constants.KEY_NAME));
        map.put(Constants.KEY_POINTS,-10);

        scoreDb.child(preferenceManagerCustom.getString(Constants.KEY_AUTH_ID)).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getActivity(), "Error adding score initially", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void updateAttemptedCount() {


        quizDb.child(Key).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Question q = currentData.getValue(Question.class);
                if (q == null) {
                    return Transaction.success(currentData);
                }

                int count = q.getAttemptedCount();
                q.setAttemptedCount(++count);
                currentData.setValue(q);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });

    }

    private void updateAnswerQueues() {

        Log.d(TAG,"Queues called: "+answer);

        quizDb.child(Key).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                Question q = currentData.getValue(Question.class);
                if (q == null) {
                    return Transaction.success(currentData);
                }

                List<String> list = new ArrayList<>();


                Log.d(TAG,q.getOptionA());
                if(answer.equals(q.getOptionA())){
                    Log.d(TAG,"EqualA");
                    if(q.getOptionAList()!=null){
                        list = q.getOptionAList();
                    }

                    list.add(preferenceManagerCustom.getString(Constants.KEY_NAME));
                    q.setOptionAList(list);
                    currentData.setValue(q);
                    return Transaction.success(currentData);

                }

                Log.d(TAG,q.getOptionB());
                if(answer.equals(q.getOptionB())){
                    Log.d(TAG,"EqualB");
                    if(q.getOptionBList()!=null){
                        list = q.getOptionBList();
                    }

                    list.add(preferenceManagerCustom.getString(Constants.KEY_NAME));
                    q.setOptionBList(list);
                    currentData.setValue(q);
                    return Transaction.success(currentData);

                }


                Log.d(TAG,q.getOptionC());
                if(answer.equals(q.getOptionC())){
                    Log.d(TAG,"EqualC");
                    if(q.getOptionCList()!=null){
                        list = q.getOptionCList();
                    }

                    list.add(preferenceManagerCustom.getString(Constants.KEY_NAME));
                    q.setOptionCList(list);
                    currentData.setValue(q);
                    return Transaction.success(currentData);

                }


                Log.d(TAG,q.getOptionD());
                if(answer.equals(q.getOptionD())){
                    Log.d(TAG,"EqualD");
                    if(q.getOptionDList()!=null){
                        list = q.getOptionDList();
                    }

                    list.add(preferenceManagerCustom.getString(Constants.KEY_NAME));
                    q.setOptionDList(list);
                    currentData.setValue(q);
                    return Transaction.success(currentData);

                }

                return null;
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                if(!committed){
                    Log.d(TAG,error.getDetails());
                }

            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}
