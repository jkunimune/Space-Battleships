/**
 * 
 */
package interaction;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * @author jkunimune
 * The JPanel that shows the main menu, instructions menu, etc. when not in a match.
 */
public class Menu extends JPanel {

	private static final long serialVersionUID = -6605421223497517651L;
	
	
	
	public Menu(int w, int h) {
		super();
		
		super.setPreferredSize(new Dimension(w,h));
		super.setLayout(null);
		super.setOpaque(true);
	}

}
