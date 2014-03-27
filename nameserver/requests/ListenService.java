//###############
// FILE : ListenService.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A thread that this name server runs to handle incoming requests.
//###############
package oop.ex3.nameserver;
import java.net.Socket;
import oop.ex3.nameserver.requests.RequestParser;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

public class ListenService implements Runnable {
	private static volatile boolean _keepListen = true;
	private Socket _service;
	private RequestParser _requestParser;

	public ListenService(Socket service) {
		_service = service;
		_requestParser = new RequestParser();
	}

	@Override
	public void run() {
		MyDataInputStream in = null;
		MyDataOutputStream out = null;
		String index;
		try {
			//Attempts reading incoming request
			in = new MyDataInputStream(_service.getInputStream());
			out = new MyDataOutputStream(_service.getOutputStream());
			index = Protocol.reciveMessage(null, in);

			while(!index.equals(Protocol.END_SESSION_MSG) && _keepListen) {
				_requestParser.parse(index, _service);
				index = Protocol.reciveMessage(null, in);
			}
			if(_keepListen) {
				Protocol.reciveEndMessage(in);
				Protocol.sendDoneEnd(out);
			}
		} 
		catch (Exception e) {
			try {
				in.close();
			} catch (Exception e2) {}
			try {
				out.close();
			} catch (Exception e2) {}
			try {	
				_service.close();
			} catch (Exception e2) {}
		}
	}

	public static void stopListen() {
		_keepListen = false;
		MyNameServer.shutdownServer();
	}
}
