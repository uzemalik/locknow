package com.example.quicklock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var compName: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        // Try to lock immediately (single-tap via launcher)
        tryLockOrPrompt()

        // Also show a simple UI
        setContentView(R.layout.activity_main)
        val btnLock = findViewById<Button>(R.id.btn_lock)
        btnLock.setOnClickListener { tryLockOrPrompt() }
    }

    private fun tryLockOrPrompt() {
        if (devicePolicyManager.isAdminActive(compName)) {
            // lock now
            devicePolicyManager.lockNow()
            // optionally finish the activity so user returns to home/lock screen
            finish()
        } else {
            // request device admin activation
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getString(R.string.activate_admin_explanation))
            }
            startActivity(intent)
            Toast.makeText(this, "Please activate device admin to allow locking.", Toast.LENGTH_LONG).show()
        }
    }
}
