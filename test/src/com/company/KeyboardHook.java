package com.company;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class KeyboardHook implements Runnable {

    private static WinUser.HHOOK hhk;
    private static WinUser.LowLevelKeyboardProc keyboardHook;
    final static User32 lib = User32.INSTANCE;
    private boolean [] on_off = null;

    public KeyboardHook(boolean [] on_off){
        this.on_off = on_off;
    }

    public void run(){
        WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = new WinUser.LowLevelKeyboardProc() {
            @Override
            public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wparam, WinUser.KBDLLHOOKSTRUCT info) {
                return lib.CallNextHookEx(hhk,nCode,wParam,info.getPointer());
            }
        }
    }

}
