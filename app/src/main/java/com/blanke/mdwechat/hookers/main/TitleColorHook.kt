package com.blanke.mdwechat.hookers.main

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.BackgroundImageUtils
import com.blanke.mdwechat.util.ViewUtils
import com.blanke.mdwechat.ViewTreeRepoThisVersion as VTTV


object TitleColorHook {
    fun setConversationColor() {
        val titleBar = Objects.Main.actionBarInConversations
        if (titleBar == null) return

        val title = ViewUtils.getChildView1(titleBar, VTTV.ActionBarInConversationItem.treeStacks.get("title")!!) as TextView
        title.setTextColor(HookConfig.get_color_secondary)

        val chattingUILayout = ViewUtils.getParentView(titleBar, 1) as ViewGroup
//        LogUtil.logView(chattingUILayout)
        //     val ChatFooter = ViewUtils.getChildView(chattingUILayout, 2, 1) as ViewGroup
        //    ChatFooter.background = NightModeUtils.getForegroundDrawable(null)
//        (ViewUtils.getChildView(chattingUILayout, 2, 1, 0, 0) as View)
//                .background = WeChatHelper.colorPrimaryDrawable
        val chatFooterChild2 = (ViewUtils.getChildView(chattingUILayout, 2, 1, 0, 1) as View)
//        chatFooterChild2.background= ColorDrawable(Color.parseColor("#00000000"))
        BackgroundImageUtils.setBackgroundBitmap("chatFooterChild2", chatFooterChild2, AppCustomConfig.getTabBg(0), null)

        val switchButton = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 0) as ImageButton)
        switchButton.setColorFilter(HookConfig.get_color_secondary, PorterDuff.Mode.SRC_ATOP)

        val editText = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 1, 0, 0) as EditText)
        editText.background = ColorDrawable(Color.parseColor("#30000000"))
        editText.setTextColor(WeChatHelper.colorWhite)
        val talkButton = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 2) as View)
        talkButton.background = ColorDrawable(Color.parseColor("#30000000"))

        val faceButton = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 3, 0) as ImageButton)
        faceButton.setColorFilter(HookConfig.get_color_secondary, PorterDuff.Mode.SRC_ATOP)
        val addButton = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 4, 1, 0) as ImageButton)
        addButton.setColorFilter(HookConfig.get_color_secondary, PorterDuff.Mode.SRC_ATOP)
        val sendButton = (ViewUtils.getChildView(chatFooterChild2, 0, 1, 4, 1, 2) as View)
        sendButton.setBackgroundColor(Color.parseColor("#30000000"))

//        talkButton.setOnTouchListener(OnTouchListener { v, event ->
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                talkButton.background= ColorDrawable(Color.parseColor("#44AA00AA"))
//            } else if (event.action == MotionEvent.ACTION_UP) {
//                talkButton.background= ColorDrawable(Color.BLUE)
//            }
//            false
//        })


        val infoButton = ViewUtils.getChildView1(titleBar, VTTV.ActionBarInConversationItem.treeStacks.get("infoButton")!!) as ImageButton
        infoButton.setColorFilter(HookConfig.get_color_secondary, PorterDuff.Mode.SRC_ATOP)

        val goBackButton = ViewUtils.getChildView1(titleBar, VTTV.ActionBarInConversationItem.treeStacks.get("goBackButton")!!) as ImageView
        goBackButton.setColorFilter(HookConfig.get_color_secondary, PorterDuff.Mode.SRC_ATOP)
    }

}