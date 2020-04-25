package com.blanke.mdwechat.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.auto_search.Main
import com.blanke.mdwechat.auto_search.bean.LogEvent
import com.blanke.mdwechat.markdown.MarkDownActivity
import com.blanke.mdwechat.settings.view.DownloadWechatDialog
import com.blanke.mdwechat.util.FileUtils
import com.blanke.mdwechat.util.LogUtil.clearFileLogs
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jaredrummler.android.colorpicker.ColorPreference
import com.joshcai.mdwechat.BuildConfig
import com.joshcai.mdwechat.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*
import kotlin.concurrent.thread


/**
 * Created by blanke on 2017/6/8.
 */

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    lateinit var wxVersion: Version

    object STATIC {
        lateinit var sharedPrefsFile: File
        lateinit var sdSPFile: File
        var isLogFile: Boolean = false
    }

    private fun getWechatPath(): String {
        val pm = activity.packageManager
        val ai = pm.getApplicationInfo(Common.WECHAT_PACKAGENAME, 0)
        return ai.publicSourceDir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val _this = this
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
//        preferenceManager.setSharedPreferencesMode(Context.MODE_WORLD_READABLE)
        preferenceManager.sharedPreferencesName = Common.MOD_PREFS
        addPreferencesFromResource(R.xml.pref_settings)
        setLayoutResource(preferenceScreen)
        setResolution()

        // 禁用 详细日志
        findPreference(getString(R.string.key_hook_debug))?.apply {
            (this as SwitchPreference).isChecked = false
        }
//        wxVersion = Version(ApkFile(getWechatPath()).apkMeta.versionName)
//        //隐藏主界面部分选项
//        val main_settings=findPreference(getString(R.string.key_main_settings))as PreferenceScreen
//        if (wxVersion < Version("7.0.3")) {
////        隐藏 小程序下拉框文字
//            main_settings.removePreference(main_settings.findPreference(getString(R.string.key_mini_program_title)) as SwitchPreference)
//        } else {
////        隐藏 移除小程序
//            main_settings.removePreference(main_settings.findPreference(getString(R.string.key_hook_remove_appbrand)) as SwitchPreference)
//
//            val a = main_settings.findPreference(getString(R.string.key_mini_program_title)) as EditTextPreference
//            a.summary = "当前文字: ${a.text}"
//        }

//        findPreference(getString(R.string.key_tab_layout_on_top))?.onPreferenceChangeListener = this
//        findPreference(getString(R.string.key_mini_program_title))?.onPreferenceChangeListener = this

        findPreference(getString(R.string.key_hook_log))?.apply {
            onPreferenceChangeListener = _this
            STATIC.isLogFile = (this as SwitchPreference).isChecked
        }
        findPreference(getString(R.string.key_hook_log_xposed))?.apply {
            onPreferenceChangeListener = _this
            STATIC.isLogFile = !(this as SwitchPreference).isChecked && STATIC.isLogFile
        }
        findPreference("key_clear_logs")?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_hook_conversation_background_alpha))?.onPreferenceChangeListener = this
        findPreference(getString(R.string.key_pre_inst_color_schemes))?.onPreferenceChangeListener = this

        findPreference(getString(R.string.key_hide_launcher_icon))?.onPreferenceChangeListener = this
        findPreference(getString(R.string.key_donate))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_wechat_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_float_bottom_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_icon_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback_email_blanke))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback_email_josh_cai))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_gitee_joshcai))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_github_blanke))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_github_joshcai))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_hook_conversation_bg))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_generate_wechat_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_donate_wechat))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_download_wechat_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback_group))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_help_float_button))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_help_bubble))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_help_icon))?.onPreferenceClickListener = this
        if (BuildConfig.VERSION_NAME.endsWith("Beta", true)) {
            AlertDialog.Builder(activity)
                    .setTitle("警告")
                    .setMessage("当前版本为${BuildConfig.VERSION_NAME}版，不是正式版本，可能会遇到各种问题/无法预估的风险等。" +
                            "如果你想反馈问题，请打开最底部的调试开关，重启微信，将/sdcard/mdwechat/logs/目录下当天的日志发我邮箱。" +
                            "谢谢合作！")
                    .setPositiveButton("我知道了", null)
                    .setCancelable(false)
                    .show()
        }
        showAppInfoDialog()
    }

    private fun setLayoutResource(preference: Preference) {
        if (preference is PreferenceScreen) {
            val ps = preference
            ps.layoutResource = R.layout.preference_screen
            val cnt = ps.preferenceCount
            for (i in 0 until cnt) {
                val p = ps.getPreference(i)
                setLayoutResource(p)
            }
        } else if (preference is PreferenceCategory) {
            val pc = preference
            pc.layoutResource = R.layout.preference_category
            val cnt = pc.preferenceCount
            for (i in 0 until cnt) {
                val p = pc.getPreference(i)
                setLayoutResource(p)
            }
        }
//        else {
//            preference_warning.layoutResource = R.layout.preference_warning
//        }
    }

    override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
        when (preference.key) {
            getString(R.string.key_hook_log) -> STATIC.isLogFile = (findPreference(getString(R.string.key_hook_log)) as SwitchPreference).isChecked
            getString(R.string.key_hook_log_xposed) -> STATIC.isLogFile = !((findPreference(getString(R.string.key_hook_log_xposed)) as SwitchPreference).isChecked)
            getString(R.string.key_hide_launcher_icon) -> showHideLauncherIcon(!(o as Boolean))
            getString(R.string.key_hook_conversation_background_alpha) -> verifyAlpha(o as String)
            getString(R.string.key_pre_inst_color_schemes) -> changeColorScheme(o as String)
//            getString(R.string.key_mini_program_title) -> setSummary(o as String)
//            getString(R.string.key_tab_layout_on_top) ->setTabLayoutOnTop((o as Boolean))
        }
        return true
    }

    @SuppressLint("ResourceAsColor")
    private fun changeColorScheme(s: String) {
//        try {
        when (s) {
            "immersion" -> {
                //深色模式
                findPreference(getString(R.string.key_hook_scheme_dark))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_night_mode))?.apply { (this as SwitchPreference).isChecked = false }
                //三色
                findPreference(getString(R.string.key_color_primary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTransparent)) }
                findPreference(getString(R.string.key_color_secondary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_color_tertiary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                //提示颜色
                findPreference(getString(R.string.key_color_tip))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTip)) }
                findPreference(getString(R.string.key_color_tip_num))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                findPreference(getString(R.string.key_change_guide_tip_color))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_color_tip_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTip)) }
                findPreference(getString(R.string.key_color_tip_num_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                //主界面字体
                findPreference(getString(R.string.key_hook_main_textcolor))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_main_textcolor_title))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_main_textcolor_content))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                //气泡文字
                findPreference(getString(R.string.key_hook_chat_text_color_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_hook_chat_text_color_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                //气泡着色
                findPreference(getString(R.string.key_hook_bubble))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.transparentDark)) }
                findPreference(getString(R.string.key_hook_bubble_tint_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.transparentDark)) }
                //背景
                findPreference(getString(R.string.key_hook_tab_bg))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_conversation_background_alpha))?.apply { (this as EditTextPreference).text = "0" }
                findPreference(getString(R.string.key_hook_bg_immersion))?.apply { (this as SwitchPreference).isChecked = true }
                //悬浮按钮
                findPreference(getString(R.string.key_hook_float_button_color_is_secondary))?.apply { (this as SwitchPreference).isChecked = true }
                //tablayout 阴影
                findPreference(getString(R.string.key_hook_tab_elevation))?.apply { (this as SwitchPreference).isChecked = true }
                //tablayout 着色
                findPreference(getString(R.string.key_tab_layout_filtered))?.apply { (this as SwitchPreference).isChecked = true }
            }
            "white" -> {
                //深色模式
                findPreference(getString(R.string.key_hook_scheme_dark))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_hook_night_mode))?.apply { (this as SwitchPreference).isChecked = true }
                //三色
                findPreference(getString(R.string.key_color_primary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                findPreference(getString(R.string.key_color_secondary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorSecondary)) }
                findPreference(getString(R.string.key_color_tertiary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTertiary)) }
                //提示颜色
                findPreference(getString(R.string.key_color_tip))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTip)) }
                findPreference(getString(R.string.key_color_tip_num))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                findPreference(getString(R.string.key_change_guide_tip_color))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_color_tip_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTip)) }
                findPreference(getString(R.string.key_color_tip_num_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                //主界面字体
                findPreference(getString(R.string.key_hook_main_textcolor))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_main_textcolor_title))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_black)) }
                findPreference(getString(R.string.key_main_textcolor_content))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_black)) }
                //气泡文字
                findPreference(getString(R.string.key_hook_chat_text_color_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorSecondary)) }
                findPreference(getString(R.string.key_hook_chat_text_color_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorTertiary)) }
                //气泡着色
                findPreference(getString(R.string.key_hook_bubble))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_hook_bubble_tint_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorSecondary)) }
                //背景
                findPreference(getString(R.string.key_hook_tab_bg))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_hook_conversation_background_alpha))?.apply { (this as EditTextPreference).text = "255" }
                findPreference(getString(R.string.key_hook_bg_immersion))?.apply { (this as SwitchPreference).isChecked = false }
                //悬浮按钮
                findPreference(getString(R.string.key_hook_float_button_color_is_secondary))?.apply { (this as SwitchPreference).isChecked = true }
                //tablayout 阴影
                findPreference(getString(R.string.key_hook_tab_elevation))?.apply { (this as SwitchPreference).isChecked = false }
                //tablayout 着色
                findPreference(getString(R.string.key_tab_layout_filtered))?.apply { (this as SwitchPreference).isChecked = false }
            }
            "khaki" -> {
                //深色模式
                findPreference(getString(R.string.key_hook_scheme_dark))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_hook_night_mode))?.apply { (this as SwitchPreference).isChecked = true }
                //三色
                findPreference(getString(R.string.key_color_primary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khaki)) }
                findPreference(getString(R.string.key_color_secondary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_color_tertiary))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                //提示颜色
                findPreference(getString(R.string.key_color_tip))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khaki)) }
                findPreference(getString(R.string.key_color_tip_num))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                findPreference(getString(R.string.key_change_guide_tip_color))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_color_tip_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.colorPrimary)) }
                findPreference(getString(R.string.key_color_tip_num_in_guide))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khaki)) }
                //主界面字体
                findPreference(getString(R.string.key_hook_main_textcolor))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_main_textcolor_title))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khakiDark)) }
                findPreference(getString(R.string.key_main_textcolor_content))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khakiDark)) }
                //气泡文字
                findPreference(getString(R.string.key_hook_chat_text_color_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khakiDark)) }
                findPreference(getString(R.string.key_hook_chat_text_color_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                //气泡着色
                findPreference(getString(R.string.key_hook_bubble))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint))?.apply { (this as SwitchPreference).isChecked = true }
                findPreference(getString(R.string.key_hook_bubble_tint_left))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.material_white)) }
                findPreference(getString(R.string.key_hook_bubble_tint_right))?.apply { (this as ColorPreference).saveValue(ContextCompat.getColor(context, R.color.khakiDark)) }
                //背景
                findPreference(getString(R.string.key_hook_tab_bg))?.apply { (this as SwitchPreference).isChecked = false }
                findPreference(getString(R.string.key_hook_conversation_background_alpha))?.apply { (this as EditTextPreference).text = "120" }
                findPreference(getString(R.string.key_hook_bg_immersion))?.apply { (this as SwitchPreference).isChecked = false }
                //悬浮按钮
                findPreference(getString(R.string.key_hook_float_button_color_is_secondary))?.apply { (this as SwitchPreference).isChecked = true }
                //tablayout 阴影
                findPreference(getString(R.string.key_hook_tab_elevation))?.apply { (this as SwitchPreference).isChecked = true }
                //tablayout 着色
                findPreference(getString(R.string.key_tab_layout_filtered))?.apply { (this as SwitchPreference).isChecked = true }
            }
        }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
    }

    private fun verifyAlpha(s: String) {
        val alpha = s.toInt()
        val p = findPreference(getString(R.string.key_hook_conversation_background_alpha)) as EditTextPreference
        p.text = Math.min(255, Math.max(alpha, 0)).toString()
    }
