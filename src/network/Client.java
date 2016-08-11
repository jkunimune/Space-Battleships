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
import java.io.IOException;
import mechanics.Battlefield;

/**
 * A class to read the <code>Socket</code> to get information from the internebs.
 * Upon running, it continuously listens for input and alerts the game of any information it receives.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Client implements Runnable {

	protected DataInputStream in;
	
	protected Battlefield field;
	
	
	
	public Client(DataInputStream dis, Battlefield bf) {
		in = dis;
		field = bf;
	}
	
	
	public Client() {	// for testing only! Clients instantiated like this will be non-functional
		in = null;
		field = null;
	}
	
	
	
	public void setField(Battlefield bf) {	// assigns it a new battlefield
		field = bf;
	}
	
	
	@Override
	public void run() {	// starts the receiver thread and listens
		if (in != null) {
			String data;
			try {
				while ((data = in.readUTF()) != null) {
					if (field != null) {
						field.receive(data, false);		// feed the game any data you get
					}
					if (!field.active()) {
						in.close();
						return;
					}
				}
			} catch (IOException e) {
				field.receive(Protocol.denoteVictory(1));	// an exit code of 1 denotes connection loss
				return;
			}
		}
	}
	
	
	
	public static Client startListening(DataInputStream input, Battlefield field) {	// opens a receiver and sets it listening
		Client c = new Client(input, field);
		new Thread(c).start();
		return c;
	}

}
