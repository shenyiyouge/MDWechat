package com.blanke.mdwechat.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.config.HookConfig

object NightModeUtils {
    private var _isNightMode = false
    fun isNightMode(): Boolean {
        return _isNightMode
    }

    fun setNightMode(b: Boolean) {
        _isNightMode =HookConfig.is_hook_night_mode && b
    }

    fun isHookMainColor(): Boolean {
        return HookConfig.is_hook_main_textcolor
    }

    fun getBackgroundDrawable(defaultDrawable: Drawable?): Drawable {
        return if (isNightMode()) WeChatHelper.drawableDark else defaultDrawable
                ?: WeChatHelper.drawableWhite
    }

    fun getBackgroundDrawable(defaultDrawable: Bitmap?): Drawable {
        return when {
            isNightMode() -> WeChatHelper.drawableDark
            defaultDrawable != null -> BitmapDrawable(defaultDrawable)
            else -> WeChatHelper.drawableWhite
        }
    }

    fun getContentTextColor(): Int {
        return getTextColor(HookConfig.get_main_text_color_content)
    }

    fun getTitleTextColor(): Int {
        return getTextColor(HookConfig.get_main_text_color_title)
    }

    private fun getTextColor(defaultColor: Int?): Int {
        return when {
            isNightMode() ->WeChatHelper.colorDarkWhite
            !isHookMainColor() -> Color.BLACK
            else -> defaultColor ?: Color.BLACK
        }
    }

    val colorPrimary: Int
        get() {
            return if (isNightMode()) WeChatHelper.colorDarkPrimary else HookConfig.get_color_primary
        }

    val colorSecondary: Int
        get() {
            return if (isNightMode()) WeChatHelper.colorDarkWhite else HookConfig.get_color_secondary

        }
    val colorTeritary: Int
        get() {
            return if (isNightMode()) WeChatHelper.colorDarkWhite else HookConfig.get_color_tertiary
        }
    val colorFloatButton: Int
        get() {
            return if (isNightMode()) WeChatHelper.colorDarkWhite else HookConfig.get_float_button_color

        }
    val colorTip: Int
        get() {
            return HookConfig.get_color_tip
        }
    val is_hook_tab_elevation: Boolean
        get() {
            return HookConfig.is_hook_tab_elevation || _isNightMode
        }
    val is_tab_layout_filtered: Boolean
        get() {
            return HookConfig.is_tab_layout_filtered || _isNightMode
        }
}