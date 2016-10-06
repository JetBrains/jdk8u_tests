cls
javac -d . QEPermission.java
javac -d . TestAction.java
jar cvf TestAction.jar org\apache\harmony\test\func\api\java\security\F_BasicPermissionTest_01\auxiliary\TestAction.class TestAction.java
rmdir /Q /S com