package com.luihum.progressarchiver95

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class ArchiveProvider : android.content.ContentProvider() {
    override fun onCreate(): Boolean {
        android.util.Log.d("ProgressArchiver95","ArchiveProvider has started")
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        //TODO("Not yet implemented")
        return null
    }

    override fun getType(p0: Uri): String? {
        //TODO("Not yet implemented")
        return null
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        //TODO("Not yet implemented")
        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        //TODO("Not yet implemented")
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        //TODO("Not yet implemented")
        return 0
    }


}