#!/bin/bash

TOP_DIR=$(pwd)

#LIBPATH=$CLASSPATH

#compile java file
javac           		   src/com/ts/quad/Point.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/quad/Vector.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/quad/Rectangle.java src/com/ts/quad/GeoCalculate.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/quad/DisRectangle.java -d ./class/.
javac -cp $TOP_DIR/class   test/com/ts/quad/ConfigParams.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/trajectory/Trajectory*.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/trajectory/QuadNode.java -d ./class/.
javac -cp $TOP_DIR/class   src/com/ts/trajectory/QuadInternalNode.java -d ./class/.
javac -cp $TOP_DIR/class   test/com/ts/quad/RectangleTest.java -d ./class/.
javac -cp $TOP_DIR/class   test/com/ts/quad/QuadInternalNodeTest.java -d ./class/.
