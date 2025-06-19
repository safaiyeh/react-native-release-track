package expo.modules.testflight

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.content.pm.PackageManager

class ReactNativeTestflightModule : Module() {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ReactNativeTestflight')` in JavaScript.
    Name("ReactNativeTestflight")

    // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    Constants(
      "environment" to detectEnvironment()
    )

    // Defines event names that the module can send to JavaScript.
    Function("getEnvironment") {
      detectEnvironment()
    }
  }

  private fun detectEnvironment(): String {
    val context = appContext.reactContext

    // If React context isn't yet available (e.g., while computing constants) we can't reliably
    // inspect installation metadata. Surface an indeterminate state.
    if (context == null) {
      return "UNKNOWN"
    }

    // 1. Emulator check takes highest priority.
    if (runningOnEmulator()) {
      return "SIMULATOR"
    }

    // 2. Allow explicit override via manifest metadata: <meta-data android:name="com.reactnativetestflight.track" android:value="test" />
    val metaOverride = try {
      val ai = context.packageManager.getApplicationInfo(
        context.packageName,
        PackageManager.GET_META_DATA
      )
      ai.metaData?.getString("com.reactnativetestflight.track")
    } catch (e: Exception) { null }

    if (metaOverride?.equals("test", ignoreCase = true) == true) {
      return "TESTFLIGHT"
    }

    // 3. Determine the installer source. If the app wasn't installed from the Play Store
    // (package ID `com.android.vending`), treat it as a TestFlight-like build for now.
    // Note: this won't distinguish Play testing tracks, but covers sideloaded/APK stores.
    val installer = try {
      context.packageManager.getInstallerPackageName(context.packageName)
    } catch (e: Exception) {
      null
    }

    return if (installer == null || installer != "com.android.vending") {
      "TESTFLIGHT"
    } else {
      "PRODUCTION"
    }
  }

  /**
   * Attempts to determine if the code is running on an Android emulator or physical device.
   * This heuristic looks at several Build.* fields documented by Google and commonly used in
   * open-source libraries. It isn't fool-proof but covers the typical emulator setups shipped
   * with Android Studio and third-party tools.
   */
  private fun runningOnEmulator(): Boolean {
    val fingerprint = android.os.Build.FINGERPRINT?.lowercase() ?: ""
    val model = android.os.Build.MODEL?.lowercase() ?: ""
    val brand = android.os.Build.BRAND?.lowercase() ?: ""
    val device = android.os.Build.DEVICE?.lowercase() ?: ""
    val product = android.os.Build.PRODUCT?.lowercase() ?: ""

    return (
      fingerprint.startsWith("generic") || fingerprint.contains("unknown") ||
      model.contains("google_sdk") || model.contains("emulator") || model.contains("android sdk built for") ||
      brand.startsWith("generic") && device.startsWith("generic") ||
      "google_sdk" == product
    )
  }
}
