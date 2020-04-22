package com.blanke.mdwechat

import com.blanke.mdwechat.bean.ViewTree
import com.blanke.mdwechat.bean.ViewTreeItem
import com.blanke.mdwechat.config.ViewTreeConfig

// 当前微信版本适用的repo
// based on ViewTreeRepo
object ViewTreeRepoThisVersion {
    val ConversationListViewItem: ViewTree by lazy {
        ViewTreeConfig.get().conversationListViewItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ContactListViewItem: ViewTree by lazy {
        ViewTreeConfig.get().contactListViewItem.invoke(ViewTreeRepo) as ViewTree
    }
    val DiscoverViewItem: ViewTree by lazy {
        ViewTreeConfig.get().discoverViewItem.invoke(ViewTreeRepo) as ViewTree
    }
    val SettingAvatarView: ViewTree by lazy {
        ViewTreeConfig.get().settingAvatarView.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatRightMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatRightMessageItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatLeftMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatLeftMessageItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatRightAudioMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatRightAudioMessageItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatLeftAudioMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatLeftAudioMessageItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatRightCallMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatRightCallMessageItem.invoke(ViewTreeRepo) as ViewTree
    }
    val ChatLeftCallMessageItem: ViewTree by lazy {
        ViewTreeConfig.get().chatLeftCallMessageItem.invoke(ViewTreeRepo) as ViewTree
    }

    //    小程序上方的action bar
    val ActionBarItem: ViewTree by lazy {
        ViewTreeConfig.get().actionBarItem.invoke(ViewTreeRepo) as ViewTree
    }

    // 聊天界面的action bar
    val ActionBarInConversationItem: ViewTree by lazy {
        ViewTreeConfig.get().actionBarInConversationItem.invoke(ViewTreeRepo) as ViewTree
    }

    // 聊天界面
    val ChattingUILayoutItem: ViewTree by lazy {
        ViewTreeConfig.get().chattingUILayoutItem.invoke(ViewTreeRepo) as ViewTree
    }

    // 主界面的action bar
    val ActionBarContainerItem: ViewTree by lazy {
        ViewTreeConfig.get().actionBarContainerItem.invoke(ViewTreeRepo) as ViewTree
    }

    // 朋友圈的action bar
    val ActionBarInFriendsgroupItem: ViewTree by lazy {
        ViewTreeConfig.get().actionBarInFriendsgroupItem.invoke(ViewTreeRepo) as ViewTree
    }
}

// 所有微信版本的repo
@Suppress("unused")
object ViewTreeRepo {

