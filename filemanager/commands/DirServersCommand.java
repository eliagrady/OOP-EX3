//###############
// FILE : DirServersCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that print all name servers this file manager know.
//###############
package oop.ex3.filemanager.commands;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.protocol.Protocol;

public class DirServersCommand extends Command {
	private Pattern _usage = Protocol.DIRSERVERS_CMD;
	@Override
	public void run() {
		for (String nameServer : FileManagerDataBase._servers.getList()) {
			System.out.println(nameServer);
		}
	}
	@Override
	public Pattern getUsage() {
		return _usage;
	}

	@Override
	public void doCommand(Matcher match) {
		this.run();
	}

	@Override
	public void run(Matcher match) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
