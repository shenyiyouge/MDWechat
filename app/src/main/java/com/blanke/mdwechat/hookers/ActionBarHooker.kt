package com.blanke.mdwechat.hookers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.ActionBarContainer
import com.blanke.mdwechat.Methods
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.WeChatHelper.colorPrimaryDrawable
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object ActionBarHooker : HookerProvider {

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(actionBarHooker
                , actionBarTitleTextColor
        )
    }

    private val actionBarHooker = Hooker {
        findAndHookMethod(ActionBarContainer, "setPrimaryBackground", Drawable::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val drawable = param.args[0]
                var needHook = true
                val actionBar = param.thisObject as ViewGroup
//                LogUtil.logXp("====================")
//                LogUtil.logViewXp(actionBar)
//                LogUtil.logStackTraceXp()
//                LogUtil.logViewStackTracesXp(ViewUtils.getParentViewSafe(actionBar,111))
//                LogUtil.logXp("====================")
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
            }
        })
    }
    private val actionBarTitleTextColor = Hooker {
        try {
//            LogUtil.logXp("\n\n\n\n\nactionBarTitleTextColor")
            val methods = Methods.HomeUI_setActionBarColor
//            LogUtil.logXp("\n\n\n\n\nmethods$methods")
            methods.forEach {
//                if(it=="a")return@forEach
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
            LogUtil.logXp("=====================")
            LogUtil.logXp(e)
        }
    }
}