import { requireNativeView } from 'expo';
import * as React from 'react';

import { ReactNativeTestflightViewProps } from './ReactNativeTestflight.types';

const NativeView: React.ComponentType<ReactNativeTestflightViewProps> =
  requireNativeView('ReactNativeTestflight');

export default function ReactNativeTestflightView(props: ReactNativeTestflightViewProps) {
  return <NativeView {...props} />;
}
