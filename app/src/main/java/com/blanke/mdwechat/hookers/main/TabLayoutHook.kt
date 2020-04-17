package com.blanke.mdwechat.hookers.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.blanke.mdwechat.*
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.StatusBarHooker
import com.blanke.mdwechat.util.*
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import de.robv.android.xposed.XposedHelpers
import java.util.*

object TabLayoutHook {
    fun newTabLayout(viewGroup: ViewGroup, indicatorGravity: Int = Gravity.BOTTOM, tabElevation: Float = 0F): CommonTabLayout {
        val primaryColor: Int = NightModeUtils.colorPrimary
        val secondaryColor: Int = NightModeUtils.colorSecondary
        val get_color_tertiary: Int = NightModeUtils.colorTeritary
        val tipColor: Int = NightModeUtils.colorTip
        val context = viewGroup.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val resContext = viewGroup.context
        val tabLayout = CommonTabLayout(context)
        tabLayout.textSelectColor = Color.WHITE
        val dp2 = ConvertUtils.dp2px(resContext, 1f)
        tabLayout.indicatorHeight = dp2.toFloat()
        tabLayout.indicatorColor = if (NightModeUtils.is_hook_tab_elevation) secondaryColor else primaryColor
        tabLayout.setIndicatorGravity(indicatorGravity)
        tabLayout.indicatorCornerRadius = dp2.toFloat()
        tabLayout.indicatorAnimDuration = 200
        tabLayout.elevation = tabElevation
        tabLayout.unreadBackground = tipColor
        tabLayout.unreadTextColor = primaryColor
        tabLayout.selectIconColor = secondaryColor
//        tabLayout.unSelectIconColor = Color.WHITE

        val mTabEntities = intArrayOf(0, 1, 2, 3)
//                .filterNot { HookConfig.is_hook_hide_wx_tab_2 && it == 2 }
//                .filterNot { HookConfig.is_hook_hide_wx_tab_3 && it == 3 }
                .mapTo(ArrayList<CustomTabEntity>()) {
                    object : CustomTabEntity.TabCustomData() {
                        override fun getTabIcon(): Drawable {
                            val drawable: Drawable = BitmapDrawable(resContext.resources, AppCustomConfig.getTabIcon(it))
                            return if (NightModeUtils.is_tab_layout_filtered) DrawableUtils.setDrawableColor(drawable, get_color_tertiary) else drawable
                        }
                    }
                }
        tabLayout.setTabData(mTabEntities)

        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                LogUtil.log("tab click position=$position")
                tabLayout.currentTab = position
                Objects.Main.LauncherUI_mViewPager?.apply {
                    Methods.WxViewPager_selectedPage.invoke(this, position, false, false, 0)
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })
        return tabLayout
    }

    fun measureHeight(view: ViewGroup): Int {
        val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, w)
        return view.measuredHeight
    }

    fun addTabLayoutAtBottom(tabView: ViewGroup, height: Int) {
        val tabLayout = newTabLayout(tabView, Gravity.TOP, 5f)

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val viewChild = tabView.getChildAt(0) as ViewGroup
        params.height = height
        mainThread {
            Objects.Main.tabLayout = tabLayout
            tabLayout.background = NightModeUtils.getForegroundDrawable(BackgroundImageUtils.getTabLayoutBitmapAtBottom(params.height, 0))
        }
        viewChild.addView(tabLayout, 4, params)
        try {
            LogUtil.log("add table layout success")
            Objects.Main.LauncherUI_mTabLayout = tabLayout
            for (index in 0..3) {
                viewChild.getChildAt(index).visibility = View.GONE
            }
        } catch (e: Exception) {
            LogUtil.log(e)
        }
    }

    fun addTabLayout(viewPagerLinearLayout: ViewGroup) {
        val context = viewPagerLinearLayout.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val resContext = viewPagerLinearLayout.context
        // 7.0.7(?) 之后小程序下拉相关
        val isHideElevation = (WechatGlobal.wxVersion!! >= Version("7.0.7")) && (HookConfig.is_hook_hide_wx_tab)
        val tabElevation = if (!isHideElevation && NightModeUtils.is_hook_tab_elevation) 5F else 0F

        val tabLayout = newTabLayout(viewPagerLinearLayout, Gravity.BOTTOM, tabElevation)

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val px48 = ConvertUtils.dp2px(resContext, 48f)
        params.height = px48 + HookConfig.value_tab_layout_offset
//        mainThread {
            Objects.Main.tabLayout = tabLayout
        BackgroundImageUtils.setTabLayoutBitmap(params.height, 0)
//        }
        if (WechatGlobal.wxVersion!! < Version("6.7.2")) {
            viewPagerLinearLayout.addView(tabLayout, 0, params)
        } else if (WechatGlobal.wxVersion!! < Version("7.0.0")) {// 672 wx布局更改
            val mockLayout = FrameLayout(context)
            var paddingTop = px48
            if (HookConfig.is_hook_hide_actionbar) {
                paddingTop = 0
            }
            mockLayout.setPadding(0, paddingTop, 0, 0)
            val viewpager = viewPagerLinearLayout.getChildAt(0)
            viewPagerLinearLayout.removeViewAt(0)
            mockLayout.addView(tabLayout, params)
            mockLayout.addView(viewpager)
            viewPagerLinearLayout.addView(mockLayout, 0)
        } else {
            val cb = { actionHeight: Int ->
                params.topMargin = actionHeight + HookConfig.statusBarHeight
                if (WechatGlobal.wxVersion!! == Version("7.0.0")) {
                    // mock status bar
                    val statusView = View(context)
                    statusView.background = ColorDrawable(StatusBarHooker.getStatusBarColor())
                    statusView.elevation = 1F
                    val statusParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    statusParam.topMargin = 0
                    statusParam.height = HookConfig.statusBarHeight
                    viewPagerLinearLayout.addView(statusView, 0, statusParam)
                }
//                viewPagerLinearLayout.height
                viewPagerLinearLayout.setPadding(0, 0, 0, 0)
                viewPagerLinearLayout.requestLayout()
                //小程序下拉之后的填空
                if ((WechatGlobal.wxVersion!! >= Version("7.0.7")) && (!HookConfig.is_hook_hide_actionbar)) {
                    val paramsAddedOnTop = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    paramsAddedOnTop.height = px48
                    paramsAddedOnTop.topMargin = actionHeight + HookConfig.statusBarHeight - px48
                    val view = FrameLayout(context)
                    Objects.Main.actionBar = view
                    view.background = NightModeUtils.getForegroundDrawable(BackgroundImageUtils.getActionBarBitmap(view.measuredHeight, Objects.Main.pagePosition))
                    viewPagerLinearLayout.addView(view, 1, paramsAddedOnTop)
                }
                viewPagerLinearLayout.addView(tabLayout, 2, params)
            }
            val mActionBar = Objects.Main.HomeUI_mActionBar!!
            waitInvoke(100, true, {
                XposedHelpers.callMethod(mActionBar, "getHeight") as Int > 0
            }, {
                val actionHeight = XposedHelpers.callMethod(mActionBar, "getHeight") as Int


//                LogUtil.log("=================1")
//                if (actionHeight > 0) AppCustomConfig.actionBarHeight = actionHeight

                if (!HookConfig.is_hook_hide_actionbar) {
                    cb(actionHeight)
                } else {
                    cb(0)
                }
            })
        }
        LogUtil.log("add table layout success")
        Objects.Main.LauncherUI_mTabLayout = tabLayout
        // change main ActionBar elevation = 0
        val actionBarLayout = ViewUtils.getParentView(viewPagerLinearLayout, 3) as ViewGroup
        val actionBar = actionBarLayout.getChildAt(1)
        actionBar.elevation = 0F
    }
}