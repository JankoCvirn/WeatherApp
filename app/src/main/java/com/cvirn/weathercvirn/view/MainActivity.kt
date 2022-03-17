package com.cvirn.weathercvirn.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.cvirn.weathercvirn.BuildConfig
import com.cvirn.weathercvirn.R
import com.cvirn.weathercvirn.databinding.ActivityMainBinding
import com.cvirn.weathercvirn.utils.allPermissionsGranted
import com.cvirn.weathercvirn.utils.requestPermissions
import com.cvirn.weathercvirn.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.forecastObservable.observe(this) {
            val test = it.getContentIfNotHandled()
        }
    }

    override fun onResume() {
        super.onResume()
        permissionsCheck()
    }

    private fun permissionsCheck() {
        if (this.allPermissionsGranted()) {
            viewModel.getLastLocation(units = "metric")
        } else {
            requestForegroundPermissions()
        }
    }

    private fun requestForegroundPermissions() {
        val provideRationale = this.allPermissionsGranted()
        if (provideRationale) {
            Snackbar.make(
                binding.root,
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    this.requestPermissions(PERMISSIONS_REQUEST_CODE)
                }
                .show()
        } else {
            this.requestPermissions(PERMISSIONS_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    Timber.d("User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    Timber.d("Permission granted")

                else -> {

                    Snackbar.make(
                        binding.root,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 34
    }
}
