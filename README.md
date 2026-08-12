COMP1786 – Mobile Application Design and Development  
M-Hike Coursework Source Code

Student name: Lê Đăng Quang  
Student ID: GCS220332

1. PROJECTS INCLUDED

This submission contains two applications:

- Android_MHike: A native Android application developed using Java and XML.
- MHikeMaui: A cross-platform application developed using .NET MAUI, C# and XAML.

The applications allow users to record, view, edit, delete and search for hike information. SQLite is used for local data persistence. The native Android application also supports observations, while the .NET MAUI application includes device geolocation.

2. RUNNING THE NATIVE ANDROID APPLICATION

Requirements:

- Android Studio
- Android SDK
- Android emulator or physical Android device
- Internet connection may be required during the first Gradle synchronisation

Instructions:

1. Open Android Studio.
2. Select Open.
3. Select the Android_MHike folder. Do not select only the app folder.
4. Wait for Gradle synchronisation to complete.
5. Select an Android emulator or connected Android device.
6. Click Run.
7. The SQLite database will be created automatically when the application is first launched.

3. RUNNING THE .NET MAUI APPLICATION

Requirements:

- Visual Studio 2022
- .NET MAUI workload
- Android emulator, physical Android device or supported Windows target

Instructions:

1. Open the MHikeMaui solution or project in Visual Studio.
2. Allow Visual Studio to restore the required NuGet packages.
3. Select an Android emulator, connected Android device or Windows Machine as the target.
4. Click Run.
5. The SQLite database will be created automatically when the application is first launched.
6. Grant location permission when using the geolocation feature.

4. SOURCE CODE REPOSITORIES

.NET MAUI:
https://github.com/DQuang45/CW_COMP1786_MAUI

Native Android:
https://github.com/DQuang45/CW_COMP1786

5. ADDITIONAL NOTES

- Application data is stored locally using SQLite.
- Location permission is required only when the geolocation feature is used.
- Generated build folders have been excluded to reduce the ZIP file size.
- The complete source code required to compile and run both applications is included in this submission.
