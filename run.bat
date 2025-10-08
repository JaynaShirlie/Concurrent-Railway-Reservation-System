@echo off
echo Compiling Railway Reservation System...
echo.

REM Try to find javac in common Java installation paths
set JAVAC_PATH=""
if exist "C:\Program Files\Java\jdk1.8.0_461\bin\javac.exe" (
    set JAVAC_PATH="C:\Program Files\Java\jdk1.8.0_461\bin\javac.exe"
) else if exist "C:\Program Files (x86)\Java\jdk1.8.0_461\bin\javac.exe" (
    set JAVAC_PATH="C:\Program Files (x86)\Java\jdk1.8.0_461\bin\javac.exe"
) else (
    echo ERROR: javac not found. Please install JDK or add it to PATH.
    echo Download JDK from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Create output directory
if not exist "out" mkdir out

REM Compile the application
echo Compiling with: %JAVAC_PATH%
%JAVAC_PATH% -d out -sourcepath src src/com/rbs/App.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful! Starting application...
echo.

REM Run the application
java -cp out com.rbs.App

echo.
echo Application closed.
pause
