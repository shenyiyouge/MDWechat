package com.blanke.mdwechat.hookers

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.ActionBarContainer
import com.blanke.mdwechat.Methods
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.BackgroundImageUtils
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
//            override fun beforeHookedMethod(param: MethodHookParam) {
////                return
//                val drawable = param.args[0]
//                var needHook = true
//                val actionBar = param.thisObject as ViewGroup
////                LogUtil.log("====================")
////                LogUtil.logView(actionBar)
////                LogUtil.logViewStackTraces(actionBar)
////                LogUtil.log("====================")
//                if (drawable is ColorDrawable) {
//                    if (drawable.color == Color.parseColor("#F2F2F2")
//                            || drawable.color == Color.parseColor("#FFFAFAFA")
//                            || drawable.color == Color.TRANSPARENT) {
//                        needHook = false
//                    }
//                    if (WechatGlobal.wxVersion!! >= Version("7.0.0")) {
//                        if (actionBar.context::class.java.name == Classes.LauncherUI.name) {
//                            needHook = true
//                        }
//                    }
//                    if (drawable.color == colorPrimaryDrawable.color) {
//                        needHook = false
//                    }
//                    if (needHook) {
//                        param.args[0] = colorPrimaryDrawable
//                    }
//                }
//                val init_elevation = XposedHelpers.getAdditionalInstanceField(actionBar, "init_elevation")
//                if (init_elevation == null) {
//                    actionBar.elevation = 5F
//                    XposedHelpers.setAdditionalInstanceField(actionBar, "init_elevation", true)
//                }
//            }

            override fun afterHookedMethod(param: MethodHookParam) {
                val actionBar = param.thisObject as View
                Objects.Main.actionBar = actionBar
                if (!HookConfig.is_hook_hide_actionbar) {
                    actionBar.background = NightModeUtils.getForegroundDrawable(BackgroundImageUtils.getActionBarBitmap(actionBar.measuredHeight, Objects.Main.pagePosition))
                } else {
                    actionBar.background = ColorDrawable(HookConfig.get_color_primary)
                }
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
                    Objects.Main.actionBar = view
                    if (!HookConfig.is_hook_hide_actionbar) {
                        view.background = NightModeUtils.getForegroundDrawable(BackgroundImageUtils.getActionBarBitmap(view.measuredHeight, Objects.Main.pagePosition))
                    } else {
                        view.background = ColorDrawable(HookConfig.get_color_primary)
                    }
//                    view.background = NightModeUtils.getForegroundDrawable(getActionBarBitmap(view.measuredHeight, Objects.Main.pagePosition))
                }
            }
        })
    }

    private val mainPageActionBarTitleTextColor = Hooker {
        return@Hooker
        try {
//            LogUtil.logXp("\n\n\n\n\nactionBarTitleTextColor")
            val methods = Methods.HomeUI_setActionBarColor
//            LogUtil.logXp("\n\n\n\n\nmethods$methods")
            methods.forEach {
                LogUtil.log("查找 方法中: ${Classes.HomeUI.name}.${it}")
                XposedBridge.hookAllMethods(Classes.HomeUI, it, object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val r0 = param.args[0] as Float
//                        if (!r0.isNaN()) {
//                        }
                        LogUtil.log("顶栏字体颜色修改===========\n${Classes.HomeUI.name}.${it}(\"${param.args[0]}\"\"${param.args[1]}\"\"${param.args[2]}\") called.")
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