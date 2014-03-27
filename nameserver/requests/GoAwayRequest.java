//###############
// FILE : GoAwayRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Request to shutdown this name server.
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;

import oop.ex3.nameserver.ListenService;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;


public class GoAwayRequest extends Request {
private String _usage = Protocol.GOAWAY_MSG;
	
	@Override
	public String getUsage() {
		return _usage;
	}

	@Override
	public void run(String match,Socket socket) throws Exception {
		MyDataOutputStream out = new MyDataOutputStream(socket.getOutputStream());
		MyDataInputStream in = new MyDataInputStream(socket.getInputStream());
		Protocol.reciveEndMessage(in);
		Protocol.sendDoneEnd(out);
		ListenService.stopListen();
		Protocol.reciveEndSessionEnd(in);
		Protocol.sendDoneEnd(out);
		
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
