package com.blanke.mdwechat.hookers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Methods
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.ViewTreeRepoThisVersion
import com.blanke.mdwechat.bean.PicPosition
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.AppCustomConfig.picPositionConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.main.ChattingRoomHook
import com.blanke.mdwechat.util.*

object BackgroundImageHooker {
    var _tabLayoutOnTop = false
    var _tabLayoutOnTopOffset = 0
    var _tabLayoutHeightOnBottom: Int = -1//微信默认tab的高度
    var contactPageParent: ViewGroup? = null
    var _contactPageWhiteBar = mutableListOf(0, 0)

    //  (y,height)
    var _actionBarLocation = mutableListOf(0, 0)
    var _tabLayoutLocation = mutableListOf(0, 0)
    var _contactPageLocation = mutableListOf(0, 0)

    var _actionBarBitmapInConversations: Bitmap? = null
    var _statusBarBitmap = mutableListOf<Bitmap?>(null, null, null, null)
    var _actionBarBitmap = mutableListOf<Bitmap?>(null, null, null, null)
    var _tabLayoutBitmap = mutableListOf<Bitmap?>(null, null, null, null)
    var _contactPageFix = mutableListOf<Drawable?>(null, null, null, null)
    var DiscoverPage: View? = null
    var _backgroundBitmap = mutableListOf<Bitmap?>(null, null, null, null)

    //region 导航
    var position = 0
    fun setGuideBarBitmaps(page: Int) {
        if (position == page) {
            return
        }
        position = page
        LogUtil.log("翻页操作: $page")
//        LogUtil.logOnlyOnce("Objects.Main.statusView=${Objects.Main.statusView}", "")
//        LogUtil.logOnlyOnce("Objects.Main.actionBar=${Objects.Main.actionBar}", "")
//        LogUtil.logOnlyOnce("Objects.Main.tabLayout=${Objects.Main.tabLayout}", "")
        Objects.Main.statusView?.background = NightModeUtils.getForegroundDrawable(getStatusBarBitmap(page))
        if (!HookConfig.is_hook_hide_actionbar) {
            Objects.Main.actionBar?.background = NightModeUtils.getForegroundDrawable(getActionBarBitmap(_actionBarLocation[1], page))
        }
        if (HookConfig.is_hook_tab && HookConfig.is_tab_layout_on_top) {
            setTabLayoutBitmap(page)
        } else {
            Objects.Main.tabLayout?.background = NightModeUtils.getForegroundDrawable(getTabLayoutBitmapAtBottom(_tabLayoutLocation[1], page))
        }
        Objects.Main.contactPageFix?.apply {
            setContactPageFixBackround(this, page)
//            this.background = NightModeUtils.getBackgroundDrawable(cutBitmap("联系人界面高度补正", bg, _contactPageWhiteBar[0], _contactPageWhiteBar[1]))
        }
    }

    fun getStatusBarBitmap(page: Int): Bitmap? {
        if (!HookConfig.is_hook_bg_immersion) return null
        if (_statusBarBitmap[page] != null) return _statusBarBitmap[page]!!
        LogUtil.log("Getting StatusBarBitmap, $page")
        val bg = AppCustomConfig.getTabBg(page)
        val height = HookConfig.statusBarHeight
        _statusBarBitmap[page] = cutBitmap("StatusBarBitmap", bg, 0, height)
        LogUtil.log("Got StatusBarBitmap, $page")
        return _statusBarBitmap[page]
    }

    fun getActionBarBitmap(actionBarHeight: Int, page: Int): Bitmap? {
        if (!HookConfig.is_hook_bg_immersion || (HookConfig.is_hook_hide_actionbar)) {
            _actionBarLocation[1] = -1
            return null
        }
        if (_actionBarBitmap[page] != null) return _actionBarBitmap[page]!!
        LogUtil.log("Getting ActionBarBitmap, $page")
        _actionBarBitmap[page] = if (actionBarHeight == 0) {
            null
        } else {
            val bg = AppCustomConfig.getTabBg(page)
            if ((_actionBarLocation[1] == 0)) {
                _actionBarLocation[0] = HookConfig.statusBarHeight
                _actionBarLocation[1] = actionBarHeight
            }
//            if ((this._actionBarLocation[1] != actionBarHeight) || (this._actionBarLocation[0] != HookConfig.statusBarHeight)) {
//                LogUtil.log("Action bar 位置改变：(${this._actionBarLocation[0]},   ${this._actionBarLocation[1]}) TO (${HookConfig.statusBarHeight},$actionBarHeight)")
//                this._actionBarLocation[0] = HookConfig.statusBarHeight
//                this._actionBarLocation[1] = actionBarHeight
//            }

            cutBitmap("ActionBarBitmap", bg, _actionBarLocation[0], _actionBarLocation[1])
        }
        LogUtil.log("Got ActionBarBitmap, $page")
        return _actionBarBitmap[page]
    }

