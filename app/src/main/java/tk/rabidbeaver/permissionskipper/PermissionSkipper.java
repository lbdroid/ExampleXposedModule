package tk.rabidbeaver.permissionskipper;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PermissionSkipper implements IXposedHookLoadPackage {
    public static final String TAG = "PermissionSkipper";
    public static final String SERVICE = "android.app.ContextImpl";

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!"android".equals(lpparam.packageName)) return;

        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass(SERVICE, lpparam.classLoader), "enforceCallingOrSelfPermission", skipPermissionHook);
        } catch (Throwable t) {
            Log.d(TAG, t.getMessage());
        }
    }

    private final XC_MethodHook skipPermissionHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            if (((String)param.args[0]).contains("android.permission.DEVICE_POWER")){
                Log.d(TAG, "skipping permission");
                param.setResult(null);
            }
        }
    };
}
