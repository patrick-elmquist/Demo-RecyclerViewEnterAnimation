package com.patrickiv.demo.enteranimationdemo.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patrickiv.demo.enteranimationdemo.R
import com.patrickiv.demo.enteranimationdemo.model.Model
import com.patrickiv.demo.enteranimationdemo.recyclerview.CardAdapter
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.include_toolbar.reloadButton
import kotlinx.android.synthetic.main.include_toolbar.spinner

class GridDemoFragment : Fragment(R.layout.fragment_grid) {

    private lateinit var selected: Model

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reloadButton.setOnClickListener { runLayoutAnimation(selected) }
        setupRecyclerView()
        setupSpinner()
        runLayoutAnimation(selected)
    }

    private fun setupRecyclerView() = recyclerView.apply {
        layoutManager = GridLayoutManager(context, 4)
        adapter = CardAdapter()
        addItemDecoration(ItemOffsetDecoration(context))
    }

    private fun setupSpinner() = spinner.apply {
        val animations = listOf(
            Model(R.string.animation_slide_from_bottom, R.anim.grid_layout_animation_from_bottom),
            Model(R.string.animation_scale, R.anim.grid_layout_animation_scale),
            Model(R.string.animation_scale_random, R.anim.grid_layout_animation_scale_random)
        )
        val animationNames = animations.map { getString(it.nameRes) }
        adapter = ArrayAdapter<Any?>(context, R.layout.row_spinner_item, animationNames)
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                selected = animations[i].also { runLayoutAnimation(it) }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) = Unit
        }
        selected = animations[0]
    }

    private fun runLayoutAnimation(model: Model) = recyclerView.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, model.resourceId)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()
    }

    private class ItemOffsetDecoration(
        context: Context
    ) : RecyclerView.ItemDecoration() {
        private val spacing = context.resources.getDimensionPixelOffset(R.dimen.default_spacing_small)
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) = outRect.set(spacing, spacing, spacing, spacing)
    }
}
