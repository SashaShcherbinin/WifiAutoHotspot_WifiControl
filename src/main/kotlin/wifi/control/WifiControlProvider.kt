package wifi.control

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build

val CONTENT_URI: Uri = Uri.parse("content://com.wifi.auto.hotspot.wifi.control.provider")

const val COLUM_VERSION_CODE = "version_code"
const val COLUM_VERSION_NAME = "version_name"
const val PATH_VERSION = "version"

class WifiControlProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
    ): Cursor {
        if (uri == CONTENT_URI.buildUpon().appendPath(PATH_VERSION).build()) {
            val versionCode = getAppVersionCode(context!!)
            val versionName = getAppVersionName(context!!)
            val cursor = MatrixCursor(arrayOf("version_code", "version_name"))
            cursor.addRow(arrayOf<Any>(versionCode, versionName))
            return cursor
        }
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        val wifiManager = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val wifiManager = context?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
        return 1
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun getAppVersionCode(context: Context): Long {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    private fun getAppVersionName(context: Context): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    }
}