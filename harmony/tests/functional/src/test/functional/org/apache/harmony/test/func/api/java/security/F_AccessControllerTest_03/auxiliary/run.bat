cls
javac -d . AccessTestClass.java
jar -cvf AccessTestClass.jar auxiliary\*.class AccessTestClass.java
rmdir /S /Q auxiliary