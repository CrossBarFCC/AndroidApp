package com.talent.crossbar.actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.talent.crossbar.R;
import com.talent.crossbar.adapters.QuizPagerAdapter;
import com.talent.crossbar.databinding.ActivityAdminBinding;
import com.talent.crossbar.fragments.AdminHistoryFragment;
import com.talent.crossbar.fragments.PlayerHistoryFragment;
import com.talent.crossbar.fragments.QuestionSetFragment;
import com.talent.crossbar.fragments.ScoreBoardFragment;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private AdminActivity activity;
    private QuizPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        initView();

    }

    private void initView() {

        setupAdminPager(binding.viewPager);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(adapter.mFragmentTitleList.get(position));
                }).attach();

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {

            TextView tv = (TextView) LayoutInflater.from(activity)
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayout.getTabAt(i).setCustomView(tv);
        }
    }

    private void setupAdminPager(ViewPager2 viewPager) {

        adapter =new QuizPagerAdapter(activity.getSupportFragmentManager(),
                activity.getLifecycle());
        adapter.addFragment(new QuestionSetFragment(), "Question");
        adapter.addFragment(new ScoreBoardFragment(),"Scoreboard");
        adapter.addFragment(new AdminHistoryFragment(),"History");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }


}