//###############
// FILE : UploadThread.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents an upload Thread that expects requests to this 
// file manager for one of it's files.
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
public class UploadThread implements Runnable {
	private Socket _socket;

	/**
	 * creates a new download request handler object with the given request
	 * session.
	 * @param downloadRequest the download request session
	 * @param t 
	 */
	public UploadThread(Socket downloadRequest, Thread t) {
		_socket = downloadRequest;
	}

	@Override
	public void run() {
		MyDataInputStream in = null;
		MyDataInputStream fileInput = null;
		MyDataOutputStream out = null;
		
		try {
			// open in and out streams
			in = new MyDataInputStream(_socket.getInputStream());
			out = new MyDataOutputStream(_socket.getOutputStream());
			// read the requested file info
			Protocol.reciveMessage(Protocol.WANT_FILE_MSG, in);
			String filename = Protocol.reciveMessage(null,in);
			Protocol.reciveEndMessage(in);

			// search for the file
			File file = null;
			synchronized(FileManagerDataBase._files) {
				file = FileManagerDataBase.getFile(filename);
				if(file == null) {
					Protocol.sendMessage(Protocol.FILE_NOT_FOUND_MSG , out);
					Protocol.sendEndMessage(out);
					return;
				}
				fileInput = new MyDataInputStream(new FileInputStream(file));
			}

			// send the file
			Protocol.sendMessage(Protocol.FILE_MSG, out);
			Protocol.sendLong(file.length() , out);
			InputStream file_in = new FileInputStream(file);
			for (int i = 0; i <= file.length(); i++) {
				int data = file_in.read();
				if (data == -1) break;
				out._out.write(data);
			}
			Protocol.sendEndMessage(out);
			file_in.close();
			this.notify();
		}
		catch(Throwable e) {
			try {
				Protocol.sendError(out);
			} 
			catch(Throwable t) {
				//DO NOTHING
			}
		}
		//TRY TO CLOSE ALL OPEN BUFFERS AND REMOVE THIS THREAD ANYWAY
		finally {
			try {
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
			} catch (Exception e2) {
				// TODO: handle exception
			}
			finally {
				MyFileManager._threads.remove(this);
			}
		}
	}
}
