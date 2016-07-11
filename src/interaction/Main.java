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

import java.io.IOException;

import mechanics.Battlefield;
import network.Receiver;

/**
 * The driver class that runs the whole application.
 * 
 * @author	jkunimune
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
	
		final String hostName = "239.0.113.0";	// start by declaring the network variables
		Receiver ceiver;
		
		Screen mainWindow = new Screen(1280, 800);	// open the main menu
		
		try {
			ceiver = new Receiver(hostName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new Thread(ceiver).start();
		
		Battlefield field = new Battlefield(hostName);	// create a game
		mainWindow.lookAt(field);
		ceiver.setField(field);
		mainWindow.display();
		
		while (true) {				// and enter the main loop
			field.update();
			mainWindow.display();
		}
	
	}

}
