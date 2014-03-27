//###############
// FILE : FireServersCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that send a GOAWAY message to all known NameServers.
//###############
package oop.ex3.filemanager.commands;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.filemanager.InitializeNameServerRequest;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;
import oop.ex3.resources.SyncedHashSet;

public class FireServersCommand extends Command {
	private Pattern _usage = Protocol.FIRE_SERVERS_CMD;

	@Override
	public Pattern getUsage() {
		return _usage;
	}

	@Override
	public void run(Matcher match) {

		//updated list of known name servers with runtime-acquired name servers
		SyncedHashSet<String> _nameServers = new SyncedHashSet<String>();
		if(AddCommand.getNameServers() != null) {
			for(String server: AddCommand.getNameServers().getList()) {
				_nameServers.putIfAbsent(server);
			}
		}
		for(String server: FileManagerDataBase._servers.getList()) {
			_nameServers.putIfAbsent(server);
		}

		for (String server : _nameServers.getList()) {
			Socket socket;
			MyDataInputStream in;
			MyDataOutputStream out;
			try {
				String ip = FileManagerDataBase.getServerIp(server);
				Integer port = FileManagerDataBase.getServerPort(server);
				socket = new Socket(ip,port);
				InitializeNameServerRequest.connect(socket);
				in = new MyDataInputStream(socket.getInputStream());
				out = new MyDataOutputStream(socket.getOutputStream());
				Protocol.sendMessage(Protocol.GOAWAY_MSG, out);
				Protocol.sendEndMessage(out);
				Protocol.reciveDoneEnd(in);
				Protocol.sendEndSessionEnd(out);
				Protocol.reciveDoneEnd(in);

			}
			catch (Exception e) {
			}
			break;
		}
	}//TODO close sockets?

	public void doCommand(Matcher match) {
		this.run(match);
	}

	@Override
	public void run() {
	}
}
