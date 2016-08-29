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
import java.net.UnknownHostException;

import interaction.Menu;

/**
 * This class exists for the sole purpose of establishing a connection on a port
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Connection implements Runnable {

	public static final int PORT_NUM = 62832;
	
	
	
	public static Connection makeDummyConnection(Menu m) {	// for testing purposes only!
		Connection c = new Connection(m);	// this Connection is non-functional
		new Thread(c).start();
		return c;
	}
	
	
	public static Connection hostConnection(Menu m) {	// opens a Connection as a client
		Connection c = new Connection(PORT_NUM, m);
		new Thread(c).start();
		return c;
	}
	
	
	public static Connection joinConnection(Menu m, String name) {	// opens a Connection as a host
		Connection c = new Connection(name, PORT_NUM, m);
		new Thread(c).start();
		return c;
	}
	
	
	
	private static final int DUMMY = 0;
	private static final int HOST = 1;
	private static final int CLIENT = 2;
	
	
	protected byte type;
	protected String name;
	protected int port;
	
	protected Socket socket;
	protected DataOutputStream out;
	protected DataInputStream in;
	protected double offset;
	
	protected ServerSocket ss;
	
	protected Menu menu;	// the class to alert when the thread finishes
	
	
	
	private Connection(Menu m) {
		menu = m;
		type = DUMMY;
	}
	
	
	private Connection(int portNum, Menu m) {
		port = portNum;
		menu = m;
		type = HOST;
	}
	
	
	private Connection(String hostname, int portNum, Menu m) {
		name = hostname;
		port = portNum;
		menu = m;
		type = CLIENT;
	}
	
	
	
	@Override
	public void run() {	// find a connection
		if (type != DUMMY) {
			try {
				if (type == HOST) {							// if you are the host
					ss = new ServerSocket(port);			// open a server socket
					socket = ss.accept();					// and wait for someone to contact you
				}
				else if (type == CLIENT) {						// if you are a client
					while (socket == null) {
						try {
							socket = new Socket(name, port);	// reach out to the server
						}
						catch (ConnectException e) {}			// and keep trying until it works
						catch (UnknownHostException e) {
							menu.abortJoin();
							menu = null;
							return;
						}
					}
				}
				
				out = new DataOutputStream(socket.getOutputStream());	// then create out and in
				in = new DataInputStream(socket.getInputStream());
				
				if (type == HOST) {							// finally, calculate offset
					long start = System.currentTimeMillis();
					out.writeLong(start);
					long middle = in.readLong();
					long end = System.currentTimeMillis();
					offset = start/2.0 + end/2.0 - middle;
				}
				else if (type == CLIENT) {
					in.readLong();
					out.writeLong(System.currentTimeMillis());
					offset = 0;
				}
			} catch (IOException e) {	// will only trip if someone tries to host on this computer twice
				return;
			}
		}
		
		menu.queueGame(this);	// tell the menu to start the game
		menu = null;
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
	
	
	public double getOffset() {
		if (type == DUMMY)	return 0.0;
		return offset;
	}
	
	
	public boolean isHost() {
		return type == HOST;
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

}
