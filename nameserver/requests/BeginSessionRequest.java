//###############
// FILE : BeginSessionRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Every new session with a file manager begins with this 
// (When a BEGIN message is sent)
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import oop.ex3.nameserver.NameServerDataBase;
import oop.ex3.protocol.Protocol;
import oop.ex3.protocol.ProtocolExeption;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class BeginSessionRequest extends Request {
	private String _usage = Protocol.BEGIN_MSG;
	private static String _address;
	
	/**
	 * @param _address the address of the file manager connected
	 */
	public static void setAddress(String _address) {
		BeginSessionRequest._address = _address;
	}

	/**
	 * @return the address of the file manager connected
	 */
	public static String getAddress() {
		return _address;
	}

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
	public void run(String text,Socket socket) throws Exception {
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
			try {
			out = new MyDataOutputStream(socket.getOutputStream());
			in = new MyDataInputStream(socket.getInputStream());
			String fileManagerAddress = 
				Protocol.reciveMessage(null, in)+
				Protocol.SEPERATOR+
				Integer.toString(Protocol.reciveInt(in));
			setAddress(fileManagerAddress);
			//check if this file manager is already known
			if(NameServerDataBase._fileManagers.containsKey(fileManagerAddress)) {
				if(!Protocol.reciveMessage(null, in).equals(Protocol.END_MSG)) 
					throw new ProtocolExeption();
				Protocol.sendDoneEnd(out);
			}
			else {
				if(!Protocol.reciveMessage(null, in).equals(Protocol.END_MSG)) {
					throw new ProtocolExeption();
				}
				WelcomeSessionRequest welcome = new WelcomeSessionRequest();
				welcome.doRequest(fileManagerAddress, socket);
			}
		}
		catch (Exception e) {
			//TODO error
		}
	}
}
