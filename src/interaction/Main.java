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
package interaction;

import mechanics.Battlefield;
import network.Client;
import network.Connection;

/**
 * The driver class that runs the whole application.
 * 
 * @author	jkunimune
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
	
		String hostname;
		if (args.length >= 2)		// start by getting the hostname from args or from hard-code
			hostname = args[1];
		else
			hostname = "522MT32.olin.edu";
		
		Screen mainWindow = new Screen(1280, 800);	// open the main menu
		while (!(mainWindow.getState().equals("Host") || mainWindow.getState().equals("Join"))) {
			mainWindow.display();
		}
		
		Connection connection;			// establish a connection based on args
		if (args.length == 0)
			connection = Connection.makeDummyConnection();
		else if (args[0].equals("host"))
			connection = Connection.hostConnection();
		else if (args[0].equals("join"))
			connection = Connection.joinConnection(hostname);
		else
			throw new IllegalArgumentException("The first argument must be 'host', 'join', or blank. '"+args[0]+"' is not a valid argument.");
		Client receiver = Client.startListening(connection.getInput());	// start the receiver
		
		Battlefield field = new Battlefield(connection.getOutput());	// create a game
		receiver.setField(field);
		mainWindow.lookAt(field);
		mainWindow.display();
		
		while (true) {				// and enter the main loop
			field.update();
			mainWindow.display();
		}
	
	}

}
