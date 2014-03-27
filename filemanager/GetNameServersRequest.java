//###############
// FILE : GetNameServersRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a request that asks the name server for it's 
// list of name servers
//###############

package oop.ex3.filemanager;
import java.net.Socket;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;
import oop.ex3.resources.SyncedHashSet;

public class GetNameServersRequest {
	/**
	 * Attempts getting all the name servers known to a given name server
	 * @param ip the ip of the given name server
	 * @param port the port of the given name server
	 * @return a set of name servers known the given name server
	 */
	public static SyncedHashSet<String> connectAndGet(String ip,Integer port) {
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		Socket socket = null;
		SyncedHashSet<String> servers = new SyncedHashSet<String>();
		try {
			socket = new Socket(ip,port);
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			if(!InitializeNameServerRequest.connect(socket)) {
				InitializeNameServerRequest.connect(socket);
			}			Protocol.sendMessage(Protocol.WANT_SERVERS_MSG, out);
			Protocol.sendEndMessage(out);
			String input = Protocol.reciveMessage(null, in);
			String server;
			while(!input.equals(Protocol.END_LIST_MSG)) {
				Protocol.reciveMessage(Protocol.CONTAIN_NAMESERVER_MSG, in);
				server = Protocol.reciveMessage(null, in) + 
				Protocol.reciveInt(in).toString();
				servers.putIfAbsent(server);
				input = Protocol.reciveMessage(Protocol.END_MSG, in);
			}
			Protocol.reciveEndMessage(in);
			Protocol.sendEndSessionEnd(out);
			try {
				out.close();
			} catch (Exception e) {}
			try {
				in.close();
			} catch (Exception e) {}
			try {
				socket.close();
			} catch (Exception e) {}
			return servers;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return servers;
	}
}