@echo off
title AnimStudio Build

echo Searching for Gradle...
echo.

set GRADLE_EXE=
for /f "delims=" %%i in ('dir /s /b C:\gradle.bat 2^>nul') do if "%%~nxi"=="gradle.bat" set GRADLE_EXE=%%i
if not "%GRADLE_EXE%"=="" goto found

for /f "delims=" %%i in ('dir /s /b "%USERPROFILE%\gradle.bat" 2^>nul') do if "%%~nxi"=="gradle.bat" set GRADLE_EXE=%%i
if not "%GRADLE_EXE%"=="" goto found

for /f "delims=" %%i in ('dir /s /b "D:\gradle.bat" 2^>nul') do if "%%~nxi"=="gradle.bat" set GRADLE_EXE=%%i
if not "%GRADLE_EXE%"=="" goto found

:askpath
echo Could not find Gradle automatically.
echo.
echo Open the folder where you extracted gradle-2.14
echo then open the "bin" folder inside it.
echo You will see a file called "gradle.bat"
echo.
echo Drag and drop that "gradle.bat" file into THIS window, then press Enter:
echo.
set /p GRADLE_EXE=
if not exist "%GRADLE_EXE%" (
    echo File not found. Try again.
    goto askpath
)

:found
echo Found: %GRADLE_EXE%
echo.

java -version >nul 2>&1
if errorlevel 1 (
    echo Java 8 is not installed!
    echo Download from: adoptium.net
    pause
    exit /b 1
)

echo Java OK.
echo.
echo Step 1/2 - Downloading Minecraft files (10-20 min, please wait)...
call "%GRADLE_EXE%" setupDecompWorkspace
if errorlevel 1 ( echo FAILED at step 1 & pause & exit /b 1 )

echo.
echo Step 2/2 - Building mod...
call "%GRADLE_EXE%" build
if errorlevel 1 ( echo FAILED at step 2 & pause & exit /b 1 )

echo.
echo ===========================
echo SUCCESS! Mod is ready!
echo ===========================
echo.
echo File is here:
echo %~dp0build\libs\AnimStudio-1.0.0.jar
echo.
echo Copy it to this folder:
echo %APPDATA%\.minecraft\mods\
echo.
if exist "%~dp0build\libs\AnimStudio-1.0.0.jar" (
    xcopy /y "%~dp0build\libs\AnimStudio-1.0.0.jar" "%APPDATA%\.minecraft\mods\"
    echo Mod copied to mods folder automatically!
)
pause
