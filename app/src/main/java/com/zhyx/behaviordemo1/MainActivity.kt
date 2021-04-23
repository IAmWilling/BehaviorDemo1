package com.zhyx.behaviordemo1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zhyx.behaviordemo1.fragment.Parent2Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragments =
        arrayListOf<Fragment>(Parent2Fragment(), Parent2Fragment(), Parent2Fragment())
    private lateinit var adapter: ViewPagerAdapter1
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtil.setStatusBarTransparent(this, false, false)
//        ThemeUtil.setStatusBarTransparent2(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_title.text = "许嵩"
        tv_show_title.text = "许嵩"
        adapter = ViewPagerAdapter1(fragments, supportFragmentManager)
        tv_viewpager.adapter = adapter
        Thread(Runnable {
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.xusong)
            val scaleBitmap =
                Bitmap.createScaledBitmap(bitmap, bitmap.width / 20, bitmap.height / 20, false)
            val blurBitmap = FastBlurUtils.doBlur(scaleBitmap, 8, true)
            runOnUiThread {
                people_cover_blur.setImageBitmap(blurBitmap)
            }
        }).start()

    }
}