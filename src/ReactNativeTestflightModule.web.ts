import { registerWebModule, NativeModule } from 'expo';

import { ReactNativeTestflightModuleEvents } from './ReactNativeTestflight.types';

class ReactNativeTestflightModule extends NativeModule<ReactNativeTestflightModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ReactNativeTestflightModule, 'ReactNativeTestflightModule');
