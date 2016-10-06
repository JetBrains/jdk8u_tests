cls
javac -d . AuxiliaryClass.java
jar -cvf AuxiliaryClass.jar auxiliary\*.class AuxiliaryClass.java
rmdir /S /Q auxiliary
jarsigner -keystore auxiliary.store AuxiliaryClass.jar auxiliary