    fun setTabLayoutBitmap(page: Int) {
        if (!HookConfig.is_hook_bg_immersion) {
            Objects.Main.tabLayout?.background = NightModeUtils.getForegroundDrawable(null)
            return
        }
//        return null
        if (_tabLayoutBitmap[page] != null) {
            Objects.Main.tabLayout?.background = NightModeUtils.getForegroundDrawable(_tabLayoutBitmap[page])
            return
        }
        LogUtil.log("Getting TabLayoutBitmap, $page")
        waitInvoke(500, true, {
            LogUtil.log("setTabLayoutBitmap[$page] 继续等待, tabLayout.height = ${Objects.Main.tabLayout?.height}")
            (Objects.Main.tabLayout != null) && (Objects.Main.tabLayout!!.height > 0)
//            _actionBarLocation[1] > 0
        }, {
            val location = IntArray(2)
            val bg = AppCustomConfig.getTabBg(page)
//            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
            Objects.Main.tabLayout!!.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
//                LogUtil.log("=================$logHead TRULY: ${location[1]} ${location[1] + view.height}")
            _tabLayoutBitmap[page] = cutBitmap("TabLayoutBitmap", bg, location[1], Objects.Main.tabLayout!!.height)
            Objects.Main.tabLayout!!.background = NightModeUtils.getForegroundDrawable(_tabLayoutBitmap[page])
            _tabLayoutLocation[0] = location[1]
            _tabLayoutLocation[1] = Objects.Main.tabLayout!!.height
        }
//                {
////        if (this._tabLayoutLocation[1] < 0) {
//            this._tabLayoutLocation[0] = HookConfig.statusBarHeight + _actionBarLocation[0]
//            this._tabLayoutLocation[1] = tabLayoutHeight
////        }
//            _tabLayoutBitmap[page] = cutBitmap("TabLayoutBitmap", bg, this._tabLayoutLocation[0], this._tabLayoutLocation[1])
//            LogUtil.log("Got TabLayoutBitmap, $page")
//            Objects.Main.tabLayout?.background = NightModeUtils.getForegroundDrawable(_tabLayoutBitmap[page])
//        })
        )

    }

    fun getTabLayoutBitmapAtBottom(tabLayoutHeight: Int, page: Int): Bitmap? {
        if (!HookConfig.is_hook_bg_immersion) return null
        if (_tabLayoutBitmap[page] != null) return _tabLayoutBitmap[page]
        LogUtil.log("Getting TabLayoutBitmapAtBottom, $page")
        val bg = AppCustomConfig.getTabBg(page)
        if (_tabLayoutLocation[1] == 0) {
            _tabLayoutLocation[0] = bg.height - tabLayoutHeight
            _tabLayoutLocation[1] = tabLayoutHeight
        }
        _tabLayoutBitmap[page] = cutBitmap("TabLayoutBitmapAtBottom", bg, _tabLayoutLocation[0], _tabLayoutLocation[1])
        LogUtil.log("Got TabLayoutBitmapAtBottom, $page")
        return _tabLayoutBitmap[page]
    }

    fun setContactPageFixBackround(contactPageFix: View, page: Int) {
        if (page != 1 && page != 2) {
            contactPageFix.background = null
        }
        if (_contactPageFix[page] != null) {
            contactPageFix.background = _contactPageFix[page]
            return
        }
        LogUtil.log("Getting contactPageFix, $page")
        val bg = AppCustomConfig.getTabBg(page)
        _contactPageFix[page] = NightModeUtils.getBackgroundDrawable(cutBitmap("联系人界面高度补正", bg, _contactPageWhiteBar[0], _contactPageWhiteBar[1]))
        contactPageFix.background = _contactPageFix[page]
    }

