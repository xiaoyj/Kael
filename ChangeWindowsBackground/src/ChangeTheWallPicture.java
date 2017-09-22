import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.win32.StdCallLibrary;

public class ChangeTheWallPicture {
    public void ChangeTheWallPicture() {
        try {
            String fullFnm = "C:/Users/Administrator/Pictures/lovewallpaper/286485-106.jpg";
            //System.out.println("Full fnm: " + fullFnm);
      /* 3 registry key changes to HKEY_CURRENT_USER\Control Panel\Desktop
         These three keys (and many others) are explained at
            http://www.virtualplastic.net/html/desk_reg.html

         List of registry functions at MSDN:
                http://msdn.microsoft.com/en-us/library/ms724875(v=VS.85).aspx
      */
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                    "Control Panel\\Desktop", "Wallpaper", fullFnm);
            //WallpaperStyle = 10 (Fill), 6 (Fit), 2 (Stretch), 0 (Tile), 0 (Center)
            //For windows XP, change to 0
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                    "Control Panel\\Desktop", "WallpaperStyle", "10"); //fill
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                    "Control Panel\\Desktop", "TileWallpaper", "0");   // no tiling

            // refresh the desktop using User32.SystemParametersInfo(), so avoiding an OS reboot
            int SPI_SETDESKWALLPAPER = 0x14;
            int SPIF_UPDATEINIFILE = 0x01;
            int SPIF_SENDWININICHANGE = 0x02;

            boolean result = MyUser32.INSTANCE.SystemParametersInfoA(SPI_SETDESKWALLPAPER, 0,
                    fullFnm, SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE);
            System.out.println("Refresh desktop result: " + result);
        } catch (Exception e) {
            System.out.println("Could not find directory path");
        }
    }

    private interface MyUser32 extends StdCallLibrary
  /* JNA Win32 extensions includes a User32 class, but it doesn't contain
     SystemParametersInfo(), so it must be defined here.

     MSDN libary docs on SystemParametersInfo() are at:
           http://msdn.microsoft.com/en-us/library/ms724947(VS.85).aspx

      BOOL WINAPI SystemParametersInfo(
          __in     UINT uiAction,
          __in     UINT uiParam,
          __inout  PVOID pvParam,
          __in     UINT fWinIni );

     When uiAction == SPI_SETDESKWALLPAPER, SystemParametersInfo() sets the desktop wallpaper.
     The value of the pvParam parameter determines the new wallpaper.
  */
    {
        MyUser32 INSTANCE = (MyUser32) Native.loadLibrary("user32", MyUser32.class);

        boolean SystemParametersInfoA(int uiAction, int uiParam, String fnm, int fWinIni);
        // SystemParametersInfoA() is the ANSI name used in User32.dll
    }  // end of MyUser32 interface
}
