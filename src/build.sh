#!/bin/bash
echo "== BUILD =========================================================="
./clean.sh

echo
echo "Compilando todas as fontes..."
find . -name "*.java" > sources.txt
javac -encoding UTF-8 @sources.txt
if [ $? -ne 0 ]; then
    echo "Houve erros de compilacao. Corrige e volta a tentar."
    read -p "Prima ENTER para sair..."
    exit 1
fi

echo "Main-Class: main.Main" > manifest.mf
echo
echo "Empacotando project.jar..."
jar cfm project.jar manifest.mf main/*.class model/*.class simulation/*.class events/*.class util/*.class
if [ $? -ne 0 ]; then
    echo "Falha ao criar o JAR."
    read -p "Prima ENTER para sair..."
    exit 1
fi
rm manifest.mf sources.txt

echo
echo "Build concluido com sucesso: project.jar"
echo "===================================================================" 
#read -p "Prima ENTER para sair..."
