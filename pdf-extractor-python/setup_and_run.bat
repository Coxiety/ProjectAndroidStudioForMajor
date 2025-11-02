@echo off
REM PDF Question Extractor - Python Version Setup and Run

echo ===============================================
echo   PDF Question Extractor - Python Version
echo ===============================================
echo.

REM Check if Python is installed
python --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Python is not installed!
    echo Please install Python from https://www.python.org/
    echo.
    pause
    exit /b 1
)

echo Python is installed.
echo.

REM Install required packages
echo Installing required packages...
pip install -r requirements.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to install packages!
    pause
    exit /b 1
)

echo.
echo Setup complete!
echo.
echo ===============================================
echo              Running the extractor
echo ===============================================
echo.

REM Run the extractor
python pdf_extractor.py

echo.
echo ===============================================
echo             Application Finished
echo ===============================================
pause

