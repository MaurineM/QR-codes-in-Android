package com.demo.qrcode

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.demo.qrcode.databinding.ActivityGenerateQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class GenerateQR : AppCompatActivity() {
    private var _genQRBinding: ActivityGenerateQrBinding? = null
    private val genQRBinding get() = _genQRBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _genQRBinding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(genQRBinding.root)

        genQRBinding.btnGenerateQRCode.setOnClickListener {
            val input = genQRBinding.inputText.text!!.toString().trim()
            genQRBinding.ivOutput.setImageBitmap(generateQRCode(input))
        }

        genQRBinding.btnScan.setOnClickListener {
            startActivity(Intent(this, ScanQR::class.java))
        }
    }

    private fun generateQRCode(inputText: String?): Bitmap? {
        val writer = MultiFormatWriter()
        var bitmap: Bitmap? = null

        if (!inputText.isNullOrEmpty()) {
            try {
                // init bit matrix
                val matrix = writer.encode(inputText, BarcodeFormat.QR_CODE, 350, 350)
                // init barcode encoder
                val encoder = BarcodeEncoder()
                // generate bitmap
                bitmap = encoder.createBitmap(matrix)
            } catch (e: WriterException) {
                // log error here
                Log.e("GENERATE QR CODE ACTIVITY", e.toString())
            }
        } else {
            genQRBinding.textInputLayout.error = "* required"
        }
        return bitmap
    }
}