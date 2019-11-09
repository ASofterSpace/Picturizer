#!/bin/bash

echo "Re-building with target Java 7 (such that the compiled .class files will be compatible with as many JVMs as possible)..."

cd src

# build build build!
javac -encoding utf8 -d ../bin -bootclasspath ../other/java7_rt.jar -source 1.7 -target 1.7 @sourcefiles.list

cd ..



echo "Creating the release file Picturizer.zip..."

mkdir release

cd release

mkdir Picturizer

# copy the main files
cp -R ../bin Picturizer
cp ../UNLICENSE Picturizer
cp ../README.md Picturizer
cp ../run.sh Picturizer
cp ../run.bat Picturizer

# convert \n to \r\n for the Windows files!
cd Picturizer
awk 1 ORS='\r\n' run.bat > rn
mv rn run.bat
cd ..

# create a version tag right in the zip file
cd Picturizer
version=$(./run.sh --version_for_zip)
echo "$version" > "$version"
cd ..

# zip it all up
zip -rq Picturizer.zip Picturizer

mv Picturizer.zip ..

cd ..
rm -rf release

echo "The file Picturizer.zip has been created in $(pwd)"
