@echo off
rem -------------------------------------------
rem  SET JAVA_HOME
rem -------------------------------------------
set JAVA_HOME=C:\Java\jdk1.7.0_79

rem -------------------------------------------
rem  SET BINARY
rem -------------------------------------------
set BINARY=MHDBMatcher

set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;./lib/mysql.jar;

set TARGET_ID=%1
set TARGET_RUN_MODE=%2
set CONFIG_PATH=%3
set CONFIG_ENCODING=%4



%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %BINARY%.java

%JAVA_HOME%\bin\java -Xmx1024m -classpath %CLASSPATH% %BINARY% %TARGET_ID% %TARGET_RUN_MODE% %CONFIG_PATH% %CONFIG_ENCODING%


