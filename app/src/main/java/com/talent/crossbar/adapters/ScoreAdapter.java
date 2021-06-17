package com.talent.crossbar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.telephony.IccOpenLogicalChannelResponse;
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

import com.talent.crossbar.databinding.ScoreTileItemBinding;
import com.talent.crossbar.models.Scores;

public class ScoreAdapter extends FirebaseRecyclerAdapter<Scores,ScoreAdapter.ScoressViewHolder> {

    private static final String TAG = "ADAPTER";
    ScoreTileItemBinding binding;
    int max=0;

    public ScoreAdapter(@NonNull FirebaseRecyclerOptions<Scores> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ScoreAdapter.ScoressViewHolder holder, int position, @NonNull Scores model) {



        String rank = String.valueOf(position+1)+") ";
        int point = -1*model.getPoints();
        String marks = String.valueOf(point);


        if(position==0){
            max = model.getPoints();
        }

        holder.name.setText(model.getName());
        holder.points.setText(marks);
        holder.rank.setText(rank);

        if(model.getPoints()<=max){
            holder.name.setTextColor(Color.MAGENTA);
        }

        else holder.name.setTextColor(Color.GRAY);

    }

    @NonNull
    @Override
    public ScoressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = ScoreTileItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new ScoreAdapter.ScoressViewHolder(binding.getRoot());
    }

    class ScoressViewHolder extends RecyclerView.ViewHolder {

        TextView rank,name,points;



        public ScoressViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = binding.positionScore;
            name = binding.nameScore;
            points = binding.pointsScore;
        }
    }
}

