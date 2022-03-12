package com.blanke.mdwechat.hookers

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.blanke.mdwechat.*
import com.blanke.mdwechat.Fields.ContactFragment_mListView
import com.blanke.mdwechat.WeChatHelper.drawableTransparent
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.hookers.main.BackgroundImageHook
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.ViewTreeUtils
import com.blanke.mdwechat.util.ViewUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import com.blanke.mdwechat.ViewTreeRepoThisVersion as VTTV

object ContactHooker : HookerProvider {
    const val keyInit = "key_init"
    var backgroundInited = false

    //todo 联系人列表头部的应该有更好的hook点，这里先从viewpager hook，因为第一次没有初始化完所以计数10次。
    var headInitCount = 0


    override fun provideStaticHookers(): List<Hooker>? {
        return if (WechatGlobal.wxVersion!! < Version("8.0.14")) {
            listOf(resumeHook)
        } else {
            listOf(wxViewPagerHook, backgroundColorResetHook, contactItermsHook)
        }
    }

    private val wxViewPagerHook = Hooker {
        try {
            XposedHelpers.findAndHookMethod(Classes.WxViewPager, Methods.WxViewPager_onLayout.name, CC.Boolean, CC.Int, CC.Int, CC.Int, CC.Int, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val fragment = param?.thisObject ?: return
                    val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                    if (isInit != null) {
                        LogUtil.log("ContactFragment 已经hook过")
                        return
                    }
                    init(fragment)
                }

                private fun init(fragment: Any) {
                    if (fragment is ViewGroup && fragment.childCount == 4 && fragment.getChildAt(1) is View) {
                        val contactView = fragment.getChildAt(1) as ViewGroup
                        if (ViewTreeUtils.equals(VTTV.ContactLayoutListenerViewItem.item, contactView)) {
                            //背景
                            if (!backgroundInited) {
                                //背景遮罩
                                VTTV.ContactLayoutListenerViewItem.treeStacks["backgroundMask"]?.apply {
                                    val backgroundITransparent = ViewUtils.getChildView1(contactView, this) as View
                                    backgroundITransparent.setBackgroundColor(Color.TRANSPARENT)
                                }
                                //背景
                                if (HookConfig.is_hook_tab_bg) {
                                    VTTV.ContactLayoutListenerViewItem.treeStacks["backgroundImage"]?.apply {
                                        val backgroundImage = ViewUtils.getChildView1(contactView, this) as View
//                                                //不写这一句背景会变回白色,WTF?(已解决)
//                                                backgroundImage.setBackgroundColor(Color.TRANSPARENT)
                                        BackgroundImageHook.setContactBitmap(backgroundImage)
                                    }
                                }
                                backgroundInited = true

                                LogUtil.logOnlyOnce("ContactFragment Done")
                                XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                            }
                            //列表第一项(头)
                            if (headInitCount < 10) {
                                VTTV.ContactLayoutListenerViewItem.treeStacks["WxRecyclerView"]?.apply {
                                    val WxRecyclerView = ViewUtils.getChildView1(contactView, this) as View
                                    WxRecyclerView.background = drawableTransparent

                                    VTTV.ContactLayoutListenerViewItem.treeStacks["WxRecyclerView_ContactHeaderItem"]?.apply {
                                        val ContactHeaderItem = ViewUtils.getChildView1(WxRecyclerView, this) as View
                                        ContactHeaderItem.background = drawableTransparent

                                        if (ContactHeaderItem is ViewGroup) {
                                            ListViewHooker.setContactHeaderItemTop(ContactHeaderItem)
                                        }
                                        ListViewHooker.setContactHeaderItem(ContactHeaderItem)
                                        (ViewUtils.getChildView1(contactView, intArrayOf(0, 1, 0)) as View).background = drawableTransparent
                                    }
                                    headInitCount++
                                }
                            }
                        }
//                        else{
//                            LogUtil.log(WechatGlobal.wxVersion!!.toString())
//                            LogUtil.log("获取联系人界面失败:")
//                            LogUtil.logViewStackTraces(contactView)
//                        }
                    }
                }
            })
        } catch (e: Exception) {
            LogUtil.log(e)
        }
    }

    private val backgroundColorResetHook = Hooker {
        try {
            Methods.findMethodsByName(
                    Classes.findClass("com.tencent.mm.ui.widget.pulldown.WeUIBounceViewV2"),
                    listOf("setStart2EndBgColorByActionBar", "setEnd2StartBgColorByNavigationBar",
                            "setStart2EndBgColor", "setEnd2StartBgColor", "setBgColor")
                    , CC.Int
            )
                    .forEach {
                        try {
                            XposedHelpers.findAndHookMethod(
                                    Classes.findClass("com.tencent.mm.ui.widget.pulldown.WeUIBounceViewV2"),
                                    it.name
                                    , CC.Int
                                    , object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam?) {
                                    param?.args?.apply {
                                        this[0] = Color.TRANSPARENT
                                    }
                                }
                            })
                        } catch (e: Exception) {
                            LogUtil.log(e)
                        }
                    }
        } catch (e: NoSuchMethodException) {
            LogUtil.log("find method error")
            LogUtil.log(e)
        } catch (e: Exception) {
            LogUtil.log("WHAT THE FUCK?")
            LogUtil.log(e)
        }
    }

    private val contactItermsHook = Hooker {
        try {
            Methods.findMethodsByName(
                    Classes.findClass("com.tencent.mm.ui.NoDrawingCacheLinearLayout"),
                    listOf("onMeasure")
                    , CC.Int
                    , CC.Int
            )
                    .forEach {
                        try {
                            XposedHelpers.findAndHookMethod(
                                    Classes.findClass("com.tencent.mm.ui.NoDrawingCacheLinearLayout"),
                                    it.name
                                    , CC.Int
                                    , CC.Int
                                    , object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam?) {
                                    val linearLayout = param?.thisObject ?: return
                                    if (linearLayout is ViewGroup
                                            && ViewTreeUtils.equals(com.blanke.mdwechat.ViewTreeRepoThisVersion.ContactListViewItem.item, linearLayout)) {
                                        ListViewHooker.setContactListViewItem(linearLayout)
                                    }
                                }
                            })
                        } catch (e: Exception) {
                            LogUtil.log(e)
                        }
                    }
        } catch (e: NoSuchMethodException) {
            LogUtil.log("find method error")
            LogUtil.log(e)
        } catch (e: Exception) {
            LogUtil.log("WHAT THE FUCK?")
            LogUtil.log(e)
        }
    }

    private val resumeHook = Hooker {
        Methods.HomeFragment_lifecycles.forEach {
            XposedHelpers.findAndHookMethod(Classes.ContactFragment, it.name, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val fragment = param?.thisObject ?: return
//                    LogUtil.log("ContactFragment fragment=$fragment,${Classes.ContactFragment.name}")
                    val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                    if (isInit != null) {
                        LogUtil.log("ContactFragment 已经hook过")
                        return
                    }
                    init(fragment)
                }

                private fun init(fragment: Any) {
                    val listView = ContactFragment_mListView.get(fragment)
                    if (listView != null && listView is ListView) {
                        LogUtil.logOnlyOnce("ContactFragment Done")
                        XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                        if (HookConfig.is_hook_tab_bg) {
                            BackgroundImageHook.setContactBitmap(listView)
                        }
//                        LogUtil.log("ContactFragment listview= $listView, ${listView.javaClass.name}")
                        if (listView.headerViewsCount > 0) {
                            val mHeaderViewInfos = getObjectField(listView, "mHeaderViewInfos") as ArrayList<*>
                            for (j in 0 until mHeaderViewInfos.size) {
                                val header = (mHeaderViewInfos[j] as ListView.FixedViewInfo).view
                                if (header != null) {
//                                        printViewTree(header, 0)
                                    if (header is ViewGroup) {
                                        ListViewHooker.setContactHeaderItemTop(header)
                                    }
                                }
                            }
                        }
                        LogUtil.logOnlyOnce("ContactFragment")
                    }
                }
            })
        }
    }
}