//###############
// FILE : MyFileManager.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : My File Manager. Allows running commands that can manage a 
// 'download' directory. Connects to other file managers using a go-between 
// name server.
//###############
package oop.ex3.filemanager;
import java.net.*;
import java.util.ArrayList;

import oop.ex3.protocol.Protocol;


public class MyFileManager {
	private static volatile boolean _keepAlive = true;
	public static FileManagerDataBase _db;
	protected static Socket _socket;
	private static Integer _port;
	public static ArrayList<Thread> _threads;
	public static void main(String[] args) {
		/** args[0] is the name server file, args[1] is this file manager's
		 *  work directory. selfInit initialize this file manager's Database
		 */
		if(args==null || args.length != 3) {
			System.out.println("Insufficiant or missing arguments");
			System.exit(-1);
		}
		//args[2] = port number this file manager listens to.
		try {
			_port = Integer.parseInt(args[2]);
		}
		catch (Exception e) {
			System.out.println("Error with port argument");
		}
		
		selfInit(args[0],args[1]);


		// starts listening to download requests at the given port number
		_threads = new ArrayList<Thread>();
		Thread listenThread;
		Thread userInputThread;
		_threads.add(listenThread = new Thread(new ListenThread(_port)));
		_threads.add(userInputThread = new Thread(new UserInputThread()));
		listenThread.start();
		userInputThread.start();
		while(_keepAlive) {}
		shutdown();
	}

	/**
	 * Initialize self database, list of files
	 * @param homeDir path to locate files of this fileManager
	 * @param nameServersFilePath path of name server list
	 */
	private static void selfInit(String nameServersFilePath, String homeDir) {
		_db = new FileManagerDataBase(nameServersFilePath, homeDir);
		try {
			FileManagerRequestSender.
			initializeNameServers(FileManagerDataBase._servers);
		} catch (Exception e) {
			System.out.println("error initializing name servers");
		}
	}
	
	public static void close() {
		_keepAlive = false;
	}
	
	
	/**
	 * Print goodbye message!
	 */
	private static void shutdown() {

		System.out.println(Protocol.BYEBYE_MSG);
	}
	
	/**
	 * returns this file manager's port number
	 * @return this file manager's port number
	 */

	public static Integer getPort() {
		return _port;
	}
}