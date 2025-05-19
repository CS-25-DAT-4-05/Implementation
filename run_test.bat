echo Blah blah, project

@echo off
echo =============================
echo Compiling Java files...
echo =============================
javac -cp .;CocoR *.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed.
    pause
    exit /b
)

echo.
echo =============================
echo Running TypeChecker on test.bolt...
echo =============================
java Test

echo.

pause