    val ConversationListViewItem: ViewTree by lazy {
        ViewTree(
                mapOf("chatNameView" to intArrayOf(1, 0, 0, 0),
                        "chatTimeView" to intArrayOf(1, 0, 1),
                        "recentMsgView" to intArrayOf(1, 1, 0, 1),
                        "unreadCountView" to intArrayOf(0, 1),
                        "unreadView" to intArrayOf(0, 2)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.ImageView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.ImageView.name)
                                        )
                                ),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name)
                                                                        )
                                                                )

                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val ConversationListViewItem_7_0_5: ViewTree by lazy {
        ViewTree(
                mapOf("chatNameView" to intArrayOf(1, 0, 0, 0),
                        "chatTimeView" to intArrayOf(1, 0, 1),
                        "recentMsgView" to intArrayOf(1, 1, 0, 1),
                        "unreadCountView" to intArrayOf(0, 1),
                        "unreadView" to intArrayOf(0, 2)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.ImageView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.ImageView.name)
                                        )
                                ),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.WeImageView),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name)
                                                                        )
                                                                )

                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val ConversationListViewItem_7_0_12: ViewTree by lazy {
        ViewTree(
                mapOf("chatNameView" to intArrayOf(1, 0, 0, 0),
                        "chatTimeView" to intArrayOf(1, 0, 1),
                        "recentMsgView" to intArrayOf(1, 1, 0, 1),
                        "unreadCountView" to intArrayOf(0, 1),
                        "unreadView" to intArrayOf(0, 2)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.ImageView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.ImageView.name)
                                        )
                                ),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.WeImageView),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.WeImageView)
                                                                        )
                                                                )

                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val ConversationListViewItem_7_0_13: ViewTree by lazy {
        ViewTree(
                mapOf("chatNameView" to intArrayOf(1, 0, 0, 0),
                        "chatTimeView" to intArrayOf(1, 0, 1),
                        "recentMsgView" to intArrayOf(1, 1, 0, 1),
                        "unreadCountView" to intArrayOf(0, 1),
                        "unreadView" to intArrayOf(0, 2)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.ImageView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.ImageView.name)
                                        )
                                ),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                        )
                                                                ),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.WeImageView),
                                                                                ViewTreeItem(CC.WeImageView),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.WeImageView)
                                                                        )
                                                                )

                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }

    val ContactListViewItem: ViewTree by lazy {
        ViewTree(
                mapOf(
                        "innerView" to intArrayOf(1, 0),
                        "contentView" to intArrayOf(1),
                        "titleView" to intArrayOf(1, 0, 3),
                        "headTextView" to intArrayOf(0)
                ),
                ViewTreeItem(
                        Classes.NoDrawingCacheLinearLayout.name,
                        arrayOf(
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.RelativeLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem("com.tencent.mm.ui.AddressView")
                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val ContactListViewItem_7_0_0: ViewTree by lazy {
        ViewTree(
                mapOf("headerView" to intArrayOf(0),
                        "innerView" to intArrayOf(1, 0),
                        "contentView" to intArrayOf(1),
                        "titleView" to intArrayOf(1, 0, 3, 1),
                        "headTextView" to intArrayOf(0, 0)
                ),
                ViewTreeItem(
                        Classes.NoDrawingCacheLinearLayout.name,
                        arrayOf(
                                ViewTreeItem(CC.RelativeLayout.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.RelativeLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.ui.chatting.view.AvatarImageView"),
                                                                                ViewTreeItem("com.tencent.mm.ui.AddressView")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }

    val DiscoverViewItem: ViewTree by lazy {
        ViewTree(
                mapOf("iconImageView" to intArrayOf(0, 0, 0, 0),
                        "titleView" to intArrayOf(0, 0, 0, 1, 0, 0)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.ui.MMImageView"),
                                                                                ViewTreeItem(
                                                                                        CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(
                                                                                                        CC.LinearLayout.name,
                                                                                                        arrayOf(
                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                ViewTreeItem(CC.ImageView.name)
                                                                                                        )
                                                                                                ),
                                                                                                ViewTreeItem(CC.TextView.name)
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val DiscoverViewItem_7_0_0: ViewTree by lazy {
        ViewTree(
                mapOf("iconImageView" to intArrayOf(0, 0, 0, 0),
                        "titleView" to intArrayOf(0, 0, 0, 1, 0, 0, 0),
                        "unreadPointView" to intArrayOf(0, 0, 0, 1, 2, 1),
                        "unreadCountView" to intArrayOf(0, 0, 0, 1, 0, 0, 1),

                        "groupBorderTop" to intArrayOf(0),
                        "contentBorder" to intArrayOf(0),
                        "groupBorderBottom" to intArrayOf(0, 0, 0, 1),
                        "borderRight" to intArrayOf(1)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.ui.MMImageView"),
                                                                                ViewTreeItem(
                                                                                        CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(
                                                                                                        CC.LinearLayout.name,
                                                                                                        arrayOf(
                                                                                                                ViewTreeItem(
                                                                                                                        CC.LinearLayout.name,
                                                                                                                        arrayOf(
                                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                                ViewTreeItem(CC.ImageView.name)
                                                                                                                        )
                                                                                                                ),
                                                                                                                ViewTreeItem(CC.TextView.name)
                                                                                                        )
                                                                                                ))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
    val DiscoverViewItem_7_0_10: ViewTree by lazy {
        ViewTree(
                mapOf("iconImageView" to intArrayOf(1, 0, 0, 0, 0),
                        "titleView" to intArrayOf(1, 0, 0, 0, 1, 0, 0, 0),
                        "unreadPointView" to intArrayOf(1, 0, 0, 0, 1, 2, 1),
                        "unreadCountView" to intArrayOf(1, 0, 0, 0, 1, 0, 0, 1),

                        "groupBorderTop" to intArrayOf(0),
                        "contentBorder" to intArrayOf(1, 0),
                        "groupBorderBottom" to intArrayOf(1, 0, 0, 0, 1),
                        "borderRight" to intArrayOf(1, 1)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(CC.View.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(
                                                                                        CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem("com.tencent.mm.ui.MMImageView"),
                                                                                                ViewTreeItem(
                                                                                                        CC.LinearLayout.name,
                                                                                                        arrayOf(
                                                                                                                ViewTreeItem(
                                                                                                                        CC.LinearLayout.name,
                                                                                                                        arrayOf(
                                                                                                                                ViewTreeItem(
                                                                                                                                        CC.LinearLayout.name,
                                                                                                                                        arrayOf(
                                                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                                                ViewTreeItem(CC.TextView.name),
                                                                                                                                                ViewTreeItem(CC.ImageView.name)
                                                                                                                                        )
                                                                                                                                ),
                                                                                                                                ViewTreeItem(CC.TextView.name)
                                                                                                                        )
                                                                                                                ))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                ))))
    }

    val SettingAvatarView: ViewTree by lazy {
        ViewTree(
                mapOf(
                        "nickNameView" to intArrayOf(0, 1, 0),
                        "wechatTextView" to intArrayOf(0, 1, 1)
                ),
                ViewTreeItem(
                        CC.LinearLayout.name,
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.ImageView.name),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(Classes.NoMeasuredTextView.name),
                                                                ViewTreeItem(CC.TextView.name)
                                                        )
                                                ),
                                                ViewTreeItem(CC.TextView.name)
                                        )
                                ),
                                ViewTreeItem(CC.ImageView.name)
                        )
                ))
    }
    val SettingAvatarView_7_0_0: ViewTree by lazy {
        ViewTree(
                mapOf("headView" to intArrayOf(0),
                        "nickNameView" to intArrayOf(1, 0, 1, 0),
                        "wechatTextView" to intArrayOf(1, 0, 1, 1)
                ),
                ViewTreeItem(
                        CC.RelativeLayout.name,
                        arrayOf(
                                ViewTreeItem(CC.ImageView.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.RelativeLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.ImageView.name),
                                                                ViewTreeItem(CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name),
                                                                                ViewTreeItem(CC.TextView.name)
                                                                        ))

                                                        ))
                                        )
                                )
                        )
                ))
    }
    val SettingAvatarView_7_0_12: ViewTree by lazy {
        ViewTree(
                mapOf("headView" to intArrayOf(0),
                        "nickNameView" to intArrayOf(2, 0, 1, 0, 0),
                        "wechatTextView" to intArrayOf(2, 0, 1, 1, 0)
                ),
                ViewTreeItem(
                        CC.RelativeLayout.name,
                        arrayOf(
                                ViewTreeItem(CC.ImageView.name),
                                ViewTreeItem(CC.FrameLayout.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.RelativeLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.ImageView.name),
                                                                ViewTreeItem(CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(Classes.NoMeasuredTextView.name)
                                                                                        ))
                                                                        )
                                                                ))
                                                )
                                        )
                                )
                        )))
    }

    val ChatRightMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 1, 1, 3)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ProgressBar.name),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                // 适应 7.0.11
                                                                                null
