package server;

import java.net.*;
import java.io.*;
public class server {

private static ServerSocket watekSerwera;
public static void main(String[] args) throws IOException { 
	int port = 8205;
try
{
	watekSerwera = new ServerSocket(port);
	watekSerwera.setSoTimeout(1000000);
}catch(IOException e) {
e.printStackTrace(); }
while(true)
{
System.out.println("Oczekiwanie na klienta na porcie " + watekSerwera.getLocalPort() + "...");
Socket server = watekSerwera.accept();

watekSerwera pt = new watekSerwera(server);
pt.start();

}
}
}
