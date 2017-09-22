package com.company;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public abstract class MouseHookListener implements WinUser.HOOKPROC {
    public User32 lib = null;//windows应用程序接口
    public WinUser.HHOOK hhk;//钩子的句柄
    public abstract WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, MouseHookStruct lParam);
}
