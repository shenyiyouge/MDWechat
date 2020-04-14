package com.blanke.mdwechat.config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.bean.FloatButtonConfig
import com.blanke.mdwechat.util.BitmapUtil
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import com.blanke.mdwechat.util.waitInvoke
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

    //自动适配屏幕分辨率
    fun getTabBg(index: Int): Bitmap {
        var bg = getIcon(Common.FILE_NAME_TAB_BG_PREFIX + "$index.png")
//        LogUtil.log("getBG $index =============== ${bg?.width}+${bg?.height}+${bg?.toString()}")
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
    var DiscoverPage: View? = null

    var ContactPageY = 0
    var ContactPageHeight = 0
    var actionBarHeight: Int = 0
    var tabLayoutHeight: Int = -1

    fun setGuideBarBitmaps(page: Int) {
        LogUtil.logOnlyOnce("翻页操作: $page", "")
        LogUtil.logOnlyOnce("Objects.Main.statusView=${Objects.Main.statusView}", "")
        LogUtil.logOnlyOnce("Objects.Main.actionBar=${Objects.Main.actionBar}", "")
        LogUtil.logOnlyOnce("Objects.Main.tabLayout=${Objects.Main.tabLayout}", "")
        Objects.Main.statusView!!.background = NightModeUtils.getForegroundDrawable(getStatusBarBitmap(page))
        Objects.Main.actionBar!!.background = NightModeUtils.getForegroundDrawable(getActionBarBitmap(actionBarHeight, page))
        if (HookConfig.is_tab_layout_on_top) {
            Objects.Main.tabLayout!!.background = NightModeUtils.getForegroundDrawable(getTabLayoutBitmap(tabLayoutHeight, page))
        } else {
            Objects.Main.tabLayout!!.background = NightModeUtils.getForegroundDrawable(getTabLayoutBitmapAtBottom(tabLayoutHeight, page))
        }
    }

    fun getStatusBarBitmap(page: Int): Bitmap? {
        if (!HookConfig.is_hook_tab_bg) return null
        if (_statusBarBitmap[page] != null) return _statusBarBitmap[page]!!
        LogUtil.log("Getting StatusBarBitmap, $page")
        val bg = getTabBg(page)
        val height = HookConfig.statusBarHeight
        _statusBarBitmap[page] = cutBitmap("StatusBarBitmap", bg, 0, height)
        LogUtil.log("Got StatusBarBitmap, $page")
        return _statusBarBitmap[page]!!
    }

    fun getActionBarBitmap(actionBarHeight: Int, page: Int): Bitmap? {
        if (!HookConfig.is_hook_tab_bg) return null
//        return null
        if (_actionBarBitmap[page] != null) return _actionBarBitmap[page]!!
        LogUtil.log("Getting ActionBarBitmap, $page")
        val bg = getTabBg(page)
        if (this.actionBarHeight == 0) this.actionBarHeight = actionBarHeight
        _actionBarBitmap[page] = if (HookConfig.is_hook_hide_actionbar)
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) else
            cutBitmap("ActionBarBitmap", bg, HookConfig.statusBarHeight, this.actionBarHeight)
        LogUtil.log("Got ActionBarBitmap, $page")
        return _actionBarBitmap[page]!!
    }

    fun getTabLayoutBitmap(tabLayoutHeight: Int, page: Int): Bitmap? {
        if (!HookConfig.is_hook_tab_bg) return null
        LogUtil.log("Getting TabLayoutBitmap, $page")
//        return null
        if (_tabLayoutBitmap[page] != null) return _tabLayoutBitmap[page]!!
        val bg = getTabBg(page)
        //todo
        if (!HookConfig.is_hook_hide_actionbar) {
            while (actionBarHeight == 0) {
                Thread.sleep(100)
            }
        }
        this.tabLayoutHeight = tabLayoutHeight
        _tabLayoutBitmap[page] = cutBitmap("TabLayoutBitmap", bg, HookConfig.statusBarHeight + actionBarHeight, tabLayoutHeight)
        LogUtil.log("Got TabLayoutBitmap, $page")
        return _tabLayoutBitmap[page]!!
    }

    fun getTabLayoutBitmapAtBottom(tabLayoutHeight: Int, page: Int): Bitmap? {
        if (!HookConfig.is_hook_tab_bg) return null
        LogUtil.log("Getting TabLayoutBitmapAtBottom, $page")
        if (_tabLayoutBitmap[page] != null) return _tabLayoutBitmap[page]!!
        val bg = getTabBg(page)
        this.tabLayoutHeight = tabLayoutHeight
        _tabLayoutBitmap[page] = cutBitmap("TabLayoutBitmapAtBottom", bg, bg.height - tabLayoutHeight, tabLayoutHeight)
        LogUtil.log("Got TabLayoutBitmapAtBottom, $page")
        return _tabLayoutBitmap[page]!!
    }

    fun setConversationBitmap(view: View) {
        val bg = getTabBg(0)
        setMainPageBitmap("setConversationBitmap", view, bg, false, true)
    }

    fun setContactBitmap(view: View) {
        val bg = getTabBg(1)
        setMainPageBitmap("setContactBitmap", view, bg, true)
    }

    fun setDiscoverBitmap(view: View) {
        val bg = getTabBg(2)
        DiscoverPage = view
        setMainPageBitmap("setDiscoverBitmap", view, bg, true)
    }

    fun setSettingsBitmap(view: View) {
        val bg = getTabBg(3)
        setMainPageBitmap("setSettingsBitmap", view, bg, false, true, false)
    }

    fun setMainPageBitmap(logHead: String, view: View, bg: Bitmap,
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
            setBitmap(logHead, view, bg, cutActionBarHeight, addTablayoutHeight, cutStatusBarHeight, actionBarHeight)
        })
    }

    //actionBarHeight确定之后再执行
    fun setBitmap(logHead: String,
                  view: View,
                  bg: Bitmap,
                  cutActionBarHeight: Boolean,
                  addTablayoutHeight: Boolean,
                  cutStatusBarHeight: Boolean,
                  actionBarHeight: Int) {
        var y = HookConfig.value_main_text_offset
        var heightPlus = 0
        if (cutActionBarHeight) y += actionBarHeight
        if (cutStatusBarHeight) y += HookConfig.statusBarHeight
        if (HookConfig.is_hook_hide_actionbar) y -= actionBarHeight
        if (addTablayoutHeight && !HookConfig.is_tab_layout_on_top) heightPlus = tabLayoutHeight

        val _mainPageBitmap: Bitmap
        if (HookConfig.is_tab_layout_on_top) {
            y = y + tabLayoutHeight
            _mainPageBitmap = cutBitmap(logHead, bg, y, bg.height - y + heightPlus)
        } else {
            _mainPageBitmap = cutBitmap(logHead, bg, y, bg.height - y - tabLayoutHeight + heightPlus)
        }

        view.background = NightModeUtils.getBackgroundDrawable(_mainPageBitmap)
        //beginning of log

        //由于未知因素, 需要对非1080*1920分辨率屏幕作调整
        if (!logHead.equals("setDiscoverBitmap")) {
            waitInvoke(100, true, {
                view.height > 0
            }, {
                val location = IntArray(2)
//            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                view.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
//                LogUtil.log("=================$logHead TRULY: ${location[1]} ${location[1] + view.height}")
                view.background = NightModeUtils.getBackgroundDrawable(cutBitmap(logHead, bg, location[1], view.height))
                if (logHead.equals("setContactBitmap")) {
                    ContactPageY = location[1]
                    ContactPageHeight = view.height
                    //联系人界面和发现界面长宽比一样，故联系人界面可作发现界面的参考
                    DiscoverPage?.background = NightModeUtils.getBackgroundDrawable(cutBitmap(logHead, bg, ContactPageY, ContactPageHeight))
                }
            })
        } else if (ContactPageHeight > 0) {
            view.background = NightModeUtils.getBackgroundDrawable(cutBitmap(logHead, bg, ContactPageY, ContactPageHeight))
        }
//        else {
//            var height = view.height
//            var locationOld = IntArray(2)
//            val location = IntArray(2)
//            view.getLocationOnScreen(locationOld);//获取在整个屏幕内的绝对坐标
//            waitInvoke(1000, true, { true }, {
//                view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
//                if ((view.height != height) || (!locationOld.contentEquals(location))) {
//                    height = view.height
//                    locationOld = location
//                    LogUtil.log("============@@@@@@@@@=====$logHead 变化: ${location[1]} ${view.height}")
//                    view.background = NightModeUtils.getBackgroundDrawable(cutBitmap(logHead, bg, location[1], view.height))
//                }
//            })
//        }
    }

    fun cutBitmap(logHead: String, source: Bitmap, y: Int, height: Int): Bitmap {
        LogUtil.logOnlyOnce("图片高度 of $logHead:" + " y=${y} height=${y + height}")
        if ((y < 0) || (y + height > source.height)) {
            val fixedBitmap = Bitmap.createBitmap(source.width, height, source.config)
            val canvas = Canvas(fixedBitmap)
            canvas.drawBitmap(source, 0.0F, -y.toFloat(), null)
//            LogUtil.log("===================fixedBitmap:${height}  ${fixedBitmap.height}")
            return Bitmap.createBitmap(fixedBitmap, 0, 0, source.width, height)
        }
        return Bitmap.createBitmap(source, 0, y, source.width, height)
    }
}
