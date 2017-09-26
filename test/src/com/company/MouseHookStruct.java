package com.company;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;

import java.util.Arrays;
import java.util.List;

public class MouseHookStruct extends Structure {
    public static class ByReference extends MouseHookStruct implements Structure.ByReference{};
        public WinDef.POINT pt;//点坐标
        public WinDef.HWND hwnd;//窗口句柄
        public int wHitTestCode;
        public BaseTSD.ULONG_PTR dwExtraInfo;//拓展信息
    @Override//
    protected List getFieldOrder() {
        return Arrays.asList("dwExtraInfo","hwnd","pt","wHitTestCode");
    }
}
