package com.luihum.progressarchiver95

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Util {
    fun zipFile(files: Array<String>, out: String) {
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(out)))
        for (file in files) {
            Log.d("ProgressArchiver95", "Included file in XAPK: $file")
            val fi = FileInputStream(file)
            val origin = BufferedInputStream(fi)
            val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
            out.putNextEntry(entry)
            origin.copyTo(out, 1024)
            origin.close()
        }
        out.close()
    }
    fun moveDataToExternalStorage(newValue: Any, context: Context) {
        if (newValue == true) {
            File(context!!.filesDir,"").walk().first().copyRecursively(context.getExternalFilesDir("")!!,true)
            File(context.filesDir,"").walk().first().deleteRecursively()
        } else {
            File(context!!.getExternalFilesDir(""),"").walk().first().copyRecursively(context.filesDir!!,true)
            File(context.getExternalFilesDir(""),"").walk().first().deleteRecursively()                }
        Toast.makeText(context, "Restart the app", Toast.LENGTH_LONG).show()
    }

}