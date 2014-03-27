//###############
// FILE : RequestParser.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A parser that parses and executes incoming requests.
//###############
package oop.ex3.nameserver.requests;

import java.net.Socket;
import java.util.ArrayList;

public class RequestParser {

	private ArrayList<Request> _knownRequests;

	public RequestParser() {
		_knownRequests = new ArrayList<Request>();
		_knownRequests.add(new BeginSessionRequest());
		_knownRequests.add(new ContainUpdateRequest());
		_knownRequests.add(new DontContainUpdateRequest());
		_knownRequests.add(new WantFileRequest());
		_knownRequests.add(new WantServersRequest());
		_knownRequests.add(new GoAwayRequest());
	}

	/**
	 * parse then execute a request
	 * @param text the request text
	 * @param sock the socket to communicate with
	 * @throws UnknownCommandException if the text cannot be resolved to a
	 * legal command
	 */
	public synchronized void parse(String text ,Socket sock) 
	throws UnknownRequestException {
		for(Request request: _knownRequests) {
			if(request.getUsage().equals(text)) {
				try {
					request.doRequest(text, sock);
				}
				catch (Exception e) {
				}
				return;
			}
		}
		throw new UnknownRequestException();
	}
	

}
