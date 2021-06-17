package com.talent.crossbar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talent.crossbar.R;
import com.talent.crossbar.adapters.PlayerHistoryQuestionAdapter;
import com.talent.crossbar.databinding.FragmentHistoryBinding;
import com.talent.crossbar.models.Question;
import com.talent.crossbar.utilities.Constants;
import com.talent.crossbar.utilities.PreferenceManagerCustom;


public class PlayerHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlayerHistoryQuestionAdapter adapter;
    private DatabaseReference db;
    PreferenceManagerCustom preferenceManagerCustom;
    private FragmentHistoryBinding binding;



    public PlayerHistoryFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManagerCustom = new PreferenceManagerCustom(getActivity().getApplicationContext());



        db = FirebaseDatabase.getInstance().getReference().child(Constants.KEY_PLAYER_DB)
                .child("Room1").child(preferenceManagerCustom.getString(Constants.KEY_AUTH_ID));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater,container,false);

        recyclerView = binding.hisotryRecyclerView;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity().getApplicationContext()));

        FirebaseRecyclerOptions<Question> options
                = new FirebaseRecyclerOptions.Builder<Question>()
                .setQuery(db, Question.class)
                .build();

        adapter = new PlayerHistoryQuestionAdapter(options);

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