package com.blanke.mdwechat.hookers

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.drawableTransparent
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import com.blanke.mdwechat.util.ViewTreeUtils
import com.blanke.mdwechat.util.ViewUtils
import com.blanke.mdwechat.util.ViewUtils.findLastChildView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import com.blanke.mdwechat.ViewTreeRepoThisVersion as VTTV

object ListViewHooker : HookerProvider {
    private val excludeContext = arrayOf("com.tencent.mm.plugin.mall.ui.MallIndexUI")

    private val titleTextColor: Int
        get() {
            return NightModeUtils.getTitleTextColor()
        }
    private val summaryTextColor: Int
        get() {
            return NightModeUtils.getContentTextColor()
        }

    private val isHookTextColor: Boolean
        get() {
            return HookConfig.is_hook_main_textcolor || NightModeUtils.isNightMode()
        }

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(listViewHook)
    }

    private val listViewHook = Hooker {
        XposedHelpers.findAndHookMethod(AbsListView::class.java, "setSelector", Drawable::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                param?.args!![0] = drawableTransparent
            }
        })
        XposedHelpers.findAndHookMethod(AbsListView::class.java, "obtainView", CC.Int, BooleanArray::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                try {
                    val view = param?.result as View
                    val context = view.context
                    val tmp = excludeContext.find { context::class.java.name.contains(it) }
                    if (tmp != null) {
                        return
                    }
//                    view.background.alpha = 120
//                view.background = defaultImageRippleDrawable

//                    LogUtil.log("----------抓取view start----------")
//                    LogUtil.log(WechatGlobal.wxVersion.toString())
//                    LogUtil.log("context=" + view.context)
//                    LogUtil.logViewStackTraces(view)
//                    LogUtil.logParentView(view, 10)
//                    LogUtil.log("--------------------")

                    // 按照使用频率重排序
                    //气泡
                    if (((!NightModeUtils.isNightMode()) || HookConfig.is_hook_bubble_in_night_mode) && HookConfig.is_hook_bubble) {
                        // 聊天消息 item
                        if (ViewTreeUtils.equals(VTTV.ChatRightMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatRightMessageItem")
                            val chatMsgRightTextColor = HookConfig.get_hook_chat_text_color_right
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightMessageItem.treeStacks["msgView"]!!) as View
//                    log("msgView=$msgView")
                            XposedHelpers.callMethod(msgView, "setTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setLinkTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setHintTextColor", chatMsgRightTextColor)
//                    val mText = XposedHelpers.getObjectField(msgView, "mText")
//                    log("msg right text=$mText")
                            val bubble = WeChatHelper.getRightBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(30, 25, 45, 25)
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatLeftMessageItem")
                            val chatMsgLeftTextColor = HookConfig.get_hook_chat_text_color_left
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatLeftMessageItem.treeStacks.get("msgView")!!) as View
//                    LogUtil.logXp("=======start=========")
//                    LogUtil.logXp("msgView=$msgView")
//                    val mText = XposedHelpers.getObjectField(msgView, "mText")
//                    LogUtil.logXp("msg left text=$mText")
//                    LogUtil.logViewXp(view)
//                    LogUtil.logStackTraceXp()
//                    LogUtil.logViewStackTracesXp(ViewUtils.getParentViewSafe(view, 111))
//                    LogUtil.logXp("=======end=========")
                            XposedHelpers.callMethod(msgView, "setTextColor", chatMsgLeftTextColor)
                            XposedHelpers.callMethod(msgView, "setLinkTextColor", chatMsgLeftTextColor)
                            XposedHelpers.callMethod(msgView, "setHintTextColor", chatMsgLeftTextColor)
                            // 聊天气泡
                            val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(45, 25, 30, 25)
                            }
                        }

                        // 聊天消息 audio
                        else if (ViewTreeUtils.equals(VTTV.ChatRightAudioMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatRightAudioMessageItem")
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightAudioMessageItem.treeStacks.get("msgView")!!) as View
                            val msgAnimView = ViewUtils.getChildView1(view, VTTV.ChatRightAudioMessageItem.treeStacks.get("msgAnimView")!!) as View
                            val bubble = WeChatHelper.getRightBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(30, 25, 45, 25)
                            }
                            msgAnimView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgAnimView.setPadding(30, 25, 45, 25)
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftAudioMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatLeftAudioMessageItem")
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatLeftAudioMessageItem.treeStacks.get("msgView")!!) as View
                            val msgAnimView = ViewUtils.getChildView1(view, VTTV.ChatLeftAudioMessageItem.treeStacks.get("msgAnimView")!!) as View
                            val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(45, 25, 30, 25)
                            }
                            msgAnimView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgAnimView.setPadding(45, 25, 30, 25)
                            }
                        }

                        // 通话消息
                        else if (ViewTreeUtils.equals(VTTV.ChatRightCallMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatRightCallMessageItem")
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightCallMessageItem.treeStacks.get("msgView")!!) as View
                            val bubble = WeChatHelper.getRightBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(30, 25, 45, 25)
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftCallMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatLeftCallMessageItem")
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatLeftCallMessageItem.treeStacks.get("msgView")!!) as View
                            val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(45, 25, 30, 25)
                            }
                        }

                        // 引用消息 item
                        if (ViewTreeUtils.equals(VTTV.RefRightMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.RefRightMessageItem")
                            val chatMsgRightTextColor = HookConfig.get_hook_chat_text_color_right
                            val msgView = ViewUtils.getChildView1(view, VTTV.RefRightMessageItem.treeStacks.get("msgView")!!) as View
                            XposedHelpers.callMethod(msgView, "setTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setLinkTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setHintTextColor", chatMsgRightTextColor)
                            val bubble = WeChatHelper.getRightBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(30, 25, 45, 25)
                            }
                        } else if (ViewTreeUtils.equals(VTTV.RefLeftMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ListViewHooker.ChatLeftMessageItem")
                            val chatMsgLeftTextColor = HookConfig.get_hook_chat_text_color_left
                            val msgView = ViewUtils.getChildView1(view, VTTV.RefLeftMessageItem.treeStacks.get("msgView")!!) as View
                            XposedHelpers.callMethod(msgView, "setTextColor", chatMsgLeftTextColor)
                            XposedHelpers.callMethod(msgView, "setLinkTextColor", chatMsgLeftTextColor)
                            XposedHelpers.callMethod(msgView, "setHintTextColor", chatMsgLeftTextColor)
                            // 聊天气泡
                            val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                            msgView.background = bubble
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                msgView.setPadding(45, 25, 30, 25)
                            }
                        }
                    }

                    // ConversationFragment 聊天列表 item
                    if (ViewTreeUtils.equals(VTTV.ConversationListViewItem.item, view)) {
                        LogUtil.logOnlyOnce("ListViewHooker.ConversationListViewItem")
                        try {
                            view.background.alpha = HookConfig.get_hook_conversation_background_alpha
                        } catch (e: Exception) {
                        }
                        val chatNameView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks["chatNameView"])
                        val chatTimeView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks["chatTimeView"])
                        val recentMsgView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks["recentMsgView"])
                        val unreadCountView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks["unreadCountView"]) as TextView
                        val unreadView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks["unreadView"]) as ImageView
