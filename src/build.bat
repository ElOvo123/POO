@echo off
echo == BUILD ==========================================================
call clean.bat

echo >nul
echo Compilando todas as fontes...
javac -encoding UTF-8 *.java
if errorlevel 1 (
    echo Houve erros de compilacao. Corrige e volta a tentar.
    pause
    exit /b 1
)

echo Main-Class: Main > manifest.mf
echo >nul
echo Empacotando project.jar...
jar cfm project.jar manifest.mf *.class
if errorlevel 1 (
    echo Falha ao criar o JAR.
    pause
    exit /b 1
)
del manifest.mf

echo >nul
echo Build concluido com sucesso: project.jar
echo ===================================================================
pause
