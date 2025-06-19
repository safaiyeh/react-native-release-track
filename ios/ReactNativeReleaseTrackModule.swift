import ExpoModulesCore
import Foundation

public class ReactNativeReleaseTrackModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ReactNativeReleaseTrack")

    Constants([
      "environment": self.detectEnvironment()
    ])

    Function("getEnvironment") { () -> String in
      return self.detectEnvironment()
    }
  }

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