package com.dicoding.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.data.adapter.StoryAdapter
import com.dicoding.response.Story
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.ui.ViewModelFactory
import com.dicoding.ui.detail.DetailActivity
import com.dicoding.ui.welcome.WelcomeActivity
import com.dicoding.upload.UploadActivity
import com.dicoding.data.Result

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupAction(user.token)
            }
        }
        setupView()
        supportActionBar?.show()

        adapter = StoryAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.NAME, data.name)
                    it.putExtra(DetailActivity.DESC, data.description)
                    it.putExtra(DetailActivity.URL, data.photoUrl)
                    startActivity(it)
                }
            }
        })
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.btn_logout -> {
                viewModel.logout()
            }

            R.id.btn_add -> {
                startActivity(Intent(this@MainActivity, UploadActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    private fun setupAction(token: String) {
        viewModel.story(token).observe(this) { user ->
            if (user != null) {
                when (user) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val adapter = StoryAdapter()
                        binding.rvStory.layoutManager = LinearLayoutManager(this)
                        adapter.submitList(user.data.listStory)
                        binding.rvStory.adapter = adapter
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}