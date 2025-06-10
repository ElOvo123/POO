@echo off
echo == CLEAN ==========================================================
rem apaga .class e o jar, se existirem
del /q *.class >nul 2>&1
del /q project.jar >nul 2>&1
del /q manifest.mf >nul 2>&1
echo Limpeza concluida.
echo ===================================================================
pause
