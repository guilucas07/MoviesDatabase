package com.guilhermelucas.moviedatabase.viewer

import android.os.Bundle
import android.view.View
import com.guilhermelucas.moviedatabase.R
import com.guilhermelucas.moviedatabase.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.backdrop_viewer_activity.*

class ImageViewer : BaseActivity() {

    private lateinit var imageUrl: String

    object ExtraParam {
        const val ITEM_URL = "viewer_activity_param_image_url"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backdrop_viewer_activity)
        imageUrl = intent.extras.getString(ExtraParam.ITEM_URL, "")
        if (imageUrl.isEmpty())
            throw IllegalArgumentException("${ExtraParam.ITEM_URL} is necessary to this activity and " +
                    "doesn't was informed. Check startActivity and try again'")

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        loadImage()
        setupUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        if (isImmersiveModeEnabled())
            leaveImmersiveMode()
    }

    private fun loadImage() {
        Glide.with(baseContext)
                .load(imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imageMovieBackdrop)
    }

    private fun setupUI() {
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            onImmersiveModeChanged(visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0)
        }

        imageMovieBackdrop.setOnClickListener {
            setImmersiveMode()
        }
    }

    private fun setImmersiveMode() {
        if (isImmersiveModeEnabled())
            leaveImmersiveMode()
        else
            enterImmersiveMode()
    }

    private fun enterImmersiveMode() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun leaveImmersiveMode() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun isImmersiveModeEnabled(): Boolean {
        val uiOptions = window.decorView.systemUiVisibility
        return uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
    }

    private fun onImmersiveModeChanged(visible: Boolean) {
        if (visible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }


}