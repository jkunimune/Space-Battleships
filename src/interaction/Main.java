package interaction;

import mechanics.Battlefield;

/**
 * @author jkunimune
 * The driver class that runs the whole application.
 */
public class Main {

	public static void main(String[] args) {
		Screen mainWindow = new Screen(1280, 800);
		Battlefield field = new Battlefield();
		mainWindow.lookAt(field);
		mainWindow.display();
		
		while (true) {
			field.update();
			mainWindow.display();
		}
	}

}
