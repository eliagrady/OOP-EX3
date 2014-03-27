//###############
// FILE : RemoveCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that deletes a file from this file manager's database.
//###############
package oop.ex3.filemanager.commands;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.filemanager.NotifyNameServerRequest;
import oop.ex3.protocol.Protocol;

public class RemoveCommand extends Command {
private Pattern _usage = Protocol.REMOVE_CMD;
public static final String REMOVE_FAIL = 
	"It is impossible to delete an absent file";
public static final String REMOVE_OK = "Removing Done";
	
	@Override
	public Pattern getUsage() {
		return _usage;
	}
	
	@Override
	public void run(Matcher match) {
		for (String f : FileManagerDataBase._files.getList()) {
			if(match.group(1).contentEquals(f)) {
				try {
					//TODO perform block to new upload request
					FileManagerDataBase._files.remove(f);
					File file = new File(FileManagerDataBase.getHomeDir()+ f);
					file.delete();
					//Update name servers that this file was deleted
					for (String server : FileManagerDataBase._servers.getList()) {
						NotifyNameServerRequest.notifyContain(file.getName(), server, false);
					}
				}
				catch (Exception e) {
					System.out.println(REMOVE_FAIL);
				}
				break;
			}
		}
	}

	public void doCommand(Matcher match) {
		this.run(match);
	}

	@Override
	public void run() {
	}
}
