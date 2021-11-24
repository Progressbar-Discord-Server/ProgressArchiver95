package com.luihum.progressarchiver95

import android.database.Cursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider

class ArchiveProvider : DocumentsProvider() {

    private val defaultRootProjection: Array<String> = Array(5){
        Root.COLUMN_ROOT_ID
        Root.COLUMN_ICON
        Root.COLUMN_TITLE
        Root.COLUMN_FLAGS
        Root.COLUMN_DOCUMENT_ID}


    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun queryRoots(p0: Array<out String>?): Cursor {
        TODO("Not yet implemented")
    }

    override fun queryDocument(p0: String?, p1: Array<out String>?): Cursor {
        TODO("Not yet implemented")
    }

    override fun queryChildDocuments(p0: String?, p1: Array<out String>?, p2: String?): Cursor {
        TODO("Not yet implemented")
    }

    override fun openDocument(
        p0: String?,
        p1: String?,
        p2: CancellationSignal?,
    ): ParcelFileDescriptor {
        TODO("Not yet implemented")
    }
}