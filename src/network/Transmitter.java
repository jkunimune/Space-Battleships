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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A class to send <code>DatagramSocket</code> signals to other computers' <code>Receiver</code> objects.
 * Upon running, it sends the current message to alert other players of orders and collisions.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Transmitter implements Runnable {

	public static final int PORT_NUM = 62832;
	
	
	protected DatagramSocket socket;	// the most important piece of this class
	protected InetAddress address;		// the IP Address of this server
	
	protected byte[] message;			// the byte[] it wants to send
	
	
	
	private Transmitter(String host) throws IOException {
		socket = new DatagramSocket(PORT_NUM);		// socket and address are the most important
		address = InetAddress.getByName(host);	// components as they communicate over the interweb
	}
	
	
	
	public void setMessage(byte[] b) {	// chooses the message to send TODO: make a queue system for messages?
		message = b;
	}
	
	
	@Override
	public void run() {	// broadcasts the message b across the internoodle
		DatagramPacket packet = new DatagramPacket(message, message.length, address, socket.getLocalPort());
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("I didn't know if this error would ever trigger, but I guess it did. You can remove these print statements now.");
		}
	}
	
	
	public void close() {		// stops and tells all the receivers to stop
		socket.close();
	}
	
	
	
	public static void transmit(byte[] data, String host) {	// starts a new Transmitter thread and sends some data
		try {
			Transmitter t = new Transmitter(host);
			t.setMessage(data);
			new Thread(t).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not resolve host name (I think. That's an error message I get a lot, anyway, so I'm guessing it applies here).");
		}
	}

}
