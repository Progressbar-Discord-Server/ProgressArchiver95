package com.luihum.progressarchiver95

import android.database.Cursor
import android.database.MatrixCursor
import android.database.MatrixCursor.RowBuilder
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import java.io.File
import java.io.FileNotFoundException

class ArchiveProvider : DocumentsProvider() {

    val baseDir = context?.getDir(context?.filesDir?.absolutePath+"archive",)
    val defaultRootProjection: Array<String> = Array(8){
        Root.COLUMN_ROOT_ID
        Root.COLUMN_MIME_TYPES
        Root.COLUMN_FLAGS
        Root.COLUMN_ICON
        Root.COLUMN_TITLE
        Root.COLUMN_SUMMARY
        Root.COLUMN_DOCUMENT_ID
        Root.COLUMN_AVAILABLE_BYTES
    }
    val defaultDocumentProjection: Array<String> = Array(6)
    {
            DocumentsContract.Document.COLUMN_DOCUMENT_ID
            DocumentsContract.Document.COLUMN_MIME_TYPE
            DocumentsContract.Document.COLUMN_DISPLAY_NAME
            DocumentsContract.Document.COLUMN_LAST_MODIFIED
            DocumentsContract.Document.COLUMN_FLAGS
            DocumentsContract.Document.COLUMN_SIZE
        }

    override fun onCreate(): Boolean {
        //TODO("Not yet implemented")
        return true
    }

    override fun queryRoots(p0: Array<out String>?): Cursor {
        val result = MatrixCursor(p0 ?: defaultRootProjection)
        val applicationName = "ProgressArchiver95"
        val row = result.newRow()
        row.add(Root.COLUMN_ROOT_ID)
        row.add(Root.COLUMN_TITLE, applicationName)
        row.add(Root.COLUMN_MIME_TYPES, "*/*")
        row.add(Root.COLUMN_AVAILABLE_BYTES, baseDir?.freeSpace)
        row.add(Root.COLUMN_ICON, R.mipmap.ic_launcher)
        return result
    }

    override fun queryDocument(p0: String?, p1: Array<out String>?): Cursor? {
        //TODO("Not yet implemented")
        return null
    }

    override fun queryChildDocuments(p0: String?, p1: Array<out String>?, p2: String?): Cursor? {
        //TODO("Not yet implemented")
        return null
    }

    override fun openDocument(
        p0: String?,
        p1: String?,
        p2: CancellationSignal?,
    ): ParcelFileDescriptor? {
        //TODO("Not yet implemented")
        return null

    }
    private fun getDocIdForFile(file: File): String? {
        return file.absolutePath
    }
    @Throws(FileNotFoundException::class)
    private fun getFileForDocId(docId: String): File? {
        val f = File(docId)
        if (!f.exists()) throw FileNotFoundException(f.absolutePath + " not found")
        return f
    }

}