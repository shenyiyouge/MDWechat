package com.blanke.mdwechat.util

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.config.HookConfig

object NightModeUtils {
    fun isNightMode(): Boolean {
        return HookConfig.is_hook_night_mode
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
            isNightMode() -> Color.WHITE
            !isHookMainColor() -> Color.BLACK
            else -> defaultColor ?: Color.BLACK
        }
    }

    val colorPrimary: Int by lazy {
        if (isNightMode()) WeChatHelper.colorDark else HookConfig.get_color_primary
    }
    val colorSecondary: Int by lazy {
        if (isNightMode()) WeChatHelper.colorWhite else HookConfig.get_color_secondary
    }
    val colorTeritary: Int by lazy {
        if (isNightMode()) WeChatHelper.colorWhite else HookConfig.get_color_tertiary
    }
    val colorFloatButton: Int by lazy {
        if (isNightMode()) WeChatHelper.colorWhite else HookConfig.get_float_button_color
    }
    val colorTip: Int by lazy {
        HookConfig.get_color_tip
    }
}