    //endregion
    //region 聊天
    fun setActionBarBitmapInConversations(actionBar: View) {
        if (!HookConfig.is_hook_bg_immersion) {
            actionBar.background = NightModeUtils.getForegroundDrawable(null)
            return
        }

        // 只修改聊天页面
        val chattingUILayout = ViewUtils.getParentView(actionBar, 1) as View
        when (chattingUILayout::class.java.name) {
            "com.tencent.mm.pluginsdk.ui.chat.ChattingUILayout" -> {
                if (ViewTreeUtils.equals(ViewTreeRepoThisVersion.ActionBarInConversationItem.item, actionBar) && (_actionBarBitmapInConversations != null)) {
                    actionBar.background = NightModeUtils.getForegroundDrawable(_actionBarBitmapInConversations)
                    ChattingRoomHook.setConversationColor(actionBar)
                    return
                }
                LogUtil.log("等待点击聊天界面以显示聊天界面的沉浸背景.....")
                waitInvoke(200, true, {
                    actionBar.height > 0
                }, {
                    val bg = AppCustomConfig.getChatBg()
                    val location = IntArray(2)
                    actionBar.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
                    val background = cutBitmap("actionBarInConversations", bg, location[1], actionBar.height)
                    ChattingRoomHook.actionBarBottom = location[1] + actionBar.height
                    actionBar.background = NightModeUtils.getForegroundDrawable(background)

                    if (ViewTreeUtils.equals(ViewTreeRepoThisVersion.ActionBarInConversationItem.item, actionBar)) {
                        LogUtil.log("已找到聊天界面")
//                        Objects.Main.actionBarInConversations = actionBar
                        _actionBarBitmapInConversations = background
                        ChattingRoomHook.setConversationColor(actionBar)
                    }
//                    else if (ViewTreeUtils.equals(ViewTreeRepoThisVersion.ActionBarInFriendsgroupItem.item, actionBar)) {
//                        LogUtil.log("已找到朋友圈界面")
//                        _actionBarBitmapInFriendsgroup = background
//                    }
                })
            }
            "android.support.v7.widget.ActionBarOverlayLayout" -> {
                val ChattingScrollLayoutItem = ViewUtils.getParentView(chattingUILayout, 3) as View
                if (!ChattingScrollLayoutItem::class.java.name.equals("com.tencent.mm.ui.widget.SwipeBackLayout")) {
                    return
                }
                if (ViewTreeUtils.equals(ViewTreeRepoThisVersion.ActionBarInSearchConversationItem.item, actionBar) && (_actionBarBitmapInConversations != null)) {
                    actionBar.background = NightModeUtils.getForegroundDrawable(_actionBarBitmapInConversations)
//                    TitleColorHook.setConversationColor(actionBar)
                    return
                }
                waitInvoke(100, true, {
                    LogUtil.log("ActionBarInSearchConversation 继续等待, view.height  = ${actionBar.height}")
                    actionBar.height > 0
                }, {
                    val bg = AppCustomConfig.getChatBg()
                    val location = IntArray(2)
                    actionBar.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
                    val background = cutBitmap("ActionBarInSearchConversation", bg, location[1], actionBar.height)
                    ChattingRoomHook.actionBarBottom = location[1] + actionBar.height
                    actionBar.background = NightModeUtils.getForegroundDrawable(background)

                    if (ViewTreeUtils.equals(ViewTreeRepoThisVersion.ActionBarInSearchConversationItem.item, actionBar)) {
                        LogUtil.log("已找到通过搜索打开的聊天界面")
//                        Objects.Main.actionBarInConversations = actionBar
                        _actionBarBitmapInConversations = background
                        ChattingRoomHook.setConversationInSearchColor(actionBar)

                    } else {
                        LogUtil.log("匹配通过搜索打开的聊天界面失败")
                        LogUtil.logViewStackTraces(actionBar)
                    }
//            isSettingMap = false
                })
            }
        }
    }


    //endregion
    //region 背景
    fun setConversationBitmap(view: View) {
        _backgroundBitmap[0]?.apply {
            view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[0])
            return
        }
//        LogUtil.log("=============0===============")
        val bg = AppCustomConfig.getTabBg(0)
        if (!HookConfig.is_hook_bg_immersion) {
            view.background = NightModeUtils.getBackgroundDrawable(bg)
        } else {
            setMainPageBitmap("setConversationBitmap", view, bg, 0)
        }
    }

    fun setContactBitmap(view: View) {
        _backgroundBitmap[1]?.apply {
            view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[1])
            return
        }
