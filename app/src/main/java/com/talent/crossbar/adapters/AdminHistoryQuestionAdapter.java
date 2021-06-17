package com.talent.crossbar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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
import com.talent.crossbar.databinding.AdminHistoryQuestionLayoutBinding;
import com.talent.crossbar.databinding.PlayerHistoryQuestionLayoutBinding;
import com.talent.crossbar.models.Question;

import java.util.ArrayList;
import java.util.List;

public class AdminHistoryQuestionAdapter extends FirebaseRecyclerAdapter<Question,AdminHistoryQuestionAdapter.questionsViewHolder> {

    private static final String TAG = "AdminADAPTER";
    AdminHistoryQuestionLayoutBinding binding;

    public AdminHistoryQuestionAdapter(@NonNull FirebaseRecyclerOptions<Question> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminHistoryQuestionAdapter.questionsViewHolder holder, int position, @NonNull Question model) {

        Log.d(TAG,model.getQuestion()+ ": "+ model.getAnswered()+" : "+model.getCorrect());

        String optionA,optionB,optionC,optionD,attempted,optionAList,optionBList,optionCList,optionDList;
        List<String> optionAListT = new ArrayList<>();
        List<String> optionBListT = new ArrayList<>();
        List<String> optionCListT = new ArrayList<>();
        List<String> optionDListT = new ArrayList<>();

        optionAListT = model.getOptionAList();
        optionBListT = model.getOptionBList();
        optionCListT = model.getOptionCList();
        optionDListT = model.getOptionDList();

        optionAList = TextUtils.join(", ",optionAListT);
        optionBList = TextUtils.join(", ",optionBListT);
        optionCList = TextUtils.join(", ",optionCListT);
        optionDList = TextUtils.join(", ",optionDListT);


        optionA = "A: "+ model.getOptionA();
        optionB = "B: "+ model.getOptionB();
        optionC = "C: "+ model.getOptionC();
        optionD = "D: "+ model.getOptionD();
        attempted = "Attempted: "+model.getAttemptedCount();

        if(model.getCorrect()!=null) {

            if(model.getOptionA()!=null) {
                if (model.getCorrect().equals(model.getOptionA()))
                    holder.optionA.setTextColor(Color.GREEN);
            }
            else if(model.getOptionB()!=null) {
                if (model.getCorrect().equals(model.getOptionB()))
                    holder.optionB.setTextColor(Color.GREEN);
            }
            else if(model.getOptionC()!=null) {
                if (model.getCorrect().equals(model.getOptionC()))
                    holder.optionC.setTextColor(Color.GREEN);
            }
            else if(model.getOptionD()!=null) {
                if (model.getCorrect().equals(model.getOptionD()))
                    holder.optionD.setTextColor(Color.GREEN);
            }


        }


        holder.question.setText(model.getQuestion());
        holder.optionA.setText(optionA);
        holder.optionB.setText(optionB);
        holder.optionC.setText(optionC);
        holder.optionD.setText(optionD);
        holder.attempted.setText(attempted);
        holder.optionAList.setText(optionAList);
        holder.optionBList.setText(optionBList);
        holder.optionCList.setText(optionCList);
        holder.optionDList.setText(optionDList);


    }


    @NonNull
    @Override
    public questionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = AdminHistoryQuestionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new AdminHistoryQuestionAdapter.questionsViewHolder(binding.getRoot());
    }

    class questionsViewHolder extends RecyclerView.ViewHolder {

        TextView question,optionA,optionB,optionC,optionD,attempted,optionAList,optionBList,optionCList,optionDList;

        public questionsViewHolder(@NonNull View itemView) {
            super(itemView);

            question = binding.questionText;
            optionA = binding.optionA;
            optionB = binding.optionB;
            optionC = binding.optionC;
            optionD = binding.optionD;
            optionAList = binding.optionAList;
            optionBList = binding.optionBList;
            optionCList = binding.optionCList;
            optionDList = binding.optionDList;
            attempted = binding.attemptedText;

        }
    }
}
