@echo off
echo ===============================
echo Compiling Java files...
echo ===============================

REM Compile all .java files from subfolders
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
TestTypeChecker.java


IF %ERRORLEVEL% NEQ 0 (
  echo Compilation failed.
  pause
  exit /b
)

echo Compilation successful.
echo ===============================
echo Choose class to run:
echo [1] TestTypeChecker
set /p choice=Enter your choice (1 or 2): 

if "%choice%"=="1" (
  echo Running TestTypeChecker.java...
  java -cp ".;CocoR/CocoR.jar" TestTypeChecker
) else if "%choice%"=="2" (
  REM Another file at some point maybe?
) else (
  echo Invalid choice. Exiting.
)

echo.
pause
