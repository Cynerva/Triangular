#!/bin/sh
javac -cp src:lib/* src/*.java -d .
jar -cvfm Triangular.jar MANIFEST.MF *.class data
rm *.class
