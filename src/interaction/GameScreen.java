package interaction;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mechanics.Battlefield;
import mechanics.Body;

/**
 * @author jkunimune
 * The JPanel that displays the gameplay objects and HUD during a match.
 */
public class GameScreen extends JPanel {

	private static final long serialVersionUID = -4461350953048532763L;
	
	
	private HashMap<String, BufferedImage> sprites;
	private HashMap<String, AudioClip> sounds;
	
	private Battlefield space;
	
	private Canvas canvs;
	private BufferStrategy strat;
	
	
	
	public GameScreen(int w, int h, Battlefield field) {
		super();
		canvs = new Canvas();
		space = field;
		
		super.add(canvs);
		super.setPreferredSize(new Dimension(w,h));
		super.setLayout(null);
		super.setOpaque(true);
		
		canvs.setBounds(0,0,w,h);
		canvs.setIgnoreRepaint(true);
		canvs.setFocusable(true);
		
		loadImages();
		loadSounds();
	}
	
	
	
	private void loadImages() {	// reads all images in the assets directory and saves them in a HashMap
		sprites = new HashMap<String, BufferedImage>();
		
		File[] files = new File("assets/images").listFiles();
		for (File f: files) {	// for every file in that folder
			try {
				int i = f.getName().lastIndexOf('.');	// find the extension
				if (i != -1 && f.getName().endsWith(".png"))	// if it is a png file
					sprites.put(f.getName().substring(0,i), ImageIO.read(f));	// put it in the HashMap
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
	
	
	@Override
	public void setVisible(boolean v) {	// draws all things and plays sounds and displays itself
		final Graphics2D g = (Graphics2D)strat.getDrawGraphics();
		final double t = (double)System.currentTimeMillis();
		
		g.setColor(Color.BLACK);	// start by blackening everything
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (Body b: space.getBodies())	// draw the bodies
			draw(b,g,t);
		
		g.dispose();
		strat.show();
		super.setVisible(v);
	}
	
	
	private void draw(Body b, Graphics2D g, double t) {	// put a picture of b on g at time t
		BufferedImage img;
		try {
			img = sprites.get(b.spriteName());	// finds the correct sprite
		} catch (java.lang.NullPointerException e) {
			throw new NullPointerException("Image "+b.spriteName()+".png not found!");
		}
		img = executeTransformation(img, b.spriteTransform(t));	// does any necessary transformations
		
		double screenX = b.xValAt(t) + getWidth()/2;	// gets coordinates of b, 
		double screenY = b.yValAt(t) + getHeight()/2;	// and offsets appropriately
		g.drawImage(img, (int)screenX-img.getWidth()/2, (int)screenY-img.getHeight()/2, null);
		
		String sfx = b.soundName(t);
		if (!sfx.isEmpty())
			sounds.get(sfx).play();
	}
	
	
	private BufferedImage executeTransformation(BufferedImage img, double[] params) {	// rotozooms img based on params
		if (params[0] == 0.0 && params[1] == 1.0 && params[2] == 1.0)	// if there is no transformation
			return img;	// return the raw image
		
		AffineTransform at = new AffineTransform();
		if (params[0] != 0.0)
			at.rotate(params[0], img.getWidth()/2, img.getHeight()/2);	// rotate (if necessary)
		if (params[1] != 1.0 || params[2] != 1.0)
			at.scale(params[1], params[2]);				// scales (if necessary
		
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null);	// executes affine transformation and returns
	}
	
	
	public void developStrategy() {	// some required stuff for graphics to not fail
		canvs.createBufferStrategy(2);
		strat = canvs.getBufferStrategy();
	}
}
