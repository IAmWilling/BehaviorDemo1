package com.zhyx.behaviordemo1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhyx.behaviordemo1.meituan.MeiTuanActivity
import com.zhyx.behaviordemo1.nestedscrollingdemo.NestedScrolling2Activity
import kotlinx.android.synthetic.main.activity_select.*

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        nested_scrolling_activity.setOnClickListener {
            startActivity(Intent(this,
                NestedScrolling2Activity::class.java))
        }
        behavior_activity.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        meituan_activity.setOnClickListener {
            startActivity(Intent(this,MeiTuanActivity::class.java))
        }
    }
}