//                    LogUtil.logXp("chatNameView=$chatNameView,chatTimeView=$chatTimeView,recentMsgView=$recentMsgView")
                        if (isHookTextColor) {
                            XposedHelpers.callMethod(chatNameView, "setTextColor", titleTextColor)
                            XposedHelpers.callMethod(chatTimeView, "setTextColor", summaryTextColor)
                            XposedHelpers.callMethod(recentMsgView, "setTextColor", summaryTextColor)
                        }
                        unreadCountView.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        unreadCountView.setTextColor(HookConfig.get_color_tip_num)
                        unreadView.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        val contentView = ViewUtils.getChildView(view, 1) as ViewGroup
                        contentView.background = defaultImageRippleDrawable
                        return
                    }

                    view.background = defaultImageRippleDrawable
                    // 联系人列表
                    if (ViewTreeUtils.equals(VTTV.ContactListViewItem.item, view)) {
                        LogUtil.logOnlyOnce("ListViewHooker.ContactListViewItem")
                        // 标题下面的线
                        if (VTTV.ContactListViewItem.treeStacks["headerView"] != null) {
                            ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks["headerView"])
                                    ?.background = drawableTransparent
                        }
                        //内容下面的线 innerView
                        ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks["innerView"])
                                ?.background = drawableTransparent

                        ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks["contentView"])
                                ?.background = drawableTransparent

                        val titleView = ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks["titleView"])
                        titleView?.background = drawableTransparent
                        if (isHookTextColor) {
                            val headTextView = ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks["headTextView"]) as TextView
                            headTextView.setTextColor(summaryTextColor)
                            XposedHelpers.callMethod(titleView, "setNickNameTextColor", ColorStateList.valueOf(titleTextColor))
                        }
                    }

                    // 联系人列表头部
                    if (ViewTreeUtils.equals(VTTV.ContactHeaderItem.item, view)) {
                        LogUtil.logOnlyOnce("ListViewHooker.ContactHeaderItem")
                        //企业联系人分组
                        ViewUtils.getChildView1(view, VTTV.ContactHeaderItem.treeStacks["ContactWorkItem"])?.apply {
                            if (ViewTreeUtils.equals(VTTV.ContactWorkItem.item, this)) {
                                LogUtil.logOnlyOnce("ListViewHooker.ContactWorkItem")

                                var ContactContentsItem = ViewUtils.getChildView1(this, VTTV.ContactWorkItem.treeStacks["ContactContentsItem"])
                                if (ContactContentsItem != null) {
                                    //企业联系人
                                    if (ViewTreeUtils.equals(VTTV.ContactWorkContactsItem.item, ContactContentsItem)) {
                                        LogUtil.logOnlyOnce("ListViewHooker.ContactWorkContactsItem")
                                        //  titleView
                                        ViewUtils.getChildView1(ContactContentsItem, VTTV.ContactWorkContactsItem.treeStacks["titleView"])
                                                ?.background = drawableTransparent
                                        if (isHookTextColor) {
                                            val headTextView = ViewUtils.getChildView1(ContactContentsItem, VTTV.ContactWorkContactsItem.treeStacks["headTextView"]) as TextView
                                            headTextView.setTextColor(summaryTextColor)
                                        }
                                        val tmpView = ViewUtils.getChildView1(this, VTTV.ContactWorkItem.treeStacks["ContactContentsItem1"])
                                        tmpView?.apply {
                                            ContactContentsItem = tmpView
                                        }
                                    }
                                    // 我的企业
                                    if (ViewTreeUtils.equals(VTTV.ContactMyWorkItem.item, ContactContentsItem!!)) {
                                        LogUtil.logOnlyOnce("ListViewHooker.ContactMyWorkItem")
                                        //  titleView
                                        ViewUtils.getChildView1(ContactContentsItem!!, VTTV.ContactMyWorkItem.treeStacks["titleView"])
                                                ?.background = drawableTransparent
                                        if (isHookTextColor) {
                                            val headTextView = ViewUtils.getChildView1(ContactContentsItem!!, VTTV.ContactMyWorkItem.treeStacks["headTextView"]) as TextView
                                            headTextView.setTextColor(summaryTextColor)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 发现 设置 item
                    else if (ViewTreeUtils.equals(VTTV.DiscoverViewItem.item, view)) {
//                        LogUtil.logViewStackTraces(view)
                        LogUtil.logOnlyOnce("ListViewHooker.DiscoverViewItem")
                        val iconImageView = ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["iconImageView"]) as View
                        if (iconImageView.visibility == View.VISIBLE) {
                            val titleView = ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["titleView"]) as TextView
                            if (isHookTextColor) {
                                titleView.setTextColor(titleTextColor)
                            }
                        }
//                        LogUtil.logViewStackTraces(view)
                        //group顶部横线
                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["groupBorderTop"])
                                ?.background = drawableTransparent
                        //内容分割线
                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["contentBorder"])
                                ?.background = drawableTransparent

                        //group底部横线
                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["groupBorderBottom"])
                                ?.background = drawableTransparent

                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["borderRight"])
                                ?.background = drawableTransparent



                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["unreadPointView"])
                                ?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks["unreadCountView"])
                                ?.apply {
                                    this.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                                    if (this is TextView) this.setTextColor(HookConfig.get_color_tip_num)
                                }
                    }

                    // 设置 头像
                    else if (ViewTreeUtils.equals(VTTV.SettingAvatarView.item, view)) {
                        LogUtil.logOnlyOnce("ListViewHooker.SettingAvatarView")
                        val nickNameView = ViewUtils.getChildView1(view, VTTV.SettingAvatarView.treeStacks.get("nickNameView")!!)
                        val wechatTextView = ViewUtils.getChildView1(view, VTTV.SettingAvatarView.treeStacks.get("wechatTextView")!!) as TextView
                        if (wechatTextView.text.startsWith("微信号") && isHookTextColor) {
                            wechatTextView.setTextColor(titleTextColor)
                            XposedHelpers.callMethod(nickNameView, "setTextColor", titleTextColor)
                        }
                        VTTV.SettingAvatarView.treeStacks["headView"]?.apply {
                            ViewUtils.getChildView1(view, this)?.background = defaultImageRippleDrawable
                        }
                    }

                    // (7.0.7 以上) 下拉小程序框
                    else if (HookConfig.is_hook_tab_bg && ViewTreeUtils.equals(VTTV.ActionBarItem.item, view)) {
                        LogUtil.logOnlyOnce("ListViewHooker.ActionBarItem")
                        try {
                            ViewUtils.getChildView1(view, VTTV.ActionBarItem.treeStacks["miniProgramPage"])?.apply {
                                val miniProgramPage = this as RelativeLayout

                                // old action bar
                                ViewUtils.getChildView1(miniProgramPage, VTTV.ActionBarItem.treeStacks["miniProgramPage_actionBarPage"])?.apply {
                                    val actionBarPage = this as LinearLayout
//                            val title: TextView
//                            title = ViewUtils.getChildView1(actionBarPage,
//                                    VTTV.ActionBarItem.treeStacks.get("actionBarPage_title")!!) as TextView
//
//                            title.gravity = Gravity.CENTER;
//                            title.text = HookConfig.value_mini_program_title
//                            val lp = title.layoutParams as LinearLayout.LayoutParams
//                            lp.setMargins(0, 0, 0, 0)
                                    ViewUtils.getChildView1(actionBarPage, VTTV.ActionBarItem.treeStacks["actionBarPage_addIcon"])?.apply {
                                        actionBarPage.removeView(this)
                                    }
                                    ViewUtils.getChildView1(actionBarPage, VTTV.ActionBarItem.treeStacks["actionBarPage_searchIcon"])?.apply {
                                        actionBarPage.removeView(this)
                                    }
                                }
//                            actionBarPage.removeView(title)
                                ViewUtils.getChildView1(miniProgramPage, VTTV.ActionBarItem.treeStacks["miniProgramPage_appBrandDesktopView"])?.apply {
                                    val appBrandDesktopView = this as ViewGroup

                                    // 小程序搜索框
                                    ViewUtils.getChildView1(appBrandDesktopView, VTTV.ActionBarItem.treeStacks["appBrandDesktopView_searchEditText"])?.apply {
                                        val searchEditText = this as EditText
                                        searchEditText.setBackgroundColor(Color.parseColor("#30000000"))
                                    }
                                    //  小程序字体
                                    setMiniProgramTitleColor(appBrandDesktopView)
                                    ViewUtils.getChildView1(appBrandDesktopView, VTTV.ActionBarItem.treeStacks["appBrandDesktopView_miniProgramTitle"])?.apply {
                                        setMiniProgramTitleColor(this as ViewGroup)
                                    }
                                }
//                    logXp("---------------------miniProgramPage------------------")
//                    LogUtil.logViewStackTracesXp(miniProgramPage)
//                    logXp("---------------------appBrandDesktopView------------------")
//                    LogUtil.logViewStackTracesXp(appBrandDesktopView)
//                    logXp("---------------------getChildView------------------")
//                    LogUtil.logViewStackTracesXp(ViewUtils.getChildView(appBrandDesktopView, 2, 0, 0) as ViewGroup)
                            }
                        } catch (e: ClassCastException) {
//                            LogUtil.log(e)
//                            LogUtil.logViewStackTraces(view)
                            return
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.log(e)
                }
            }
        })
    }

    fun setMiniProgramTitleColor(fatherView: ViewGroup) {
        val childCount = fatherView.childCount
        for (i in 0 until childCount) {
            val view0 = fatherView.getChildAt(i)
            if (view0 is ViewGroup) {
                val textView = findLastChildView(view0, CC.TextView.name)
                if (textView is TextView) {
                    textView.setTextColor(titleTextColor)
                }
            }
        }

    }
}