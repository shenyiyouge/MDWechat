package com.blanke.mdwechat.util

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by blanke on 2017/10/3.
 */

object LogUtil {
    private object STATIC {
        var logged = mutableSetOf<String>()
        var wxVersion: String = "-"
    }

    private val dateStr
        get() = SimpleDateFormat("yyyy-MM-dd").format(Date())

    fun printLog2File(log: String) {
        val logFile = File(AppCustomConfig.getLogFile(dateStr))
        logFile.parentFile.mkdirs()
        val time = SimpleDateFormat("HH:mm:ss").format(Date())
        FileUtils.write(logFile.absolutePath, "$time $log\n", true)
    }

    fun clearFileLogs() {
        val logFile = File(AppCustomConfig.getLogFile("MDWechat_log")).parentFile
        FileUtils.deleteDirectoryFiles(logFile, "MDWechat_log_")
//        val result = logFile.delete()
        logFile.mkdirs()
        printLog2File("------- beginning of log -------")
        STATIC.logged.clear()
    }

    @JvmStatic
    fun logOnlyOnce(key: String, value: String = "Hook") {
        try {
            if (!HookConfig.is_hook_log) return
        } catch (e: RuntimeException) {
        }

        if (STATIC.wxVersion != WechatGlobal.wxVersion.toString()) {
            log("version changed from ${STATIC.wxVersion} to ${WechatGlobal.wxVersion.toString()}")
            STATIC.logged.clear()
            STATIC.wxVersion = WechatGlobal.wxVersion.toString()
        }
        if (STATIC.logged.indexOf(key) > -1) {
            return
        }
        STATIC.logged.add(key)
        log("$key $value")
    }

    @JvmStatic
    fun log(msg: String) {
        try {
            if (HookConfig.is_hook_log) printLog2File("LogUtil: " + msg)
        } catch (e: RuntimeException) {
        }
        Log.i("MDWechatModule", "MDWechat $msg")
    }

    @JvmStatic
    fun log(t: Throwable) {
        try {
            if (HookConfig.is_hook_log) printLog2File("LogUtil: " + Log.getStackTraceString(t))
        } catch (e: RuntimeException) {
        }
        Log.e("MDWechatModule", "MDWechat " + Log.getStackTraceString(t))
    }

    fun logStackTrace(find: String = ""): Boolean {
        log("Dump Stack: --------------start----------------")
        var ret = false
        val ex = Throwable()
        val stackElements = ex.stackTrace
        if (stackElements != null) {
            for (i in stackElements.indices) {
                val s: String = stackElements[i].className + "----" +
                        stackElements[i].methodName + "----" +
                        stackElements[i].fileName + "----" +
                        stackElements[i].lineNumber
                if (s.contains(find)) {
                    ret = true
                }
                log("Dump Stack$i: " + s)
            }
        }
        log("Dump Stack: --------------over----------------")
        return ret
    }

    fun bundleToString(bundle: Bundle?): String? {
        val str = bundle?.keySet()?.joinToString(", ") {
            "$it = ${bundle[it]}"
        }
        return "{$str}"
    }

    fun logSuperClasses(clazz: Class<*>) {
        log(clazz.name)
        if (clazz.superclass != null) {
            logSuperClasses(clazz.superclass)
        }
    }

    fun logStackTraces(methodCount: Int = 15, methodOffset: Int = 3) {
        val trace = Thread.currentThread().stackTrace
        var level = ""
        log("---------logStackTraces start----------")
        for (i in methodCount downTo 1) {
            val stackIndex = i + methodOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].className)
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            log(builder.toString())
        }
        log("---------logStackTraces end----------")
    }

    fun logParentView(view: View, level: Int = 5) {
        log("---------logParentView start----------")
        var currentView = view
        for (i in 0 until level) {
            val v = currentView.parent
            if (v != null && v is View) {
                logView(v)
                currentView = v
            }
        }
        log("---------logParentView end----------")
    }

    fun logView(view: View) {
        log(getViewLogInfo(view))
    }

    fun getViewLogInfo(view: View): String {
        val sb = StringBuffer(view.toString())
        if (view is TextView) {
            sb.append("${view.text}(" + view.hint + ")")
        } else if (view is ViewGroup) {
            sb.append(" childCount = ${view.childCount}")
        }
        sb.append(" desc= ${view.contentDescription}")
        if (view.background is ColorDrawable) {
            val bg = view.background as ColorDrawable
            sb.append(" bgColor=${bg.color}")
        }
        return sb.toString()
    }

    fun logViewStackTraces(view: View, level: Int = 0) {
        val sb = StringBuffer()
        for (i in 0..level) {
            sb.append("\t")
        }
        sb.append(getViewLogInfo(view))
        log(sb.toString())
        try {
//            替换 if (view is ViewGroup)
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                logViewStackTraces(viewGroup.getChildAt(i), level + 1)
            }
        } catch (e: ClassCastException) {
        }
    }

    fun findTextViewStackTrace(view: View, text: String): View? {
        if (view is TextView) {
            if (view.text.contains(text)) {
                return view
            }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val res = findTextViewStackTrace(view.getChildAt(i), text)
                if (res != null) {
                    return res
                }
            }
        }
        return null
    }
}
