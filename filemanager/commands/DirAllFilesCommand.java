//###############
// FILE : DirAllFilesCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that prints all files accessible to this file manager.
//###############
package oop.ex3.filemanager.commands;

import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.ws.ProtocolException;

import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.filemanager.InitializeNameServerRequest;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;
import oop.ex3.resources.SyncedHashSet;
import oop.ex3.resources.SyncedTreeSet;

public class DirAllFilesCommand extends Command {
private static SyncedHashSet<String> _knownNameServers;
private Pattern _usage = Protocol.DIR_ALL_FILES_CMD;

@Override
public Pattern getUsage() {
	return _usage;
}

	@Override
	public void run() {
		SyncedTreeSet<String> _allKnownFiles = new SyncedTreeSet<String>();
		//add own files
		for (String file : FileManagerDataBase._files.getList()) {
			_allKnownFiles.putIfAbsent(file);
		}
		//Initialize local known servers
		_knownNameServers = new SyncedHashSet<String>();
		for(String nameServer:FileManagerDataBase._servers.getList()) {
			_knownNameServers.putIfAbsent(nameServer);
		}
		//Acquire new name servers
		ArrayList<String> visitedNameServers = new ArrayList<String>();
		ArrayList<String> newNameServers = null;
		try {
			newNameServers = AddCommand.
			searchAllNameServers(_knownNameServers.getList(), visitedNameServers);
		} catch (Exception e) {
		}
		if(newNameServers != null) {
			updateKnownNameServersList(newNameServers);
		}
		//connect to every name server and ask for all his files
		for(String server: _knownNameServers.getList()) {
			MyDataInputStream in = null;
			MyDataOutputStream out = null;
			Socket socket = null;
			try {
				String ip = FileManagerDataBase.getServerIp(server);
				Integer port = FileManagerDataBase.getServerPort(server);
				socket = new Socket(ip,port);			
				if(!InitializeNameServerRequest.connect(socket)) {
					InitializeNameServerRequest.connect(socket);
				}
				in = new MyDataInputStream(socket.getInputStream());
				out = new MyDataOutputStream(socket.getOutputStream());
				Protocol.sendMessage(Protocol.WANT_ALL_FILES_MSG,out);
				Protocol.sendEndMessage(out);
				String index = Protocol.
				reciveMessage(Protocol.NAME_SERVER_CONTAIN_FILE, in);
				while(!index.equals(Protocol.END_LIST_MSG)) {
					String fileName = Protocol.reciveMessage(null, in);
					//add the file to the list
					_allKnownFiles.putIfAbsent(fileName);
					Protocol.reciveEndMessage(in);
					try{
						index = Protocol.
						reciveMessage(Protocol.NAME_SERVER_CONTAIN_FILE, in);
					}
					catch (Exception e) {
						if(!index.equals(Protocol.END_LIST_MSG)) {
							throw new ProtocolException();
						}
					}
				}
				Protocol.reciveEndMessage(in);
				Protocol.sendEndSessionEnd(out);
				Protocol.reciveDoneEnd(in);
				try {
					in.close();
				} catch (Exception e) {}
				try {
					out.close();
				} catch (Exception e) {}
				try {
					socket.close();
				} catch (Exception e) {}
			}
			catch (Exception e) {
				// not working? try the next one
			}
		}
		//now print all results you got.
		for(String f: _allKnownFiles.getList()) {
			System.out.println(f);
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

	@Override
	public void doCommand(Matcher match) {
		this.run();
	}

	@Override
	public void run(Matcher match) throws Exception {
	}

}
