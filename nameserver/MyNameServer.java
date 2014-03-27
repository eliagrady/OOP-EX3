//###############
// FILE : MyNameServer.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION: A Name Server. A go-between for file managers, that holds 
// information about it's known file managers and enables them to get in touch with 
// other file manager clients.
//###############
package oop.ex3.nameserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import oop.ex3.protocol.Protocol;
public class MyNameServer {
	public static NameServerDataBase _db;
	private static volatile boolean _keepListen = true;
	private static ServerSocket _server;
	private MyNameServer() {
		return;
	}
	public static void main(String[] args) {
		_db = new NameServerDataBase();
		int _port = 0;
		try {
			_port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Problem with port argument");
		}
		try {
			_server = new ServerSocket(_port);
			_server.setSoTimeout(Protocol.TIMOUT_IN_MS);
			//Accept new clients
			do{
				try {
					Socket socket = _server.accept();
					ListenService service = new ListenService(socket);
					Thread ListenService = new Thread(service);
					ListenService.start();
				} catch (Exception e) {}			
			}
			while(_keepListen);
		}
		catch(Throwable e) {
			serverError(e);
		}
		finally {
			if(_server != null) {
				try {
					_server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
	
	private static void serverError(Throwable  e) {
		System.out.println("error with server");
		System.exit(1);
	}
	
	public static void shutdownServer() { 
		_keepListen = false;
	}
}
