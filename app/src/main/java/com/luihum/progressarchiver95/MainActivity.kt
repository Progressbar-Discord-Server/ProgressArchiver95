package com.luihum.progressarchiver95

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.luihum.progressarchiver95.databinding.ActivityMainBinding
import java.io.File
import java.io.FileReader

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
        val archiveDir = File(filesDir, "archive")
        val statusLabel = binding.statusText
        val versionLabel = binding.versionLabel
        val archiveButton = binding.archiveButton
        val apkInfo: ApplicationInfo
        val apkInfoB: PackageInfo
        val apkPath: String
        val apkPathHandle: File
        val apkVer: String
        val copyBase: Boolean = binding.copybase.isChecked
        val copyDpis: Boolean = binding.copydpis.isChecked
        archiveDir.mkdir()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        try {
            apkInfo = pm.getApplicationInfo("com.spookyhousestudios.progressbar95", 0)
            apkInfoB = pm.getPackageInfo("com.spookyhousestudios.progressbar95", 0)
            apkPath = apkInfo.publicSourceDir.removeSuffix("base.apk")
            apkVer = apkInfoB.versionName
            apkPathHandle = File(apkPath,"")
            statusLabel.text = getString(R.string.files_found_at)
            statusLabel.setOnClickListener { view ->
                Snackbar.make(view, apkPath, Snackbar.LENGTH_LONG).setAction("", null).show()
            }
            versionLabel.text = getString(R.string.version_label) + apkVer

            val apks: Sequence<File> = apkPathHandle.walk().maxDepth(1).filter { f: File ->
                f.absolutePath.endsWith(".apk")
            }
            archiveButton.setOnClickListener {
                val chosenApks: Sequence<File> = apks
                    .filter { f: File -> !(f.name == "base.apk" && !copyBase) }
                    .filter { f: File -> !(f.name.endsWith("dpi.apk") && !copyDpis) }
                Toast.makeText(context, "Started to archive $apkVer", Toast.LENGTH_SHORT).show()
                chosenApks.forEach { a ->
                    Log.d("ProgressArchiver95", "Found APK: " + a.absolutePath.toString())
                    val archiveVerDir = File(archiveDir, apkVer)
                    archiveVerDir.mkdir()
                    a.copyTo(archiveVerDir, true)
                    Log.d("ProgressArchiver95", "Archived ${a.name} successfully")
                }
                Toast.makeText(context, "Archived $apkVer successfully", Toast.LENGTH_SHORT).show()
            }

        } catch (e: PackageManager.NameNotFoundException) {
            statusLabel.text = getString(R.string.pb95_not_found)
            archiveButton.isVisible = false
        }
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
            R.id.action_old_archive -> oldArchive(context)
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun oldArchive(context: Context): Boolean {
        Toast.makeText(context,getString(R.string.old_archive_mode_started),Toast.LENGTH_SHORT).show()
        return true
    }

}