//###############
// FILE : WantAllFilesRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Request for all known files that this name server know about.
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class WantAllFilesRequest extends Request {
private String _usage = Protocol.WANT_ALL_FILES_MSG;
	
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
			
			for(String fileManager: NameServerDataBase._fileManagers.keySet()) {
				try {
					for(String fileName:NameServerDataBase.
							_fileManagers.get(fileManager).getList()) {
						try {
							Protocol.sendMessage(
									Protocol.NAME_SERVER_CONTAIN_FILE, out);
							Protocol.sendMessage(fileName,out);
							Protocol.sendEndMessage(out);
						}
						catch (Exception e) {
							// not working? try the next one
						}
					}
				} catch (Exception e) {
					// // not working? try the next one
				}
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
		}
	}

	@Override
	public void run() {
	}
}
