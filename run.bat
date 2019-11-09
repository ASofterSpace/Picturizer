@echo off

start "Picturizer" javaw -classpath "%~dp0\bin" -Xms16m -Xmx1024m com.asofterspace.picturizer.Main %*

pause
