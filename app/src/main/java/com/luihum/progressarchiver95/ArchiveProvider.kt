package com.luihum.progressarchiver95

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document.FLAG_SUPPORTS_DELETE
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ArchiveProvider : DocumentsProvider() {
    private val baseDir = Constants().ARCHIVE_BASE_DIR
    private val DEFAULT_ROOT_PROJECTION: Array<String> = arrayOf(
        Root.COLUMN_ROOT_ID,
        Root.COLUMN_MIME_TYPES,
        Root.COLUMN_FLAGS,
        Root.COLUMN_ICON,
        Root.COLUMN_TITLE,
        Root.COLUMN_SUMMARY,
        Root.COLUMN_DOCUMENT_ID,
        Root.COLUMN_AVAILABLE_BYTES
    )

    private val DEFAULT_DOCUMENT_PROJECTION: Array<String> = arrayOf(
        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
        DocumentsContract.Document.COLUMN_MIME_TYPE,
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        DocumentsContract.Document.COLUMN_FLAGS,
        DocumentsContract.Document.COLUMN_SIZE
    )


    override fun onCreate(): Boolean {
        Log.d("ProgressArchiver95", "ArchiveProvider path:" + baseDir.absolutePath)
        //TODO("Not yet implemented")
        return true
    }

    override fun queryRoots(projection: Array<out String>?): Cursor {
        // Use a MatrixCursor to build a cursor
        // with either the requested fields, or the default
        // projection if "projection" is null.
        val result = MatrixCursor(resolveRootProjection(projection))


        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        result.newRow().apply {
            add(Root.COLUMN_ROOT_ID, "progressarchiver95")
            add(Root.COLUMN_FLAGS, Root.FLAG_SUPPORTS_SEARCH)
            add(Root.COLUMN_TITLE, "ProgressArchiver95")
            add(Root.COLUMN_DOCUMENT_ID, getDocIdForFile(baseDir))
            add(Root.COLUMN_AVAILABLE_BYTES, baseDir.freeSpace)
            add(Root.COLUMN_ICON, R.drawable.ic_launcher_foreground)
        }
        Log.d("ProgressArchiver95", "ArchiveProvider cursor:$result")


        return result

    }

    private fun resolveRootProjection(projection: Array<out String>?): Array<out String>? {
        return projection ?: DEFAULT_ROOT_PROJECTION
    }


    override fun queryDocument(p0: String?, p1: Array<out String>?): Cursor? {
        //TODO("Not yet implemented")
        return null
    }

    override fun queryChildDocuments(
        parentDocumentId: String,
        projection: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return MatrixCursor(projection).apply {
            val parent: File = getFileForDocId(parentDocumentId)
            parent.listFiles()
                .forEach { file ->
                    includeFile(this, null, file)
                }
        }
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
            flags = flags or FLAG_SUPPORTS_DELETE
            val displayName = file.name
            val mimeType = getMimeType(file)
            if (mimeType.startsWith("image/")) flags =
                flags or DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL
            val row = result.newRow()
            row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, docId)
            row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, displayName)
            row.add(DocumentsContract.Document.COLUMN_SIZE, file.length())
            row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType)
            row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified())
            row.add(DocumentsContract.Document.COLUMN_FLAGS, flags)
            row.add(DocumentsContract.Document.COLUMN_ICON, R.drawable.ic_launcher)
        }

    }
}