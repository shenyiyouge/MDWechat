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
    private val isHookMiniProgram: Boolean
        get() {
            return HookConfig.is_hook_mini_program
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
                    if ((!NightModeUtils.isNightMode()) || HookConfig.is_hook_bubble_in_night_mode) {
                        // 聊天消息 item
                        if (ViewTreeUtils.equals(VTTV.ChatRightMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatRightMessageItem")
                            val chatMsgRightTextColor = HookConfig.get_hook_chat_text_color_right
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightMessageItem.treeStacks.get("msgView")!!) as View
//                    log("msgView=$msgView")
                            XposedHelpers.callMethod(msgView, "setTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setLinkTextColor", chatMsgRightTextColor)
                            XposedHelpers.callMethod(msgView, "setHintTextColor", chatMsgRightTextColor)
//                    val mText = XposedHelpers.getObjectField(msgView, "mText")
//                    log("msg right text=$mText")
                            if (HookConfig.is_hook_bubble) {
                                val bubble = WeChatHelper.getRightBubble(msgView.resources)
                                msgView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgView.setPadding(30, 25, 45, 25)
                                }
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatLeftMessageItem")
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
                            if (HookConfig.is_hook_bubble) {
                                val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                                msgView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgView.setPadding(45, 25, 30, 25)
                                }
                            }
                        }
                        // 聊天消息 audio
                        else if (ViewTreeUtils.equals(VTTV.ChatRightAudioMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatRightAudioMessageItem")
                            val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightAudioMessageItem.treeStacks.get("msgView")!!) as View
                            val msgAnimView = ViewUtils.getChildView1(view, VTTV.ChatRightAudioMessageItem.treeStacks.get("msgAnimView")!!) as View
                            if (HookConfig.is_hook_bubble) {
                                val bubble = WeChatHelper.getRightBubble(msgView.resources)
                                msgView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgView.setPadding(30, 25, 45, 25)
                                }
                                msgAnimView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgAnimView.setPadding(30, 25, 45, 25)
                                }
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftAudioMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatLeftAudioMessageItem")
                            if (HookConfig.is_hook_bubble) {
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
                        }
                        // 通话消息
                        else if (ViewTreeUtils.equals(VTTV.ChatRightCallMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatRightCallMessageItem")
                            if (HookConfig.is_hook_bubble) {
                                val msgView = ViewUtils.getChildView1(view, VTTV.ChatRightCallMessageItem.treeStacks.get("msgView")!!) as View
                                val bubble = WeChatHelper.getRightBubble(msgView.resources)
                                msgView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgView.setPadding(30, 25, 45, 25)
                                }
                            }
                        } else if (ViewTreeUtils.equals(VTTV.ChatLeftCallMessageItem.item, view)) {
                            LogUtil.logOnlyOnce("ChatLeftCallMessageItem")
                            if (HookConfig.is_hook_bubble) {
                                val msgView = ViewUtils.getChildView1(view, VTTV.ChatLeftCallMessageItem.treeStacks.get("msgView")!!) as View
                                val bubble = WeChatHelper.getLeftBubble(msgView.resources)
                                msgView.background = bubble
                                if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                    msgView.setPadding(45, 25, 30, 25)
                                }
                            }
                        }
                    }

                    // ConversationFragment 聊天列表 item sum
                    if (ViewTreeUtils.equals(VTTV.ConversationListViewItem.item, view)) {
                        LogUtil.logOnlyOnce("ConversationListViewItem")
                        try {
                            view.background.alpha = HookConfig.get_hook_conversation_background_alpha
                        } catch (e: Exception) {
                        }
                        val chatNameView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks.get("chatNameView")!!)
                        val chatTimeView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks.get("chatTimeView")!!)
                        val recentMsgView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks.get("recentMsgView")!!)
                        val unreadCountView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks.get("unreadCountView")!!) as TextView
                        val unreadView = ViewUtils.getChildView1(view, VTTV.ConversationListViewItem.treeStacks.get("unreadView")!!) as ImageView
