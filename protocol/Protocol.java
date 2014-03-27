//###############
// FILE : Protocol.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents the language the name server and file manager speak.
// also contains methods that simplify writing in this language. 
//###############
package oop.ex3.protocol;

import java.io.IOException;
import java.util.regex.Pattern;


import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

public class Protocol {
	//CONVENTIONS
	public static final String SEPERATOR = "%";
	public static final int TIMOUT_IN_MS = 5000;
	//request a file. can be sent to file managers or name servers.
	public static final String WANT_FILE_MSG = "WANTFILE";
	public static final String BYEBYE_MSG = "Bye-bye!";
	//MESSAGES.words that a file manager send to a name server
	public static final String ERROR_MSG = "ERROR";
	public static final String END_MSG = "END";
	public static final String FILE_NOT_FOUND_MSG = "FILENOTFOUND";
	public static final String FILE_MSG = "FILE";
	public static final String GOAWAY_MSG = "GOAWAY";
	public static final String BEGIN_MSG = "BEGIN";
	public static final String CONTAIN_FILE_MSG = "CONTAINFILE";
	public static final String CONTAIN_NAMESERVER_MSG = "CONTAINNAMESERVER";
	public static final String WANT_ALL_FILES_MSG = "WANTALLFILES";
	public static final String WANT_SERVERS_MSG = "WANTSERVERS";
	public static final String WELCOME_MSG = "WELCOME";
	public static final String DONE_MSG = "DONE";
	public static final String END_LIST_MSG = "ENDLIST";
	public static final String END_SESSION_MSG = "ENDSESSION";
	public static final String FILEADDRESS_MSG = "FILEADDRESS";
	public static final String DONT_CONTAIN_FILE_MSG = "DONTCONTAINFILE";
	public static final String NAME_SERVER_CONTAIN_FILE = "NSCONTAINFILE";
	public static final String GOODBYE_MSG = "GOODBYE";
	
	//COMMANDS. used by a file manager
	private static final String fileName = "+ (\\w+(\\.\\w+)?)";
	public static final Pattern DIR_CMD = Pattern.compile("DIR");
	public static final Pattern DIRSERVERS_CMD = Pattern.compile("DIRSERVERS");
	public static final Pattern REMOVE_CMD = Pattern.compile("REMOVE+ (\\w+(\\.\\w+)?)");
	public static final Pattern RENAME_CMD = Pattern.compile("RENAME"+fileName+fileName);
	public static final Pattern ADD_CMD = Pattern.compile("ADD+ (\\w+(\\.\\w+)?)");
	public static final String QUIT_MSG = "QUIT";
	public static final Pattern QUIT_CMD = Pattern.compile(QUIT_MSG);
	public static final Pattern GOAWAY_CMD = Pattern.compile("GOAWAY");
	public static final Pattern FIRE_SERVERS_CMD = Pattern.compile("FIRESERVERS");
	public static final Pattern DIR_ALL_FILES_CMD = Pattern.compile("DIRALLFILES");




	/**
	 * try to send a message using the data stream
	 * @param msg the string representation of the message
	 * @param out the output stream to write to.
	 */
	public static void sendMessage(String msg,MyDataOutputStream out) {
		try {
			out.writeUTF(msg);
		} catch (Exception e) {
			System.out.println(ERROR_MSG);
		}
	}
	/**
	 * try to send an END message using the data stream
	 * @param out the output stream to write to.
	 */
	public static void sendEndMessage(MyDataOutputStream out) {
		try {
			out.writeUTF(END_MSG);
		} catch (Exception e) {
			System.out.println(ERROR_MSG);
		}
	}
	
	/**
	 * try to send an END message using the data stream
	 * @param out the output stream to write to.
	 * @throws IOException if the input stream has failed
	 * @throws ProtocolExeption if the END message was not received
	 */
	public static void reciveEndMessage(MyDataInputStream in) throws IOException,
	ProtocolExeption {
		reciveMessage(END_MSG, in);
	}
	
	/**
	 * try to send a number using the data stream
	 * @param number the integer representation of the number
	 * @param out the output stream to write to.
	 */
	public static void sendInt(int number,MyDataOutputStream out) {
		try {
			out.writeInt(number);
		} catch (Exception e) {
			System.out.println(ERROR_MSG);
		}
	}
	
