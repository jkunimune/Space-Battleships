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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The JPanel that shows the main menu, instructions menu, etc. when not in a
 * match.
 * 
 * @author jkunimune
 * @version 1.0
 */
public class Menu extends JPanel {

	private static final long serialVersionUID = -6605421223497517651L;
	
	private static final Color COLOR_OFF = new Color(116, 88, 255);
	private static final Color COLOR_ON = new Color(66, 38, 221);
	private static final Color COLOR_LIT = new Color(250, 250, 250);
	private static final Font TITLE_FONT = new Font("Comic Sans MS", Font.BOLD, 96);
	private static final Font MENU_FONT = new Font("Comic Sans MS", Font.PLAIN, 36);
	
		
	private String menu_pos;
	
	private HashMap<String, String[][]> menu_structure;
	private HashMap<String, BufferedImage> buttons;
	private BufferedImage background;
	
	
	private Canvas canvs;
	private BufferStrategy strat;
	
	
	
	public Menu(int w, int h, String startMenu) {
		super();
		canvs = new Canvas();
		
		super.add(canvs);
		super.setPreferredSize(new Dimension(w,h));
		super.setLayout(null);
		super.setOpaque(true);
		super.setFocusable(true);
		
		canvs.setBounds(0,0,w,h);
		canvs.setIgnoreRepaint(true);
		canvs.setFocusable(true);
		
		loadImages();
		loadMenus();
		
		menu_pos = startMenu;
	}
	
	
	
	@Override
	public void setVisible(boolean v) {
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		String[][] gui = menu_structure.get(this.getState());
		for (String[] component: gui)
			draw(component, g);
		
		g.dispose();
		strat.show();
		
		super.setVisible(v);
	}
	
	
	private void draw(String[] component, Graphics2D g) {	// draw the given component
		if (component[0].equals("layt")) {		// for a layout
			BufferedImage img = buttons.get("layout_"+component[1]);
			int x = Integer.parseInt(component[2]) - img.getWidth()/2 + this.getWidth()/2;
			int y = Integer.parseInt(component[3]);
			g.drawImage(img, x, y, null);		// just draw the provide image
		}
		else if (component[0].equals("text")) {	// for text
			g.setColor(Color.WHITE);
			g.setFont(TITLE_FONT);
			int x = Integer.parseInt(component[1]) - g.getFontMetrics().stringWidth(component[3])/2 + this.getWidth()/2;
			int y = Integer.parseInt(component[2]);
			g.drawString(component[3], x, y);	// draw the text centered and big
		}
		else if (component[0].equals("butn")) {
			g.setColor(COLOR_OFF);
			g.setFont(MENU_FONT);
			int x = Integer.parseInt(component[2]) - g.getFontMetrics().stringWidth(component[4])/2 + this.getWidth()/2;
			int y = Integer.parseInt(component[3]);
			g.drawString(component[4], x, y);
		}
	}
	
	
	private void loadImages() {
		buttons = new HashMap<String, BufferedImage>();
		
		File[] files = new File("assets/images/menu").listFiles();
		for (File f: files) {	// for every file in that folder
			try {
				int i = f.getName().lastIndexOf('.');	// find the extension
				if (i != -1 && f.getName().endsWith(".png"))	// if it is a png file
					buttons.put(f.getName().substring(0,i), ImageIO.read(f));	// put it in the HashMap
			} catch (IOException e) {
				System.err.println("Something went wrong with "+f);	// I see no reason this error would ever throw
			}
		}
		
		try {
			background = ImageIO.read(new File("assets/images/menu/background.jpg"));
		} catch (IOException e) {
			System.err.println("Gah! Where's background.jpg? It's supposed to be in assets/images/menu/! Oh, well, I guess we'll just continue without it.");
		}
	}
	
	
	private void loadMenus() {	// loads the assets/other/menu_structure.txt file into menu_structure
		menu_structure = new HashMap<String, String[][]>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File("assets/other/menu_structure.txt")));
			String line = in.readLine();
			
			while (line != null) {		// until you hit the end of file:
				ArrayList<String[]> menu = new ArrayList<String[]>();
				
				while (!line.equals("")) {					// read until a blank line
					String[] component = line.split(":");	// split the line by ':'
					menu.add(component);					// and save the array
					line = in.readLine();
				}
				
				menu_structure.put(menu.get(0)[1], menu.toArray(new String[menu.size()][]));
				line = in.readLine();	// each group of string arrays is put in menu_structure
			}
			
			in.close();
		} catch (IOException e) {
			// TODO: not auto-generated try-catch
		}
	}
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}
	
	
	public String getState() {	// returns the current menu screen
		return menu_pos.substring(menu_pos.lastIndexOf("/")+1);	// the state is after the last slash in menu_pos
	}

}
