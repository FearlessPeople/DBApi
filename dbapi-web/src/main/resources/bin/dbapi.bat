@echo off
setlocal

REM 项目基础目录，可以根据需要修改为绝对路径
set BASE_DIR=%~dp0..
set LIB_DIR=%BASE_DIR%\lib
set CONFIG_DIR=%BASE_DIR%\config
set LOG_DIR=%BASE_DIR%\logs
set JAR_NAME=dbapi-web-0.0.1-SNAPSHOT.jar
set JAR_PATH=%LIB_DIR%\%JAR_NAME%
set CONFIG_FILE=%CONFIG_DIR%\application.yml
set PID_FILE=%BASE_DIR%\dbapi-web.pid

:start
if exist %PID_FILE% (
    echo Service is already running. (PID: )
    type %PID_FILE%
    exit /b 1
)

echo Starting service...
start /b java -jar "%JAR_PATH%" --spring.config.location="%CONFIG_FILE%" > "%LOG_DIR%\app.log" 2>&1
echo %ERRORLEVEL% > %PID_FILE%
echo Service started. (PID: )
type %PID_FILE%
exit /b

:stop
if exist %PID_FILE% (
    for /F "usebackq" %%p in (%PID_FILE%) do (
        echo Stopping service with PID %%p...
        taskkill /PID %%p /F
    )
    del %PID_FILE%
    echo Service stopped.
) else (
    echo Service is not running.
)
exit /b

:status
if exist %PID_FILE% (
    for /F "usebackq" %%p in (%PID_FILE%) do (
        tasklist /FI "PID eq %%p" | findstr /I "java" >nul
        if %ERRORLEVEL% equ 0 (
            echo Service is running. (PID: %%p)
        ) else (
            echo Service is not running, but PID file exists.
        )
    )
) else (
    echo Service is not running.
)
exit /b

:restart
call :stop
call :start
exit /b

:usage
echo Usage: %0 {start^|stop^|status^|restart}
exit /b

REM 根据输入的参数进行判断
if "%1"=="start" (
    call :start
) else if "%1"=="stop" (
    call :stop
) else if "%1"=="status" (
    call :status
) else if "%1"=="restart" (
    call :restart
) else (
    call :usage
)
