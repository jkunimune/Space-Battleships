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
import java.net.InetAddress;
import java.net.MulticastSocket;

import mechanics.Battlefield;

/**
 * A class to receive <code>DatagramSocket</code> signals sent by <code>Transmitter</code> objects.
 * Upon running, it continuously listens for input and alerts the game of any information it receives.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Receiver implements Runnable {

	protected MulticastSocket socket;
	protected InetAddress address;
	
	protected Battlefield field;
	
	protected boolean stillGoing;	// whether the game is in session
	
	
	
	public Receiver(String host) throws IOException {
		socket = new MulticastSocket(Transmitter.PORT_NUM);
		address = InetAddress.getByName(host);
		socket.joinGroup(address);
	}
	
	
	
	public void setField(Battlefield bf) {	// assigns it a new battlefield
		field = bf;
	}
	
	
	@Override
	public void run() {	// starts the receiver thread and listens
		while (stillGoing)
			listen();
		
		try {
			socket.leaveGroup(address);
		} catch (IOException e) {}
		socket.close();
	}
	
	
	public void close() {	// stops the receiver
		stillGoing = false;	// TODO: I wonder if this will be enough to stop it...
	}
	
	
	private void listen() {	// waits for data and returns false when the game ends
		byte[] b = new byte[26];
		DatagramPacket packet = new DatagramPacket(b, b.length);
		try {
			socket.receive(packet);
			
			field.receive(packet.getData(), false);
		} catch (IOException e) {}
	}

}
