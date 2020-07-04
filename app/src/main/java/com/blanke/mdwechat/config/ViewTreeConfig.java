package com.blanke.mdwechat.config;

import com.blanke.mdwechat.Version;
import com.blanke.mdwechat.ViewTreeRepo;

import java.lang.reflect.Method;

//Created by JoshCai.
//usage:
//        1. add Method
//        2. add getter
//        3. add init code [private void init()]
//
//         goto ViewTreeRepo.kt
//        4. add com.blanke.mdwechat.ViewTreeRepoThisVersion
//        5. add com.blanke.mdwechat.ViewTreeRepo
public class ViewTreeConfig {
    private Version versionName = new Version("0");
    private static ViewTreeConfig viewTreeConfig = null;

    private Method ConversationListViewItem = null;
    private Method ContactListViewItem = null;
    private Method ContactHeaderItem = null;
    private Method ContactCompanySumItem = null;
    private Method ContactCompanyHeaderItem = null;
    private Method ContactCompanyListViewItem = null;
    private Method DiscoverViewItem = null;
    private Method SettingAvatarView = null;
    private Method ChatRightMessageItem = null;
    private Method ChatLeftMessageItem = null;
    private Method ChatRightAudioMessageItem = null;
    private Method ChatLeftAudioMessageItem = null;
    private Method ChatRightCallMessageItem = null;
    private Method ChatLeftCallMessageItem = null;
    private Method RefRightMessageItem = null;
    private Method RefLeftMessageItem = null;
    private Method ActionBarItem = null;
    private Method ActionBarContainerItem = null;
    private Method ActionBarInFriendsgroupItem = null;
    private Method ActionBarInSubscriptsItem = null;
    private Method ActionBarInConversationItem = null;
    private Method ActionBarInSearchConversationItem = null;
    private Method ChattingScrollLayoutItem = null;
    private Method ChattingUILayoutItem = null;
    private Method ChattingUILayoutInSearchItem = null;

    //region getters
    public Method getConversationListViewItem() {
        return ConversationListViewItem;
    }


    public Method getContactListViewItem() {
        return ContactListViewItem;
    }

    public Method getContactHeaderItem() {
        return ContactHeaderItem;
    }

    public Method getContactCompanySumItem() {
        return ContactCompanySumItem;
    }

    public Method getContactCompanyHeaderItem() {
        return ContactCompanyHeaderItem;
    }

    public Method getContactCompanyListViewItem() {
        return ContactCompanyListViewItem;
    }

    public Method getDiscoverViewItem() {
        return DiscoverViewItem;
    }

    public Method getSettingAvatarView() {
        return SettingAvatarView;
    }

    public Method getChatRightMessageItem() {
        return ChatRightMessageItem;
    }

    public Method getChatLeftMessageItem() {
        return ChatLeftMessageItem;
    }

    public Method getChatRightAudioMessageItem() {
        return ChatRightAudioMessageItem;
    }

    public Method getChatLeftAudioMessageItem() {
        return ChatLeftAudioMessageItem;
    }

    public Method getChatRightCallMessageItem() {
        return ChatRightCallMessageItem;
    }

    public Method getChatLeftCallMessageItem() {
        return ChatLeftCallMessageItem;
    }

    public Method getRefRightMessageItem() {
        return RefRightMessageItem;
    }

    public Method getRefLeftMessageItem() {
        return RefLeftMessageItem;
    }

    public Method getActionBarItem() {
        return ActionBarItem;
    }

    public Method getActionBarContainerItem() {
        return ActionBarContainerItem;
    }

    public Method getActionBarInFriendsgroupItem() {
        return ActionBarInFriendsgroupItem;
    }

    public Method getActionBarInSubscriptsItem() {
        return ActionBarInSubscriptsItem;
    }

    public Method getActionBarInConversationItem() {
        return ActionBarInConversationItem;
    }

    public Method getActionBarInSearchConversationItem() {
        return ActionBarInSearchConversationItem;
    }

    public Method getChattingScrollLayoutItem() {
        return ChattingScrollLayoutItem;
    }

