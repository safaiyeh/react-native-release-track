package expo.modules.releasetrack

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import android.content.pm.PackageManager

class ReactNativeReleaseTrackModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("ReactNativeReleaseTrack")

    Constants(
      "environment" to detectEnvironment()
    )

    Function("getEnvironment") {
      detectEnvironment()
    }
  }

  private fun detectEnvironment(): String {
    val context = appContext.reactContext ?: return "UNKNOWN"

    if (runningOnEmulator()) {
      return "SIMULATOR"
    }

    val metaOverride = try {
      val ai = context.packageManager.getApplicationInfo(
        context.packageName,
        PackageManager.GET_META_DATA
      )
      ai.metaData?.getString("com.reactnativereleasetrack.track")
    } catch (e: Exception) { null }

    if (metaOverride?.equals("test", ignoreCase = true) == true) {
      return "TESTFLIGHT"
    }

    val installer = try {
      context.packageManager.getInstallerPackageName(context.packageName)
    } catch (e: Exception) { null }

    return if (installer == null || installer != "com.android.vending") {
      "TESTFLIGHT"
    } else {
      "PRODUCTION"
    }
  }

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