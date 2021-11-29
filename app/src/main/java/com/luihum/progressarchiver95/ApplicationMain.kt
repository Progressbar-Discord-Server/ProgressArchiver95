package com.luihum.progressarchiver95

import android.app.Application
import android.content.Context

class ApplicationMain: Application() {
    val appContext: Context = this.applicationContext
    val archiveBaseDir = appContext.getDir(appContext.filesDir.absolutePath + "archive", MODE_PRIVATE)
    }
