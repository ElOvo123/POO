#!/bin/bash
echo "== BUILD =========================================================="
./clean.sh

echo
echo "Compilando todas as fontes..."
javac -encoding UTF-8 *.java
if [ $? -ne 0 ]; then
    echo "Houve erros de compilacao. Corrige e volta a tentar."
    read -p "Prima ENTER para sair..."
    exit 1
fi

echo "Main-Class: Main" > manifest.mf
echo
echo "Empacotando project.jar..."
jar cfm project.jar manifest.mf *.class
if [ $? -ne 0 ]; then
    echo "Falha ao criar o JAR."
    read -p "Prima ENTER para sair..."
    exit 1
fi
rm manifest.mf

echo
echo "Build concluido com sucesso: project.jar"
echo "===================================================================" 
read -p "Prima ENTER para sair..."