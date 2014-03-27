//###############
// FILE : NotifyNameServerRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that notify a name server about a change in one of
// it's files (can update that it have/don't have a file
//###############
package oop.ex3.filemanager;

import java.net.InetAddress;
import java.net.Socket;
import oop.ex3.protocol.Protocol;
import oop.ex3.protocol.ProtocolExeption;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

public class NotifyNameServerRequest {
	/**
	 * Connect to a name server and updates it about a file 
	 * in this file manager's possession
	 * @param fileName the file this file manager now have or have removed
	 * @param server the name server to connect with
	 * @param add true if the fileName is added false if the fileName is removed 
	 */
	public static void notifyContain(String fileName, String server,boolean add) {
		Socket socket = null;
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			String ip = FileManagerDataBase.getServerIp(server);
			Integer port = FileManagerDataBase.getServerPort(server);
			socket = new Socket(ip,port);
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
				if(add == true) {
					Protocol.sendMessage(Protocol.CONTAIN_FILE_MSG, out);
				}
				else {
					Protocol.sendMessage(Protocol.DONT_CONTAIN_FILE_MSG, out);
				}
				Protocol.sendMessage(fileName, out);
				Protocol.sendEndMessage(out);
				Protocol.reciveDoneEnd(in);
				Protocol.sendEndSessionEnd(out);
				Protocol.reciveDoneEnd(in);
			}
			else if(index.equals(Protocol.WELCOME_MSG)) {
				InitializeNameServerRequest.welcome(in, out);
				notifyContain(fileName,server,add);
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
				in.close();
			} catch (Exception e2) {}
			try {
				out.close();
			} catch (Exception e2) {}
			//TODO Close socket??
		}
	}
}
