/**
 * Copyright (c) 2016 Kunimune
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class exists for the sole purpose of establishing a connection on a port
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Connection implements Runnable {

	private static final int DUMMY = 0;
	private static final int HOST = 1;
	private static final int CLIENT = 2;
	public static final int PORT_NUM = 62832;
	
	
	protected byte type;
	protected String name;
	protected int port;
	
	protected Socket socket;
	protected DataOutputStream out;
	protected DataInputStream in;
	
	protected ServerSocket ss;
	
	
	
	private Connection() {
		type = DUMMY;
	}
	
	
	private Connection(int portNum) {
		port = portNum;
		type = HOST;
	}
	
	
	private Connection(String hostname, int portNum) {
		name = hostname;
		port = portNum;
		type = CLIENT;
	}
	
	
	
	@Override
	public void run() {	// find a connection
		if (type == DUMMY)	return;
		
		try {
			if (type == HOST) {									// if you are the host
				ss = new ServerSocket(port);	// open a server socket
				socket = ss.accept();					// and wait for someone to contact you
			}
			else if (type == CLIENT) {											// if you are a client
				while (socket == null) {
					try {
					socket = new Socket(name, port);		// reach out to the server
					} catch (ConnectException e) {}
				}
			}
			
			out = new DataOutputStream(socket.getOutputStream());	// then create out and in
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean running() {
		return type == DUMMY || in == null;
	}
	
	
	public Socket getSocket() {
		if (type == DUMMY)	return null;
		return socket;
	}
	
	
	public DataOutputStream getOutput() {
		if (type == DUMMY)	return null;
		return out;
	}
	
	
	public DataInputStream getInput() {
		if (type == DUMMY)	return null;
		return in;
	}
	
	
	public void close() {
		try {
			socket.close();
			out.close();
			in.close();
			ss.close();
		}
		catch (IOException e) {}
		catch (NullPointerException e) {}
	}
	
	
	@Override
	public String toString() {
		return  "Socket: " + socket + "\n" +
				"Output: " + out + "\n" +
				"Input:  " + in;
	}
	
	
	
	public static Connection makeDummyConnection() {	// for testing purposes only!
		Connection c = new Connection();	// this Connection is non-functional
		return c;
	}
	
	
	public static Connection hostConnection() {	// opens a Connection as a client
		Connection c = new Connection(PORT_NUM);
		new Thread(c).start();
		while (c.running()) {System.out.print("");}	// until I figure out what to do while this thread is running, just wait for it
		return c;
	}
	
	
	public static Connection joinConnection(String name) {	// opens a Connection as a host
		Connection c = new Connection(name, PORT_NUM);
		new Thread(c).start();
		while (c.running()) {System.out.print("");}	// until I figure out what to do while this thread is running, just wait for it
		return c;
	}

}
