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

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mechanics.Battlefield;
import mechanics.Body;
import mechanics.Ship;

/**
 * The JPanel that displays the gameplay objects and HUD during a match.
 * 
 * @author jkunimune
 * @version 1.0
 */
public class GameScreen extends JPanel {

	private static final long serialVersionUID = -4461350953048532763L;
	
	private static final double MIN_SCALE = Math.exp(-1);	// the limits of the zooming
	private static final double MAX_SCALE = Math.exp(1);
	
	private static final Color MSG_COLOR = new Color(250, 245, 250);
	private static final Font MSG_FONT = new Font("Comic Sans MS", Font.ITALIC, 64);
	
	
	private HashMap<String, BufferedImage> sprites;	// the images it uses to display objects
	private HashMap<String, BufferedImage> icons;
	private HashMap<String, AudioClip> sounds;
	
	private Application main;
	private Battlefield game;
	private Controller listener;
	private Ship activeShip;
	
	private Canvas canvs;			// some necessary java.awt stuff
	private BufferStrategy strat;
	
	private int origX, origY;
	private double scale;	// the variables that define the screen's position and zoom-level
	
	boolean gameStarted = false;	// whether we are in the game or the pregame
	
	
	
	public GameScreen(int w, int h, Battlefield field, Application app) {
		super();
		canvs = new Canvas();
		main = app;
		game = field;
		
		super.add(canvs);
		super.setPreferredSize(new Dimension(w,h));
		super.setLayout(null);
		super.setOpaque(true);
		super.setFocusable(true);
		
		canvs.setBounds(0,0,w,h);
		canvs.setIgnoreRepaint(true);
		canvs.setFocusable(true);
		
		addListener(new ShipPlacer(this, field));
		
		loadImages();
		loadSounds();
		
		origX = w/2;
		origY = h/2;
		scale = 1.0;
		gameStarted = false;
	}
	
	
	
