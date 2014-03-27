//###############
// FILE : DontContainUpdateRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : An update about a file from a file manager.
//###############
package oop.ex3.nameserver.requests;
import java.net.Socket;

import javax.xml.ws.ProtocolException;

import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class DontContainUpdateRequest extends Request {
private String _usage = Protocol.DONT_CONTAIN_FILE_MSG;
	
	@Override
	public String getUsage() {
		return _usage;
	}

	@Override
	public void run(String match,Socket socket) throws Exception {
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			Protocol.reciveEndMessage(in);
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			boolean add = false;
			String index = match;
			while(!index.equals(Protocol.END_LIST_MSG)) {
				if(!(match.equals(Protocol.CONTAIN_FILE_MSG) || 
						match.equals(Protocol.DONT_CONTAIN_FILE_MSG))) 
					throw new ProtocolException();
				
				if(match.contains(Protocol.CONTAIN_FILE_MSG)) {
					add = true;
				}
				if(match.contains(Protocol.DONT_CONTAIN_FILE_MSG)) {
					add = false;
				}
				String fileName = Protocol.reciveMessage(null, in);
				Protocol.reciveEndMessage(in);
				if(add){
					NameServerDataBase._fileManagers.get
					(BeginSessionRequest.getAddress()).putIfAbsent(fileName);
				}
				else {
					NameServerDataBase._fileManagers.get
					(BeginSessionRequest.getAddress()).remove(fileName);
				}
				Protocol.sendDoneEnd(out);
				index = Protocol.reciveMessage(null, in);
			}
			Protocol.reciveEndMessage(in);
			Protocol.reciveEndSessionEnd(in);
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
