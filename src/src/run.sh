#!/bin/bash
echo "== RUN ============================================================"
# se o JAR ainda nao existir, compila primeiro
if [ ! -f project.jar ]; then
    echo "project.jar nao encontrado — a compilar agora..."
    ./build.sh
fi

echo
java -jar project.jar -f input.txt
echo "===================================================================" 
read -p "Prima ENTER para sair..."