//                                                                        before 7.0.6
//                                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeatTextView")

//                                                                        after
//                                                                        ViewTreeItem(
//                                                                                CC.LinearLayout.name,
//                                                                                arrayOf(
//                                                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeat7extView"),
//                                                                                        ViewTreeItem(CC.TextView.name)
//                                                                                )
//                                                                        )
                                                                        )
                                                                ),
                                                                ViewTreeItem(CC.ViewStub.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                )
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }
    val ChatRightMessageItem_7_0_7: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 1, 1, 3, 0)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ProgressBar.name),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                // 适应 7.0.11
                                                                                null
//                                                                        before 7.0.6
//                                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeatTextView")

//                                                                        after
//                                                                        ViewTreeItem(
//                                                                                CC.LinearLayout.name,
//                                                                                arrayOf(
//                                                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeat7extView"),
//                                                                                        ViewTreeItem(CC.TextView.name)
//                                                                                )
//                                                                        )
                                                                        )
                                                                ),
                                                                ViewTreeItem(CC.ViewStub.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                )
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }

    val ChatLeftMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 1, 1)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
//                                                        former
//                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeatTextView"),
//                                                        7.0.11
//                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMNeat7extView"),
                                                                null,
                                                                ViewTreeItem(
                                                                        CC.RelativeLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.TextView.name)
                                                                        )
                                                                ), null
