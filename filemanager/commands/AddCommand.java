//###############
// FILE : AddCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION: A Command That tries to add a given filename to this 
// file managers work folder
//###############
package oop.ex3.filemanager.commands;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oop.ex3.filemanager.DownloadAttemptRequest;
import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.filemanager.FileManagerRequestSender;
import oop.ex3.filemanager.GetNameServersRequest;
import oop.ex3.filemanager.NotifyNameServerRequest;
import oop.ex3.filemanager.ServerSyntaxExeption;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.SyncedHashSet;

public class AddCommand extends Command {
	private Pattern _usage = Protocol.ADD_CMD;
	public static final String ADD_FILE_FAILED_EXIST = "File already exist";
	public static final String ADD_FILE_FAILED_ABSENT = 
		"It is impossible to delete an absent file";
	public static final String ADD_FILE_SUCCESS = "Removing Done";
	private static SyncedHashSet<String> _knownNameServers;
	private boolean fileFoundAndDownloaded = false;
	@Override
	public Pattern getUsage() {
		return _usage;
	}
	
	/**
	 * A getter for known name servers during runtime
	 * @return a data structure potentially holding new servers
	 */
	public static SyncedHashSet<String> getNameServers() {
		return _knownNameServers;
	}

	@Override
	public void run(Matcher match) throws Exception {
		String fileName = match.group(1);
		//check if the file is already in the database
		if(FileManagerDataBase.getFile(fileName) != null) {
			throw new Exception(ADD_FILE_FAILED_EXIST);
		}
		//Initialize local known servers
		_knownNameServers = new SyncedHashSet<String>();
		for(String nameServer:FileManagerDataBase._servers.getList()) {
			_knownNameServers.putIfAbsent(nameServer);
		}
		//go over local known name servers 
		if(fileFoundAndDownloaded = 
			tryKnownNameServers(fileName, _knownNameServers.getList())) return;
		//Acquire new name servers
		ArrayList<String> visitedNameServers = new ArrayList<String>();
		ArrayList<String> newNameServers = 
			searchAllNameServers(_knownNameServers.getList(), visitedNameServers);
		//Filter new name servers list, only remove known servers
		for(String server:_knownNameServers.getList()) {
			if (newNameServers.contains(server)) {
				newNameServers.remove(server);
			}
		}
		if(fileFoundAndDownloaded = 
			tryKnownNameServers(fileName, newNameServers)) {
			updateKnownNameServersList(newNameServers);
			for(String server:_knownNameServers.getList()) {
				NotifyNameServerRequest.notifyContain(fileName,server,true);
			}
			return;
		}
		else {
			System.out.println("Downloading failed");
		}
		//add all newly acquired name servers to local known name server list
		for(String server:newNameServers) {
			_knownNameServers.putIfAbsent(server);
		}
	}
	
	/**
	 * updates the current known name servers set with newly acquired name servers
	 * @param newNameServers new servers to add
	 */
	private void updateKnownNameServersList(ArrayList<String> newNameServers) {
		for(String server: newNameServers) {
			_knownNameServers.putIfAbsent(server);
		}
	}

	/**
	 * Attempts requesting the file from current known name servers
	 * @return true iff the file has been found and downloaded successfully
	 * @throws ServerSyntaxExeption 
	 */
	private boolean tryKnownNameServers(String fileName,ArrayList<String> known) 
	throws ServerSyntaxExeption {
		
		for(String nameServer:known) {
			SyncedHashSet<String> fileManagers = FileManagerRequestSender.
			requestFileLocation(fileName, nameServer);
			if (fileManagers != null) {
				for(String fileManager : fileManagers.getList()) {
					Integer port = FileManagerDataBase.getServerPort(fileManager);
					String ip = FileManagerDataBase.getServerIp(fileManager);
					if(DownloadAttemptRequest.connectAndDownload(ip, port, fileName)) {
						fileFoundAndDownloaded = true;
						System.out.println
						("File Downloaded Successfully from "+ip+":"+port);
						return true;
					}
				}
				if (fileFoundAndDownloaded) return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Recursively retrieve a list of all known name servers
	 * @param nameServers the current known servers
	 * @param visited already visited name servers
	 * @return a list of newly acquired name servers
	 * @throws Exception if something went terribly wrong
	 */
	public static ArrayList<String> searchAllNameServers(ArrayList<String> nameServers,
			ArrayList<String> visited) throws Exception {
		for (String server : nameServers) {
			if(!visited.contains(server)) {
				visited.add(server);
				Integer port = FileManagerDataBase.getServerPort(server);
				String ip = FileManagerDataBase.getServerIp(server);
				searchAllNameServers
				(GetNameServersRequest.connectAndGet(ip, port).getList(), visited);
			}
		}
		return nameServers;
	}

	public void doCommand(Matcher match) {
		try {
			this.run(match);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
	}
}
