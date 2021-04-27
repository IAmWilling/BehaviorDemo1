package com.zhyx.behaviordemo1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingParent3
import androidx.core.widget.NestedScrollView
import com.zhyx.behaviordemo1.R


class Parent2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        NestedScrollView(context!!).isNestedScrollingEnabled
        return inflater.inflate(R.layout.fragment_parent2, container, false)
    }



}