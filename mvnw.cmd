@echo off
setlocal enabledelayedexpansion

set "WRAPPER_PROPS=.mvn\wrapper\maven-wrapper.properties"
if not exist "%WRAPPER_PROPS%" (
  echo Missing %WRAPPER_PROPS%
  exit /b 1
)

for /f "tokens=1,* delims==" %%A in ('type "%WRAPPER_PROPS%"') do (
  if "%%A"=="distributionUrl" set "DIST_URL=%%B"
)

if "%DIST_URL%"=="" (
  echo distributionUrl not found in %WRAPPER_PROPS%
  exit /b 1
)

for %%I in ("%DIST_URL%") do set "DIST_FILE=%%~nxI"
set "DIST_NAME=%DIST_FILE:-bin.zip=%"
set "M2_HOME=%USERPROFILE%\.m2\wrapper\dists"
set "TARGET_DIR=%M2_HOME%\%DIST_NAME%"
set "ZIP_PATH=%TARGET_DIR%\%DIST_FILE%"

if not exist "%TARGET_DIR%" mkdir "%TARGET_DIR%"

for /d %%D in ("%TARGET_DIR%\apache-maven-*") do set "MAVEN_HOME=%%D"
if not "%MAVEN_HOME%"=="" goto runmaven

if not exist "%ZIP_PATH%" (
  echo Downloading Maven distribution...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%ZIP_PATH%'"
  if errorlevel 1 exit /b 1
)

echo Extracting Maven distribution...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ZIP_PATH%' -DestinationPath '%TARGET_DIR%' -Force"
if errorlevel 1 exit /b 1

for /d %%D in ("%TARGET_DIR%\apache-maven-*") do set "MAVEN_HOME=%%D"
if "%MAVEN_HOME%"=="" (
  echo Could not locate extracted Maven directory.
  exit /b 1
)

:runmaven
"%MAVEN_HOME%\bin\mvn.cmd" %*
