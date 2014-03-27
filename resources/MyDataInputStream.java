//###############
// FILE : MyDataInputStream.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : MyDataInputStream wraps a DataInputStream to 
// extend it's functionality.
//###############
package com;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
public class MyDataInputStream {
	public DataInputStream _in;
	public MyDataInputStream(InputStream inputStream) {
		_in = new DataInputStream(inputStream);
	}
	public String readUTF() {
		String line = "";
		try {
			line = _in.readUTF();
		} catch (IOException e) {
		}
		//System.out.println("read input:"+line);
		return line;
	}
	public Integer readInt() {
		int num = -1;
		try {
			num = _in.readInt();
			//System.out.println("read input:"+num);
		} catch (IOException e) {
			//System.out.println("error");
		}
		return num;
	}
	public void close() {
		try {
			_in.close();
		} catch (IOException e) {
			//System.out.println("shit happans, and i cant close it");
		}
	}
	public long readLong() throws IOException {		
		return _in.readLong();
	}
	public int available() throws IOException {
		return _in.available();
	}
}
