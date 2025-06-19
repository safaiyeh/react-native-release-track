// Reexport the native module. On web, it will be resolved to ReactNativeTestflightModule.web.ts
// and on native platforms to ReactNativeTestflightModule.ts
export { default } from './ReactNativeTestflightModule';
export { default as ReactNativeTestflightView } from './ReactNativeTestflightView';
export * from  './ReactNativeTestflight.types';
