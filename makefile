
# https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        bbst.java \
        RedBlackTree.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
