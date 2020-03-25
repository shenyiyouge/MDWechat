package com.blanke.mdwechat.hookers

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blanke.mdwechat.*
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.drawableTransparent
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.*
import com.blanke.mdwechat.util.ViewUtils.findLastChildView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.lang.Exception

object ListViewHooker : HookerProvider {
    private val excludeContext = arrayOf("com.tencent.mm.plugin.mall.ui.MallIndexUI")

    private var titleTextColor = Color.BLACK
        get() {
            return NightModeUtils.getTitleTextColor()
        }
    private var summaryTextColor = Color.BLACK
        get() {
            return NightModeUtils.getContentTextColor()
        }

    private var isHookTextColor = false
        get() {
            return HookConfig.is_hook_main_textcolor || NightModeUtils.isNightMode()
        }
    private var isHookMiniProgram = false
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
//                logXp("--------------------")
//                logXp(WechatGlobal.wxVersion.toString())
//                logXp("context=" + view.context)
//                LogUtil.logViewStackTracesXp(view)
//                logParentViewXp(view, 10)
//                logXp("--------------------")

                    // 按照使用频率重排序
                    if (!NightModeUtils.isNightMode()) {// 夜间模式不改变气泡
                        // 聊天消息 item
                        if (ViewTreeUtils.equals(ViewTreeRepo.ChatRightMessageItem, view)) {
                            val chatMsgRightTextColor = HookConfig.get_hook_chat_text_color_right
                            val msgView: View?
                            if (WechatGlobal.wxVersion!! >= Version("7.0.7")) {
                                msgView = ViewUtils.getChildView(view, 3, 1, 1, 3, 0) as View
                            } else {
                                msgView = ViewUtils.getChildView(view, 3, 1, 1, 3) as View
                            }
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
                        } else if (ViewTreeUtils.equals(ViewTreeRepo.ChatLeftMessageItem, view)) {
                            val chatMsgLeftTextColor = HookConfig.get_hook_chat_text_color_left
                            val msgView = ViewUtils.getChildView(view, 3, 1, 1) as View
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
                        else if (ViewTreeUtils.equals(ViewTreeRepo.ChatRightAudioMessageItem, view)) {
                            val msgView = ViewUtils.getChildView(view, 3, 5, 0, 0) as View
                            val msgAnimView = ViewUtils.getChildView(view, 3, 5, 0, 1) as View
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
                        } else if (ViewTreeUtils.equals(ViewTreeRepo.ChatLeftAudioMessageItem, view)) {
                            val msgView = ViewUtils.getChildView(view, 3, 1, 3, 0, 0) as View
                            val msgAnimView = ViewUtils.getChildView(view, 3, 1, 3, 0, 1) as View
                            if (HookConfig.is_hook_bubble) {
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
                    }  //

                    // ConversationFragment 聊天列表 item 7.0.11
                    if (ViewTreeUtils.equals(ViewTreeRepo.ConversationListViewItem_7_0_10, view)) {
                        view.background.alpha = 120
//                    LogUtil.logXp("=====================")
//                    LogUtil.logViewStackTracesXp(ViewUtils.getParentViewSafe(view, 15))
//                    LogUtil.logStackTraceXp()
//                    LogUtil.logXp("=====================")
                        val chatNameView = ViewUtils.getChildView(view, 1, 0, 0, 0)
                        val chatTimeView = ViewUtils.getChildView(view, 1, 0, 1)
                        val recentMsgView = ViewUtils.getChildView(view, 1, 1, 0, 1)
                        val unreadCountView = ViewUtils.getChildView(view, 0, 1) as TextView
                        val unreadView = ViewUtils.getChildView(view, 0, 2) as ImageView

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
                    // ConversationFragment 聊天列表 item
                    else if (ViewTreeUtils.equals(ViewTreeRepo.ConversationListViewItem, view)) {
                        view.background.alpha = 120
                        val chatNameView = ViewUtils.getChildView(view, 1, 0, 0, 0)
                        val chatTimeView = ViewUtils.getChildView(view, 1, 0, 1)
                        val recentMsgView = ViewUtils.getChildView(view, 1, 1, 0, 1)
                        val unreadCountView = ViewUtils.getChildView(view, 0, 1) as TextView
                        val unreadView = ViewUtils.getChildView(view, 0, 2) as ImageView

//                    LogUtil.log("chatNameView=$chatNameView,chatTimeView=$chatTimeView,recentMsgView=$recentMsgView")
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
                    // 联系人列表 7.0.0
                    if (ViewTreeUtils.equals(ViewTreeRepo.ContactListViewItem_7_0_0, view)) {
                        val headLayout = ViewUtils.getChildView(view, 0) as ViewGroup
                        headLayout.background = drawableTransparent
                        val headTextView = ViewUtils.getChildView(headLayout, 0) as TextView
                        val contentLayout = ViewUtils.getChildView(view, 1) as ViewGroup
                        contentLayout.background = drawableTransparent
                        val titleView = ViewUtils.getChildView(contentLayout, 0, 3, 1)
                        ViewUtils.getChildView(contentLayout, 0)?.background = drawableTransparent
                        titleView?.background = drawableTransparent
                        if (isHookTextColor) {
                            headTextView.setTextColor(summaryTextColor)
                            XposedHelpers.callMethod(titleView, "setNickNameTextColor", ColorStateList.valueOf(titleTextColor))
                        }
                    }
                    // 联系人列表
                    else if (ViewTreeUtils.equals(ViewTreeRepo.ContactListViewItem, view)) {
                        val headTextView = ViewUtils.getChildView(view, 0) as TextView
                        val titleView = ViewUtils.getChildView(view, 1, 0, 3)
//                    log("headTextView=$headTextView,titleView=$titleView")
                        if (isHookTextColor) {
                            headTextView.setTextColor(summaryTextColor)
                            XposedHelpers.callMethod(titleView, "setNickNameTextColor", ColorStateList.valueOf(titleTextColor))
                        }
                        // 修改背景
                        val contentView = ViewUtils.getChildView(view, 1) as ViewGroup
                        contentView.background = defaultImageRippleDrawable
                        val innerView = ViewUtils.getChildView(contentView, 0) as View
                        // 去掉分割线
                        innerView.background = drawableTransparent
                    }

                    // 发现 设置 item 7.0.11
                    else if (ViewTreeUtils.equals(ViewTreeRepo.DiscoverViewItem_7_0_10, view)) {
                        val iconImageView = ViewUtils.getChildView(view, 1, 0, 0, 0, 0) as View
                        if (iconImageView.visibility == View.VISIBLE) {
                            val titleView = ViewUtils.getChildView(view, 1, 0, 0, 0, 1, 0, 0, 0) as TextView
                            if (isHookTextColor) {
                                titleView.setTextColor(titleTextColor)
                            }
                        }
                        val unreadPointView = ViewUtils.getChildView(view, 1, 0, 0, 0, 1, 2, 1)
                        val unreadCountView = ViewUtils.getChildView(view, 1, 0, 0, 0, 1, 0, 0, 1)
                        unreadPointView?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        unreadCountView?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                    }
                    // 发现 设置 item 7.0.0
                    else if (ViewTreeUtils.equals(ViewTreeRepo.DiscoverViewItem_7_0_0, view)) {
                        val iconImageView = ViewUtils.getChildView(view, 0, 0, 0, 0) as View
                        if (iconImageView.visibility == View.VISIBLE) {
                            val titleView = ViewUtils.getChildView(view, 0, 0, 0, 1, 0, 0, 0) as TextView
                            if (isHookTextColor) {
                                titleView.setTextColor(titleTextColor)
                            }
                        }
                        val unreadPointView = ViewUtils.getChildView(view, 0, 0, 0, 1, 2, 1)
                        val unreadCountView = ViewUtils.getChildView(view, 0, 0, 0, 1, 0, 0, 1)
                        unreadPointView?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                        unreadCountView?.backgroundTintList = ColorStateList.valueOf(NightModeUtils.colorTip)
                    }
                    // 发现 设置 item
                    else if (ViewTreeUtils.equals(ViewTreeRepo.DiscoverViewItem, view)) {
                        val iconImageView = ViewUtils.getChildView(view, 0, 0, 0, 0) as View
                        if (iconImageView.visibility == View.VISIBLE) {
                            val titleView = ViewUtils.getChildView(view, 0, 0, 0, 1, 0, 0) as TextView
                            if (isHookTextColor) {
                                titleView.setTextColor(titleTextColor)
                            }
                        }
                    }

                    // 设置 头像 7.0.12
                    else if (ViewTreeUtils.equals(ViewTreeRepo.SettingAvatarView_7_0_12, view)) {
                        ViewUtils.getChildView(view, 0)?.background = defaultImageRippleDrawable
                        val nickNameView = ViewUtils.getChildView(view, 2, 0, 1, 0, 0)
                        val wechatTextView = ViewUtils.getChildView(view, 2, 0, 1, 1, 0) as TextView
                        if (wechatTextView.text.startsWith("微信号") && isHookTextColor) {
                            wechatTextView.setTextColor(titleTextColor)
                            XposedHelpers.callMethod(nickNameView, "setTextColor", titleTextColor)
                        }
                    }
                    // 设置 头像 7.0.0
                    else if (ViewTreeUtils.equals(ViewTreeRepo.SettingAvatarView_7_0_0, view)) {
                        ViewUtils.getChildView(view, 0)?.background = defaultImageRippleDrawable
                        val nickNameView = ViewUtils.getChildView(view, 1, 0, 1, 0)
                        val wechatTextView = ViewUtils.getChildView(view, 1, 0, 1, 1) as TextView
                        if (wechatTextView.text.startsWith("微信号") && isHookTextColor) {
                            wechatTextView.setTextColor(titleTextColor)
                            XposedHelpers.callMethod(nickNameView, "setTextColor", titleTextColor)
                        }
                    }
                    // 设置 头像
                    else if (ViewTreeUtils.equals(ViewTreeRepo.SettingAvatarView, view)) {
                        val nickNameView = ViewUtils.getChildView(view, 0, 1, 0)
                        val wechatTextView = ViewUtils.getChildView(view, 0, 1, 1) as TextView
                        if (wechatTextView.text.startsWith("微信号") && isHookTextColor) {
                            wechatTextView.setTextColor(titleTextColor)
                            XposedHelpers.callMethod(nickNameView, "setTextColor", titleTextColor)
                        }
                    }
                    // (7.0.7 以上) 下拉小程序框
                    else if (ViewTreeUtils.equals(ViewTreeRepo.ActionBarItem, view)) {
//                    LogUtil.logXp("-------------------")
//                    LogUtil.logViewStackTracesXp(view)
                        val miniProgramPage = ViewUtils.getChildView(view, 0) as RelativeLayout

                        // old action bar
                        val ActionBarPage = ViewUtils.getChildView(miniProgramPage, 1, 1) as LinearLayout
                        val title = ViewUtils.getChildView(ActionBarPage, 0) as TextView
                        title.gravity = Gravity.CENTER;
                        title.text = HookConfig.value_mini_program_title
                        val lp = title.layoutParams as LinearLayout.LayoutParams
                        lp.setMargins(0, 0, 0, 0)
                        ActionBarPage.removeView(ViewUtils.getChildView(ActionBarPage, 1))
                        ActionBarPage.removeView(ViewUtils.getChildView(ActionBarPage, 2))

                        val appBrandDesktopView = ViewUtils.getChildView(miniProgramPage, 0, 0, 2, 0) as ViewGroup
                        //小程序搜索框
                        val searchEditText = ViewUtils.getChildView(appBrandDesktopView, 0, 0) as EditText
                        searchEditText.setBackgroundColor(Color.parseColor("#30000000"))
//                    小程序字体
                        setMiniProgramTitleColor(appBrandDesktopView)
                        setMiniProgramTitleColor(ViewUtils.getChildView(appBrandDesktopView, 2, 0, 0) as ViewGroup)
//                    logXp("---------------------miniProgramPage------------------")
//                    LogUtil.logViewStackTracesXp(miniProgramPage)
//                    logXp("---------------------appBrandDesktopView------------------")
//                    LogUtil.logViewStackTracesXp(appBrandDesktopView)
//                    logXp("---------------------getChildView------------------")
//                    LogUtil.logViewStackTracesXp(ViewUtils.getChildView(appBrandDesktopView, 2, 0, 0) as ViewGroup)
                    }
                } catch (e: Exception) {
                    LogUtil.logXp(e)
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