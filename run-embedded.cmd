@echo off
REM TP5 — Tomcat embarqué (mode développement)
set DB_URL=jdbc:postgresql://localhost:5432/gameverseacademy
set DB_USER=postgres
set DB_PASSWORD=0636

cd /d "%~dp0"

echo Compilation du projet...
call mvn clean package -q

echo.
echo Lancement sur http://localhost:6060/gameverseacademy/
call mvn exec:java -Dexec.mainClass="ma.ac.esi.gameverseacademy.Main"