	@Override
	public void setVisible(boolean v) {	// draws all things and plays sounds and displays itself
		if (strat == null) {	// if strat isn't ready yet, give it more time
			super.setVisible(v);
			return;
		}
		
		if (!game.update()) {	// start by updating the game model
			main.goToMenu();
			return;				// and quitting if the game is over
		}
		
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		final double t = (double)System.currentTimeMillis();
		
		g.drawImage(icons.get("space"), 0, 0, null);
		
		for (int i = game.getBodies().size()-1; i >= 0; i --) {	// then draw the bodies
			final Body b = game.getBodies().get(i);
			
			if (b.existsAt(t))
				draw(b, g, t);
			try {
				sounds.get(b.soundName(t)).play();
			} catch (NullPointerException e) {}
		}
		
		if (gameStarted)
			drawHUD(g, t);		// and the heads-up display
		else
			drawPregame(g);		// or the pre-game HUD
		
		drawMessage(g, t);
		
		g.dispose();
		strat.show();
		super.setVisible(v);
	}
	
	
	private void draw(Body b, Graphics2D g, double t) {	// put a picture of b on g at time t
		BufferedImage img;
		if (gameStarted && b instanceof Ship && ((Ship) b).getID() == listener.getShip())
			img = sprites.get(b.spriteName()+"i");	// active ships have a special sprite
		else
			img = sprites.get(b.spriteName());	// finds the correct sprite
		if (img == null)
			throw new NullPointerException("Image "+b.spriteName()+".png not found!");
		try {
			img = executeTransformation(img, b.spriteTransform(t), b.doesScale());	// does any necessary transformations
		} catch (java.awt.image.RasterFormatException e) {
			return;	// if there's a problem with the transformation (probably roundoff), just skip it
		} catch (java.awt.image.ImagingOpException e) {
			return;	// I don't know the difference between these two exceptions
		}
		double screenX = screenXFspaceX(b.xValAt(t));	// gets coordinates of b, 
		double screenY = screenYFspaceY(b.yValAt(t));	// and offsets appropriately
		g.drawImage(img, (int)screenX-img.getWidth()/2, (int)screenY-img.getHeight()/2, null);
	}
	
	
	private void drawPregame(Graphics2D g) {	// draw the ships being placed
		// TODO: Draw available ships and held ship
	}
	
	
	private void drawHUD(Graphics2D g, double t) {	// draw the heads up display
		final int pos = getMousePos(listener.getMouseLocation(), t);
		final int active = listener.getOrder();
		
		if (active == -2)	// the buttons may be lit, on, or off depending on circumstance
			g.drawImage(icons.get("button0_lt"), 0, 0, null);
		else if (pos == -2)
			g.drawImage(icons.get("button0_on"), 0, 0, null);
		else
			g.drawImage(icons.get("button0_of"), 0, 0, null);
		
		if (active == -3)
			g.drawImage(icons.get("button1_lt"), 0, 400, null);
		else if (pos == -3)
			g.drawImage(icons.get("button1_on"), 0, 400, null);
		else
			g.drawImage(icons.get("button1_of"), 0, 400, null);
		
		if (active == -4)
			g.drawImage(icons.get("button2_lt"), 880, 400, null);
		else if (pos == -4)
			g.drawImage(icons.get("button2_on"), 880, 400, null);
		else
			g.drawImage(icons.get("button2_of"), 880, 400, null);
		
		if (activeShip == null)		return;	// if there's no selected ship, that's the end of it
		try {								// otherwise, draw more HUD
			final Point hudPos = new Point(1050, 30);
			g.drawImage(icons.get("bars"), hudPos.x, hudPos.y, null);	// draw more HUD stuff
			
			AffineTransform at;		// these classes help with the HP/PP bars
			AffineTransformOp op;
			BufferedImage mask;
			
			final double hTheta = Math.PI/2 - activeShip.hValAt(t)/Ship.MAX_H_VALUE*Math.PI/2;
			at = new AffineTransform();			// start with an AffineTransform
			at.rotate(hTheta, 200, 200);		// set it to rotate based on health
			op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			mask = op.filter(icons.get("health_mask"), null).getSubimage(200,0,200,200);	// now rotate the mask and draw it over the bars
			g.drawImage(mask, hudPos.x, hudPos.y, null);
			
			final double eTheta = -Math.PI/2 + activeShip.eValAt(t)/Ship.MAX_E_VALUE*Math.PI/2;
			at = new AffineTransform();			// repeat for energy bar
			at.rotate(eTheta, 0, 200);
			op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			mask = op.filter(icons.get("energy_mask"), null).getSubimage(0, 0, 200, 200);
			g.drawImage(mask, hudPos.x, hudPos.y, null);
		} catch (NullPointerException e) {}	// it might throw a NullPointerException if the controller modifies activeShip at the wrong moment
	}
	
	
	private void drawMessage(Graphics2D g, double t) {
		if (game.message.isEmpty())	// if there is no message,
			return;						// don't do anything
		
		final BufferedImage img = icons.get("popup");
		final int xi = this.getWidth()/2 - img.getWidth()/2;
		final int yi = this.getHeight()/2 - img.getHeight()/2;
		g.drawImage(img, xi, yi, null);
		g.setColor(MSG_COLOR);
		g.setFont(MSG_FONT);
		final int xs = this.getWidth()/2 - g.getFontMetrics().stringWidth(game.message)/2;
		final int ys = this.getHeight()/2 + g.getFontMetrics().getHeight()/4;
		g.drawString(game.message, xs, ys);
	}
	
	
	private BufferedImage executeTransformation(BufferedImage img, double[] params, boolean zoomScale) {	// rotozooms img based on params
		double zoominess;
		if (zoomScale)		zoominess = scale;	// the scale might affect the AffineTransform
		else				zoominess = 1.0;
		
		AffineTransform at = new AffineTransform();
		
		at.scale(params[1]/zoominess, params[2]/zoominess);					// scales (if necessary)
		
		if (params[0] != 0.0)
			at.rotate(params[0], img.getWidth()/2, img.getHeight()/2);		// rotates (if necessary)
		
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		Point size = new Point((int) (img.getWidth()*params[1]/zoominess),	// this is how big it would be if it didn't pad with zeros
				(int) (img.getHeight()*params[2]/zoominess));
		return op.filter(img, null).getSubimage(0, 0, size.x, size.y);	// executes affine transformation, crops, and returns
	}
	
	
	private void loadImages() {	// reads all images in the assets directory and saves them in a HashMap
		sprites = new HashMap<String, BufferedImage>();
		icons = new HashMap<String, BufferedImage>();
		
		File[] files = new File("assets/images/game").listFiles();
		for (File f: files) {	// for every file in that folder
			try {
				int i = f.getName().lastIndexOf('.');	// find the extension
				if (i != -1 && f.getName().endsWith(".png"))	// if it is a png file
					sprites.put(f.getName().substring(0,i), ImageIO.read(f));	// put it in the HashMap
			} catch (IOException e) {
				System.err.println("Something went wrong with "+f);	// I see no reason this error would ever throw
			}
		}
		
		files = new File("assets/images/interface").listFiles();	// do the same for HUD pics now
		for (File f: files) {	// for every file in that folder
			try {
				int i = f.getName().lastIndexOf('.');	// find the extension
				if (i != -1 && f.getName().endsWith(".png"))	// if it is a png file
					icons.put(f.getName().substring(0,i), ImageIO.read(f));	// put it in the HashMap
			} catch (IOException e) {
				System.err.println("Something went wrong with "+f);	// I see no reason this error would ever throw
			}
		}
	}
	
	
	private void loadSounds() {
		sounds = new HashMap<String, AudioClip>();
		
		File[] files = new File("assets/sounds").listFiles();
		for (File f: files) {	// for every file in that folder
			try {
				int i = f.getName().lastIndexOf('.');	// find the extension
				if (i != -1 && f.getName().endsWith(".wav"))	// if it is a wav file
					sounds.put(f.getName().substring(0,i), Applet.newAudioClip(f.toURI().toURL()));	// put it in the HashMap
			} catch (IOException e) {
				System.err.println("Something went wrong with "+f);	// I see no reason this error would ever throw
			}
		}
	}
	
	
	public void startGame() {	// exit pregame and begin the real battle
		gameStarted = true;
		addListener(new Controller(this, game));
	}
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}
	
	
	public void addListener(Controller c) {
		if (listener != null) {
			canvs.removeMouseListener(listener);
			canvs.removeMouseWheelListener(listener);
			canvs.removeMouseMotionListener(listener);
			canvs.removeKeyListener(listener);
		}
		
		canvs.addMouseListener(c);
		canvs.addMouseWheelListener(c);
		canvs.addMouseMotionListener(c);
		canvs.addKeyListener(c);
		
		listener = c;
	}
	
	
	public void zoom(int amount, int mx, int my) {	// changes scale based on a multiplicative amount
		final double expAmount = Math.exp(amount/5.0);
		if (amount > 0 && scale*expAmount >= MAX_SCALE)	// first check that you aren't out of bounds
			return;
		if (amount < 0 && scale*expAmount <= MIN_SCALE)
			return;
		
		scale *= expAmount;		// then reset scale
		
		origX = (int) Math.round((origX-mx)/expAmount) + mx;	// then alter origX and origY
		origY = (int) Math.round((origY-my)/expAmount) + my;
	}
	
	
	public void pan(int delX, int delY) {	// changes offsetX and offsetY based on a mouse drag
		origX += delX;
		origY += delY;
	}
	
	
	public byte getMousePos(Point mCoords, double t) {	// decides which, if any, button the mouse is currently on
		return getMousePos(mCoords.x, mCoords.y, t);
	}
	
	
	public byte getMousePos(int x, int y, double t) {	// decides which, if any, button the mouse is currently on
		final int r2 = 190000;
		if (Math.pow(x, 2)*3 + Math.pow(y, 2) < r2)				return -2;	// move button
		if (Math.pow(x, 2)*3 + Math.pow(y-800, 2) < r2)			return -3;	// shoot button
		if (Math.pow(x-1280, 2)*3 + Math.pow(y-800, 2) < r2)	return -4;	// special button
		for (Body b: game.getBodies())
			if (b instanceof Ship)
				if (((Ship) b).isBlue())
				if (Math.hypot(x-screenXFspaceX(b.xValAt(t)),
					           y-screenYFspaceY(b.yValAt(t))) < 15)
					return ((Ship) b).getID();	// a ship
		return -1;	// empty space
	}
	
	
	public final double spaceXFscreenX(int sx) {	// converts an x on screen to an x in space
		return (sx-origX)*scale;
		
	}
	
	
	public final double spaceYFscreenY(int sy) {	// converts a y on screen to a y in space
		return (sy-origY)*scale;
	}
	
	
	public final int screenXFspaceX(double sx) {	// converts an x in space to an x on screen
		return (int)(sx/scale) + origX;
	}
	
	
	public final int screenYFspaceY(double sy) {	// converts a y in space to a y on screen
		return (int)(sy/scale) + origY;
	}
	
	
	public void setShip(byte id) {			// sets the activeShip field to the one with the matching id
		activeShip = game.getShipByID(id);
	}

}