//                                                        7.0.11 新增
//                                                        android.widget.LinearLayout
//                                                                android.widget.TextView
//                                                                android.view.View
//                                                        android.view.ViewStub

//                                                        ViewTreeItem(CC.ViewStub.name)
                                                        )
                                                )
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }

    val ChatRightAudioMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 5, 0, 0),
                        "msgAnimView" to intArrayOf(3, 5, 0, 1)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                ),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.ProgressBar.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.FrameLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.FrameLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem("com.tencent.mm.ui.base.AnimImageView"),
                                                                                ViewTreeItem(CC.TextView.name)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }
    val ChatLeftAudioMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 1, 3, 0, 0),
                        "msgAnimView" to intArrayOf(3, 1, 3, 0, 1)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                ),
                                                ViewTreeItem(CC.RelativeLayout.name
                                                        , arrayOf(
                                                        ViewTreeItem(CC.TextView.name),
                                                        ViewTreeItem(CC.TextView.name),
                                                        null,
                                                        null
                                                ))
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }

    val ChatRightCallMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 0)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.TextView.name),
                                                                ViewTreeItem(CC.LinearLayout.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                )
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }
    val ChatLeftCallMessageItem: ViewTree by lazy {
        ViewTree(
                mapOf("msgView" to intArrayOf(3, 1)
                ),
                ViewTreeItem(
                        "com.tencent.mm.ui.chatting.viewitems.",
                        arrayOf(
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(CC.View.name),
                                                ViewTreeItem(CC.TextView.name),
                                                ViewTreeItem(CC.View.name)
                                        )
                                ),
                                ViewTreeItem(CC.TextView.name),
                                ViewTreeItem(CC.CheckBox.name),
                                ViewTreeItem(
                                        CC.LinearLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        "com.tencent.mm.ui.base.MaskLayout",
                                                        arrayOf(
                                                                null,
                                                                ViewTreeItem(CC.ViewStub.name),
                                                                ViewTreeItem(CC.View.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        )
                                                ),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(CC.LinearLayout.name),
                                                                ViewTreeItem(CC.TextView.name)
                                                        )),
                                                ViewTreeItem(CC.ImageView.name)
                                        )
                                ),
                                ViewTreeItem(CC.View.name)
                        )
                ))
    }

    val ActionBarContainerItem: ViewTree by lazy {
        ViewTree(
                mapOf(),
                ViewTreeItem(
                        "android.support.v7.widget.ActionBarContainer",
                        arrayOf(
                                ViewTreeItem(
                                        "android.support.v7.widget.Toolbar",
                                        arrayOf(
                                                ViewTreeItem(
                                                        "android.support.v7.widget.ActionMenuView",
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.RelativeLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name))),
                                                                ViewTreeItem(
                                                                        CC.RelativeLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.ImageView.name)))
                                                        )),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name))),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(
                                                                                        CC.RelativeLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                                ViewTreeItem(CC.TextView.name))),
                                                                                ViewTreeItem(CC.TextView.name))))))))))
    }
    val ActionBarInFriendsgroupItem: ViewTree by lazy {
        ViewTree(
                mapOf(),
                ViewTreeItem(
                        "android.support.v7.widget.ActionBarContainer",
                        arrayOf(
                                ViewTreeItem(
                                        "android.support.v7.widget.Toolbar",
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageView.name))),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.ui.widget.AlbumChooserView")
                                                                        )))))))))
    }
    val ActionBarInConversationItem: ViewTree by lazy {
        ViewTree(
                mapOf("title" to intArrayOf(0, 1, 1, 0),
                        "goBackButton" to intArrayOf(0, 1, 0, 0),
                        "infoButton" to intArrayOf(0, 0, 0, 4, 0)
                ),
                ViewTreeItem(
                        "android.support.v7.widget.ActionBarContainer",
                        arrayOf(
                                ViewTreeItem(
                                        "android.support.v7.widget.Toolbar",
                                        arrayOf(
                                                ViewTreeItem(
                                                        "android.support.v7.widget.ActionMenuView",
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.ImageButton.name),
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem(
                                                                                        CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(CC.ImageView.name))),
                                                                                ViewTreeItem(CC.Button.name),
                                                                                ViewTreeItem(
                                                                                        CC.RelativeLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(CC.ImageButton.name),
                                                                                                ViewTreeItem(CC.ImageView.name)))
                                                                        )))),
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.WeImageView))),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem(CC.TextView.name))),
                                                                ViewTreeItem(CC.WeImageView),
                                                                ViewTreeItem(CC.WeImageView),
                                                                ViewTreeItem(CC.ImageView.name),
                                                                ViewTreeItem(CC.ImageView.name),
                                                                ViewTreeItem(CC.ImageView.name)
                                                        ))
                                        )),
                                ViewTreeItem("android.support.v7.widget.ActionBarContextView")
                        )))
    }

    val ChattingUILayoutItem: ViewTree by lazy {
        ViewTree(
                mapOf("chattingBgShade" to intArrayOf(2, 0, 0, 1),
                        "chatFooterChild2" to intArrayOf(2, 1, 0),
                        "chatFooterChild2_switchButton" to intArrayOf(0, 1, 0),
                        "chatFooterChild2_editText" to intArrayOf(0, 1, 1, 0, 0),
                        "chatFooterChild2_talkButton" to intArrayOf(0, 1, 2),
                        "chatFooterChild2_faceButton" to intArrayOf(0, 1, 3, 0),
                        "chatFooterChild2_addButton" to intArrayOf(0, 1, 4, 1, 0),
                        "chatFooterChild2_sendButton" to intArrayOf(0, 1, 4, 1, 2)),
                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingUILayout", arrayOf(
                        ViewTreeRepoThisVersion.ActionBarInConversationItem.item,
                        ViewTreeItem("android.view.ViewStub"),
                        ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingScrollLayout", arrayOf(
                                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingContent", arrayOf(
                                        ViewTreeItem(CC.FrameLayout.name, arrayOf(
                                                ViewTreeItem("com.tencent.mm.ui.chatting.ChattingImageBGView"),
                                                ViewTreeItem(CC.ImageView.name))))),
                                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChatFooter", arrayOf(
                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                ViewTreeItem(CC.WeImageView),
                                                                ViewTreeItem(CC.WeImageView),
                                                                ViewTreeItem(CC.ImageView.name))),
                                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                ViewTreeItem(CC.WeImageButton),
                                                                ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                        ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.MaxHeightScrollView", arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.ui.widget.MMEditText"))),
                                                                        ViewTreeItem(CC.RelativeLayout.name))),
                                                                ViewTreeItem(CC.Button.name),
                                                                ViewTreeItem(CC.RelativeLayout.name, arrayOf(
                                                                        ViewTreeItem(CC.WeImageButton))),
                                                                ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                        ViewTreeItem(CC.TextView.name),
                                                                        ViewTreeItem(CC.RelativeLayout.name, arrayOf(
                                                                                ViewTreeItem(CC.WeImageButton),
                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                ViewTreeItem(CC.Button.name))))))))))))))))))
    }

    val ChattingUILayoutItem_7_0_13: ViewTree by lazy {
        ViewTree(
                mapOf("chattingBgShade" to intArrayOf(2, 0, 0, 1),
                        "chatFooterChild2" to intArrayOf(2, 1, 0, 1),
                        "chatFooterChild2_switchButton" to intArrayOf(0, 1, 0),
                        "chatFooterChild2_editText" to intArrayOf(0, 1, 1, 0, 0),
                        "chatFooterChild2_talkButton" to intArrayOf(0, 1, 2),
                        "chatFooterChild2_faceButton" to intArrayOf(0, 1, 3, 0),
                        "chatFooterChild2_addButton" to intArrayOf(0, 1, 4, 1, 0),
                        "chatFooterChild2_sendButton" to intArrayOf(0, 1, 4, 1, 2)),
                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingUILayout", arrayOf(
                        ViewTreeRepoThisVersion.ActionBarInConversationItem.item,
                        ViewTreeItem("android.view.ViewStub"),
                        ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingScrollLayout", arrayOf(
                                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChattingContent", arrayOf(
                                        ViewTreeItem(CC.FrameLayout.name, arrayOf(
                                                ViewTreeItem("com.tencent.mm.ui.chatting.ChattingImageBGView"),
                                                ViewTreeItem(CC.ImageView.name))))),
                                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.ChatFooter", arrayOf(
                                        ViewTreeItem(CC.RelativeLayout.name, arrayOf(
                                                ViewTreeItem(CC.RelativeLayout.name),
                                                ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                ViewTreeItem(CC.LinearLayout.name),
                                                                ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                        ViewTreeItem(CC.WeImageButton),
                                                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                                ViewTreeItem("com.tencent.mm.pluginsdk.ui.chat.MaxHeightScrollView", arrayOf(
                                                                                        ViewTreeItem("com.tencent.mm.ui.widget.MMEditText"))),
                                                                                ViewTreeItem(CC.RelativeLayout.name))),
                                                                        ViewTreeItem(CC.Button.name),
                                                                        ViewTreeItem(CC.RelativeLayout.name, arrayOf(
                                                                                ViewTreeItem(CC.WeImageButton))),
                                                                        ViewTreeItem(CC.LinearLayout.name, arrayOf(
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem(CC.RelativeLayout.name, arrayOf(
                                                                                        ViewTreeItem(CC.WeImageButton),
                                                                                        ViewTreeItem(CC.ImageView.name),
                                                                                        ViewTreeItem(CC.Button.name))))))))))))))))))))
    }

    val ActionBarItem: ViewTree by lazy {
        ViewTree(
                mapOf("miniProgramPage" to intArrayOf(0),
                        "miniProgramPage_actionBarPage" to intArrayOf(1, 1),
                        "actionBarPage_title" to intArrayOf(0),
                        "actionBarPage_searchIcon" to intArrayOf(1),
                        "actionBarPage_addIcon" to intArrayOf(2),
                        "miniProgramPage_appBrandDesktopView" to intArrayOf(0, 0, 2, 0),
                        "appBrandDesktopView_searchEditText" to intArrayOf(0, 0),
                        "appBrandDesktopView_miniProgramTitle" to intArrayOf(2, 0, 0)
                ),
                ViewTreeItem(
                        "com.tencent.mm.plugin.appbrand.widget.desktop.AppBrandDesktopContainer",
                        arrayOf(
                                ViewTreeItem(
                                        CC.RelativeLayout.name,
                                        arrayOf(
                                                ViewTreeItem(
                                                        CC.LinearLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                // 小程序
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem(CC.View.name),
                                                                                ViewTreeItem(
                                                                                        CC.LinearLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(
                                                                                                        "com.tencent.mm.plugin.appbrand.widget.desktop.AppBrandDesktopView",
                                                                                                        arrayOf(null))
                                                                                        )))))),
                                                ViewTreeItem(
                                                        CC.FrameLayout.name,
                                                        arrayOf(
                                                                ViewTreeItem("com.tencent.mm.plugin.appbrand.widget.desktop.AppBrandDesktopBottomView"),
                                                                ViewTreeItem(
                                                                        CC.LinearLayout.name,
                                                                        arrayOf(
                                                                                ViewTreeItem(CC.TextView.name),
                                                                                ViewTreeItem(
                                                                                        CC.RelativeLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                                ViewTreeItem(CC.ImageView.name)
                                                                                        )),
                                                                                ViewTreeItem(
                                                                                        CC.RelativeLayout.name,
                                                                                        arrayOf(
                                                                                                ViewTreeItem(CC.ImageView.name),
                                                                                                ViewTreeItem(CC.ImageView.name)
                                                                                        ))
                                                                        ))
                                                        )
                                                )
                                        )
                                )
                        )
                ))
    }
}
