@echo off
cls
REM ================================================
REM Project: BOLT (GPU DSL: A High-Level Language for Simplified CUDA Programming)
REM File: build-and-run.bat
REM Description:
REM   Batch script to automate:
REM     1. Enters the sample directory
REM     2. Deletes old .class files
REM     3. Regenerates parser code with ANTLR
REM     4. Compiles all Java source files
REM     5. Runs the main ParserWrapper class
REM Authors: Gruppe 5
REM Date: 28/05/2025
REM ================================================

echo ===============================
echo Cleaning up all .class files...
echo ===============================
del /s /q *.class
echo Done.

echo ===============================
echo Compiling Java files...
echo ===============================

javac -cp ".;CocoR/CocoR.jar" ^
.\AbstractSyntax\Definitions\*.java ^
.\AbstractSyntax\Expressions\*.java ^
.\AbstractSyntax\Program\*.java ^
.\AbstractSyntax\Statements\*.java ^
.\AbstractSyntax\SizeParams\*.java ^
.\AbstractSyntax\Types\*.java ^
.\boltparser\*.java ^
.\Semantic\*.java ^
.\Transpiler\*.java ^
.\Lib\*.java ^
TestTypeChecker.java > compile_output.txt 2>&1

IF %ERRORLEVEL% NEQ 0 (
  echo Compilation failed.
  pause
  exit /b
)

echo ===============================
echo Compilation successful.
echo ===============================
echo Choose class to run:
echo [1] TestTypeChecker
set /p choice=Enter your choice (1 or 2): 

if "%choice%"=="1" (
  echo Running TestTypeChecker.java...
  java -cp ".;CocoR/CocoR.jar" TestTypeChecker
) else if "%choice%"=="2" (
  REM Reserved for future use
) else (
  echo Invalid choice. Exiting.
)

echo.
pause
