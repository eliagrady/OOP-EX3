//###############
// FILE : FileManagerDataBase.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a file manager's data base. 
// Holds files and name servers.
//###############
package oop.ex3.filemanager;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.SyncedHashSet;
import oop.ex3.resources.SyncedTreeSet;
public class FileManagerDataBase {
	private static String _homeDir;
	public static SyncedTreeSet<String> _files;
	public static SyncedHashSet<String> _servers;
	public FileManagerDataBase(String nameServerFilePath ,String homeDirPath) {
		_homeDir = homeDirPath;
		_files = new SyncedTreeSet<String>();
		_files = loadFiles(_homeDir);
		_servers = new SyncedHashSet<String>();
		_servers = parseNameServers(nameServerFilePath);

	}
	/**
	 * Return this file manager's work directory
	 * @return
	 */
	public static String getHomeDir() {
		return _homeDir;
	}

	/**
	 * Listing all files currently present in this work directory and store
	 * them in a self arranging data structure
	 * @param homeDir the directory to load file names from
	 * @return data structure that lists all files in the homeDir
	 */
	private SyncedTreeSet<String> loadFiles(String homeDir) {
		File file = new File(homeDir);
		File[] fileList = file.listFiles();
		for (File f : fileList) {
			//add all files in the folder by their name
			_files.putIfAbsent(f.getName());
		}
		return _files;
	}
	
	/**
	 * search for the wanted file in this database list
	 * @param name the file name to search for
	 * @return the file if it's found, and null otherwise.
	 */
	public static File getFile(String name) {
		for(String f: _files.getList()) {
			if(f.equals(name)) {
				File found = new File(getHomeDir()+f);
				return found;
			}
		}
		return null;
	}
	
	/**
	 * Loads name servers from a given text document
	 * @param serverListFile the path of the text file holding name servers
	 * @return a data structure that holds a string representation of name 
	 * servers.
	 */
	private SyncedHashSet<String> parseNameServers
	(String serverListFile) {
		String serverList = "";
		try {
			serverList = readFile(serverListFile);
		} catch (IOException e) {
			System.out.println("ErrorReadingNameServerFile");
		}
		String[] lines = serverList.split(System.getProperty(
		"line.separator"));
		for (String line : lines) {
			if(line.contains("localhost")) {
				try {
					line.replace("localhost", InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					System.out.println("cant get host ip address");
				}
			}
			_servers.putIfAbsent(line.trim());
		}
		return _servers;
	}

	/**
	 * Read a text file and returns it's content
	 * @param filePath the path of the file to read
	 * @return the content of the file
	 * @throws IOException if the file is invalid
	 */
	private static String readFile(String filePath) throws IOException {
		File f = new File(filePath);
		FileReader fr = null;
		BufferedReader br = null;
		fr = new FileReader(f);
		br = new BufferedReader(fr);
		String line, text = "";
		while((line = br.readLine()) != null) {
			text += line + System.getProperty("line.separator");
		}
		try {
			fr.close();
		}
		catch(Throwable e) {}
		try {
			br.close();
		}
		catch(Throwable e) {}
		
		return text;
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
