import { NativeModule, requireNativeModule } from 'expo';

// Enum describing the possible runtime environments the native side can detect.
export enum Environment {
  /** Running on an iOS Simulator or Android Emulator */
  SIMULATOR = 'SIMULATOR',
  /** TestFlight (iOS) or any pre-release / non-Play-Store build (Android) */
  TESTFLIGHT = 'TESTFLIGHT',
  /** Production build downloaded from the App Store / Play Store */
  PRODUCTION = 'PRODUCTION',
  /** Indeterminate runtime; detection not yet possible */
  UNKNOWN = 'UNKNOWN',
}

declare class ReactNativeTestflightModule extends NativeModule {
  /**
   * Detected runtime environment.
   */
  environment: Environment;

  /**
   * Returns the current runtime environment.
   */
  getEnvironment(): Environment;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ReactNativeTestflightModule>('ReactNativeTestflight');
