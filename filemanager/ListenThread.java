//###############
// FILE : ListenThread.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : this thread listens to incoming download requests, and
// send them to be handled.
//###############
package oop.ex3.filemanager;

import java.io.IOException;
import java.net.*;

import oop.ex3.protocol.Protocol;

/**
 * when the run method is called, the object will listen to the given
 * port, and will open a service thread for every incoming connection.
 */
public class ListenThread implements Runnable {
	private ServerSocket _server;
	private static volatile boolean _keepListen = true;
	
	/**
	 * constructs a new download request listener, that listens to the given
	 * port.
	 * @param port the port to listen to
	 */
	public ListenThread(int port) {
		try {
			_server = new ServerSocket(port);
			
		} 
		catch (IOException e) {
			throw new Error();
		}
	}
	
	public static void stopListen() {
		_keepListen = false;	
	}
	
	@Override
	public void run() {
		try {
			_server.setSoTimeout(Protocol.TIMOUT_IN_MS);
		} catch (SocketException e1) {
			//TCP error
		}
		do {
			Thread t = null;
			try {
				Socket request = _server.accept();
				if(_keepListen) {
					t = new Thread(new UploadThread(request,t));
					MyFileManager._threads.add(t);
					t.start();
				}
				else {
					try {
						_server.close();
					} catch (Exception e) {}
				}
			} 
			catch (IOException e) {
			}
		}
		while(_keepListen);
		MyFileManager._threads.remove(this);
	}
}
