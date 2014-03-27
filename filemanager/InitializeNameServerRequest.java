//###############
// FILE : InitializeNameServerRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a request for a new session. it deals with any name
// server whether or not it knows this file manager. 
//###############
package oop.ex3.filemanager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import oop.ex3.protocol.Protocol;
import oop.ex3.protocol.ProtocolExeption;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

public class InitializeNameServerRequest {
	/**
	 * Attempts a connection to a name server.
	 * @param socket the socket connecting to the name server
	 */
	public static boolean connect(Socket socket) {
		boolean recivedDoneEnd = false;
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			Protocol.sendMessage(Protocol.BEGIN_MSG, out);
			//tell the name server your ip and address
			Protocol.sendMessage(InetAddress.getLocalHost().getHostAddress(), out);
			Protocol.sendInt(MyFileManager.getPort(), out);
			Protocol.sendMessage(Protocol.END_MSG, out);
			String index = Protocol.reciveMessage(null, in);
			if(index.equals(Protocol.DONE_MSG)) {
				Protocol.reciveMessage(Protocol.END_MSG, in);
				recivedDoneEnd = true;
				return recivedDoneEnd;
			}
			else if(index.equals(Protocol.WELCOME_MSG)) {
				welcome(in,out);
				return false;
			}
			else if(Protocol.reciveMessage(Protocol.ERROR_MSG, in)
					.equals(Protocol.ERROR_MSG)) {
				throw new ProtocolExeption();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			try {
				if (!recivedDoneEnd) {
					try {
						in.close();
					} catch (Exception e2) {}
					try {
						out.close();
					} catch (Exception e2) {}
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
		return false;
	}

	public static void welcome(MyDataInputStream in, MyDataOutputStream out) 
	throws IOException, ProtocolExeption, ServerSyntaxExeption {
		Protocol.reciveMessage(Protocol.END_MSG, in);
		//send all file names
		for(String fileName:FileManagerDataBase._files.getList()) {
			Protocol.sendMessage(Protocol.CONTAIN_FILE_MSG, out);
			Protocol.sendMessage(fileName, out);
			Protocol.sendMessage(Protocol.END_MSG, out);
			Protocol.reciveDoneEnd(in);
		}
		out.writeUTF(Protocol.END_LIST_MSG);
		out.writeUTF(Protocol.END_MSG);
		Protocol.reciveDoneEnd(in);
		//send known name servers
		for(String nameServer:FileManagerDataBase._servers.getList()) {
			Protocol.sendMessage(Protocol.CONTAIN_NAMESERVER_MSG, out);
			Protocol.sendMessage(FileManagerDataBase.getServerIp(nameServer),out);
			Protocol.sendInt(FileManagerDataBase.getServerPort(nameServer),out);
			Protocol.sendEndMessage(out);
			Protocol.reciveDoneEnd(in);
		}
		Protocol.sendMessage(Protocol.END_LIST_MSG, out);
		Protocol.sendEndMessage(out);
		Protocol.reciveDoneEnd(in);
		Protocol.sendEndSessionEnd(out);
		Protocol.reciveDoneEnd(in);
	}
}