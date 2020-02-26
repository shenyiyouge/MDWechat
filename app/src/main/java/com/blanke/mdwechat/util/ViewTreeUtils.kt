package com.blanke.mdwechat.util

import android.view.View
import android.view.ViewGroup
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.bean.ViewTreeItem
import com.blanke.mdwechat.util.LogUtil.logXp

object ViewTreeUtils {

    /**
     * 判断 view 的层级结构是否和定义的相等
     */
    fun equals(tree: ViewTreeItem, view: View): Boolean {
        if (!equals(tree.clazz, view)) {
            return false
        }
        if (view is ViewGroup) {
            if (view.childCount >= tree.children.size) {
                for (i in 0 until tree.children.size) {
                    if (tree.children[i] == null) {
                        continue
                    }
                    if (!equals(tree.children[i]!!, view.getChildAt(i))) {
                        return false
                    }
                }
                return true
            }
        } else {
            if (tree.children.isEmpty()) {
                return true
            }
        }
        return false
    }

    fun equals(clazz: String, view: View): Boolean {
        return view.javaClass.name.contains(clazz, true)
    }
    // region debug
    /**
     * 抓取view里面的class, 参数与equal形式相同便于debug切换
     */
    fun printView(tree: ViewTreeItem, view: View): Boolean {
        if (!equals(tree.clazz, view)) {
            return false
        }
        logXp("------------------")
        logXp("WechatGlobal.wxVersion=${WechatGlobal.wxVersion}")
        logXp("view.javaClass.name=${view.javaClass.name}")
//        logXp("tree.clazz=${tree.clazz}")
        search(view, 0)
        logXp("------------------")
        return false
    }

    fun search(view: View, depth: Int) {
        print(view.javaClass.name, depth)
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                search(view.getChildAt(i), depth + 1)
            }
        }
        return
    }

    fun print(s: String, d: Int) {
        var s1 = ""
        for (j in 0..d) {
            s1 += "\t"
        }
        logXp("$s1$s")
    }
    // endregion
}