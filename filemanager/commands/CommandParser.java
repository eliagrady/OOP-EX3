//###############
// FILE : CommandParser.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A parser that parses and executes user input commands.
//###############
package oop.ex3.filemanager.commands;
import java.util.ArrayList;
import java.util.regex.Matcher;



public class CommandParser {
	private ArrayList<Command> _knownCommands;
	
	/**
	 * creates a new command parser, and initializes it's known commands 
	 * to be the default commands mentioned in the exercise description.
	 * If we ever need adding new commands, we must also add it here.
	 */
	public CommandParser() {
		_knownCommands = new ArrayList<Command>();
		_knownCommands.add(new DirCommand());
		_knownCommands.add(new DirServersCommand());
		_knownCommands.add(new DirAllFilesCommand());
		_knownCommands.add(new RemoveCommand());
		_knownCommands.add(new RenameCommand());
		_knownCommands.add(new AddCommand());
		_knownCommands.add(new FireServersCommand());
		_knownCommands.add(new QuitCommand());
	}
	
	/**
	 * parse then execute a command
	 * @param text the command text
	 * @throws UnknownCommandException if the text cannot be resolved to a
	 * legal command
	 */
	public synchronized void exec(String text) throws UnknownCommandException {
		for(Command command: _knownCommands) {
			Matcher m = command.getUsage().matcher(text);
			if(m.matches()) {
				command.doCommand(m);
				return;
			}
		}
		throw new UnknownCommandException();
	}
}