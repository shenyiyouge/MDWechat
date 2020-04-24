package com.blanke.mdwechat

import android.app.Activity
import android.view.MenuItem
import android.view.View
import com.flyco.tablayout.CommonTabLayout

object Objects {
    object Main {
        var LauncherUI: Activity? = null
        var LauncherUI_mContentLayout: View? = null
        var HomeUI_mActionBar: Any? = null
        var LauncherUI_mViewPager: View? = null
        var LauncherUI_mTabLayout: CommonTabLayout? = null
        var LauncherUI_mWechatXMenuItem: MenuItem? = null
        var statusView: View? = null
        var actionBar: View? = null

        var tabLayout: CommonTabLayout? = null
        var pagePosition = 0
    }

//    fun clear() {
//        Main.LauncherUI=null
//        Main.LauncherUI_mContentLayout=null
//        Main.HomeUI_mActionBar=null
//        Main.LauncherUI_mViewPager.clear()
//        Main.LauncherUI_mTabLayout.clear()
//        Main.LauncherUI_mWechatXMenuItem.clear()
//    }
}