# ExampleXposedModule

Important points to be aware of;
1) metadata blocks in the AndroidManifest.xml
2) assets/xposed_init is a newline separated list of classes in this package that are xposed modules. There may be multiple.
3) The module itself, in this case tk/rabidbeaver/permissionskipper/PermissionSkipper.java

This module is checking that the package name is "android", and otherwise terminating. Essentially, it means it only loads
for the Android SYSTEM. This can be changed to match any package that you want to hook into. But if you want to do something
GLOBAL, it has to hook into the system.

XposedBridge.hookAllMethods: this one basically hooks every method in a particular class (SERVICE = android.app.ContextImpl
in this case, which should be changed to the class having the method you want to hook) with a particular name (here we are
hooking "enforceCallingOrSelfPermission" which is the function called to validate permissions). It also specifies the hook
to call, "skipPermissionHook", defined just below. Be aware of overloaded method declarations when using hookAllMethods. It
will hook everything with that name, regardless of its arguments. There are other options besides hookAllMethods that allow
you to specify which method to hook, in the case of overloaded methods. You also have the option of REPLACING methods.

XC_MethodHook: Here you specify the code to run before or after the hooked method. In this case, BEFORE. If you look at the
source for enforceCallingOrSelfPermission, you will see that the first argument (index 0) is a String containing the specific
permission being validated. So what we are doing in this particular example, is IF the permission being validated is
"android.permission.DEVICE_POWER", then we return immediately (param.setResult) rather than actually running the method.
This method is a void return, rather than the true/false that one might anticipate from a "check permission" function. Rather
than returning a result, it either throws a security exception, or it does NOTHING.
