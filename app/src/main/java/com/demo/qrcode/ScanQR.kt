package com.demo.qrcode

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.demo.qrcode.databinding.ActivityScanQrBinding
import com.google.android.material.snackbar.Snackbar


class ScanQR : AppCompatActivity() {
    private lateinit var scanBinding: ActivityScanQrBinding
    private var codeScanner: CodeScanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanBinding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(scanBinding.root)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            initiateScan()
        } else {
            // request for Camera permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                initiateScan()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initiateScan() {
        val scannerView = scanBinding.codeScannerView
        codeScanner = CodeScanner(this, scannerView)

        // The default values
        codeScanner?.camera = CodeScanner.CAMERA_BACK
        codeScanner?.formats = CodeScanner.ALL_FORMATS

        codeScanner?.apply {
            isAutoFocusEnabled = true
            isFlashEnabled = false
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
        }

        // Decode Callback (Results)
        codeScanner!!.decodeCallback = DecodeCallback {
            runOnUiThread {
                Snackbar.make(scannerView, "Scan result: ${it.text}", 5000).show()
            }
        }
        // Error CallBack
        codeScanner?.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner!!.startPreview()
        }
    }

    // when the app resumes
    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    // just before the app is paused
    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}
