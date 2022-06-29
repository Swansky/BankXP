if [ ! -d "server" ]; then
 mkdir server
fi
cd server || exit
if [ ! -f "server.jar" ]; then
   curl https://api.papermc.io/v2/projects/paper/versions/1.19/builds/36/downloads/paper-1.19-36.jar -o server.jar
   echo "server.jar has been downloaded"
fi

if [ ! -f "eula.txt" ]; then
 file="eula.txt"
 echo "eula=true" > $file
 cat $file
 echo "Eula file has been created !"
fi

 java -jar server.jar --nogui









