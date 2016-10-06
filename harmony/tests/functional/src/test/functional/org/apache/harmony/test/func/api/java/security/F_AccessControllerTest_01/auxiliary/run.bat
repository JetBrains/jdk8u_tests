cls
javac -d . SimpleServerSocket.java
jar -cvf SimpleServerSocket.jar auxiliary\simple*.class SimpleServerSocket.java
javac -d . SocketConnector.java
jar -cvf SocketConnector.jar auxiliary\socketconnect*.class SocketConnector.java
javac -d . SocketClient.java
jar -cvf SocketClient.jar auxiliary\socketclient*.class SocketClient.java
rmdir /S /Q auxiliary