package com.github.panpf.recycler.sticky.sample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.github.panpf.recycler.sticky.sample.databinding.ActivityMainBinding
import com.github.panpf.recycler.sticky.sample.ui.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.mainFragmentContainerView.id, MainFragment())
            .commit()
    }
}
