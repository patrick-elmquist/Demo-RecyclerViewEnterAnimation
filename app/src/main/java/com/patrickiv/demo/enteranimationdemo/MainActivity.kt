package com.patrickiv.demo.enteranimationdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
        Log.d("PKET", "OnClick fragment: ${(view as? TextView)?.text}")
        when (view.id) {
            R.id.listSample -> ListDemoFragment.instance(withHeader = false)
            R.id.headerSample -> ListDemoFragment.instance(withHeader = true)
            else -> GridDemoFragment()
        }.show()
    }

    private fun Fragment.show() = supportFragmentManager.beginTransaction()
        .replace(R.id.container, this)
        .addToBackStack(null)
        .commit()
        .also { Log.d("PKET", "Show fragment: ${javaClass.simpleName}") }
}