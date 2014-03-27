//###############
// FILE : DirCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION: A command that print all files this file manager have.
//###############
package oop.ex3.filemanager.commands;

import java.util.regex.*;

import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.protocol.Protocol;

public class DirCommand extends Command {
	private Pattern _usage = Protocol.DIR_CMD;
	
	@Override
	public Pattern getUsage() {
		return _usage;
	}
	
	@Override
	public void doCommand(Matcher match) {
		try {
			this.run(match);
		} catch (Exception e) {
			//DO NOTHING
		}
	}

	@Override
	public void run(Matcher match) throws Exception {
		for (String f : FileManagerDataBase._files.getList()) {
			System.out.println(f);
		}
	}

	@Override
	public void run() {
	}
}
