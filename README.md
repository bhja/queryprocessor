Download the jar and install it using the maven command as given below


Command to install 
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version-no> -Dpackaging=<packaging-type>


Example :: mvn install:install-file -Dfile=~/sybase.jar -DgroupId=sybase -DartifactId=jconn4 -Dversion=4.0 -Dpackaging=jar
