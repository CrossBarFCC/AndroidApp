package com.talent.crossbar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.talent.crossbar.R;
import com.talent.crossbar.databinding.PlayerHistoryQuestionLayoutBinding;
import com.talent.crossbar.models.Question;

public class PlayerHistoryQuestionAdapter extends FirebaseRecyclerAdapter<Question,PlayerHistoryQuestionAdapter.questionsViewHolder> {

    private static final String TAG = "ADAPTER";
    PlayerHistoryQuestionLayoutBinding binding;

    public PlayerHistoryQuestionAdapter(@NonNull FirebaseRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlayerHistoryQuestionAdapter.questionsViewHolder holder, int position, @NonNull Question model) {



        String correct = "Correct Answer: "+model.getCorrect();
        String answer = "Your Answer: "+model.getAnswered();

        holder.question.setText(model.getQuestion());
        holder.correct.setText(correct);
        holder.answer.setText(answer);
        if(model.getAnswered().equals(model.getCorrect())){
            holder.answer.setTextColor(Color.GREEN);
        }
        else holder.answer.setTextColor(Color.RED);

    }

    @NonNull
    @Override
    public questionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = PlayerHistoryQuestionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new PlayerHistoryQuestionAdapter.questionsViewHolder(binding.getRoot());
    }

    class questionsViewHolder extends RecyclerView.ViewHolder {

        TextView question,answer, correct;



        public questionsViewHolder(@NonNull View itemView) {
            super(itemView);

            question = binding.questionText;
            answer = binding.answerText;
            correct = binding.correctText;
        }
    }
}
