package com.patrickiv.demo.enteranimationdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.patrickiv.demo.enteranimationdemo.fragment.GridDemoFragment
import com.patrickiv.demo.enteranimationdemo.fragment.ListDemoFragment
import kotlinx.android.synthetic.main.activity_main.buttonContainer
import kotlinx.android.synthetic.main.activity_main.gridSample
import kotlinx.android.synthetic.main.activity_main.headerSample
import kotlinx.android.synthetic.main.activity_main.listSample

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listSample.setOnClickListener(this)
        headerSample.setOnClickListener(this)
        gridSample.setOnClickListener(this)

        supportFragmentManager.addOnBackStackChangedListener {
            buttonContainer.alpha = if (supportFragmentManager.backStackEntryCount > 0) 0f else 1f
        }
    }

    override fun onClick(view: View) {
        val fragment = when (view.id) {
            R.id.listSample -> ListDemoFragment.instance(withHeader = false)
            R.id.headerSample -> ListDemoFragment.instance(withHeader = true)
            else -> GridDemoFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}