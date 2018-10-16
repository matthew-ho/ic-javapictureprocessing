# java-picture-processing

A simple program which transforms a given input images and saves it in the give output file.
Available transformations include: invert, grayscale, rotate, flip, blend, blur, mosaic.

To run the program either use and IDE such as Intellij or from the command line you could run the following commands:

`mkdir out`
`javac -g -d out -sourcepath src src/picture/Main.java`
`java -cp out picture.Main invert images/green64x64doc.png test.png`

You can replace `invert` with other tranformation commands and replace the paths to the input and output images accordingly as well.
