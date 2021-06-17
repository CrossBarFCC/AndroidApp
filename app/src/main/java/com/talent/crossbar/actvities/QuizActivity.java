package com.talent.crossbar.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;
import com.talent.crossbar.R;
import com.talent.crossbar.adapters.QuizPagerAdapter;
import com.talent.crossbar.databinding.ActivityQuizBinding;
import com.talent.crossbar.fragments.PlayerHistoryFragment;
import com.talent.crossbar.fragments.QuizAreaFragment;
import com.talent.crossbar.fragments.ScoreBoardFragment;

public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private QuizActivity activity;
    private QuizPagerAdapter adapter;
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // firebaseDatabase.setPersistenceEnabled(true);

        activity = this;

        initView();
    }

    private void initView() {

        setupQuizPager(binding.viewPager);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position)-> {tab.setText(adapter.mFragmentTitleList.get(position));
                }).attach();

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++){

            TextView tv = (TextView) LayoutInflater.from(activity)
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayout.getTabAt(i).setCustomView(tv);
        }

    }

    private void setupQuizPager(ViewPager2 viewPager) {

        adapter =new QuizPagerAdapter(activity.getSupportFragmentManager(),
                activity.getLifecycle());
        adapter.addFragment(new QuizAreaFragment(), "Quiz");
        adapter.addFragment(new ScoreBoardFragment(),"Scoreboard");
        adapter.addFragment(new PlayerHistoryFragment(),"History");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }


}