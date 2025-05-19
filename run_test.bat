@echo off
echo Blah blah, project. Put read me thingy maybe?

echo ================================
echo Compiling Java files...
echo ================================

REM Compile all .java files across folders
javac -cp ".;CocoR/CocoR.jar" ^
    AbstractSyntax\Definitions\*.java ^
    AbstractSyntax\Expressions\*.java ^
    AbstractSyntax\Program\*.java ^
    AbstractSyntax\SizeParams\*.java ^
    AbstractSyntax\Statements\*.java ^
    AbstractSyntax\Types\*.java ^
    Lib\*.java ^
    Transpiler\*.java ^
    Semantic\*.java ^
    boltparser\*.java ^
    Test.java

IF %ERRORLEVEL% NEQ 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Compilation successful.
echo Running the program...

echo.
echo ===============================
echo Running TypeChecker on test.bolt...
echo ===============================
java -cp ".;CocoR/CocoR.jar" Test

echo.
pause
