cd ..\bin

echo Main-Class: eli.Main > manifest.txt
jar cvfm ..\jar\netporttester.jar manifest.txt eli\*.class
del manifest.txt

cd ..\jar