	/**
	 * Attempt reading a message (specific or not) from an input stream.
	 * @param message the message expected to receive , null for any message
	 * @param in the input stream to read from
	 * @return the message received
	 * @throws IOException if the input stream has failed
	 * @throws ProtocolExeption if the specific message was not received
	 */
	public static String reciveMessage(String message,MyDataInputStream in) 
	throws IOException, ProtocolExeption {
		String msg = message;
		if(msg != null){
		if(!in.readUTF().equals(msg)) throw new ProtocolExeption();
		}
		else {
			msg = in.readUTF();
		}
		return msg;
	}
	
	/**
	 * Attempts reading a integer number from the input stream
	 * @param in the input stream to read from
	 * @return the integer number that was read from the input stream
	 * @throws IOException if there was a problem with the input stream
	 */
	public static Integer reciveInt(MyDataInputStream in) throws IOException {
		Integer number = in.readInt();
		return number;
	}
	
	/**
	 * Exercise specific: Attempts reading a DONE END message from the input stream
	 * @param in the input stream to read from
	 * @throws IOException if there was a problem with the input stream
	 * @throws ProtocolExeption if the DONE END messages were not received
	 */
	public static void reciveDoneEnd(MyDataInputStream in) throws IOException, ProtocolExeption {
		reciveMessage(DONE_MSG, in);
		reciveMessage(END_MSG, in);
	}
	
	/**
	 * Exercise specific: Attempts reading a ENDLIST END message from the input stream
	 * @param in the input stream to read from
	 * @throws IOException if there was a problem with the input stream
	 * @throws ProtocolExeption if the ENDLIST END messages were not received
	 */
	public static void reciveEndListEnd(MyDataInputStream in) throws IOException, ProtocolExeption {
		reciveMessage(END_LIST_MSG, in);
		reciveMessage(END_MSG, in);
	}
	
	/**
	 * Attempts reading a ENDSESSION END message from the input stream
	 * @param in the input stream to read from
	 * @throws IOException if there was a problem with the input stream
	 * @throws ProtocolExeption if the ENDSESSION END messages were not received
	 */
	public static void reciveEndSessionEnd(MyDataInputStream in) throws IOException, ProtocolExeption {
		reciveMessage(END_SESSION_MSG, in);
		reciveMessage(END_MSG, in);
	}
	
	/**
	 * Attempts writing DONE END message to the output stream
	 * @param out the output stream to write to
	 * @throws IOException if there was a problem with the output stream
	 * @throws ProtocolExeption if the DONE END messages were not sent
	 */
	public static void sendDoneEnd(MyDataOutputStream out) {
		sendMessage(DONE_MSG, out);
		sendMessage(END_MSG, out);
	}
	/**
	 * Attempts writing ENDLIST END message to the output stream
	 * @param out the output stream to write to
	 * @throws IOException if there was a problem with the output stream
	 * @throws ProtocolExeption if the ENDLIST END messages were not sent
	 */
	public static void sendEndListEnd(MyDataOutputStream out) {
		sendMessage(END_LIST_MSG, out);
		sendMessage(END_MSG, out);
	}
	/**
	 * Attempts writing ENDSESSION END message to the output stream
	 * @param out the output stream to write to
	 * @throws IOException if there was a problem with the output stream
	 * @throws ProtocolExeption if the ENDSESSION END messages were not sent
	 */
	public static void sendEndSessionEnd(MyDataOutputStream out) {
		sendMessage(END_SESSION_MSG, out);
		sendMessage(END_MSG, out);
	}
	/**
	 * Attempts writing ERROR message to the output stream
	 * @param out the output stream to write to
	 * @throws IOException if there was a problem with the output stream
	 * @throws ProtocolExeption if the ERROR message were not sent
	 */
	public static void sendError(MyDataOutputStream out) {
		sendMessage(ERROR_MSG, out);
	}
	
	/**
	 * Attempts reading a long number from the input stream
	 * @param in the input stream to read from
	 * @return the long number that was read from the input stream
	 * @throws IOException if there was a problem with the input stream
	 */
	public static long reciveLong(MyDataInputStream in) throws IOException {
		long number = in.readLong();
		return number;
	}
	/**
	 * Attempts writing a long number to the output stream
	 * @param in the output stream to write to
	 * @throws IOException if there was a problem with the input stream
	 */
	public static void sendLong(long _long, MyDataOutputStream out) {
		try {
			out.writeLong(_long);
		} catch (Exception e) {
			sendMessage(ERROR_MSG, out);
		}
	}
}