#!/bin/bash
#Lancia l'applicazione.

if [ -z "$JAVA_HOME" ] ; then
    echo "Could not find a JDK."
    echo "Either you have to install a JDK (1.4 or up),"
    echo "or you have to set JAVA_HOME to your JDK installation directory."
    exit
fi

instdir=`dirname "$0"`

JAVACMD=$JAVA_HOME/bin/java
JARPATH=../timekeeper.jar

par1=$1
par2=$2
par3=$3

"$JAVACMD" -Xmx64m -jar $JARPATH $par1 $par2 $par3

