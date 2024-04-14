package com.example.bookmanagementapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView
import java.util.*

class CustomScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {
    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var switchFlashlightButton: Button
    private lateinit var viewfinderView: ViewfinderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_scanner)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner) ?: throw RuntimeException("No view with ID zxing_barcode_scanner found")
        barcodeScannerView.setTorchListener(this)

        viewfinderView = findViewById(R.id.zxing_viewfinder_view) ?: throw RuntimeException("No view with ID zxing_viewfinder_view found")

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            switchFlashlightButton.visibility = View.GONE
        }

        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()

        changeMaskColor()
        changeLaserVisibility(true)
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    fun changeMaskColor() {
        val rnd = Random()
        val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        viewfinderView.setMaskColor(color)
    }

    fun changeLaserVisibility(visible: Boolean) {
        viewfinderView.setLaserVisibility(visible)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("SetTextI18n")
    override fun onTorchOn() {
        switchFlashlightButton.setText("TURN OFF FLASH")
    }

    @SuppressLint("SetTextI18n")
    override fun onTorchOff() {
        switchFlashlightButton.setText("TURN ON FLASH")
    }
}