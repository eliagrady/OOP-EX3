//###############
// FILE : NameServerDataBase.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : This class holds all data of this name server:
// known file managers, known name servers etc.
//###############
package oop.ex3.nameserver;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import oop.ex3.filemanager.ServerSyntaxExeption;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.SyncedHashSet;
import oop.ex3.resources.SyncedTreeSet;


public class NameServerDataBase {
	public static ConcurrentHashMap<String, SyncedTreeSet<String>> _fileManagers;
	public static SyncedHashSet<String> _servers;
	public NameServerDataBase() {
		_fileManagers = new ConcurrentHashMap<String,SyncedTreeSet<String>>();
		_servers = new SyncedHashSet<String>();

	}

	/**
	 * A method that returns and all file Managers that hold a given file name 
	 * @return a list of file managers addresses that holds a given file name
	 */
	public static ArrayList<String> getAllFileManagers(String fileName) {
		ArrayList<String> listOfFileManagers = new ArrayList<String>();
		for(String fileManagerAddress: _fileManagers.keySet()) {
			if(_fileManagers.get(fileManagerAddress).contains(fileName)) {
				listOfFileManagers.add(fileManagerAddress);
			}
		}
		return listOfFileManagers;
	}

	/**
	 * Searches for a file in the name server database
	 * @param filename the file to search for
	 * @return the address (ip,port) of a file manager that contains the file,
	 * or null if the file is not found.
	 */
	public static String searchFile(String fileName) {
		String fileManagerAddress = null;
		for(String fileManager:_fileManagers.keySet()) {
			if(_fileManagers.get(fileManager).contains(fileName)) {
				fileManagerAddress = fileManager;
				return fileManagerAddress;
			}
		}
		//TODO recursive search to other name servers , while holding a list to avoid repetition
		return null;
	}

	/**
	 * Simple reader of the name server syntax, 
	 * this returns a string representation of it's ip
	 * @param server the string representation of this server
	 * @return a string representation of this server's ip
	 * @throws ServerSyntaxExeption
	 */
	public static String getServerIp(String server) throws ServerSyntaxExeption {
		String[] serv;
		try {
			serv = server.split(Protocol.SEPERATOR);
			if(serv[0].equals("localhost")) {
				serv[0] = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (Exception e) {
			throw new ServerSyntaxExeption() ;
		}
		return serv[0];
	}

	/**
	 * Simple reader of the name server syntax, 
	 * this returns a Integer representation of it's port
	 * @param server the string representation of this server
	 * @return a Integer representation of this server's port
	 * @throws ServerSyntaxExeption
	 */
	public static Integer getServerPort(String server) throws ServerSyntaxExeption {
		String[] serv;
		try {
			serv = server.split(Protocol.SEPERATOR);
		} catch (Exception e) {
			throw new ServerSyntaxExeption() ;
		}
		Integer port = -1;
		try {
			port = Integer.parseInt(serv[1].trim());
		}
		catch (Exception e) {
			throw new ServerSyntaxExeption();
		}
		return port;
	}
}
