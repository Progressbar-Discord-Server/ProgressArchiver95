package com.luihum.progressarchiver95

import android.util.Log
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
    fun moveDataToExternalStorage() {

    }

}