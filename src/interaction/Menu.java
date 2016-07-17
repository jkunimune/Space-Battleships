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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	
	
	private HashMap<String, BufferedImage> buttons;
	private BufferedImage background;
	
	
	private Canvas canvs;
	private BufferStrategy strat;
	
	
	
	public Menu(int w, int h) {
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
	}
	
	
	
	@Override
	public void setVisible(boolean v) {
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		g.dispose();
		strat.show();
		
		super.setVisible(v);
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
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}

}
