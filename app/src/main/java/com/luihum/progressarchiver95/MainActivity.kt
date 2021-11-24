package com.luihum.progressarchiver95

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.luihum.progressarchiver95.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pm : PackageManager
    private lateinit var context : Context

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        context.packageManager.also { pm = it }
        binding = ActivityMainBinding.inflate(layoutInflater)
        val statusLabel = binding.statusText
        val versionLabel = binding.versionLabel
        setContentView(binding.root)
        val apkInfo: ApplicationInfo
        val apkInfoB: PackageInfo
        val apkPath: String
        val apkVer: String
        setSupportActionBar(binding.toolbar)
        try {
            apkInfo = pm.getApplicationInfo("com.spookyhousestudios.progressbar95", 0)
            apkInfoB = pm.getPackageInfo("com.spookyhousestudios.progressbar95", 0)
            apkPath = apkInfo.publicSourceDir.removeSuffix("base.apk")
            apkVer = apkInfoB.versionName
            statusLabel.text = getString(R.string.files_found_at)
            statusLabel.setOnClickListener { view ->
                Snackbar.make(view, apkPath, Snackbar.LENGTH_LONG).setAction("", null).show()
            }
            versionLabel.text = getString(R.string.version_label) + apkVer
        } catch (e: PackageManager.NameNotFoundException) {
            statusLabel.text = getString(R.string.pb95_not_found)
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasWritePerms: Int = context.checkSelfPermission("WRITE_EXTERNAL_STORAGE")
            if (hasWritePerms != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Please grant storage permission", Toast.LENGTH_LONG).show()
                requestPermissions(arrayOf("WRITE_EXTERNAL_STORAGE"),5125)
            }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}