//        LogUtil.log("=============1===============")
        val bg = AppCustomConfig.getTabBg(1)
        if (!HookConfig.is_hook_bg_immersion) {
            view.background = NightModeUtils.getBackgroundDrawable(bg)
        } else {
            setMainPageBitmap("setContactBitmap", view, bg, 1)
        }
    }

    fun setDiscoverBitmap(view: View) {
        _backgroundBitmap[2]?.apply {
            view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[2])
            return
        }
//        LogUtil.log("============2================")
        if (!HookConfig.is_hook_bg_immersion) {
            view.background = NightModeUtils.getBackgroundDrawable(AppCustomConfig.getTabBg(2))
        } else {
            DiscoverPage = view
            //先跳转至联系人界面
            Objects.Main.LauncherUI_mViewPager?.apply {
                Methods.WxViewPager_selectedPage.invoke(this, 1, false, false, 0)
            }
        }
//        setMainPageBitmap("setDiscoverBitmap", view, bg, true)
    }

    fun setSettingsBitmap(view: View) {
        _backgroundBitmap[3]?.apply {
            view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[3])
            return
        }
//        LogUtil.log("============3================")
        val bg = AppCustomConfig.getTabBg(3)
        if (!HookConfig.is_hook_bg_immersion) {
            view.background = NightModeUtils.getBackgroundDrawable(bg)
        } else {
            setMainPageBitmap("setSettingsBitmap", view, bg, 3)
        }
    }
    //endregion

    fun setMainPageBitmap(logHead: String, view: View, bg: Bitmap, index: Int) {
//        加载记录的高度
        if (HookConfig.is_bg_preload_mode) {
            picPositionConfig.apply {
                LogUtil.log("加载已记录位置的背景图片:" + index)
                try {
                    if (picPositionConfig.lastModifiedTimeOfSettings == 0.toLong()
                            || picPositionConfig.screenHeight <= 0
                            || picPositionConfig.backgroundPicPos == null
                            || picPositionConfig.backgroundPicPos!!.size < 4
                            || index > 3) {
                        return@apply
                    }
                    val position = picPositionConfig.backgroundPicPos!![index]
                    if (position.height <= 0) {
                        return@apply
                    }
                    _backgroundBitmap[index] = cutBitmap(logHead, bg, position.y, position.height)
                    view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[index])

                    if (index == 1) {
                        //发现页
                        _contactPageLocation[0] = position.y
                        _contactPageLocation[1] = position.height
                        _backgroundBitmap[2] = cutBitmap(
                                logHead,
                                AppCustomConfig.getTabBg(2),
                                _contactPageLocation[0],
                                _contactPageLocation[1])
                        DiscoverPage?.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[2])

                        //回到最近对话界面
                        Objects.Main.LauncherUI_mViewPager?.apply {
                            Methods.WxViewPager_selectedPage.invoke(this, 0, false, false, 0)
                        }
                    }
                    LogUtil.log("背景图片" + index + "已生成")
                    return
                } catch (e: Exception) {
                    LogUtil.log(e)
                }
            }
        }

        waitInvoke(500, true, {
            LogUtil.log("$logHead 继续等待, view.height  = ${view.height}")
            view.height > 0
        }, {
            val location = IntArray(2)
//            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
            view.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标

            var height = view.height
            when (index) {
                0, 3 -> {
                    if (_tabLayoutOnTop) {
                        location[1] += _tabLayoutOnTopOffset
                        height -= _tabLayoutOnTopOffset
                    }
                }
            }
            _backgroundBitmap[index] = cutBitmap(logHead, bg, location[1], height)
            view.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[index])

            when (index) {
                3 -> {
                    if (_tabLayoutOnTop) {
                        location[1] -= _tabLayoutOnTopOffset
                        height += _tabLayoutOnTopOffset
                    }
                }
                1 -> {
                    _contactPageLocation[0] = location[1]
                    _contactPageLocation[1] = view.height
                    //联系人界面和发现界面长宽比一样，故联系人界面可作发现界面的参考
                    _backgroundBitmap[2] = cutBitmap(
                            logHead,
                            AppCustomConfig.getTabBg(2),
                            _contactPageLocation[0],
                            _contactPageLocation[1])
                    DiscoverPage?.background = NightModeUtils.getBackgroundDrawable(_backgroundBitmap[2])

                    val pageBodyTop = if (_tabLayoutOnTop)
                        _tabLayoutLocation[0] + _tabLayoutLocation[1]
                    else _actionBarLocation[0] + _actionBarLocation[1]

                    LogUtil.log("==========pageBodyTop=$pageBodyTop===========")
                    LogUtil.log("==========_contactPageLocation=${_contactPageLocation[0]}===========")


                    if (_contactPageLocation[0] > pageBodyTop) {
                        _contactPageWhiteBar[0] = pageBodyTop
                        _contactPageWhiteBar[1] = _contactPageLocation[0] - pageBodyTop
                        contactPageParent?.apply {
                            val paramsAddedOnTop = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            paramsAddedOnTop.topMargin = _contactPageWhiteBar[0]
                            paramsAddedOnTop.height = _contactPageWhiteBar[1]
                            Objects.Main.contactPageFix = FrameLayout(this.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY))
                            setContactPageFixBackround(this, 1)
//                        Objects.Main.contactPageFix!!.background = NightModeUtils.getBackgroundDrawable(cutBitmap("联系人界面高度补正", bg, _contactPageWhiteBar[0], _contactPageWhiteBar[1]))
                            this.addView(Objects.Main.contactPageFix!!, 1, paramsAddedOnTop)
                        }
                    }
                    //回到最近对话界面
                    Objects.Main.LauncherUI_mViewPager?.apply {
                        Methods.WxViewPager_selectedPage.invoke(this, 0, false, false, 0)
                    }
                }
            }

