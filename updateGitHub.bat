@echo off
cd /C ../../EguraldiaXML/

git checkout master

git add .

git commit -m "Commit automático"

git push origin master

echo Push realizado con éxito.
pause