//###############
// FILE : WelcomeSessionRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : When this name server does not know a file manager it 
// starts a welcome session with him.
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;
import oop.ex3.resources.SyncedHashSet;
import oop.ex3.resources.SyncedTreeSet;


public class WelcomeSessionRequest extends Request {
	private String _usage = Protocol.WELCOME_MSG;

	@Override
	public String getUsage() {
		return _usage;
	}

	@Override
	public void run() {
	}

	@Override
	public void doRequest(String text,Socket socket) {
		try {
			this.run(text , socket);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void run(String fileManagerAddress,Socket socket) throws Exception {
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			SyncedTreeSet<String> files = new SyncedTreeSet<String>();
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			Protocol.sendMessage(Protocol.WELCOME_MSG, out);
			Protocol.sendMessage(Protocol.END_MSG, out);
			String index = Protocol.reciveMessage(Protocol.CONTAIN_FILE_MSG, in);
			//Deal with files of this file manager
			while(!index.equals(Protocol.END_LIST_MSG)) {
				files.putIfAbsent(Protocol.reciveMessage(null, in));
				Protocol.reciveEndMessage(in);
				Protocol.sendDoneEnd(out);
				index = Protocol.reciveMessage(null, in);
				}
			Protocol.reciveEndMessage(in);
			Protocol.sendDoneEnd(out);
			//Deal with name servers
			index = Protocol.reciveMessage(Protocol.CONTAIN_NAMESERVER_MSG, in);
			SyncedHashSet<String> servers = new SyncedHashSet<String>();
			while(!index.equals(Protocol.END_LIST_MSG)) {
				String nameServerAddress = 
					Protocol.reciveMessage(null, in)+
					Protocol.SEPERATOR+
					Integer.toString(Protocol.reciveInt(in));
				servers.putIfAbsent(nameServerAddress);
				Protocol.reciveEndMessage(in);
				Protocol.sendDoneEnd(out);
				index = Protocol.reciveMessage(null, in);
			}
			Protocol.reciveEndMessage(in);
			Protocol.sendDoneEnd(out);
			//Session success, Store data
			NameServerDataBase._fileManagers.putIfAbsent(fileManagerAddress, files);
			for (String server:servers.getList()) {
				NameServerDataBase._servers.putIfAbsent(server);
			}
		}
		catch (Exception e) {
		}
	}
}
