//###############
// FILE : RenameCommand.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : A command that renames a file this file manager holds.
//###############
package oop.ex3.filemanager.commands;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oop.ex3.filemanager.FileManagerDataBase;
import oop.ex3.filemanager.NotifyNameServerRequest;
import oop.ex3.protocol.Protocol;

public class RenameCommand extends Command {
private Pattern _usage = Protocol.RENAME_CMD;
public static final String RENAME_FAIL_ABSENT = 
	"It is impossible to delete an absent file";
public static final String RENAME_FAIL_EXIST = 
	"It is illegal to use an existing file name as a new name";
public static final String RENAME_OK = "Renaming Done";
	
	@Override
	public Pattern getUsage() {
		return _usage;
	}
	
	@Override
	public void run(Matcher match) throws Exception {
		File newName = new File(FileManagerDataBase.getHomeDir()+match.group(3)); 
		for (String f2 : FileManagerDataBase._files.getList()) {
			if(newName.getName().contentEquals(f2)) {
				//check if the old file is in the database
				for (String f : FileManagerDataBase._files.getList()) {
					if(match.group(1).contentEquals(f)) {
						throw new Exception(RENAME_FAIL_EXIST);
					}
				}
				throw new Exception(RENAME_FAIL_ABSENT);
			}
		}
		boolean success = false;
		for (String fileName : FileManagerDataBase._files.getList()) {
			if(match.group(1).contentEquals(fileName)) {
				for (String server : FileManagerDataBase._servers.getList()) {
					NotifyNameServerRequest.notifyContain(fileName, server, false);
				}
				// Rename file //TODO notify name servers about the change
				File file = new File(FileManagerDataBase.getHomeDir() + fileName);
				success = file.renameTo(newName);
				if (!success) {
					throw new Exception(Protocol.ERROR_MSG);
				}
				FileManagerDataBase._files.remove(fileName);
				FileManagerDataBase._files.putIfAbsent(newName.getName());
				System.out.println(RENAME_OK);
				//TODO perform block to new upload request
				for (String server : FileManagerDataBase._servers.getList()) {
					NotifyNameServerRequest.notifyContain(newName.getName(), server, true);
				}
				//TODO Update servers that this file was deleted using DONTCONTAINFILE & CONTAINFILE
				break;
			}
		}
		if(!success) {
			throw new Exception(RENAME_FAIL_ABSENT);
		}
	}

	public void doCommand(Matcher match) {
		try {
			this.run(match);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
	}
}