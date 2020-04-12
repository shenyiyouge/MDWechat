package com.blanke.mdwechat.config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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


    var _statusBarBitmap = mutableListOf<Bitmap?>(null, null, null, null)
    var _actionBarBitmap = mutableListOf<Bitmap?>(null, null, null, null)
    var _tabLayoutBitmap = mutableListOf<Bitmap?>(null, null, null, null)


    var actionBarHeight: Int = 0
    var tabLayoutHeight: Int = -1

    fun setGuideBarBitmaps(page: Int) {
        Objects.Main.statusView!!.background = NightModeUtils.getBackgroundDrawable(getStatusBarBitmap(page))
        Objects.Main.actionBar!!.background = NightModeUtils.getBackgroundDrawable(getActionBarBitmap(actionBarHeight, page))
        if (HookConfig.is_tab_layout_on_top) {
            Objects.Main.tabLayout!!.setBackground(NightModeUtils.getBackgroundDrawable(getTabLayoutBitmap(tabLayoutHeight, page)))
        } else {
            Objects.Main.tabLayout!!.setBackground(NightModeUtils.getBackgroundDrawable(getTabLayoutBitmapAtBottom(tabLayoutHeight, page)))
        }
    }

    fun getStatusBarBitmap(page: Int): Bitmap {
        if (_statusBarBitmap[page] != null) return _statusBarBitmap[page]!!
        val bg = getTabBg(page)!!
        val height = BarUtils.getStatusBarHeight()
        _statusBarBitmap[page] = cutBitmap(bg, 0, height)
        return _statusBarBitmap[page]!!
    }

    fun getActionBarBitmap(actionBarHeight: Int, page: Int): Bitmap {
        if (_actionBarBitmap[page] != null) return _actionBarBitmap[page]!!
        val bg = getTabBg(page)!!
        if (this.actionBarHeight == 0) this.actionBarHeight = actionBarHeight
        _actionBarBitmap[page] = cutBitmap(bg, BarUtils.getStatusBarHeight(), this.actionBarHeight)
        return _actionBarBitmap[page]!!
    }

    fun getTabLayoutBitmap(tabLayoutHeight: Int, page: Int): Bitmap {
        if (_tabLayoutBitmap[page] != null) return _tabLayoutBitmap[page]!!
        val bg = getTabBg(page)!!
        //todo
        if (!HookConfig.is_hook_hide_actionbar) {
            while (actionBarHeight == 0) {
                Thread.sleep(100)
            }
        }
        this.tabLayoutHeight = tabLayoutHeight
        _tabLayoutBitmap[page] = cutBitmap(bg, BarUtils.getStatusBarHeight() + actionBarHeight, tabLayoutHeight)
        return _tabLayoutBitmap[page]!!
    }

    fun getTabLayoutBitmapAtBottom(tabLayoutHeight: Int, page: Int): Bitmap {
        if (_tabLayoutBitmap[page] != null) return _tabLayoutBitmap[page]!!
        val bg = getTabBg(page)!!
        this.tabLayoutHeight = tabLayoutHeight
        _tabLayoutBitmap[page] = cutBitmap(bg, bg.height - tabLayoutHeight, tabLayoutHeight)
        return _tabLayoutBitmap[page]!!
    }

    fun setConversationBitmap(view: View) {
        val bg = getTabBg(0)
//        val cutTop = BarUtils.getStatusBarHeight() + HookConfig.value_main_text_offset
        setMainPageBitmap(view, bg!!, false, true)
    }

    fun setContactBitmap(view: View) {
        val bg = getTabBg(1)
        setMainPageBitmap(view, bg!!, true)
    }

    fun setDiscoverBitmap(view: View) {
        val bg = getTabBg(2)
        setMainPageBitmap(view, bg!!, true)
    }

    fun setSettingsBitmap(view: View) {
        val bg = getTabBg(3)
        setMainPageBitmap(view, bg!!, false, true, false)
    }

    fun setMainPageBitmap(view: View, bg: Bitmap,
                          cutActionBarHeight: Boolean,
                          addTablayoutHeight: Boolean = false,
                          cutStatusBarHeight: Boolean = true) {
        var height = 0
        waitInvoke(100, true, {
            val mActionBar = Objects.Main.HomeUI_mActionBar
            if (mActionBar != null) height = XposedHelpers.callMethod(mActionBar, "getHeight") as Int
            if (height > 0) actionBarHeight = height
            (tabLayoutHeight >= 0) && (mActionBar != null) && (actionBarHeight > 0)
        }, {
            setBitmap(view, bg, cutActionBarHeight, addTablayoutHeight, cutStatusBarHeight, actionBarHeight)
        })
    }

    //actionBarHeight确定之后再执行
    fun setBitmap(view: View, bg: Bitmap, cutActionBarHeight: Boolean, addTablayoutHeight: Boolean, cutStatusBarHeight: Boolean, actionBarHeight: Int) {
        var y = HookConfig.value_main_text_offset
        var heightPlus = 0
        if (cutActionBarHeight) y += actionBarHeight
        if (cutStatusBarHeight) y += BarUtils.getStatusBarHeight()
        if (HookConfig.is_hook_hide_actionbar) y -= actionBarHeight
        if (addTablayoutHeight && !HookConfig.is_tab_layout_on_top) heightPlus = tabLayoutHeight

        val _mainPageBitmap: Bitmap
        if (HookConfig.is_tab_layout_on_top) {
            y = y + tabLayoutHeight
            _mainPageBitmap = cutBitmap(bg, y, bg.height - y + heightPlus)
        } else {
            _mainPageBitmap = cutBitmap(bg, y, bg.height - y - tabLayoutHeight + heightPlus)
        }

        view.background = NightModeUtils.getBackgroundDrawable(_mainPageBitmap)

    }

    fun cutBitmap(source: Bitmap, y: Int, height: Int): Bitmap {
//        LogUtil.log("===================cutBitmap:${source.height}  $y ${y + height}")
        if ((y < 0) || (y + height > source.height)) {
            val fixedBitmap = Bitmap.createBitmap(source.width, height, source.getConfig())
            val canvas = Canvas(fixedBitmap)
            canvas.drawBitmap(source, 0.0F, -y.toFloat(), null)
//            LogUtil.log("===================fixedBitmap:${height}  ${fixedBitmap.height}")
            return Bitmap.createBitmap(fixedBitmap, 0, 0, source.width, height)
        }
        return Bitmap.createBitmap(source, 0, y, source.width, height)
    }
}
