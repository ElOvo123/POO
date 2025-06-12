#!/bin/bash
echo "== CLEAN =========================================================="
find . -name "*.class" -delete
rm -f project.jar
echo "Limpeza concluida."
echo "==================================================================="
