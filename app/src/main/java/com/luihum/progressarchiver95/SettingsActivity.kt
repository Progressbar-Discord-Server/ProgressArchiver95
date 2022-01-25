package com.luihum.progressarchiver95


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import java.io.File


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
            if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val context = activity
            findPreference<SwitchPreferenceCompat>("legacy_storage")?.setOnPreferenceChangeListener { _, newValue  ->
                Toast.makeText(context, "Please wait while the files are being moved to the new storage location. ,", Toast.LENGTH_LONG).show()
                Thread.sleep(1000)
                if (newValue == true) {
                    File(context!!.filesDir,"").walk().first().copyRecursively(context.getExternalFilesDir("")!!,true)
                    File(context.filesDir,"").walk().first().deleteRecursively()
                } else {
                    File(context!!.getExternalFilesDir(""),"").walk().first().copyRecursively(context.filesDir!!,true)
                    File(context.getExternalFilesDir(""),"").walk().first().deleteRecursively()                }
                Toast.makeText(context, "Restart the app", Toast.LENGTH_LONG).show()
                true
            }
            findPreference<SwitchPreferenceCompat>("dark_mode")?.setOnPreferenceChangeListener { _, newValue ->
                if (newValue == true) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
        }
    }
}


