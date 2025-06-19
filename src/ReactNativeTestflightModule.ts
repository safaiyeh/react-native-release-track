import { NativeModule, requireNativeModule } from 'expo';

import { ReactNativeTestflightModuleEvents } from './ReactNativeTestflight.types';

declare class ReactNativeTestflightModule extends NativeModule<ReactNativeTestflightModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ReactNativeTestflightModule>('ReactNativeTestflight');
