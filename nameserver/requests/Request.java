//###############
// FILE : Request.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A Request this name server accepts.
//###############
package oop.ex3.nameserver.requests;
import java.net.Socket;

public abstract class Request implements Runnable{
	private String _patt;
	/**
	 * asks the request for it's usage pattern
	 * @return the usage pattern of the request
	 */
	public String getUsage() {
		return _patt;
	}
	/**
	 * Executes the request, given a match to the request's usage pattern.
	 * @param text a string representation of the request
	 * @param sock a socket to send/receive messages from
	 * pattern.
	 * @return 
	 */
	public abstract void doRequest(String text, Socket sock);
	/**
	 * run the request according to matcher arguments , using request's usage
	 * pattern.
	 * @param text a string representation of the request
	 * @param sock the socket to write/read data from
	 * @return returns the address of this request sender
	 * @throws Exception if there's a problem with execution of the request
	 */
	public abstract void run(String text, Socket sock) throws Exception;
}
