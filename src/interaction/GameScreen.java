/**
 * The JPanel that displays the gameplay objects and HUD during a match.
 */
package interaction;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
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

/**
 * @author jkunimune
 * @version
 */
public class GameScreen extends JPanel {

	private static final long serialVersionUID = -4461350953048532763L;
	
	private static final double MIN_SCALE = Math.exp(1);	// the limits of the zooming
	private static final double MAX_SCALE = Math.exp(-1);
	
	
	private HashMap<String, BufferedImage> sprites;	// the images it uses to display objects
	private HashMap<String, BufferedImage> hudPics;
	private HashMap<String, AudioClip> sounds;
	
	private Battlefield space;
	
	private Canvas canvs;			// some necessary java.awt stuff
	private BufferStrategy strat;
	
	private double origX, origY, scale;	// the variables that define the screen's position and zoom-level
	
	
	
	public GameScreen(int w, int h, Battlefield field) {
		super();
		canvs = new Canvas();
		space = field;
		
		super.add(canvs);
		super.setPreferredSize(new Dimension(w,h));
		super.setLayout(null);
		super.setOpaque(true);
		super.setFocusable(true);
		
		canvs.setBounds(0,0,w,h);
		canvs.setIgnoreRepaint(true);
		canvs.setFocusable(true);
		
		loadImages();
		loadSounds();
		
		origX = 0;
		origY = 0;
		scale = 1.0;
	}
	
	
	
	private void loadImages() {	// reads all images in the assets directory and saves them in a HashMap
		sprites = new HashMap<String, BufferedImage>();
		hudPics = new HashMap<String, BufferedImage>();
		
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
					hudPics.put(f.getName().substring(0,i), ImageIO.read(f));	// put it in the HashMap
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
	
	
	public void addListener(Controller c) {
		canvs.addMouseListener(c);
		canvs.addMouseWheelListener(c);
		canvs.addMouseMotionListener(c);
		canvs.addKeyListener(c);
	}
	
	
	@Override
	public void setVisible(boolean v) {	// draws all things and plays sounds and displays itself
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		final double t = (double)System.currentTimeMillis();
		
		g.drawImage(hudPics.get("space"), 0, 0, null);
		
		for (int i = space.getBodies().size()-1; i >= 0; i --) {	// then draw the bodies
			final Body b = space.getBodies().get(i);
			
			if (b.existsAt(t))
				draw(b, g, t);
			try {
				sounds.get(b.soundName(t)).play();
			} catch (NullPointerException e) {}
		}
		
		drawHUD(g);		// and the heads-up display
		
		g.dispose();
		strat.show();
		super.setVisible(v);
	}
	
	
	private void draw(Body b, Graphics2D g, double t) {	// put a picture of b on g at time t
		BufferedImage img;
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
	
	
	private void drawHUD(Graphics2D g) {	// draw the heads up display
		final int pos = getMousePos(MouseInfo.getPointerInfo().getLocation(), this.getLocationOnScreen());
		
		if (pos == -2)
			g.drawImage(hudPics.get("button0_on"), 0, 0, null);
		else
			g.drawImage(hudPics.get("button0_of"), 0, 0, null);
		
		if (pos == -3)
			g.drawImage(hudPics.get("button1_on"), 0, 400, null);
		else
			g.drawImage(hudPics.get("button1_of"), 0, 400, null);
		
		if (pos == -4)
			g.drawImage(hudPics.get("button2_on"), 880, 400, null);
		else
			g.drawImage(hudPics.get("button2_of"), 880, 400, null);
	}
	
	
	private BufferedImage executeTransformation(BufferedImage img, double[] params, boolean zoomScale) {	// rotozooms img based on params
		double zoominess;
		if (zoomScale)		zoominess = scale;	// the scale might affect the AffineTransform
		else				zoominess = 1.0;
		
		AffineTransform at = new AffineTransform();
		
		at.scale(params[1]/zoominess, params[2]/zoominess);						// scales (if necessary)
		
		if (params[0] != 0.0)
			at.rotate(params[0], img.getWidth()/2, img.getHeight()/2);	// rotates (if necessary)
		
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		Point size = new Point((int) (img.getWidth()*params[1]/zoominess),
				(int) (img.getHeight()*params[2]/zoominess));
		return op.filter(img, null).getSubimage(0, 0, size.x, size.y);	// executes affine transformation, crops, and returns
	}
	
	
	public void zoom(int amount, int mx, int my) {	// changes scale based on a multiplicative amount
		final double expAmount = Math.exp(amount/5.0);
		scale *= expAmount;
		scale = Math.min(Math.max(scale, MAX_SCALE), MIN_SCALE);
		
	}
	
	
	public void pan(int delX, int delY) {	// changes offsetX and offsetY based on a mouse drag
		origX -= delX*scale;
		origY -= delY*scale;
	}
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}
	
	
	public byte getMousePos(Point mCoordsOnScreen, Point screenCoords) {	// decides which, if any, button the mouse is currently on
		return getMousePos(mCoordsOnScreen.x-screenCoords.x, mCoordsOnScreen.y-screenCoords.y);
	}
	
	
	public byte getMousePos(Point mCoords) {	// decides which, if any, button the mouse is currently on
		return getMousePos(mCoords.x, mCoords.y);
	}
	
	
	public byte getMousePos(int x, int y) {	// decides which, if any, button the mouse is currently on
		final int r2 = 190000;
		if (Math.pow(x, 2)*3 + Math.pow(y, 2) < r2)				return -2;	// move button
		if (Math.pow(x, 2)*3 + Math.pow(y-800, 2) < r2)			return -3;	// shoot button
		if (Math.pow(x-1280, 2)*3 + Math.pow(y-800, 2) < r2)	return -4;	// special button
		return -1;	// empty space
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	
	public double spaceXFscreenX(int sx) {	// converts an x on screen to an x in space
		return (sx-getWidth()/2)*scale + origX;
		
	}
	
	
	public double spaceYFscreenY(int sy) {	// converts a y on screen to a y in space
		return (sy-getHeight()/2)*scale + origY;
	}
	
	
	public int screenXFspaceX(double sx) {	// converts an x in space to an x on screen
		return (int)((sx-origX)/scale) + getWidth()/2;
	}
	
	
	public int screenYFspaceY(double sy) {	// converts a y in space to a y on screen
		return (int)((sy-origY)/scale) + getHeight()/2;
	}

}
