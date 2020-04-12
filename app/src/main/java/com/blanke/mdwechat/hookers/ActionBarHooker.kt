package com.blanke.mdwechat.hookers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.blanke.mdwechat.*
import com.blanke.mdwechat.Classes.ActionBarContainer
import com.blanke.mdwechat.WeChatHelper.colorPrimaryDrawable
import com.blanke.mdwechat.config.AppCustomConfig.getActionBarBitmap
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object ActionBarHooker : HookerProvider {

    override fun provideStaticHookers(): List<Hooker>? {
        val list = mutableListOf(actionBarHooker)
        if (!HookConfig.is_hook_hide_actionbar) {
            list.add(mainPageActionBarHooker)
            if (HookConfig.is_hook_actionbar_color) list.add(mainPageActionBarTitleTextColor)
        }
        return list
    }

    private val actionBarHooker = Hooker {
        XposedHelpers.findAndHookMethod(ActionBarContainer, "setPrimaryBackground", Drawable::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
//                return
                val drawable = param.args[0]
                var needHook = true
                val actionBar = param.thisObject as ViewGroup
//                LogUtil.log("====================")
//                LogUtil.logView(actionBar)
//                LogUtil.logViewStackTraces(actionBar)
//                LogUtil.log("====================")
                if (drawable is ColorDrawable) {
                    if (drawable.color == Color.parseColor("#F2F2F2")
                            || drawable.color == Color.parseColor("#FFFAFAFA")
                            || drawable.color == Color.TRANSPARENT) {
                        needHook = false
                    }
                    if (WechatGlobal.wxVersion!! >= Version("7.0.0")) {
                        if (actionBar.context::class.java.name == Classes.LauncherUI.name) {
                            needHook = true
                        }
                    }
                    if (drawable.color == colorPrimaryDrawable.color) {
                        needHook = false
                    }
                    if (needHook) {
                        param.args[0] = colorPrimaryDrawable
                    }
                }
                val init_elevation = XposedHelpers.getAdditionalInstanceField(actionBar, "init_elevation")
                if (init_elevation == null) {
                    actionBar.elevation = 5F
                    XposedHelpers.setAdditionalInstanceField(actionBar, "init_elevation", true)
                }
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                val actionBar = param.thisObject as View
                if (!HookConfig.is_hook_hide_actionbar)
                    actionBar.background = NightModeUtils.getBackgroundDrawable(getActionBarBitmap(actionBar))
            }
        })
    }

    //主界面下滑之后需要保持ActionBar不变
    private val mainPageActionBarHooker = Hooker {
        XposedBridge.hookAllMethods(CC.View, "setBackgroundColor", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                val clazz = view::class.java.name
                if (clazz == ActionBarContainer.name) {
//                    param.result = NightModeUtils.colorPrimary
                    view.background = NightModeUtils.getBackgroundDrawable(getActionBarBitmap(view))
                }
            }
        })
    }

    private val mainPageActionBarTitleTextColor = Hooker {
        try {
//            LogUtil.logXp("\n\n\n\n\nactionBarTitleTextColor")
            val methods = Methods.HomeUI_setActionBarColor
//            LogUtil.logXp("\n\n\n\n\nmethods$methods")
            methods.forEach {
                XposedHelpers.findAndHookMethod(Classes.HomeUI, it,
                        Float::class.java, Int::class.java, Int::class.java, object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
//                        LogUtil.logXp("${Classes.HomeUI.name}.${it}(\"$ param.args[0]\"\"$ param.args[1]\"\"$param.args[2]\") called.")
                        param.args[1] = NightModeUtils.colorSecondary
                        param.args[2] = NightModeUtils.colorSecondary
//                        LogUtil.logStackTraceXp()
                    }
                })
            }
        } catch (e: Exception) {
            LogUtil.log(e)
        }
    }
}