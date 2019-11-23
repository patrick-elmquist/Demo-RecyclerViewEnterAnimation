package com.patrickiv.demo.enteranimationdemo.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.patrickiv.demo.enteranimationdemo.R
import com.patrickiv.demo.enteranimationdemo.model.Model
import com.patrickiv.demo.enteranimationdemo.recyclerview.CardAdapter
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.include_toolbar.reloadButton
import kotlinx.android.synthetic.main.include_toolbar.spinner

class ListDemoFragment : Fragment(R.layout.fragment_list) {

    private lateinit var selected: Model
    private var hasHeader: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hasHeader = arguments?.getBoolean(ARG_WITH_HEADER) ?: hasHeader
        reloadButton.setOnClickListener { runLayoutAnimation(selected) }
        setupRecyclerView(hasHeader)
        setupSpinner(hasHeader)
        runLayoutAnimation(selected)
    }

    private fun setupRecyclerView(withHeader: Boolean) = recyclerView.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = CardAdapter(withHeader = withHeader)
        addItemDecoration(ItemOffsetDecoration(context, withHeader = withHeader))
    }

    private fun setupSpinner(withHeader: Boolean) = spinner.apply {
        val animations = if (withHeader) {
            listOf(
                Model(R.string.animation_slide_from_bottom, R.anim.layout_animation_from_bottom),
                Model(R.string.animation_slide_from_right, R.anim.layout_animation_from_right)
            )
        } else {
            listOf(
                Model(R.string.animation_fall_down, R.anim.layout_animation_fall_down),
                Model(R.string.animation_slide_from_bottom, R.anim.layout_animation_from_bottom),
                Model(R.string.animation_slide_from_right, R.anim.layout_animation_from_right)
            )
        }
        selected = animations[0]
        adapter = ArrayAdapter<Any?>(
            context,
            R.layout.row_spinner_item,
            animations.map { getString(it.nameRes) }
        )
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                selected = animations[i].also { runLayoutAnimation(it) }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) = Unit
        }
    }

    private fun runLayoutAnimation(model: Model) = recyclerView.apply {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, model.resourceId)
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()

        if (hasHeader) {
            layoutAnimationListener = object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    layoutManager?.findViewByPosition(0)?.clearAnimation()
                }
                override fun onAnimationEnd(animation: Animation?) = Unit
                override fun onAnimationRepeat(animation: Animation?) = Unit
            }
        }
    }

    companion object {
        private const val ARG_WITH_HEADER = "arg_with_header"
        fun instance(withHeader: Boolean) = ListDemoFragment().apply {
            arguments = bundleOf(ARG_WITH_HEADER to withHeader)
        }
    }
}

private class ItemOffsetDecoration(
    context: Context,
    private val withHeader: Boolean = false
) : RecyclerView.ItemDecoration() {
    private val spacing = context.resources.getDimensionPixelOffset(R.dimen.default_spacing_medium)
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter ?: return
        val vh = parent.findContainingViewHolder(view) ?: return
        val isFirst = vh.adapterPosition == 0
        val isLast = vh.adapterPosition == adapter.itemCount - 1
        if (withHeader) {
            when {
                isFirst -> outRect.set(0, 0, 0, spacing / 2)
                isLast -> outRect.set(spacing, spacing / 2, spacing, spacing)
                else -> outRect.set(spacing, spacing / 2, spacing, spacing / 2)
            }
        } else {
            when {
                isFirst -> outRect.set(spacing, spacing, spacing, spacing / 2)
                isLast -> outRect.set(spacing, spacing / 2, spacing, spacing)
                else -> outRect.set(spacing, spacing / 2, spacing, spacing / 2)
            }
        }
    }
}