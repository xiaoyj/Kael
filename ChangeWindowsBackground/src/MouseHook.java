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

    public void addMouseHookListener(MouseHookListener mouseHook){
        this.mouseHook = mouseHook;
        this.mouseHook.lib = lib;
    }

    public void startWindowsHookEx(){
        if(isWindows){
            lib.SetWindowsHookEx(WinUser.WH_MOUSE_LL,mouseHook,hMod,0);
            int result;
            WinUser.MSG msg = new WinUser.MSG();
            while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
                if (result == -1) {
                    System.out.println("error in get message");
                    break;
                }else{
                    System.out.println("got message");
                    lib.TranslateMessage(msg);
                    lib.DispatchMessage(msg);
                }
            }
        }
    }

    public void stopWindowsHoookEx(){
        if(isWindows){
            lib.UnhookWindowsHookEx(hhk);
        }
    }

    public static void main(String[] args){
        try{
            MouseHook mouseHook = new MouseHook();
            mouseHook.addMouseHookListener(new MouseHookListener() {
                @Override
                public WinDef.LRESULT callback(int nCode, WinDef.WPARAM wParam, MouseHookStruct lParam) {
                    if(nCode>=0){
                        switch(wParam.intValue()){
                            case MouseHook.WM_LBUTTONDOWN:
                                System.err.println("mouse down left button down, x=" + lParam.pt.x + " y=" + lParam.pt.y);
                                break;
                            case MouseHook.WM_LBUTTONUP:
                                ChangeTheWallPicture hi = new ChangeTheWallPicture();
                                System.err.println("mouse up left button up, x=" + lParam.pt.x + " y=" + lParam.pt.y);
                                break;
                        }
                    }
                    return lib.CallNextHookEx(hhk,nCode,wParam,lParam.getPointer());
                }
            });
            mouseHook.startWindowsHookEx();

            Thread.sleep(20000);
            mouseHook.stopWindowsHoookEx();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
