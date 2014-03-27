//###############
// FILE : SearchFileRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a search file request this file manager sends
//###############
package oop.ex3.filemanager;

import java.net.Socket;
import oop.ex3.protocol.Protocol;
import oop.ex3.protocol.ProtocolExeption;
import oop.ex3.resources.*;

public class SearchFileRequest {
	public static SyncedHashSet<String> search(Socket socket, String fileName) {
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			if(!InitializeNameServerRequest.connect(socket)) {
				InitializeNameServerRequest.connect(socket);
			}
			Protocol.sendMessage(Protocol.WANT_FILE_MSG, out);
			Protocol.sendMessage(fileName, out);
			Protocol.sendEndMessage(out);
			String input = Protocol.reciveMessage(null, in);
			if(input.equals(Protocol.FILE_NOT_FOUND_MSG)) {
				Protocol.reciveEndMessage(in);
				Protocol.sendEndSessionEnd(out);
			}
			else if(input.equals(Protocol.FILEADDRESS_MSG)) {
				SyncedHashSet<String> _fileManagers = new SyncedHashSet<String>();
				//puts all received file managers in a data structure
				while(!input.equals(Protocol.END_LIST_MSG)) {
					_fileManagers.putIfAbsent(Protocol.reciveMessage(null, in)+
							Protocol.SEPERATOR+Protocol.reciveInt(in).toString());
					Protocol.reciveEndMessage(in);
					input = Protocol.reciveMessage(null, in);
				}
				Protocol.reciveEndMessage(in);
				Protocol.sendEndSessionEnd(out);
				Protocol.reciveDoneEnd(in);
				return _fileManagers;
			}
			else {
				Protocol.reciveEndMessage(in);
				Protocol.sendEndSessionEnd(out);
				throw new ProtocolExeption();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}