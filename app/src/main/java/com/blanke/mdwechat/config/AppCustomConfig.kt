package com.blanke.mdwechat.config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.bean.FloatButtonConfig
import com.blanke.mdwechat.bean.PicPositionConfig
import com.blanke.mdwechat.util.BitmapUtil
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Created by blanke on 2017/10/13.
 */

object AppCustomConfig {
    var bitmapScale = 1F

    fun getWxVersionConfig(version: String): WxVersionConfig {
        val configName = version + ".config"
//        if (HookConfig.is_play) {
//            configName = version + "-play.config"
//        }
        val `is` = FileInputStream(getWxConfigFile(configName))
        return Gson().fromJson(InputStreamReader(`is`), WxVersionConfig::class.java)
    }

    fun getWxConfigFile(fileName: String): String {
        return Common.APP_DIR_PATH + Common.CONFIG_WECHAT_DIR + File.separator + fileName
    }

    fun getConfigFile(fileName: String): String {
        return Common.APP_DIR_PATH + Common.CONFIG_DIR + File.separator + fileName
    }

    fun getViewConfigFile(fileName: String): String {
        return Common.APP_DIR_PATH + Common.CONFIG_VIEW_DIR + File.separator + fileName
    }

    fun getLogFile(date: String): String {
        return Common.APP_DIR_PATH + Common.LOGS_DIR + File.separator + "MDWechat_log_$date.txt"
    }

    fun getTabIcon(index: Int): Bitmap? {
        return getScaleBitmap(getIcon(Common.FILE_NAME_TAB_PREFIX + "$index.png"))
    }

    fun getBubbleLeftIcon(): Bitmap? {
        return getIcon(Common.CHAT_BUBBLE_LEFT_FILENAME)
    }

    fun getBubbleRightIcon(): Bitmap? {
        return getIcon(Common.CHAT_BUBBLE_RIGHT_FILENAME)
    }

    //自动适配屏幕分辨率
    fun getTabBg(index: Int): Bitmap {
        val bg = getIcon(Common.FILE_NAME_TAB_BG_PREFIX + "$index.png")
        return resizeBg(bg)
    }

    //自动适配屏幕分辨率
    fun getChatBg(): Bitmap {
        var bg = getIcon(Common.FILE_NAME_CHAT_BG)
        if (bg == null) {
            bg = getTabBg(0)
        }
        return resizeBg(bg)
    }

    fun resizeBg(_bg: Bitmap?): Bitmap {
        var bg = _bg
        if (bg == null) bg = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        val value_resolution = HookConfig.value_resolution
        if (value_resolution[0] > 0) {
            return BitmapUtil.scaleImage(bg!!, value_resolution[0], value_resolution[1])
        } else {
            return bg!!
        }
    }

    fun getFloatButtonConfig(): FloatButtonConfig? {
        val path = getViewConfigFile(Common.FILE_NAME_FLOAT_BUTTON)
        try {
            val `is` = FileInputStream(path)
            return Gson().fromJson(InputStreamReader(`is`), FloatButtonConfig::class.java)
        } catch (e: Exception) {
            return null
        }
    }

    fun getPicPositionConfig(): PicPositionConfig? {
        val path = getViewConfigFile(Common.FILE_NAME_PIC_POSITION)
        try {
            val `is` = FileInputStream(path)
            return Gson().fromJson(InputStreamReader(`is`), PicPositionConfig::class.java)
        } catch (e: Exception) {
            return null
        }
    }

    fun getIconPath(fileName: String): String {
        return Common.APP_DIR_PATH + Common.ICON_DIR + File.separator + fileName
    }

    fun getIcon(fileName: String): Bitmap? {
        val filePath = getIconPath(fileName)
        return BitmapFactory.decodeFile(filePath)
    }

    fun getScaleBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        return Bitmap.createScaledBitmap(bitmap, (bitmap.width * bitmapScale).toInt(), (bitmap.height * bitmapScale).toInt(), true)
    }
}
