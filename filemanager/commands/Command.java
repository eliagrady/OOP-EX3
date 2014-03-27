//###############
// FILE : Command.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a user input command.
//###############
package oop.ex3.filemanager.commands;
import java.util.regex.*;

public abstract class Command implements Runnable{
	private Pattern _patt;
	/**
	 * asks the command for it's usage pattern
	 * @return the usage pattern of the command
	 */
	public Pattern getUsage() {
		return _patt;
	}
	/**
	 * Executes the command, given a match to the command's usage pattern.
	 * @param match a matcher that has currently matched the command's usage
	 * pattern.
	 */
	public abstract void doCommand(Matcher match);
	/**
	 * run the command according to matcher arguments , using command's usage
	 * pattern.
	 * @param match a matcher that has currently matched the command's usage
	 * pattern, contains arguments necessary for operation
	 * @throws Exception if there's a problem with execution of the command
	 */
	public abstract void run(Matcher match) throws Exception;
}
