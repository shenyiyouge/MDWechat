package com.blanke.mdwechat.hookers

import android.content.res.Configuration
import android.content.res.Resources
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object NightModeHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(getNightModeConfiguration)
    }

    private val getNightModeConfiguration = Hooker {
        XposedHelpers.findAndHookMethod(Resources::class.java, "getConfiguration", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                val configuration = param.result as Configuration
                val uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                NightModeUtils.setNightMode(uiMode == Configuration.UI_MODE_NIGHT_YES)
            }
        })
    }
    private val getNightModeConfiguration1 = Hooker {
        try {
            XposedHelpers.findAndHookMethod(Classes.CustomViewPager, "isGutterDrag", object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
//                    val configuration = param.result as Configuration
                    param.result = false
                }
            })
        } catch (e: Exception) {
            LogUtil.log(e)
        }
    }
//    private val Hook5 = Hooker {
//        try {
////        深色模式实现 7010
//            XposedBridge.hookAllMethods(Classes.findClass("com.tencent.mm.cb.h"), "a", object : XC_MethodHook() {
//                @Throws(Throwable::class)
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    val asd = LogUtil.logStackTraceXp("com.tencent.mm.ui.MMFragmentActivity")
//                    LogUtil.logXp("\n\n\nasd=$asd")
//                    if (asd) return
//                    val para0 = param.args[0] as Configuration
//                    val para1 = param.args[1] as Resources
//                    LogUtil.logXp("\n\n\nresumeHook5(\"${para0.uiMode}\",\"${para1.configuration.uiMode}\") called.")
////                    para0.uiMode = Configuration.UI_MODE_NIGHT_YES
//                    para1.configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
//                }
//            })
//        } catch (e: Exception) {
//            LogUtil.logXp("=====================")
//            LogUtil.logXp(e)
//        }
//    }

}