package com.luihum.progressarchiver95

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import java.io.File
import java.io.FileNotFoundException
import android.webkit.MimeTypeMap
import java.util.*

class ArchiveProvider : DocumentsProvider() {

    private val baseDir = context?.getDir(context?.filesDir?.absolutePath + "archive", 0)
    private val defaultRootProjection: Array<String> = Array(8){
        Root.COLUMN_ROOT_ID
        Root.COLUMN_MIME_TYPES
        Root.COLUMN_FLAGS
        Root.COLUMN_ICON
        Root.COLUMN_TITLE
        Root.COLUMN_SUMMARY
        Root.COLUMN_DOCUMENT_ID
        Root.COLUMN_AVAILABLE_BYTES
    }
    private val defaultDocumentProjection: Array<String> = Array(6)
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
        row.add(Root.COLUMN_ROOT_ID, "pb95archive")
        row.add(Root.COLUMN_DOCUMENT_ID, "pb95archive")
        row.add(Root.COLUMN_TITLE, "ProgressArchiver95")
        row.add(Root.COLUMN_MIME_TYPES, "*/*")
        row.add(Root.COLUMN_AVAILABLE_BYTES, baseDir?.freeSpace)
        row.add(Root.COLUMN_ICON, R.mipmap.ic_launcher)
        return result
    }

    override fun queryDocument(p0: String?, p1: Array<out String>?): Cursor? {
        //TODO("Not yet implemented")
        return null
    }

    @Throws(FileNotFoundException::class)
    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<String?>?,
        sortOrder: String?,
    ): Cursor {
        val result = MatrixCursor(projection ?: defaultDocumentProjection)
        val parent = getFileForDocId(parentDocumentId!!)
        for (file in parent?.listFiles()) {
            includeFile(result, null, file)
        }
        return result
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
    override fun getDocumentType(documentId: String?): String {
        val file = getFileForDocId(documentId!!)
        return getMimeType(file)
    }
    private fun getMimeType(file: File): String {
        return if (file.isDirectory) {
            DocumentsContract.Document.MIME_TYPE_DIR
        } else {
            val name = file.name
            val lastDot = name.lastIndexOf('.')
            if (lastDot >= 0) {
                val extension = name.substring(lastDot + 1).lowercase(Locale.getDefault())
                val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                if (mime != null) return mime
            }
            "application/octet-stream"
        }
    }
    @Throws(FileNotFoundException::class)
    private fun getFileForDocId(docId: String): File {
        val f = File(docId)
        if (!f.exists()) throw FileNotFoundException(f.absolutePath + " not found")
        return f
    }
    @Throws(FileNotFoundException::class)
    private fun includeFile(result: MatrixCursor, docId: String?, file: File?) {
        var docId = docId
        var file = file
        if (docId == null) {
            docId = getDocIdForFile(file!!)
        } else {
            file = getFileForDocId(docId)
        }
        var flags = 0
        if (file.isDirectory) {
            if (file.canWrite()) flags = flags or DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE
        } else if (file.canWrite()) {
            flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_WRITE
        }
        if (file.parentFile.canWrite()) flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_DELETE
        val displayName = file.name
        val mimeType = getMimeType(file)
        if (mimeType.startsWith("image/")) flags = flags or DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL
        val row = result.newRow()
        row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, docId)
        row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, displayName)
        row.add(DocumentsContract.Document.COLUMN_SIZE, file.length())
        row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType)
        row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
        row.add(DocumentsContract.Document.COLUMN_FLAGS, flags)
        row.add(DocumentsContract.Document.COLUMN_ICON, R.mipmap.ic_launcher)
    }

}