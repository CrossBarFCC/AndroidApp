package com.talent.crossbar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talent.crossbar.R;
import com.talent.crossbar.adapters.AdminHistoryQuestionAdapter;
import com.talent.crossbar.adapters.ScoreAdapter;
import com.talent.crossbar.databinding.FragmentHistoryBinding;
import com.talent.crossbar.databinding.FragmentScoreBoardBinding;
import com.talent.crossbar.models.Question;
import com.talent.crossbar.models.Scores;
import com.talent.crossbar.utilities.Constants;
import com.talent.crossbar.utilities.PreferenceManagerCustom;


public class ScoreBoardFragment extends Fragment {

    FragmentScoreBoardBinding binding;
    private RecyclerView recyclerView;
    private ScoreAdapter adapter;
    private DatabaseReference db;
    PreferenceManagerCustom preferenceManagerCustom;


    public ScoreBoardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManagerCustom = new PreferenceManagerCustom(getActivity().getApplicationContext());



        db = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_SCORE_DB).child("Room1");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScoreBoardBinding.inflate(inflater,container,false);

        recyclerView = binding.scoreRecyclerview;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getApplicationContext()));

        FirebaseRecyclerOptions<Scores> options
                = new FirebaseRecyclerOptions.Builder<Scores>()
                .setQuery(db.orderByChild(Constants.KEY_POINTS), Scores.class)
                .build();

        adapter = new ScoreAdapter(options);

        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}