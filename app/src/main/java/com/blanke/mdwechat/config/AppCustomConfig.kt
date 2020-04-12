package com.blanke.mdwechat.config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.bean.FloatButtonConfig
import com.blanke.mdwechat.util.NightModeUtils
import com.blanke.mdwechat.util.waitInvoke
import com.blankj.utilcode.util.BarUtils
import com.google.gson.Gson
import de.robv.android.xposed.XposedHelpers
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Created by blanke on 2017/10/13.
 */

object AppCustomConfig {
    var bitmapScale = 1F

    fun getWxVersionConfig(version: String): WxVersionConfig {
        var configName = version + ".config"
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

    fun getTabBg(index: Int): Bitmap? {
        return getIcon(Common.FILE_NAME_TAB_BG_PREFIX + "$index.png")
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

    fun getIcon(fileName: String): Bitmap? {
        val filePath = Common.APP_DIR_PATH + Common.ICON_DIR + File.separator + fileName
        return BitmapFactory.decodeFile(filePath)
    }

    fun getScaleBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        return Bitmap.createScaledBitmap(bitmap, (bitmap.width * bitmapScale).toInt(), (bitmap.height * bitmapScale).toInt(), true)
    }


    var _statusBarBitmap: Bitmap? = null
    var _actionBarBitmap: Bitmap? = null
    var _tabLayoutBitmap: Bitmap? = null

    var _mainPageBitmapSet: Boolean = false

    var actionBarHeight: Int = 0
    var tabLayoutHeight: Int = 0

    fun getStatusBarBitmap(): Bitmap {
        if (_statusBarBitmap != null) return _statusBarBitmap!!
        val bg = getTabBg(0)!!
        val height = BarUtils.getStatusBarHeight()
        _statusBarBitmap = cutBitmap(bg, 0, 0, bg.width, height)
        return _statusBarBitmap!!
    }

    fun getActionBarBitmap(actionBar: View): Bitmap {
        if (_actionBarBitmap != null) return _actionBarBitmap!!
        val bg = getTabBg(0)!!
        actionBarHeight = actionBar.measuredHeight
        _actionBarBitmap = cutBitmap(bg, 0, BarUtils.getStatusBarHeight(), bg.width, actionBarHeight)
        return _actionBarBitmap!!
    }

    fun getTabLayoutBitmap(height: Int): Bitmap {
        if (_tabLayoutBitmap != null) return _tabLayoutBitmap!!
        val bg = getTabBg(0)!!
        //todo
        if (!HookConfig.is_hook_hide_actionbar) {
            while (actionBarHeight == 0) {
                Thread.sleep(100)
            }
        }
        tabLayoutHeight = height
        _tabLayoutBitmap = cutBitmap(bg, 0, BarUtils.getStatusBarHeight() + actionBarHeight, bg.width, height)
        return _tabLayoutBitmap!!
    }

    fun getTabLayoutBitmapAtBottom(height: Int): Bitmap {
        if (_tabLayoutBitmap != null) return _tabLayoutBitmap!!
        val bg = getTabBg(0)!!
        tabLayoutHeight = height
        _tabLayoutBitmap = cutBitmap(bg, 0, bg.height - height, bg.width, height)
        return _tabLayoutBitmap!!
    }

    fun setConversationBitmap(view: View) {
        val bg = getTabBg(0)
//        val cutTop = BarUtils.getStatusBarHeight() + HookConfig.value_main_text_offset
        setMainPageBitmap(view, bg!!, false)
    }

    fun setContactBitmap(view: View) {
        val bg = getTabBg(1)
        setMainPageBitmap(view, bg!!, true)
    }

    fun setDiscoverBitmap(view: View) {
        val bg = getTabBg(2)
        setMainPageBitmap(view, bg!!, true)
//        LogUtil.log("===========actionBarHeight=====$actionBarHeight")
//        setBitmap(view, bg!!, true, actionBarHeight)
    }

    fun setMainPageBitmap(view: View, bg: Bitmap, cutActionBarHeight: Boolean) {
        var height = 0
        waitInvoke(100, true, {
            val mActionBar = Objects.Main.HomeUI_mActionBar
            if (mActionBar != null) height = XposedHelpers.callMethod(mActionBar, "getHeight") as Int
            if (height > 0) actionBarHeight = height
            (tabLayoutHeight != 0) && (mActionBar != null) && (actionBarHeight > 0)
        }, {
            setBitmap(view, bg, cutActionBarHeight, actionBarHeight)
        })
    }

    //actionBarHeight确定之后再执行
    fun setBitmap(view: View, bg: Bitmap, cutActionBarHeight: Boolean, actionBarHeight: Int) {
        var y = BarUtils.getStatusBarHeight() + HookConfig.value_main_text_offset
        if (cutActionBarHeight) y += actionBarHeight
        if (HookConfig.is_hook_hide_actionbar) y -= actionBarHeight

        val _mainPageBitmap: Bitmap
        if (HookConfig.is_tab_layout_on_top) {
            y = y + tabLayoutHeight
            _mainPageBitmap = cutBitmap(bg, 0, y, bg.width, bg.height - y)
        } else {
            _mainPageBitmap = cutBitmap(bg, 0, y, bg.width, bg.height - y - tabLayoutHeight)
        }

        view.background = NightModeUtils.getBackgroundDrawable(_mainPageBitmap)

    }

    fun cutBitmap(source: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
//        LogUtil.log("cutBitmap:${source.width} ${source.height} $x $y ${x + width} ${y + height}")
        return Bitmap.createBitmap(source, x, y, width, height)
    }
}
