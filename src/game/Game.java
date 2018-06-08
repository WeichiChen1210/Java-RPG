package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;


import gfx.Screen;
import gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 8;
	public static final String NAME = "Game";

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private Screen screen;
	public InputHandler input;
	
	// dialog
	public static Dialog dialog;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		//frame.setLayout(null);
		frame.add(this, BorderLayout.CENTER);
		//frame.add(this);
		frame.pack();
		//Container contentPane = frame.getContentPane();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		// init dialog
		dialog=new Dialog();
		frame.add(dialog.panel,BorderLayout.SOUTH);
		//contentPane.add(dialog.panel, BorderLayout.SOUTH);
		
		frame.setVisible(true);

	}

	public void init(){
		screen= new Screen(WIDTH,HEIGHT,new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		
		
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
/*			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
*/			
			if (shouldRender) {
				frames++;
				render();

			}
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks, " + frames+" frames");
				frames = 0;
				ticks = 0;
			}

		}
	}

	public void tick() {
		tickCount++;
		if(input.up.getPressed())
		{
			screen.yOffset--;
		}
		if(input.down.getPressed())
		{
			screen.yOffset++;
		}
		if(input.right.getPressed())
		{
			screen.xOffset++;
		}
		if(input.left.getPressed())
		{
			screen.xOffset--;
		}
		//screen.xOffset++;
//		for(int i = 0; i < pixels.length; i++) {
//			pixels[i] = i + tickCount;
//		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(pixels, 0, WIDTH);
		
		Graphics g=bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
