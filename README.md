# ProgressArchiver95

Tool to archive Progressbar95


## How to use
Simply select the APKs you want to archive and click the Archive button.

A list of ABIs and DPIs that were found on your device is available.

Your archived versions are stored internally and made available through the standard SAF interface, so anything that uses SAF will support ProgressArchiver95.
SAF (Storage Access Framework) is used in many apps to select files, such as Discord, Chrome, Termux, Firefox, and the built-in Files app. 

If you don't see it, the apps are debuggable, so you can use ADB to extract the files from internal storage. Contact me (Luihum#1287) for instructions on this. 

Old archive mode only extracts the base.apk, ignoring ABIs. This is intended for versions before 0.50 before the split APK scheme.

## What are split APKs?
Recently, Google has changed the way Android apps are distributed and installed. Previously, everything: the code, assets, DPI-optimized assets, the low-level instructions for the CPU, translations, etc. were all in one file, called the "fat APK". 
But as the name suggests, fat APKs are pretty large, with lots of code and assets that weren't used for the specific device. This proved problematic with the size increase of apps, particularly games, which were already big, plus the unused data. 

To fix this problem, Google developed the split APK system: the developer sends a bundle with every APK to the Play Store, and the Play Store client downloads that and unpacks only the needed files. The rest is discarded.
Now, every app is split into various APKs:
- base.apk, containing the shared code, assets, data and everything; the biggest file of the three parts, for Progressbar95 easily more than 90MB;
- the ABIs: split_config.armeabi_v7a.apk, split_config.arm64_v8a.apk, split_config.x86_64.apk and split_config.x86.apk. These contain the low-level code for the CPU. Respectively, ARMv7 (on most older phones), ARMv8 (on newer phones, 64-bit), x86_64 (mostly on computers) and x86 (32-bit, on older CPUs). Progressbar95 doesn't support x86, but just in case ProgressArchiver95 supports it. Some phones may have multiple ABIs.
- the DPIs: ldpi, mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi, nodpi and tvdpi. These contain optimized assets for the various screen resolutions. Unlike ABIs, they're not strictly required, but the game looks nicer on some screens with the correct DPI file.
- Last and least, the language APKs. There's many of them: en, pt, it, es, ch, jp, ru... The purpose of those is unknown.

The game won't install if the required ABI is missing, or if the base.apk is missing. 
Due to the way ABIs work, some versions of the game may not work on all devices.

This broke traditional APK extraction apps, which were designed with fat APKs in mind. So I made ProgressArchiver95, a tool specifically made to archive Progressbar95 easily.

## Credits   
    Original Creator: Luihum#1287
    Logo: (nobody yet)
    Testing: Lol Guy (Android 10)
    Special Thanks: Christian230102
    Translators:
      Portuguese (Brazil) - Luihum
