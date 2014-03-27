//###############
// FILE : DownloadThread.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represent a download service. the download service gets a
// communication session that is supposed to be an incoming download request,
// and handles it..
//###############
package oop.ex3.filemanager;
import java.net.*;
import java.io.*;

import oop.ex3.protocol.Protocol;
import oop.ex3.resources.MyDataInputStream;
import oop.ex3.resources.MyDataOutputStream;

/**
 * implements a download service. the download service gets a
 * communication session that is supposed to be an incoming download request,
 * and handles it.
 */
public class DownloadThread implements Runnable {
	private Socket _socket;
	
	/**
	 * creates a new download request handler object with the given request
	 * session.
	 * @param downloadRequest the download request session
	 */
	public DownloadThread(Socket downloadRequest) {
		_socket = downloadRequest;
	}
	
	@Override
	public void run() {
		DataInputStream fileInput = null;
		DataOutputStream fileOutput = null;
		MyDataInputStream in = null;
		MyDataOutputStream out = null;
		try {
			// open in and out streams
			in = new MyDataInputStream(_socket.getInputStream());
			out = new MyDataOutputStream(_socket.getOutputStream());
			fileOutput = new DataOutputStream(_socket.getOutputStream());
			// read the requested file info
			Protocol.reciveMessage(Protocol.WANT_FILE_MSG, in);
			String filename = in.readUTF();
			Protocol.reciveEndMessage(in);
			
			// search for the file
			File file = null;
			synchronized(FileManagerDataBase._files) {
				file = FileManagerDataBase.getFile(filename);
				if(file == null) {
					Protocol.sendMessage(Protocol.FILE_NOT_FOUND_MSG, out);
					return;
				}
				fileInput = new DataInputStream(new FileInputStream(file));
			}
			
			// send the file
			Protocol.sendMessage(Protocol.FILE_MSG, out);
			Protocol.sendLong(file.length(), out);
			while(fileInput.available() != 0) {
				fileOutput.write(fileInput.readByte());
			}
			Protocol.sendEndMessage(out);
		}
		catch(Throwable e) {
			try {
				Protocol.sendError(out);
			} catch(Throwable t) { }
		}
		finally {
			try {
				out.close();
			} catch(Throwable e) { }
			try {
				in.close();
			} catch(Throwable e) { }
			try {
				fileInput.close();
			} catch(Throwable e) { }
			try {
				_socket.close();
			} catch(Throwable e) { }
		}
	}
}