//            记录位置信息到文件
            if (HookConfig.is_bg_preload_mode) {
                try {
                    if (picPositionConfig.screenHeight <= 0) {
                        picPositionConfig.screenHeight = HookConfig.value_resolution[1]
                    }
                    if (picPositionConfig.backgroundPicPos == null) {
                        picPositionConfig.backgroundPicPos = mutableListOf(
                                PicPosition(0, 0),
                                PicPosition(0, 0),
                                PicPosition(0, 0),
                                PicPosition(0, 0)
                        )
                    }
                    //图片底部的y值
                    val bottomY: Int = location[1] + height
                    if (bottomY == picPositionConfig.screenHeight
                            || bottomY + _tabLayoutHeightOnBottom == picPositionConfig.screenHeight) {
                        picPositionConfig.backgroundPicPos!![index] = PicPosition(location[1], height)
                        if (index == 1) {
                            //连同发现页一起写了
                            picPositionConfig.backgroundPicPos!![2] = PicPosition(location[1], height)
                        }
                        AppCustomConfig.writePicPositionConfig()
                    }
                } catch (e: Exception) {
                    LogUtil.log(e)
                }
            }
        })
    }

    fun cutBitmap(logHead: String, source: Bitmap, y: Int, height: Int): Bitmap {
        LogUtil.log("图片高度 of $logHead:" + " y=${y} height=${height}")
        if (height <= 0) {
            LogUtil.log("cutBitmap failed")
            LogUtil.logStackTraces()
            return Bitmap.createBitmap(source, 0, 0, source.width, 1)
        }
        if ((y < 0) || (y + height > source.height)) {
            val fixedBitmap = Bitmap.createBitmap(source.width, height, source.config)
            val canvas = Canvas(fixedBitmap)
            canvas.drawBitmap(source, 0.0F, -y.toFloat(), null)
//            LogUtil.log("===================fixedBitmap:${height}  ${fixedBitmap.height}")
            return Bitmap.createBitmap(fixedBitmap, 0, 0, source.width, height)
        }
        return Bitmap.createBitmap(source, 0, y, source.width, height)
    }


    /////////////////////////////////////////////////////
//TODO 可整合代码
//根据绝对位置来设置view 的背景图片
    fun setBackgroundBitmap(logHead: String, view: View, screenImage: Bitmap, cache: Bitmap?): List<Int> {
        if (cache != null) {
            view.background = NightModeUtils.getBackgroundDrawable(cache)
            return mutableListOf(-1, -1)
        }
        val location = IntArray(2)
        view.getLocationOnScreen(location)//获取在整个屏幕内的绝对坐标
        view.background = NightModeUtils.getBackgroundDrawable(cutBitmap(logHead, screenImage, location[1], view.height))
        return mutableListOf(location[1], view.height)
    }
}