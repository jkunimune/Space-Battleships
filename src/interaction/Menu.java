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
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import network.Connection;

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
	private static final Color COLOR_ON = new Color(104, 76, 247);
	private static final Color COLOR_LIT = new Color(250, 250, 250);
	private static final Font TITLE_FONT = new Font("Comic Sans MS", Font.BOLD, 96);
	private static final Font BODY_FONT = new Font("Comic Sans MS", Font.PLAIN, 24);
	private static final Font BUTTON_FONT = new Font("Papyrus", Font.PLAIN, 36);
	private static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 36);
	
	
	private String menuPos;
	
	private HashMap<String, String[][]> menu_structure;
	private BufferedImage buttonImg;
	private BufferedImage background;
	
	private Screen application;
	private MenuListener listener;
	
	private Canvas canvs;
	private BufferStrategy strat;
	
	private boolean menuChanged;
	
	
	
	public Menu(int w, int h, String startMenu, Screen s) {
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
		
		menuPos = startMenu;
		application = s;
		menuChanged = false;
	}
	
	
	
	@Override
	public void setVisible(boolean v) {	// display all buttons and GUI components
		if (strat == null) {	// if strat isn't ready yet, just wait
			super.setVisible(v);
			return;
		}
		
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		String[][] gui = menu_structure.get(this.getState());
		int[] heights = getHeights(gui, g);	// calculate the height of each component
		final int mPos = getMousePos(listener.getMouseLocation());
		final boolean menuChangedBefore = menuChanged;
		
		for (int i = 0; i < gui.length; i ++) {
			if (i == mPos) {
				if (listener.getMousePressed())
					draw(gui[i], g, heights[i], COLOR_LIT, menuChangedBefore);
				else
					draw(gui[i], g, heights[i], COLOR_ON, menuChangedBefore);
			}
			else
				draw(gui[i], g, heights[i], COLOR_OFF, menuChangedBefore);
		}
		
		g.dispose();
		strat.show();
		if (menuChangedBefore)
			menuChanged = false;
		
		super.setVisible(v);
	}
	
	
	private void draw(String[] component, Graphics2D g, int yRel, Color c, boolean autorun) {	// draw the given component
		if (component[0].equals("butn")) {		// button
			final BufferedImage img = buttonImg;
			final int xi = this.getWidth()/2 - img.getWidth()/2;
			final int yi = yRel + this.getHeight()/2 - img.getHeight()/2;
			g.drawImage(img, xi, yi, null);
			g.setFont(BUTTON_FONT);
			g.setColor(c);
			final int xs = this.getWidth()/2 - g.getFontMetrics().stringWidth(component[2])/2;
			final int ys = yRel + this.getHeight()/2 + g.getFontMetrics().getHeight()/4;
			g.drawString(component[2], xs, ys);
		}
		else if (component[0].equals("text")) {	// text
			if (component[1].equals("titl"))
				g.setFont(TITLE_FONT);
			if (component[1].equals("body"))
				g.setFont(BODY_FONT);
			g.setColor(Color.WHITE);
			final int xs = this.getWidth()/2 - g.getFontMetrics().stringWidth(component[2])/2;
			final int ys = yRel + this.getHeight()/2 + g.getFontMetrics().getHeight()/4;
			g.drawString(component[2], xs, ys);
		}
		else if (component[0].equals("inpt")) {	// text input
			g.setFont(MONO_FONT);
			final int ws = g.getFontMetrics().stringWidth("_")*30;
			final int hs = g.getFontMetrics().getHeight();
			final int xs = this.getWidth()/2 - ws/2;
			final int ys = yRel + this.getHeight()/2 + hs/2;
			final int wr = ws + 12;
			final int hr = hs + 12;
			final int xr = xs - 6;
			final int yr = ys - hs - 6;
			g.setColor(Color.WHITE);
			g.fillRect(xr, yr, wr, hr);
			g.setColor(Color.BLACK);
			g.fillRect(xr+2, yr+2, wr-4, hr-4);
			g.setColor(Color.WHITE);
			g.drawString(component[2], xs, ys - hs/4);
		}
	}
	
	
	private void loadImages() {
		try {
			buttonImg = ImageIO.read(new File("assets/images/menu/button.png"));
			background = ImageIO.read(new File("assets/images/menu/background.png"));
		} catch (IOException e) {
			System.err.println("Gah! Where's background.png? It's supposed to be in assets/images/menu/! Actually, you may or may not be missing button.png as well. Oh, well, I guess we'll just continue without 'em.");
		}
	}
	
	
	private void loadMenus() {	// loads the assets/other/menu_structure.txt file into menu_structure
		menu_structure = new HashMap<String, String[][]>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File("assets/other/menu_structure.txt")));
			String key;
			String line;
			
			while ((key = in.readLine()) != null) {		// until you hit the end of file:
				ArrayList<String[]> menu = new ArrayList<String[]>();
				
				while (!(line = in.readLine()).equals("")) {// read until a blank line
					String[] component = line.split("%");	// split the line by ':'
					for (int i = 0; i < component.length; i ++)
						if (component[i].equals(" ")) {	// if it is a space,
							component[i] = "";			// replace it with the empty string
						}
					menu.add(component);					// and save the array
				}
				
				menu_structure.put(key, menu.toArray(new String[menu.size()][]));
			}
			
			in.close();
		} catch (NullPointerException e) {
			System.err.println("ERROR: You forgot the \\n\\n at the end of menu_structure.txt");
			return;
		} catch (IOException e) {
			System.err.println("ERROR: Where is menu_structure.txt?! Ugh!");
		}
	}
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}
	
	
	public void addListener(MenuListener l) {
		canvs.addMouseListener(l);
		canvs.addMouseMotionListener(l);
		canvs.addKeyListener(l);
		listener = l;
	}
	
	
	public String getState() {	// returns the current menu screen
		return menuPos.substring(menuPos.lastIndexOf("/")+1);	// the state is after the last slash in menu_pos
	}
	
	
	public int getMousePos(Point coords) {	// decides which, if any, button the mouse is currently on
		return getMousePos(coords.x, coords.y);
	}
	
	
	public int getMousePos(int mx, int my) {	// return the button index this point is on
		int x = mx - this.getWidth()/2;		// normalize
		int y = my - this.getHeight()/2;	// to center
		
		if (x < -buttonImg.getWidth()/2 || x > buttonImg.getWidth()/2)
			return -1;	// -1 means no button
		
		final int dist = buttonImg.getHeight()/2;
		final Graphics2D g = (Graphics2D) strat.getDrawGraphics();
		final int[] heights = getHeights(menu_structure.get(getState()), g);
		
		for (int i = 0; i < heights.length; i ++)
			if (Math.abs(y-heights[i]) <= dist)
				return i;	// on the button
		return -1;	// no button here
	}
	
	
	public String getCommand(int but) {	// figure out the command for the button at index but
		if (but == -1)	return "null";
		
		String[][] gui = menu_structure.get(getState());
		if (gui[but][0].equals("butn")) {
			try {
				return gui[but][1];
			} catch (IndexOutOfBoundsException e) {
				return "null";
			}
		}
		return "null";
	}
	
	
	public void interpCommand(String command) {	// act according to this command
		if (command.equals("null"))
			return;
		
		else if (command.equals("back"))				// if it is a command
			goToMenu(menuPos.substring(0,menuPos.length()-5));
		else if (command.equals("exit"))				// execute it
			System.exit(ABORT);
		else if (command.equals("main"))
			menuPos = "main";
		else if (command.equals("host"))
			Connection.hostConnection(this);
		else if (command.equals("join"))
			Connection.joinConnection(this, listener.getInput());
		else if (command.equals("fake"))
			Connection.makeDummyConnection(this);
		
		else if (menu_structure.containsKey(command))	// if it is a menu
			goToMenu(menuPos+"/"+command);
		
		else
			System.err.println("ERROR: '"+command+"' is not a recognized command nor an existing menu.");
	}
	
	
	public void startGame(Connection c) {
		if (getState().equals("seek") ||
			getState().equals("wait") ||
			getState().equals("test"))	// XXX: temporary fix; later it should just terminate the thread when it leaves these menus
			application.startGame(c);
	}
	
	
	public void abortJoin() {	// go back to the menu screen 
		if (getState().equals("seek"))	// XXX: also a temporary fix
			goToMenu(menuPos+"/fail");
	}
	
	
	private void goToMenu(String newMenuPos) {	// change the menu screen to seomthing else
		menuPos = newMenuPos;
		listener.deleteTextbox();
		
		final String suffix = newMenuPos.substring(newMenuPos.lastIndexOf("/")+1);
		for (String[] component: menu_structure.get(suffix)) {	// the main reason this method is here
			if (component[0].equals("inpt"))					// is to focus on textboxes
				listener.setTextbox(component);
			else if (component[0].equals("auto"))				// and execute commands
				interpCommand(component[1]);
		}
	}
	
	
	private int[] getHeights(String[][] gui, Graphics2D g) {	// determines the correct y positions for the components in gui
		int totHeight = 0;
		int[] heights = new int[gui.length];
		for (int i = 0; i < gui.length; i ++) {	// first calculate what the heights would be if
			final int top = totHeight;			// if the menu were flush with the top of the screen
			if (gui[i][0].equals("butn")) {
				totHeight += buttonImg.getHeight();
			}
			else if (gui[i][0].equals("text")) {
				if (gui[i][1].equals("titl"))
					totHeight += g.getFontMetrics(TITLE_FONT).getHeight();
				else if (gui[i][1].equals("body"))
					totHeight += g.getFontMetrics(BODY_FONT).getHeight();
			}
			else if (gui[i][0].equals("inpt")) {
				totHeight += g.getFontMetrics(MONO_FONT).getHeight();
			}
			heights[i] = (top + totHeight)/2;
		}
		for (int i = 0; i < heights.length; i ++)	// normalize heights to be relative to the center of the screen
			heights[i] -= totHeight/2;
		return heights;
	}

}
