package com.example.androidstudy

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class MyContentProvider : ContentProvider() {

    companion object {
        private const val TAG = "MyContentProvider"
        private const val AUTHORITY = "com.example.androidstudy.provider"
        private const val PATH_USERS = "users"
        private const val PATH_USER_ID = "users/#"
        
        private const val USERS = 1
        private const val USER_ID = 2
        
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_USERS")
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_USERS, USERS)
            addURI(AUTHORITY, PATH_USER_ID, USER_ID)
        }

        private val sampleUsers = listOf(
            mapOf("id" to 1, "name" to "김철수", "email" to "kim@example.com"),
            mapOf("id" to 2, "name" to "이영희", "email" to "lee@example.com"),
            mapOf("id" to 3, "name" to "박민수", "email" to "park@example.com")
        )
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "ContentProvider 생성됨")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "쿼리 요청: $uri")
        
        val cursor = MatrixCursor(arrayOf("id", "name", "email"))
        
        when (uriMatcher.match(uri)) {
            USERS -> {
                sampleUsers.forEach { user ->
                    cursor.addRow(arrayOf(user["id"], user["name"], user["email"]))
                }
            }
            USER_ID -> {
                val id = uri.lastPathSegment?.toIntOrNull()
                val user = sampleUsers.find { it["id"] == id }
                user?.let {
                    cursor.addRow(arrayOf(it["id"], it["name"], it["email"]))
                }
            }
        }
        
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            USERS -> "vnd.android.cursor.dir/vnd.example.users"
            USER_ID -> "vnd.android.cursor.item/vnd.example.users"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "삽입 요청: $uri")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d(TAG, "삭제 요청: $uri")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.d(TAG, "업데이트 요청: $uri")
        return 0
    }
} 