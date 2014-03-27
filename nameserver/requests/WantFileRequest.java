//###############
// FILE : WantFileRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A file request this name server deals with.
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import java.util.ArrayList;

import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class WantFileRequest extends Request {
	private String _usage = Protocol.WANT_FILE_MSG;

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
			String fileName = Protocol.reciveMessage(null, in);
			Protocol.reciveEndMessage(in);
			ArrayList<String> fileManagers = NameServerDataBase.getAllFileManagers(fileName);
			if(fileManagers != null && fileManagers.size() > 0) {
				for(String fileManagerAddress: fileManagers) {
					Protocol.sendMessage(Protocol.FILEADDRESS_MSG, out);
					Protocol.sendMessage(NameServerDataBase.
							getServerIp(fileManagerAddress), out);
					Protocol.sendInt(NameServerDataBase.
							getServerPort(fileManagerAddress), out);
					Protocol.sendEndMessage(out);
				}
				Protocol.sendEndListEnd(out);
			}
			else {
				Protocol.sendMessage(Protocol.FILE_NOT_FOUND_MSG, out);
				Protocol.sendEndMessage(out);
			}
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