    public Method getChattingUILayoutItem() {
        return ChattingUILayoutItem;
    }

    public Method getChattingUILayoutInSearchItem() {
        return ChattingUILayoutInSearchItem;
    }
    //endregion

    public static ViewTreeConfig get() {
        if (viewTreeConfig == null) {
            viewTreeConfig = new ViewTreeConfig();
            viewTreeConfig.init(viewTreeConfig.versionName);
        }
        return viewTreeConfig;
    }

    public static ViewTreeConfig set(Version versionName) {
        viewTreeConfig = new ViewTreeConfig();
        viewTreeConfig.versionName = versionName;
        viewTreeConfig.init(viewTreeConfig.versionName);
        return viewTreeConfig;
    }

    public static ViewTreeConfig set(String versionName) {
        return set(new Version(versionName));
    }

    private void init(Version versionName) {
        ConversationListViewItem = findViewTreeMethod("ConversationListViewItem", versionName);
        ContactListViewItem = findViewTreeMethod("ContactListViewItem", versionName);
        ContactHeaderItem = findViewTreeMethod("ContactHeaderItem", versionName);
        ContactCompanySumItem = findViewTreeMethod("ContactCompanySumItem", versionName);
        ContactCompanyHeaderItem = findViewTreeMethod("ContactCompanyHeaderItem", versionName);
        ContactCompanyListViewItem = findViewTreeMethod("ContactCompanyListViewItem", versionName);
        DiscoverViewItem = findViewTreeMethod("DiscoverViewItem", versionName);
        SettingAvatarView = findViewTreeMethod("SettingAvatarView", versionName);
        ChatRightMessageItem = findViewTreeMethod("ChatRightMessageItem", versionName);
        ChatLeftMessageItem = findViewTreeMethod("ChatLeftMessageItem", versionName);
        ChatRightAudioMessageItem = findViewTreeMethod("ChatRightAudioMessageItem", versionName);
        ChatLeftAudioMessageItem = findViewTreeMethod("ChatLeftAudioMessageItem", versionName);
        ChatRightCallMessageItem = findViewTreeMethod("ChatRightCallMessageItem", versionName);
        ChatLeftCallMessageItem = findViewTreeMethod("ChatLeftCallMessageItem", versionName);
        RefRightMessageItem = findViewTreeMethod("RefRightMessageItem", versionName);
        RefLeftMessageItem = findViewTreeMethod("RefLeftMessageItem", versionName);
        ActionBarItem = findViewTreeMethod("ActionBarItem", versionName);
        ActionBarContainerItem = findViewTreeMethod("ActionBarContainerItem", versionName);
        ActionBarInSubscriptsItem = findViewTreeMethod("ActionBarInSubscriptsItem", versionName);
        ActionBarInConversationItem = findViewTreeMethod("ActionBarInConversationItem", versionName);
        ActionBarInSearchConversationItem = findViewTreeMethod("ActionBarInSearchConversationItem", versionName);
        ChattingScrollLayoutItem = findViewTreeMethod("ChattingScrollLayoutItem", versionName);
        ChattingUILayoutItem = findViewTreeMethod("ChattingUILayoutItem", versionName);
        ChattingUILayoutInSearchItem = findViewTreeMethod("ChattingUILayoutInSearchItem", versionName);
        ActionBarInFriendsgroupItem = findViewTreeMethod("ActionBarInFriendsgroupItem", versionName);
    }

    private static Method findViewTreeMethod(String name, Version versionName) {
        Method[] methods = ViewTreeRepo.class.getMethods();
        Method output = null;
        Version version = new Version("0");
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("get" + name)) {
                Version versionTmp;
                if (methodName.length() > 4 + name.length()) {
                    versionTmp = new Version(methodName.substring(4 + name.length()).replace('_', '.'));
                } else {
                    versionTmp = new Version("0");
                }
                if (versionTmp.compareTo(versionName) <= 0) {
                    if ((output == null) || (versionTmp.compareTo(version) >= 0)) {
                        output = method;
                        version = versionTmp;
                    }
                }
            }
        }
        return output;
    }

}