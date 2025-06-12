#!/bin/bash
echo "== RUN ============================================================"
# se o JAR ainda nao existir, compila primeiro
if [ ! -f project.jar ]; then
    echo "project.jar nao encontrado — a compilar agora..."
    ./build.sh
fi

echo
#java -jar project.jar -r 5 4 1 1 5 4 1 4 100 10 100 3 10 1 1
java -jar project.jar -f ./SIM/input1.txt
echo "===================================================================" 
read -p "Prima ENTER para sair..."
