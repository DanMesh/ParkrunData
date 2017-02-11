# Makefile for ParkrunData
# Daniel Mesham
# 23 January 2017
# v1.0

all: Result.java ParkrunData.java
	@printf "Compiling..."
	@javac Result.java ParkrunData.java
	@printf "Done\n"

run: all
	@java ParkrunData

clean:
	@printf "Removing class files..."
	@rm *.class
	@printf "Done\n"
