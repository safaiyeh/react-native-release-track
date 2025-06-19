import ExpoModulesCore
import Foundation

public class ReactNativeTestflightModule: Module {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  public func definition() -> ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ReactNativeTestflight')` in JavaScript.
    Name("ReactNativeTestflight")

    // Expose the detected environment to JavaScript as a constant.
    Constants([
      "environment": self.detectEnvironment()
    ])

    // Provide a simple synchronous function so the environment can be queried from JS.
    Function("getEnvironment") { () -> String in
      return self.detectEnvironment()
    }
  }

  /// Returns a string describing the run-time environment the app is currently running under.
  /// Possible values: "SIMULATOR", "TESTFLIGHT", "PRODUCTION".
  private func detectEnvironment() -> String {
    #if targetEnvironment(simulator)
      return "SIMULATOR"
    #else
      if let receiptURL = Bundle.main.appStoreReceiptURL,
         receiptURL.lastPathComponent == "sandboxReceipt" {
        return "TESTFLIGHT"
      }
      return "PRODUCTION"
    #endif
  }
}
