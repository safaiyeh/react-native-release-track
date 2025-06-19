# React Native Testflight

Detect at runtime whether your React-Native app is running in:

* **SIMULATOR** – iOS Simulator / Android Emulator
* **TESTFLIGHT** – TestFlight build on iOS, *or* any non-Play-Store / pre-release build on Android
* **PRODUCTION** – Production release from the App Store / Play Store
* **UNKNOWN** – Indeterminate state when detection isn't yet possible (very early app init)

The library exposes a **constant** and a **function** so you can branch logic or surface UI messages without shipping separate builds.

---

## Installation

```bash
# with npm
yarn add react-native-testflight
# or
yarn add react-native-testflight
# if you use Expo managed workflow
npx expo install react-native-testflight
```

This is an [Expo Modules](https://docs.expo.dev/modules/overview/)–style package. If you are on bare React-Native the native code is automatically linked via `pod install` (iOS) and Gradle (Android).

---

## Usage

```ts
import Testflight, { Environment } from 'react-native-testflight';

// 1. constant (preferred – synchronous)
console.log(Testflight.environment);   // Environment.SIMULATOR | Environment.TESTFLIGHT | Environment.PRODUCTION | Environment.UNKNOWN

// 2. function (identical value, but callable later)
const env: Environment = Testflight.getEnvironment();

if (env === Environment.TESTFLIGHT) {
  // ...
}
```

### Safe-guarding for Web

When bundling with **react-native-web** the module resolves to `null`. Guard your calls accordingly:

```ts
if (Testflight && Testflight.environment === 'TESTFLIGHT') {
  // …
}
```

---

## iOS implementation

| Situation        | Value returned |
|------------------|----------------|
| Simulator        | `SIMULATOR`    |
| TestFlight (sandbox receipt) | `TESTFLIGHT` |
| App Store build  | `PRODUCTION`   |
| Early init (context unavailable) | `UNKNOWN` |

Logic lives in `detectEnvironment()` inside the Swift module and relies on:

* `#if targetEnvironment(simulator)`
* `Bundle.main.appStoreReceiptURL?.lastPathComponent == "sandboxReceipt"`

No extra configuration required.

---

## Android implementation & nuances

Android doesn't expose which **Play track** (internal, alpha, beta) the app came from. We therefore use a pragmatic hierarchy:

1. **Override via manifest** – you can explicitly mark a build as test by adding:

   ```xml
   <!-- AndroidManifest.xml -->
   <application …>
     <meta-data
         android:name="com.reactnativetestflight.track"
         android:value="test"/>
   </application>
   ```

   You can inject this only in your `internal`, `alpha`, or `beta` productFlavors so production stays clean.

2. **Emulator detection** – Build fingerprint contains `generic`/`emulator` → `SIMULATOR`.
3. **Installer package** – If the app *wasn't* installed from the official Play Store (`com.android.vending`) it's considered `TESTFLIGHT` (covers sideloads, alternative stores, etc.).
4. Fallback → `UNKNOWN` (very early init) otherwise `PRODUCTION`.

> **Note:** Because Google keeps track membership on its servers, *Play Store* internal/alpha/beta installs cannot be distinguished from production at runtime – you **must** use the manifest override (or another build-time flag) if you need that distinction.

---

## API reference

| Member                | Type      | Platform | Description |
|-----------------------|-----------|----------|-------------|
| `environment`         | `Environment`  | native   | Synchronous constant. Runtime environment enum. May be `UNKNOWN` very early. |
| `getEnvironment()`    | `() => Environment` | native | Same value, callable any time. |

On Web the default export is `null` and therefore none of the members exist.

---

## Example app

A minimal Expo example is in the `example/` folder. It prints the detected environment:

```tsx
import Testflight, { Environment } from 'react-native-testflight';
import { Text } from 'react-native';

export default function App() {
  return <Text>{Testflight?.environment}</Text>;
}
```

---

## Contributing / Issues

Found a bug or have an improvement? Please open an issue or PR.

1. Clone repo & install deps: `pnpm install` / `yarn`.
2. Run the example app via `expo run:ios` or `run:android`.
3. Submit PR targeting `main`.

---

## License

MIT © 2025 Jason Safaiyeh