//    private fun setSummary(s: String) {
//        val a = preferenceScreen.findPreference(getString(R.string.key_mini_program_title)) as EditTextPreference
//        a.summary = "当前文字：$s"
//    }
//    private fun setTabLayoutOnTop(o:Boolean){
//        val hook_hide_actionbar = preferenceScreen.findPreference(getString(R.string.key_hook_hide_actionbar)) as SwitchPreference
//        hook_hide_actionbar.setChecked(o)
//    }


    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "key_clear_logs" -> _clearLogs()
            getString(R.string.key_donate) -> donate()
            getString(R.string.key_feedback) -> feedback()
            getString(R.string.key_reset_wechat_config) -> copyWechatConfig()
            getString(R.string.key_reset_config) -> deleteConfig()
            getString(R.string.key_reset_float_bottom_config) -> copyFloatBottomConfig()
            getString(R.string.key_reset_icon_config) -> copyIcons()
            getString(R.string.key_feedback_email_blanke) -> sendEmail()
            getString(R.string.key_feedback_email_josh_cai) -> sendEmailCai()
            getString(R.string.key_gitee_joshcai) -> gotoGiteeCai()
            getString(R.string.key_github_blanke) -> gotoGithub()
            getString(R.string.key_github_joshcai) -> gotoGithubCai()
            getString(R.string.key_generate_wechat_config) -> generateWechatFile()
            getString(R.string.key_donate_wechat) -> donateWechat()
            getString(R.string.key_download_wechat_config) -> downloadWechatConfig()
            getString(R.string.key_feedback_group) -> gotoMarkDownAct(getString(R.string.text_feedback_group), Common.URL_JOIN_GROUP)
            getString(R.string.key_help_float_button) -> gotoMarkDownAct(getString(R.string.text_help_float_button), Common.URL_HELP_FLOAT_BUTTON)
            getString(R.string.key_help_bubble) -> gotoMarkDownAct(getString(R.string.text_help_bubble), Common.URL_HELP_BUBBLE)
            getString(R.string.key_help_icon) -> gotoMarkDownAct(getString(R.string.text_help_icon), Common.URL_HELP_ICON)
        }
        return true
    }

    private fun _clearLogs() {
        clearFileLogs(STATIC.isLogFile)
        Toast.makeText(activity, getString(R.string.msg_clear_ok), Toast.LENGTH_SHORT).show()
    }

    private fun gotoMarkDownAct(title: String, url: String) {
        MarkDownActivity.start(activity, title, url)
    }

    private fun downloadWechatConfig() {
        DownloadWechatDialog.show(activity)
    }

    private var generateWechatLogView: TextView? = null
    private var generateWechatLogScrollView: ScrollView? = null

    private fun generateWechatFile() {
        generateWechatLogScrollView = ScrollView(activity)
        generateWechatLogView = TextView(activity)
        generateWechatLogView?.setPadding(15, 0, 15, 0)
        generateWechatLogScrollView?.addView(generateWechatLogView)
        AlertDialog.Builder(activity)
                .setView(generateWechatLogScrollView)
                .setTitle(R.string.text_generate_wechat_config)
                .setCancelable(false)
                .setPositiveButton(R.string.text_confirm, null)
                .show()
        val outputPath = Common.APP_DIR_PATH + Common.CONFIG_WECHAT_DIR
        try {
            Main().main(activity.applicationContext, getWechatPath(), outputPath)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort(R.string.msg_wechat_notfound)
            generateWechatLogView?.append(getString(R.string.msg_wechat_notfound) + "\n\n")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGerateWechatLogEvent(e: LogEvent) {
        generateWechatLogView?.append(e.msg + "\n\n")
        generateWechatLogScrollView?.fullScroll(View.FOCUS_DOWN)
    }

    private fun copyWechatConfig() {
        thread {
            FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.CONFIG_WECHAT_DIR, true)
        }
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun deleteConfig() {
        var success = true
        if (STATIC.sharedPrefsFile.exists()) {
            success = success && STATIC.sharedPrefsFile.delete()
        }
        if (STATIC.sdSPFile.exists()) {
            success = success && STATIC.sdSPFile.delete()
        }
        if (success) {
            Toast.makeText(activity, R.string.msg_reset_mdwechat_ok, Toast.LENGTH_SHORT).show()
            thread {
                Thread.sleep(2000)
                System.exit(0)
            }
        } else {
            Toast.makeText(activity, R.string.msg_reset_failed, Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyFloatBottomConfig() {
        thread {
            FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.CONFIG_VIEW_DIR, true)
        }
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun copyIcons() {
        thread {
            FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.ICON_DIR, true)
            val nomediaFile = File(Common.APP_DIR_PATH + Common.ICON_DIR + File.separator + ".nomedia")
            if (!nomediaFile.exists()) {
                nomediaFile.createNewFile()
            }
        }
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun showHideLauncherIcon(show: Boolean) {
        val p = activity.packageManager
        val componentName = ComponentName(activity, "com.blanke.mdwechat.SettingsLauncher")
//        val componentName = ComponentName(activity, Common.MY_APPLICATION_PACKAGE + ".SettingsLauncher")
        p.setComponentEnabledSetting(componentName,
                if (show) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun donate() {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val payUrl = "https://qr.alipay.com/tsx05730go4ditv2dmwia15"
        intent.data = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl)
        if (intent.resolveActivity(activity.packageManager) != null) {
            startActivity(intent)
            return
        }
        intent.data = Uri.parse(payUrl.toLowerCase(Locale.getDefault()))
        startActivity(intent)
    }

    private fun donateWechat() {
        val wechatPayCode = "f2f0YjlNObKWk7zwpDQoGtBDBe-Cper5cndi"
        val className = "com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity"
        val componentName = ComponentName("com.tencent.mm", className)
        try {
            view?.context?.startActivity(Intent(Intent.ACTION_VIEW).apply {
                component = componentName
                data = Uri.parse("weixin://mdwechat/donate/$wechatPayCode")
                flags = Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
            })
            Toast.makeText(view?.context, "模块生效会自动跳转到微信捐赠页面", Toast.LENGTH_SHORT).show()
        } catch (t: Throwable) {
            Toast.makeText(view?.context, "模块未生效,捐赠失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun feedback() {
        try {
            val str = "market://details?id=" + Common.MY_APPLICATION_PACKAGE
            val intent = Intent("android.intent.action.VIEW")
            intent.data = Uri.parse(str)
            intent.`package` = "com.coolapk.market"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent("android.intent.action.VIEW",
                    Uri.parse("http://www.coolapk.com/apk/" + Common.MY_APPLICATION_PACKAGE)))
        }
    }

    private fun sendEmail() {
        try {
            val info = "mailto:blanke.master+mdwechat@gmail.com?subject=[MDWechat] 请简明描述该问题" +
                    "&body=请按以下步骤填写,不按此填写的邮件可能会被忽略,谢谢!%0d%0a[问题描述] 请描述遇到了什么问题%0d%0a[环境]请写明安卓版本 手机 rom xp 微信 版本%0d%0a[日志]可以传附件"
            val uri = Uri.parse(info)
            startActivity(Intent(Intent.ACTION_SENDTO, uri))
        } catch (e: Exception) {

        }
    }

    private fun sendEmailCai() {
        try {
            val info = "mailto:joshcai_mdwechat@163.com?subject=[MDWechat] 请简明描述该问题" +
                    "&body=请按以下步骤填写,不按此填写的邮件可能会被忽略,谢谢!%0d%0a[问题描述] 请描述遇到了什么问题%0d%0a[环境]请写明安卓版本 手机 rom xp 微信 版本%0d%0a[日志]可以传附件"
            val uri = Uri.parse(info)
            startActivity(Intent(Intent.ACTION_SENDTO, uri))
        } catch (e: Exception) {

        }
    }

    private fun gotoGiteeCai() {
        val uri = Uri.parse("https://gitee.com/JoshCai/MDWechat")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun gotoGithub() {
        val uri = Uri.parse("https://github.com/Blankeer/MDWechat")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun gotoGithubCai() {
        val uri = Uri.parse("https://github.com/JoshCai233/MDWechat")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun showAppInfoDialog() {
        val lastLaunchTime = preferenceManager.sharedPreferences.getLong("last_launch_time", -1)
        preferenceManager.sharedPreferences.edit().putLong("last_launch_time", System.currentTimeMillis()).apply()
        if (lastLaunchTime > 0 && TimeUtils.isToday(lastLaunchTime)) {
            return
        }
        val packageManager = activity.applicationContext.packageManager
        val packageInfo = packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0)
        val firstInstallTime = Date(packageInfo.firstInstallTime)
        val installDateStr = TimeUtils.getFitTimeSpan(Date(), firstInstallTime, 4)
        val message = getString(R.string.text_app_desc) + "\n" + getString(R.string.text_app_donate, installDateStr)
        AlertDialog.Builder(activity)
                .setTitle(R.string.text_app_tips)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(R.string.text_app_know, null)
                .setNegativeButton(R.string.text_donate_wechat) { dialog, which -> donateWechat() }
                .setNeutralButton(R.string.text_donate) { dialog, which -> donate() }
                .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    //    private val weChatVersion: String
//        get() = "unKnow"
//
//    private val isSupportWechat: Boolean
//        get() = false
    fun setResolution() {
        val setManually = findPreference(getString(R.string.key_change_resolution)) as SwitchPreference
        val textPreference = findPreference(getString(R.string.key_resolution)) as EditTextPreference
        if (setManually.isChecked) {
            val resolution = textPreference.text
                    .replace(" ", "")
                    .replace("，", ",")
                    .split(",")
            try {
                if ((resolution.count() == 2) && (resolution[0].toInt() > 0) && (resolution[1].toInt() > 0)) return
            } catch (e: java.lang.Exception) {
            }
        }
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels

        textPreference.text = "$screenWidth,$screenHeight"
    }

}