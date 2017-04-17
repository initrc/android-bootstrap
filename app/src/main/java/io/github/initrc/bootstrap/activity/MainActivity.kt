package io.github.initrc.bootstrap.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.initrc.bootstrap.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