//                    LogUtil.logXp("chatNameView=$chatNameView,chatTimeView=$chatTimeView,recentMsgView=$recentMsgView")
                        if (isHookTextColor) {
                            XposedHelpers.callMethod(chatNameView, "setTextColor", titleTextColor)
                            XposedHelpers.callMethod(chatTimeView, "setTextColor", summaryTextColor)
                            XposedHelpers.callMethod(recentMsgView, "setTextColor", summaryTextColor)
                        }
                        unreadCountView.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        unreadView.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        val contentView = ViewUtils.getChildView(view, 1) as ViewGroup
                        contentView.background = drawableTransparent
                        return
                    }

                    view.background = defaultImageRippleDrawable
                    // 联系人列表 sum
                    if (ViewTreeUtils.equals(VTTV.ContactListViewItem.item, view)) {
                        LogUtil.logOnlyOnce("ContactListViewItem")
                        // 标题下面的线
                        if (VTTV.ContactListViewItem.treeStacks.get("headerView") != null) {
                            ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks.get("headerView")!!)
                                    ?.background = drawableTransparent
                        }
                        //内容下面的线 innerView
                        ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks.get("innerView")!!)
                                ?.background = drawableTransparent

                        ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks.get("contentView")!!)
                                ?.background = drawableTransparent

                        val titleView = ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks.get("titleView")!!)
                        titleView?.background = drawableTransparent
                        if (isHookTextColor) {
                            val headTextView = ViewUtils.getChildView1(view, VTTV.ContactListViewItem.treeStacks.get("headTextView")!!) as TextView
                            headTextView.setTextColor(summaryTextColor)
                            XposedHelpers.callMethod(titleView, "setNickNameTextColor", ColorStateList.valueOf(titleTextColor))
                        }
                    }

                    // 发现 设置 item sum
                    else if (ViewTreeUtils.equals(VTTV.DiscoverViewItem.item, view)) {
                        LogUtil.logOnlyOnce("DiscoverViewItem")
                        val iconImageView = ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks.get("iconImageView")!!) as View
                        if (iconImageView.visibility == View.VISIBLE) {
                            val titleView = ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks.get("titleView")!!) as TextView
                            if (isHookTextColor) {
                                titleView.setTextColor(titleTextColor)
                            }
                        }

                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks.get("unreadPointView")!!)
                                ?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        ViewUtils.getChildView1(view, VTTV.DiscoverViewItem.treeStacks.get("unreadCountView")!!)
                                ?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                    }

                    // 设置 头像 sum
                    else if (ViewTreeUtils.equals(VTTV.SettingAvatarView.item, view)) {
                        LogUtil.logOnlyOnce("SettingAvatarView")
                        val nickNameView = ViewUtils.getChildView1(view, VTTV.SettingAvatarView.treeStacks.get("nickNameView")!!)
                        val wechatTextView = ViewUtils.getChildView1(view, VTTV.SettingAvatarView.treeStacks.get("wechatTextView")!!) as TextView
                        if (wechatTextView.text.startsWith("微信号") && isHookTextColor) {
                            wechatTextView.setTextColor(titleTextColor)
                            XposedHelpers.callMethod(nickNameView, "setTextColor", titleTextColor)
                        }
                        ViewUtils.getChildView1(view, VTTV.SettingAvatarView.treeStacks.get("headView")!!)
                                ?.background = defaultImageRippleDrawable
                    }

                    // (7.0.7 以上) 下拉小程序框
                    else if (HookConfig.is_hook_tab_bg && ViewTreeUtils.equals(VTTV.ActionBarItem.item, view)) {
                        LogUtil.logOnlyOnce("ActionBarItem")
                        try {
                            val miniProgramPage = ViewUtils.getChildView1(view, VTTV.ActionBarItem.treeStacks.get("miniProgramPage")!!) as RelativeLayout
                            miniProgramPage.visibility
                            // old action bar
                            val actionBarPage = ViewUtils.getChildView1(miniProgramPage,
                                    VTTV.ActionBarItem.treeStacks.get("miniProgramPage_actionBarPage")!!) as LinearLayout
//                            val title: TextView
//                            title = ViewUtils.getChildView1(actionBarPage,
//                                    VTTV.ActionBarItem.treeStacks.get("actionBarPage_title")!!) as TextView
//
//                            title.gravity = Gravity.CENTER;
//                            title.text = HookConfig.value_mini_program_title
//                            val lp = title.layoutParams as LinearLayout.LayoutParams
//                            lp.setMargins(0, 0, 0, 0)
                            actionBarPage.removeView(ViewUtils.getChildView1(actionBarPage,
                                    VTTV.ActionBarItem.treeStacks.get("actionBarPage_addIcon")!!))
                            actionBarPage.removeView(ViewUtils.getChildView1(actionBarPage,
                                    VTTV.ActionBarItem.treeStacks.get("actionBarPage_searchIcon")!!))
//                            actionBarPage.removeView(title)

                            val appBrandDesktopView = ViewUtils.getChildView1(miniProgramPage,
                                    VTTV.ActionBarItem.treeStacks.get("miniProgramPage_appBrandDesktopView")!!) as ViewGroup
                            //小程序搜索框
                            val searchEditText = ViewUtils.getChildView1(appBrandDesktopView,
                                    VTTV.ActionBarItem.treeStacks.get("appBrandDesktopView_searchEditText")!!) as EditText
                            searchEditText.setBackgroundColor(Color.parseColor("#30000000"))
//                    小程序字体
                            setMiniProgramTitleColor(appBrandDesktopView)
                            setMiniProgramTitleColor(ViewUtils.getChildView1(appBrandDesktopView,
                                    VTTV.ActionBarItem.treeStacks.get("appBrandDesktopView_miniProgramTitle")!!) as ViewGroup)
//                    logXp("---------------------miniProgramPage------------------")
//                    LogUtil.logViewStackTracesXp(miniProgramPage)
//                    logXp("---------------------appBrandDesktopView------------------")
//                    LogUtil.logViewStackTracesXp(appBrandDesktopView)
//                    logXp("---------------------getChildView------------------")
//                    LogUtil.logViewStackTracesXp(ViewUtils.getChildView(appBrandDesktopView, 2, 0, 0) as ViewGroup)

                        } catch (e: ClassCastException) {
                            LogUtil.log(e)
                            LogUtil.logViewStackTraces(view)
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