cls
javac -d . FullAccessClass.java
jar -cvf FullAccessClass.jar auxiliary\*.class FullAccessClass.java
rmdir /S /Q auxiliary