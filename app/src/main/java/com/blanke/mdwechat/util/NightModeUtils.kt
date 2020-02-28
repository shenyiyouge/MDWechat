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
        return if (isNightMode()) WeChatHelper.darkDrawable else defaultDrawable
                ?: WeChatHelper.whiteDrawable
    }

    fun getBackgroundDrawable(defaultDrawable: Bitmap?): Drawable {
        return when {
            isNightMode() -> WeChatHelper.darkDrawable
            defaultDrawable != null -> BitmapDrawable(defaultDrawable)
            else -> WeChatHelper.whiteDrawable
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
}