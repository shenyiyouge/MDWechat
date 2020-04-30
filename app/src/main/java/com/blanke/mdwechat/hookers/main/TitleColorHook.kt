package com.blanke.mdwechat.hookers.main

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.config.AppCustomConfig.getChatBg
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.BackgroundImageUtils
import com.blanke.mdwechat.util.NightModeUtils
import com.blanke.mdwechat.util.ViewTreeUtils
import com.blanke.mdwechat.util.ViewUtils
import com.blanke.mdwechat.util.ViewUtils.measureHeight
import com.blanke.mdwechat.ViewTreeRepoThisVersion as VTTV


object TitleColorHook {
    var actionBarBottom = 0
    private val transparentDark = Color.parseColor("#30000000")
    private val transparentLight = Color.parseColor("#30EEEEEE")
    private val isBgLight = !HookConfig.is_hook_scheme_dark && !NightModeUtils.isNightMode()
    private val transparentBackground = if (isBgLight) transparentLight else transparentDark
    private var footerLocation = mutableListOf(-1, -1)

    fun setConversationColor(actionBar: View) {
        val title = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInConversationItem.treeStacks.getValue("title")) as TextView
        title.setTextColor(NightModeUtils.colorSecondary)
        val infoButton = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInConversationItem.treeStacks.getValue("infoButton")) as ImageButton
        infoButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)
        val goBackButton = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInConversationItem.treeStacks.getValue("goBackButton")) as ImageView
        goBackButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)

        val chattingUILayout = ViewUtils.getParentView(actionBar, 1) as ViewGroup
        if (ViewTreeUtils.equals(VTTV.ChattingUILayoutItem.item, chattingUILayout)) {
            val chattingScrollLayoutItem = ViewUtils.getChildView1(chattingUILayout,
                    VTTV.ChattingUILayoutItem.treeStacks.getValue("ChattingScrollLayoutItem")) as ViewGroup
            setConversationFooterColor(chattingScrollLayoutItem, VTTV.ChattingScrollLayoutItem.treeStacks)
        }
    }

    fun setConversationInSearchColor(actionBar: View) {
        val title = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInSearchConversationItem.treeStacks.getValue("title")) as TextView
        title.setTextColor(NightModeUtils.colorSecondary)
        val infoButton = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInSearchConversationItem.treeStacks.getValue("infoButton")) as ImageButton
        infoButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)
        val goBackButton = ViewUtils.getChildView1(actionBar, VTTV.ActionBarInSearchConversationItem.treeStacks.getValue("goBackButton")) as ImageView
        goBackButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)

        val chattingUILayout = ViewUtils.getParentView(actionBar, 1) as ViewGroup
        if (ViewTreeUtils.equals(VTTV.ChattingUILayoutInSearchItem.item, chattingUILayout)) {
            val chattingScrollLayoutItem = ViewUtils.getChildView1(chattingUILayout,
                    VTTV.ChattingUILayoutInSearchItem.treeStacks.getValue("ChattingScrollLayoutItem")) as ViewGroup
            setConversationFooterColor(chattingScrollLayoutItem, VTTV.ChattingScrollLayoutItem.treeStacks)
        }
    }

    private fun setConversationFooterColor(ChattingScrollLayoutItem: ViewGroup, treeStacks: Map<String, IntArray>) {
        //判断是否为公众号页面
        if ((ChattingScrollLayoutItem.childCount >= 3) &&
                (ChattingScrollLayoutItem.getChildAt(2)::class.java.name == "com.tencent.mm.ui.chatting.ChatFooterCustom")) {
            //底栏
            val footer = ChattingScrollLayoutItem.getChildAt(2) as View
            val location = BackgroundImageUtils.setBackgroundBitmap("公众号 footer", footer, getChatBg(), null)
            if ((footerLocation[1] < 0) && (location[1] > 0)) {
                footerLocation[0] = location[0]
                footerLocation[1] = location[1]
            }

        }


        // 去除默认聊天背景
        val bgGroup = ViewUtils.getChildView1(ChattingScrollLayoutItem,
                treeStacks.getValue("bgGroup")) as ViewGroup
        val chattingBgShade = ViewUtils.getChildView1(ChattingScrollLayoutItem,
                treeStacks.getValue("chattingBgShade")) as View
        if (HookConfig.is_hook_scheme_dark || !HookConfig.is_hook_night_mode) chattingBgShade.setBackgroundColor(0)

//        自定义聊天背景
        val context = bgGroup.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val view = FrameLayout(context)
        params.height = HookConfig.value_resolution[1] - actionBarBottom
        bgGroup.addView(view, 1, params)
        view.background = NightModeUtils.getBackgroundDrawable(BackgroundImageUtils.cutBitmap("ChattingImageBGView",
                getChatBg(), actionBarBottom, params.height))

        //底栏
        val chatFooterChild2 = ViewUtils.getChildView1(ChattingScrollLayoutItem,
                treeStacks.getValue("chatFooterChild2")) as View
        if (footerLocation[1] > 0) {
            chatFooterChild2.background = NightModeUtils.getBackgroundDrawable(BackgroundImageUtils.cutBitmap("chatFooterChild2",
                    getChatBg(), footerLocation[0], measureHeight(chatFooterChild2)))
        } else {
            BackgroundImageUtils.setBackgroundBitmap("chatFooterChild2", chatFooterChild2, getChatBg(), null)
        }
        //语音打字切换
        val switchButton = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_switchButton")) as ImageButton
        switchButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)

        val editText = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_editText")) as EditText
        editText.background = ColorDrawable(transparentBackground)
        editText.setTextColor(NightModeUtils.colorSecondary)
//        editText.setTextColor(if (isBgLight) WeChatHelper.colorDark else WeChatHelper.colorWhite)
        val talkButton = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_talkButton")) as View
        talkButton.background = ColorDrawable(transparentBackground)

        //表情
        val faceButton = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_faceButton")) as ImageButton
        faceButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)
        val addButton = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_addButton")) as ImageButton
        addButton.setColorFilter(NightModeUtils.colorSecondary, PorterDuff.Mode.SRC_ATOP)
        //发送
        val sendButton = ViewUtils.getChildView1(chatFooterChild2,
                treeStacks.getValue("chatFooterChild2_sendButton")) as Button
        sendButton.setBackgroundColor(transparentBackground)
        sendButton.setTextColor(NightModeUtils.colorSecondary)

    }
}