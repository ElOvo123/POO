@echo off
echo == RUN ============================================================
rem se o JAR ainda nao existir, compila primeiro
if not exist project.jar (
    echo project.jar nao encontrado — a compilar agora...
    call build.bat
)

echo >nul
java -jar project.jar -f input1.txt
echo ===================================================================
pause
 
