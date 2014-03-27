//###############
// FILE : DownloadAttemptRequest.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a file request this file manager sends.
//###############
package oop.ex3.filemanager;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

public class DownloadAttemptRequest {
	public static boolean connectAndDownload(String ip,Integer port,String fileName) {
		boolean transferSuccess = false;
		MyDataOutputStream out = null;
		MyDataInputStream in = null;
		try {
			Socket socket = new Socket(ip,port);
			out = new MyDataOutputStream(socket.getOutputStream());
			Protocol.sendMessage(Protocol.WANT_FILE_MSG, out);
			Protocol.sendMessage(fileName, out);
			Protocol.sendEndMessage(out);
			in = new MyDataInputStream(socket.getInputStream());
			String path = FileManagerDataBase.getHomeDir()+fileName;
			FileOutputStream fileOutput = new FileOutputStream(path);
			String input = Protocol.reciveMessage(null, in);
			if(input.equals(Protocol.FILE_MSG)) {
				long length = Protocol.reciveLong(in);
				try {			
					for (int i = 0; i <= length; i++) {
						int data = in._in.read();
						if (data == -1) break;
						fileOutput.write(data);
					}			
					transferSuccess = true;
					return transferSuccess;
				} 
				catch (Exception e) {
					fileOutput.close();
					File f = new File(FileManagerDataBase.getHomeDir()+fileName);
					f.delete();//TODO check if download interrupts keeps file
				}
				finally {
					if(transferSuccess) {
						fileOutput.close();
					}
					else {
						fileOutput.close();
						File f = new File(path);
						f.delete();
					}
					
				}
				try {
					out.close();
				} catch (Exception e) {}
				try {
					in.close();
				} catch (Exception e) {}
				try {
					socket.close();
				} catch (Exception e) {}
			} 

			else if(input.equals(Protocol.FILE_NOT_FOUND_MSG)) {
				return transferSuccess;
			}
		}
		catch (Throwable e) {
			System.out.println(Protocol.ERROR_MSG);
		}
		return transferSuccess;
	}
}