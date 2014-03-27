//###############
// FILE : UserInputThread.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a thread that is responsible of 
// handling incoming user commands.
//###############
package oop.ex3.filemanager;

import java.util.Scanner;

import oop.ex3.filemanager.commands.CommandParser;
import oop.ex3.protocol.Protocol;

public class UserInputThread implements Runnable {
	Scanner input = new Scanner(System.in);
	String _line = "";
	CommandParser cmdParser = new CommandParser();
	@Override
	/**
	 * Attempts executing inputed user's commands. 
	 */
	public void run() {
		do {
			try {
				_line = input.nextLine().trim();
				cmdParser.exec(_line);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		} while (!_line.equals(Protocol.QUIT_MSG));
		MyFileManager.close();
	}

}
