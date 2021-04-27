package com.zhyx.behaviordemo1.nestedscrollingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zhyx.behaviordemo1.R
import com.zhyx.behaviordemo1.ViewPagerAdapter1
import com.zhyx.behaviordemo1.fragment.Parent2Fragment
import kotlinx.android.synthetic.main.activity_my_info.*

class NestedScrolling2Activity:AppCompatActivity() {
    private val fragments =
        arrayListOf<Fragment>(Parent2Fragment(), Parent2Fragment(), Parent2Fragment())
    private lateinit var adapter: ViewPagerAdapter1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        adapter = ViewPagerAdapter1(
            fragments,
            supportFragmentManager
        )
        view_pager.adapter = adapter
    }
}