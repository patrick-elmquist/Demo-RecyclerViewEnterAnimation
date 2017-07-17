package com.patrickiv.demo.enteranimationdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.patrickiv.demo.enteranimationdemo.R;
import com.patrickiv.demo.enteranimationdemo.model.AnimationItem;
import com.patrickiv.demo.enteranimationdemo.recyclerview.ItemOffsetDecoration;
import com.patrickiv.demo.enteranimationdemo.recyclerview.CardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for layout animation demos
 *
 * Created by Patrick Ivarsson on 7/17/17.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private final Handler mHandler = new Handler();

    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    private RecyclerView mRecyclerView;
    private AppCompatSpinner mSpinner;

    /**
     * Get the layout to use for the demo
     * @return the resource id
     */
    protected abstract int getLayoutResId();

    /**
     * Get the layout manager to use for the demo
     * @param context the context
     * @return the layout manager
     */
    protected abstract RecyclerView.LayoutManager getLayoutManager(Context context);

    /**
     * Get the array of animations to choose from
     * @return the array
     */
    protected abstract AnimationItem[] getAnimationItems();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAnimationItems = getAnimationItems();
        mSelectedItem = mAnimationItems[0];

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner);
        view.findViewById(R.id.btn_reload).setOnClickListener(this);

        setupRecyclerView();
        setupSpinner();
        runLayoutAnimation(mRecyclerView, mSelectedItem);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_reload) {
            runLayoutAnimation(mRecyclerView, mSelectedItem);
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, item.getResourceId());

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void setupRecyclerView() {
        final Context context = mRecyclerView.getContext();
        final int spacing = getResources().getDimensionPixelOffset(R.dimen.default_spacing_small);
        mRecyclerView.setLayoutManager(getLayoutManager(context));
        mRecyclerView.setAdapter(new CardAdapter());
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(spacing));
    }

    private void setupSpinner() {
        final List<String> itemNames = new ArrayList<>();
        for (final AnimationItem item : mAnimationItems) {
            itemNames.add(item.getName());
        }

        final Context ctx = mRecyclerView.getContext();
        // Apply another theme to make the spinner text the right color
        final ContextThemeWrapper themedCtx = new ContextThemeWrapper(ctx, R.style.Theme_AppCompat);
        mSpinner.setAdapter(new ArrayAdapter<>(themedCtx, R.layout.row_spinner_item, itemNames));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedItem = mAnimationItems[i];
                runLayoutAnimation(mRecyclerView, mSelectedItem);
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
}
