package com.patrickiv.demo.enteranimationdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.patrickiv.demo.enteranimationdemo.fragment.GridDemoFragment;
import com.patrickiv.demo.enteranimationdemo.fragment.ListDemoFragment;

/**
 * Created by Patrick Ivarsson on 7/17/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup mButtonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonContainer = findViewById(R.id.button_container);
        findViewById(R.id.btn_list_fragment).setOnClickListener(this);
        findViewById(R.id.btn_grid_fragment).setOnClickListener(this);

        if (savedInstanceState != null) {
            final int count = getSupportFragmentManager().getBackStackEntryCount();
            mButtonContainer.setAlpha(count > 0 ? 0f : 1f);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            mButtonContainer.animate().alpha(1f).start();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_list_fragment:
                showFragment(new ListDemoFragment());
                break;
            case R.id.btn_grid_fragment:
                showFragment(new GridDemoFragment());
                break;
            default:
                break;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        mButtonContainer.animate().alpha(0f).start();
    }

}
