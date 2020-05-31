package com.blanke.mdwechat.settings.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.widget.ScrollView
import android.widget.TextView
import com.blanke.mdwechat.settings.api.APIManager
import com.blanke.mdwechat.settings.bean.NewestVersionConfig
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joshcai.mdwechat.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


object GetNewestVersion {

    fun show(context: Activity, versionCode: Int) {
        APIManager().getNewestVersion(
                object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        ToastUtils.showLong("获取最新版本失败," + e?.message)
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        val data = Gson().fromJson<NewestVersionConfig>(response.body()?.string(), object : TypeToken<NewestVersionConfig>() {}.type)
                        if (data.versionCode.toInt() > versionCode) {
                            context.runOnUiThread {
                                showNewestVersion(context, data)
                            }
                        }
                    }
                }
        )
    }


    @SuppressLint("SetTextI18n")
    private fun showNewestVersion(activity: Activity, data: NewestVersionConfig) {
        val generateWechatLogScrollView = ScrollView(activity)

        val generateWechatLogView = TextView(activity)
        generateWechatLogView.setPadding(72, 15, 72, 0)
        generateWechatLogScrollView.addView(generateWechatLogView)

        generateWechatLogView.text = "版本: ${data.version}\n更新内容: ${data.info}"

        activity.runOnUiThread {
            AlertDialog.Builder(activity)
                    .setTitle("发现新版本")
                    .setView(generateWechatLogScrollView)
                    .setCancelable(true)
                    .setNegativeButton(R.string.text_cancel, null)
                    .setPositiveButton(R.string.text_update) { _, which ->
                        startActivity(activity, Intent(Intent.ACTION_VIEW, Uri.parse("https://gitee.com/JoshCai/MDWechat/releases/${data.version}")), null)
                    }
                    .show()
        }
    }

}
