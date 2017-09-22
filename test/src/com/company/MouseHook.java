package com.company;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class MouseHook {
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_LBUTTONUP = 514;//鼠标事件编码

    public User32 lib;
    private static WinUser.HHOOK hhk;
    private MouseHookListener mouseHook;
    private WinDef.HMODULE hMod;
    private boolean isWindows = false;
    public MouseHook() {
        isWindows = Platform.isWindows();
        if (isWindows) {
            lib = User32.INSTANCE;
            hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        }
    }
}
