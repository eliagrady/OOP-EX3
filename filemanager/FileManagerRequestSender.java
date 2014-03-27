//###############
// FILE : FileManagerRequestSender.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents the requests that this file manager can send to 
// any of it's name servers.
//###############
package oop.ex3.filemanager;

import java.net.Socket;

import oop.ex3.resources.SyncedHashSet;

public class FileManagerRequestSender {
	/**
	 * Introduces this file manager to every name server in it's database
	 * @param servers the name server database this file manager holds
	 * @throws Exception if there's an error with introduction
	 */
	public static void initializeNameServers
	(SyncedHashSet<String> servers) {
		try {
			for (String nameServer : servers.getList()) {
				Socket socket = new Socket
				(FileManagerDataBase.getServerIp(nameServer),
						FileManagerDataBase.getServerPort(nameServer));
				InitializeNameServerRequest.connect(socket);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * As part of the add command, this method returns a set of file 
	 * managers that have the requested file.  
	 * @param fileName the file name to search for
	 * @param nameServer the name server to ask file managers from
	 * @return a HashSet that contains string representation of file managers(ip%port)
	 */

	public static SyncedHashSet<String> requestFileLocation(String fileName ,String nameServer) {
		Socket socket;
		try {
			socket = new Socket(FileManagerDataBase.getServerIp(nameServer),
								FileManagerDataBase.getServerPort(nameServer));
			return SearchFileRequest.search(socket,fileName);
		}
		catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * As part of the add command, this method returns a set of file 
	 * managers that have the requested file.  
	 * @param fileName the file name to search for
	 * @param nameServer the name server to ask file managers from
	 * @return a HashSet that contains string representation of file managers(ip%port)
	 */
	public static SyncedHashSet<String> requestNameServers(String nameServer) {
		try {
			String ip = FileManagerDataBase.getServerIp(nameServer);
			Integer port = FileManagerDataBase.getServerPort(nameServer);
			return GetNameServersRequest.connectAndGet(ip,port);
		}
		catch(Exception e) {
			
		}
		return null;
	}
}

