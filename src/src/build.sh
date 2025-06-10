#!/bin/bash
echo "== BUILD =========================================================="
./clean.sh

echo
echo "Compilando todas as fontes..."
javac -encoding UTF-8 *.java
if [ $? -ne 0 ]; then
    echo "Houve erros de compilacao. Corrige e volta a tentar."
    exit 1
fi

echo "Main-Class: Main" > manifest.mf
echo
echo "Empacotando project.jar..."
jar cfm project.jar manifest.mf *.class
if [ $? -ne 0 ]; then
    echo "Falha ao criar o JAR."
    exit 1
fi
rm manifest.mf

echo
echo "Build concluido com sucesso: project.jar"
echo "===================================================================" 