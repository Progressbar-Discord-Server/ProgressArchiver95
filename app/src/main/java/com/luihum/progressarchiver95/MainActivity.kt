package com.luihum.progressarchiver95

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.luihum.progressarchiver95.databinding.ActivityMainBinding
import java.io.File
import com.luihum.progressarchiver95.Util

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
        val preferences :SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var archiveLocation = if ((preferences.getBoolean("legacy_storage", false))) {
        getExternalFilesDir("")} else { filesDir }
        val archiveDir = File(archiveLocation, "archive")
        val statusLabel = binding.statusText
        val versionLabel = binding.versionLabel
        val archiveButton = binding.archiveButton
        val abiList = binding.foundAbiList
        val dpiList = binding.foundDpiList
        val langList = binding.foundLangList
        val apkInfo: ApplicationInfo
        val apkInfoB: PackageInfo
        val apkPath: String
        val apkPathHandle: File
        val apkVer: String
        var copyBase = binding.copybase.isChecked
        var copyDpis = binding.copydpis.isChecked
        var copyLang = binding.copylang.isChecked
        var makeXapk = binding.generateXapk.isChecked
        archiveDir.mkdir()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.copydpis.setOnClickListener { copyDpis = binding.copydpis.isChecked }
        binding.copybase.setOnClickListener { copyBase = binding.copybase.isChecked }
        binding.copylang.setOnClickListener { copyLang = binding.copylang.isChecked }
        binding.generateXapk.setOnClickListener { makeXapk = binding.generateXapk.isChecked }
        try {
            apkInfo = pm.getApplicationInfo("com.spookyhousestudios.progressbar95", 0)
            apkInfoB = pm.getPackageInfo("com.spookyhousestudios.progressbar95", 0)
            apkPath = apkInfo.publicSourceDir.removeSuffix("base.apk")
            apkVer = apkInfoB.versionName
            val apkBuild = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                apkInfoB.longVersionCode
            } else {
                apkInfoB.versionCode
            }
            apkPathHandle = File(apkPath,"")
            statusLabel.text = getString(R.string.files_found_at)
            statusLabel.setOnClickListener { view ->
                Snackbar.make(view, apkPath, Snackbar.LENGTH_LONG).setAction("", null).show()
            }
            versionLabel.text = getString(R.string.version_label) + apkVer + "\nBuild $apkBuild"

            val apks: Sequence<File> = apkPathHandle.walk().maxDepth(1).filter { f: File ->
                f.absolutePath.endsWith(".apk")
            }
            apks.forEach { a ->
                val apkName = a.nameWithoutExtension.removePrefix("split_config.")
                if (apkName == "armeabi_v7a" || apkName == "arm64_v8a" || apkName == "x86_64" || apkName == "x86" ) {
                    Log.d("ProgressArchiver95", "ABI found: $apkName")
                    val abiFound: String = (when (apkName) {
                        "armeabi_v7a" -> getString(R.string.armeabi_v7a)
                        "arm64_v8a" -> getString(R.string.arm64_v8a)
                        "x86" -> getString(R.string.x86)
                        "x86_64" -> getString(R.string.x86_64)
                        else -> getString(R.string.unknown)
                    })
                    abiList.text = abiList.text.toString() + abiFound
                }
                if (apkName.endsWith("dpi")) {
                    Log.d("ProgressArchiver95", "DPI found: $apkName")
                    val dpiFound: String = (when (apkName) {
                        "nodpi" -> getString(R.string.nodpi)
                        "tvdpi" -> getString(R.string.tvdpi)
                        "ldpi" -> getString(R.string.ldpi)
                        "mdpi" -> getString(R.string.mdpi)
                        "hdpi" -> getString(R.string.hdpi)
                        "xhdpi" -> getString(R.string.xhdpi)
                        "xxhdpi" -> getString(R.string.xxhdpi)
                        "xxxhdpi" -> getString(R.string.xxxhdpi)
                        else -> getString(R.string.unknown)
                    })
                    dpiList.text = dpiList.text.toString() + dpiFound
                }
                if (!
                    apkName.endsWith("armeabi_v7a") &&!
                    apkName.endsWith("arm64_v8a")   &&!
                    apkName.endsWith("x86")         &&!
                    apkName.endsWith("x86_64")      &&!
                    apkName.endsWith("dpi")         &&
                    apkName != "base"
                ) langList.text = langList.text.toString() + apkName + "\n"

            }

            archiveButton.setOnClickListener {
                //BUG: This doesn't show for some reason
                Toast.makeText(context, "Started to archive $apkVer", Toast.LENGTH_SHORT).show()
                val chosenApks = parseApks(apks, true, copyBase, copyDpis, copyLang)
                if (!makeXapk) {
                    val archiveVerDir = File(archiveDir, apkVer)
                    archive(chosenApks, archiveVerDir)
                   Toast.makeText(context, "Archived $apkVer successfully", Toast.LENGTH_SHORT)
                       .show()
                } else {
                    val archiveVerPath = archiveDir.absolutePath + "/" + apkVer + ".xapk"
                    //var size = 0
                //  chosenApks.forEach { size++ }
                    var chosenApksArray = chosenApks.map {
                        it.absolutePath
                    }.toList().toTypedArray()
                    Util().zipFile(chosenApksArray,archiveVerPath)
                }
            }

        } catch (e: PackageManager.NameNotFoundException) {
            statusLabel.text = getString(R.string.pb95_not_found)
            archiveButton.isVisible = false
            binding.copydpis.isVisible = false
            binding.copybase.isVisible = false
            abiList.isVisible = false
            dpiList.isVisible = false
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
            R.id.action_about -> about(context)
            R.id.action_settings -> settings(context)
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun oldArchive(context: Context): Boolean {
        Toast.makeText(context,getString(R.string.old_archive_mode_started),Toast.LENGTH_SHORT).show()
        val archiveDir = File(getExternalFilesDir(null), "archive")
        val apkInfo: ApplicationInfo = pm.getApplicationInfo("com.spookyhousestudios.progressbar95", 0)
        val apkInfoB: PackageInfo = pm.getPackageInfo("com.spookyhousestudios.progressbar95", 0)
        val apkPath: String = apkInfo.publicSourceDir.removeSuffix("base.apk")
        val apkVer: String = apkInfoB.versionName
        val apkPathHandle = File(apkPath,"")
        val base = apkPathHandle.walk().maxDepth(1).filter { f: File -> f.name == "base.apk"}.first()
        val archiveVerDir = File(archiveDir, apkVer)
        archiveVerDir.mkdir()
        val target = File(archiveVerDir, base.name)
        target.createNewFile()
        base.copyTo(target, true)
        Log.d("ProgressArchiver95", "Archived ${base.name} successfully")
        Toast.makeText(context, "Archived $apkVer successfully", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun about(context: Context): Boolean {
        AlertDialog.Builder(context)
            .setTitle(R.string.about)
            .setMessage(R.string.about_content)
            .setNeutralButton(R.string.ok, null)
            .show()
        return true
    }

    private fun settings(context: Context): Boolean {
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun archive(chosenApks: Sequence<File>, archiveVerDir: File) {
        chosenApks.forEach { a ->
            Log.d("ProgressArchiver95", "Found APK: " + a.absolutePath.toString())
            archiveVerDir.mkdir()
            val target = File(archiveVerDir, a.name)
            target.createNewFile()

            a.copyTo(target, true)
            Log.d("ProgressArchiver95", "Archived ${a.name} successfully in $archiveVerDir")
        }
    }

    private fun parseApks(
        apks: Sequence<File>,
        copyAbis: Boolean,
        copyBase: Boolean,
        copyDpis: Boolean,
        copyLang: Boolean,
    ): Sequence<File> {
        val abis = apks.filter { f: File ->
            f.nameWithoutExtension.endsWith("armeabi_v7a") ||
                    f.nameWithoutExtension.endsWith("arm64_v8a") ||
                    f.nameWithoutExtension.endsWith("x86") ||
                    f.nameWithoutExtension.endsWith("x86_64")
        }
        val dpis = apks.filter { f: File -> f.nameWithoutExtension.endsWith("dpi") }
        val base = apks.filter { f: File -> f.name.equals("base.apk") }
        val langs = apks.filter { f: File ->
            !f.nameWithoutExtension.endsWith("armeabi_v7a") && !
            f.nameWithoutExtension.endsWith("arm64_v8a") && !
            f.nameWithoutExtension.endsWith("x86") && !
            f.nameWithoutExtension.endsWith("x86_64") && !
            f.nameWithoutExtension.endsWith("dpi") &&
            f.nameWithoutExtension != "base"
        }
        Log.d("ProgressArchiver95", "ABIs: $abis")
        Log.d("ProgressArchiver95", "DPIs: $dpis")
        Log.d("ProgressArchiver95", "Base: $base")
        Log.d("ProgressArchiver95", "Lang: $langs")
        var chosenApks: Sequence<File> = emptySequence()
        if (copyAbis) chosenApks += abis
        if (copyDpis) chosenApks += dpis
        if (copyBase) chosenApks += base
        if (copyLang) chosenApks += langs
        return chosenApks
    }
}