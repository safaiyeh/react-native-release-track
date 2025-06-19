import * as React from 'react';

import { ReactNativeTestflightViewProps } from './ReactNativeTestflight.types';

export default function ReactNativeTestflightView(props: ReactNativeTestflightViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
