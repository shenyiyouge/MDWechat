package com.blanke.mdwechat.config;

import com.blanke.mdwechat.Version;
import com.blanke.mdwechat.ViewTreeRepo;

import java.lang.reflect.Method;

public class ViewTreeConfig {
    private Version versionName = new Version("0");
    private static ViewTreeConfig viewTreeConfig = null;

    private Method ConversationListViewItem = null;
    private Method ContactListViewItem = null;
    private Method DiscoverViewItem = null;
    private Method SettingAvatarView = null;
    private Method ChatRightMessageItem = null;
    private Method ChatLeftMessageItem = null;
    private Method ChatRightAudioMessageItem = null;
    private Method ChatLeftAudioMessageItem = null;
    private Method ChatRightCallMessageItem = null;
    private Method ChatLeftCallMessageItem = null;
    private Method ActionBarItem = null;

    //region getters
    public Method getConversationListViewItem() {
        return ConversationListViewItem;
    }

    public Method getContactListViewItem() {
        return ContactListViewItem;
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

    public Method getActionBarItem() {
        return ActionBarItem;
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
        DiscoverViewItem = findViewTreeMethod("DiscoverViewItem", versionName);
        SettingAvatarView = findViewTreeMethod("SettingAvatarView", versionName);
        ChatRightMessageItem = findViewTreeMethod("ChatRightMessageItem", versionName);
        ChatLeftMessageItem = findViewTreeMethod("ChatLeftMessageItem", versionName);
        ChatRightAudioMessageItem = findViewTreeMethod("ChatRightAudioMessageItem", versionName);
        ChatLeftAudioMessageItem = findViewTreeMethod("ChatLeftAudioMessageItem", versionName);
        ChatRightCallMessageItem = findViewTreeMethod("ChatRightCallMessageItem", versionName);
        ChatLeftCallMessageItem = findViewTreeMethod("ChatLeftCallMessageItem", versionName);
        ActionBarItem = findViewTreeMethod("ActionBarItem", versionName);
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