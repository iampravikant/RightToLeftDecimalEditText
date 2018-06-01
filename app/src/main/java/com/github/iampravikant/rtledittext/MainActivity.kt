package com.github.iampravikant.rtledittext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.pravikant.rtledittext.RightToLeftDecimalEditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_weight.setNextFocusableView(main_width)
        main_width.setNextFocusableView(main_height)
        main_height.setListener(object : RightToLeftDecimalEditText.RightToLeftDecimalEditTextListener {
            override fun onKeyboardEnterClicked() {
                onSubmit()
            }
        })

        main_submit.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        Toast.makeText(this, "Weight: " + main_weight.getDecimalPoints(), Toast.LENGTH_SHORT).show()
    }
}
