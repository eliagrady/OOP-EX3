//###############
// FILE : WantServersRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Request for this name server known name servers
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class WantServersRequest extends Request {
private String _usage = Protocol.WANT_SERVERS_MSG;
	
	@Override
	public String getUsage() {
		return _usage;
	}

	@Override
	public void run(String match,Socket socket) throws Exception {
		MyDataOutputStream out;
		MyDataInputStream in;
		try {
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			Protocol.reciveEndMessage(in);
			for(String server: NameServerDataBase._servers.getList()) {
				Protocol.sendMessage(Protocol.CONTAIN_NAMESERVER_MSG, out);
				Protocol.sendMessage(NameServerDataBase.
						getServerIp(server), out);
				Protocol.sendInt(NameServerDataBase.
						getServerPort(server), out);
				Protocol.sendEndMessage(out);
			}
				Protocol.sendEndListEnd(out);
		}
		catch (Exception e) {
		}
	}

	@Override
	public void doRequest(String match, Socket socket) {
		try {
			this.run(match, socket);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}
