//###############
// FILE : MyDataOutputStream.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : MyDataOutputStream wraps a DataOutputStream to 
// extend it's functionality.
//###############
package com;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyDataOutputStream {
	public DataOutputStream _out;
	public MyDataOutputStream(OutputStream outputStream) {
		_out = new DataOutputStream(outputStream);
	}
	public void writeUTF(String text) {
		try {
			//System.out.println("write:"+text);
			_out.writeUTF(text);
		} catch (IOException e) {
			//System.out.println("error");
		}
	}
	public void writeInt(int text) {
		try {
			//System.out.println("write:"+text);
			_out.writeInt(text);
		} catch (IOException e) {
			//System.out.println("error");
		}
	}
	public void close() {
		try {
			_out.close();
		} catch (IOException e) {
			//System.out.println("error");
		}
	}
	public void writeLong(long _long) {
		try {
			//System.out.println("write:"+_long);
			_out.writeLong(_long);
		} catch (IOException e) {
			//System.out.println("error");
		}
	}
}
