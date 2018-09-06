package it.a4smart.vateoperator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.a4smart.vateoperator.setup.BluetoothFragment;
import it.a4smart.vateoperator.setup.NFCFragment;
import it.a4smart.vateoperator.setup.ParametersFragment;

public class SetupActivity extends FragmentActivity {

    private final int PAGE_NUM = 3;

    private ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        setBottomDots(0);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(v -> launchHomeScreen());

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem() + 1;
            if (current < PAGE_NUM) viewPager.setCurrentItem(current);
            else launchHomeScreen();
        });
    }

    private void setBottomDots(int page) {
        dots = new TextView[PAGE_NUM];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.WHITE);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) dots[page].setTextColor(Color.BLACK);
    }


    private void launchHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int i) {
            setBottomDots(i);

            if (i == PAGE_NUM - 1) {
                btnNext.setText("fine");
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText("succ");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new NFCFragment();
                case 1:
                    return new BluetoothFragment();
                case 2:
                    return new ParametersFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }
    }
}