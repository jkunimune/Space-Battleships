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
import java.awt.MouseInfo;
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
	private static final Font TITLE_FONT = new Font("Papyrus", Font.BOLD, 96);
	private static final Font BODY_FONT = new Font("Papyrus", Font.PLAIN, 24);
	private static final Font BUTTON_FONT = new Font("Comic Sans MS", Font.PLAIN, 36);
	
	
	private String menu_pos;
	
	private HashMap<String, String[][]> menu_structure;
	private BufferedImage buttonImg;
	private BufferedImage background;
	
	private MenuListener listener;
	
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
		int[] heights = getHeights(gui, g);	// calculate the height of each component
		final int mPos = getMousePos(listener.getMouseLocation());
		
		for (int i = 0; i < gui.length; i ++)
			if (i == mPos) {
				if (listener.getMousePressed())
					draw(gui[i], g, heights[i], COLOR_LIT);
				else
					draw(gui[i], g, heights[i], COLOR_ON);
			}
			else
				draw(gui[i], g, heights[i], COLOR_OFF);
		
		g.dispose();
		strat.show();
		
		super.setVisible(v);
	}
	
	
	private void draw(String[] component, Graphics2D g, int yRel, Color c) {	// draw the given component
		BufferedImage img;
		if (component[0].equals("butn")) {
			img = buttonImg;
			int xi = this.getWidth()/2 - img.getWidth()/2;
			int yi = yRel + this.getHeight()/2 - img.getHeight()/2;
			g.drawImage(img, xi, yi, null);
			g.setFont(BUTTON_FONT);
			g.setColor(c);
			int xs = this.getWidth()/2 - g.getFontMetrics().stringWidth(component[2])/2;
			int ys = yRel + this.getHeight()/2 + g.getFontMetrics().getHeight()/4;
			g.drawString(component[2], xs, ys);
		}
		else if (component[0].equals("text")) {
			if (component[1].equals("titl"))
				g.setFont(TITLE_FONT);
			if (component[1].equals("body"))
				g.setFont(BODY_FONT);
			g.setColor(Color.WHITE);
			int xs = this.getWidth()/2 - g.getFontMetrics().stringWidth(component[2])/2;
			int ys = yRel + this.getHeight()/2 + g.getFontMetrics().getHeight()/4;
			g.drawString(component[2], xs, ys);
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
					String[] component = line.split(":");	// split the line by ':'
					menu.add(component);					// and save the array
				}
				
				menu_structure.put(key, menu.toArray(new String[menu.size()][]));
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
	
	
	public void addListener(MenuListener l) {
		canvs.addMouseListener(l);
		canvs.addMouseMotionListener(l);
		listener = l;
	}
	
	
	public String getState() {	// returns the current menu screen
		return menu_pos.substring(menu_pos.lastIndexOf("/")+1);	// the state is after the last slash in menu_pos
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
	
	
	public int[] getHeights(String[][] gui, Graphics2D g) {	// determines the correct y positions for the components in gui
		int totHeight = 0;
		int[] heights = new int[gui.length];
		for (int i = 0; i < gui.length; i ++) {	// first calculate what the heights would be if
			final int top = totHeight;			// if the menu were flush with the top of the screen
			if (gui[i][0].equals("butn"))
				totHeight += buttonImg.getHeight();
			else if (gui[i][0].equals("text")) {
				if (gui[i][1].equals("titl"))
					totHeight += g.getFontMetrics(TITLE_FONT).getHeight();
				else if (gui[i][1].equals("body"))
					totHeight += g.getFontMetrics(BODY_FONT).getHeight();
			}
			heights[i] = (top + totHeight)/2;
		}
		for (int i = 0; i < heights.length; i ++)	// normalize heights to be relative to the center of the screen
			heights[i] -= totHeight/2;
		return heights;
	}

}
