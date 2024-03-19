@echo off

REM   check if we are running a 64-Bit version of Java
java -version 2>&1 | find "64-Bit" >nul

if errorlevel 1 goto :32bit

:64bit
REM   we are running 64 bit - nice! assign loads of heap!
java -classpath "%~dp0\bin" -Xms16m -Xmx8192m com.asofterspace.picturizer.Main %*
goto :end

:32bit
REM   we are running 32 bit - oh no! assign only a tinly little bit of heap!
java -classpath "%~dp0\bin" -Xms16m -Xmx1024m com.asofterspace.picturizer.Main %*

:end
