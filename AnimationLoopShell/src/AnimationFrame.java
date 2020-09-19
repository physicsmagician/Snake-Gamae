import javax.swing.*;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AnimationFrame extends JFrame {
	
	final public static int FRAMES_PER_SECOND = 60;
	public static int SCREEN_HEIGHT = 700;
	public static int SCREEN_WIDTH = 700;

	private JPanel panel = null;
	private JButton btnPauseRun;
	private JButton btnHelp;
	private JLabel lblTimeLabel;
	private JLabel lblScoreLabel;
	private JLabel lblScore;
	private JLabel lblTime;
	private JLabel lblLevelLabel;
	private JLabel lblLevel;

	private static Thread game;
	private static boolean stop = false;

	private long points = 0;
	private long current_time = 0;						//MILLISECONDS
	private long next_refresh_time = 0;							//MILLISECONDS
	private long last_refresh_time = 0;
	private long minimum_delta_time = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS
	private static long actual_delta_time = 0;							//MILLISECONDS
	private static long elapsed_time = 0;
	private boolean isPaused = false;
	
	HelpDialog help;

	private KeyboardInput keyboard = new KeyboardInput();
	private Universe universe = null;
	//local (and direct references to various objects within the universe... this appears to reduce lag
	private ActiveSprite player1 = null;
	ArrayList<StaticSprite> tailSprites = null;
	ArrayList<ActiveSprite> activeSprites = null;
	ArrayList<StaticSprite> staticSprites = null;
	Background background = null;
	boolean centreOnPlayer = false;
	int level = 1;

	private int xOffset = 0;
	private int yOffset = 0;

	public AnimationFrame()
	{
		super("");
		this.setFocusable(true);
		this.setSize(SCREEN_WIDTH + 20, SCREEN_HEIGHT + 36);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});

		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);

		panel = new DrawPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		getContentPane().add(panel, BorderLayout.CENTER);

		btnPauseRun = new JButton("||");
		btnPauseRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnPauseRun_mouseClicked(arg0);
			}
		});

		btnPauseRun.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPauseRun.setBounds(20, 20, 48, 32);
		btnPauseRun.setFocusable(false);
		getContentPane().add(btnPauseRun);
		getContentPane().setComponentZOrder(btnPauseRun, 0);


		btnHelp = new JButton("HELP");
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnHelp_mouseClicked(arg0);
			}
		});
		
		btnHelp.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnHelp.setBounds(20, 670, 65, 32);
		btnHelp.setFocusable(false);
		getContentPane().add(btnHelp);
		getContentPane().setComponentZOrder(btnHelp, 0);
		
		lblScoreLabel = new JLabel("Score: ");
		lblScoreLabel.setForeground(Color.YELLOW);
		lblScoreLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblScoreLabel.setBounds(300, 22, 326, 30);
		getContentPane().add(lblScoreLabel);
		getContentPane().setComponentZOrder(lblScoreLabel, 0);
		
		lblScore = new JLabel("000");
		lblScore.setForeground(Color.YELLOW);
		lblScore.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblScore.setBounds(420, 22, 620, 30);
		getContentPane().add(lblScore);
		getContentPane().setComponentZOrder(lblScore, 0);
		
		lblTimeLabel = new JLabel("Time: ");
		lblTimeLabel.setForeground(Color.YELLOW);
		lblTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTimeLabel.setBounds(80, 22, 96, 30);
		getContentPane().add(lblTimeLabel);
		getContentPane().setComponentZOrder(lblTimeLabel, 0);

		lblTime = new JLabel("000");
		lblTime.setForeground(Color.YELLOW);
		lblTime.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTime.setBounds(192, 22, 320, 30);
		getContentPane().add(lblTime);
		getContentPane().setComponentZOrder(lblTime, 0);

		lblLevelLabel = new JLabel("Level: ");
		lblLevelLabel.setForeground(Color.YELLOW);
		lblLevelLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblLevelLabel.setBounds(528, 22, 128, 30);
		getContentPane().add(lblLevelLabel);
		getContentPane().setComponentZOrder(lblLevelLabel, 0);

		lblLevel = new JLabel("1");
		lblLevel.setForeground(Color.YELLOW);
		lblLevel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblLevel.setBounds(672, 22, 48, 30);
		getContentPane().add(lblLevel);
		getContentPane().setComponentZOrder(lblLevel, 0);
		
	}

	public static void main(String[] args)
	{
		AnimationFrame me = new AnimationFrame();
		me.setVisible(true);

		game = new Thread()
		{
			public void run()
			{
				me.animationLoop();
				System.out.println("run() complete");
			}
		};
		game.start();
		System.out.println("main() complete");

	}

	private void animationLoop() {

		universe = UniverseFactory.getNextUniverse();

		while (stop == false && universe != null) {

			activeSprites = universe.getActiveSprites();
			staticSprites = universe.getStaticSprites();
			tailSprites = universe.getTailSprites();
			
			player1 = universe.getPlayer1();
			background = universe.getBackground();
			centreOnPlayer = universe.centerOnPlayer();
			level = UniverseFactory.getLevel();

			// main game loop
			while (stop == false && universe.isComplete() == false) {

				//adapted from http://www.java-gaming.org/index.php?topic=24220.0
				last_refresh_time = System.currentTimeMillis();
				next_refresh_time = current_time + minimum_delta_time;

				//sleep until the next refresh time
				while (current_time < next_refresh_time)
				{
					//allow other threads (i.e. the Swing thread) to do its work
					Thread.yield();

					try {
						Thread.sleep(1);
					}
					catch(Exception e) {    					
					} 

					//track current time
					current_time = System.currentTimeMillis();
				}

				//    			System.out.println(String.format("start update: %d", System.currentTimeMillis()));
				//read input
				keyboard.poll();
				handleKeyboardInput();

				//UPDATE STATE
				updateTime();
				universe.update(keyboard, actual_delta_time);
				updateControls();
				
				if ((UniverseFactory.getLevel() == 1 && points == 5)) {	//5 points
					universe.complete = true;
				}
				else if ((UniverseFactory.getLevel() == 2 && points == 15)) {	//10 points
					universe.complete = true;
				}
				else if ((UniverseFactory.getLevel() == 3 && points == 30)) {	//15 points
					universe.complete = true;
				}
				else if ((UniverseFactory.getLevel() == 4 && points == 50)) {	//20 points
					universe.complete = true;
				}
				else if ((UniverseFactory.getLevel() == 5 && points == 75)) {	//25 points
					universe.complete = true;
				}
				else if ((UniverseFactory.getLevel() == 6 && points == 100)) {	//25 points
					JOptionPane.showMessageDialog(null, "Congratulations! You beat the game!");
					stop = true;
				}

				if (SnakeSprite.getGameOver()) {
					isPaused = true;
					JOptionPane.showMessageDialog(null, "GAME OVER");
					stop = true;
				}

				//REFRESH
				this.repaint();
				//    			System.out.println(String.format("end update: %d", System.currentTimeMillis()));
			}

			//    		System.out.println(String.format("level %d complete", UniverseFactory.getLevel()));

			universe = UniverseFactory.getNextUniverse();

		}

		System.out.println("animation complete");
		AudioPlayer.setStopAll(true);
		dispose();	

	}

	private void updateControls() {
		this.lblTime.setText(Long.toString(elapsed_time));
		this.lblLevel.setText(Integer.toString(level));
		this.points = SnakeSprite.getPoints();
		this.lblScore.setText(Integer.toString(SnakeSprite.getPoints()));
	}

	private void updateTime() {
		current_time = System.currentTimeMillis();
		actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
		elapsed_time += actual_delta_time;
	}
	
	public static long getTime() {
		return elapsed_time;
	}

	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.btnPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.btnPauseRun.setText(">");
		}
	}
	
	protected void btnHelp_mouseClicked(MouseEvent arg0) {
		isPaused = true;
		HelpDialog help = new HelpDialog();
		help.setLocationRelativeTo(this);
		help.setModalityType(ModalityType.APPLICATION_MODAL);
		help.setVisible(true);
		if (!help.isVisible()) {
			isPaused = false;
		} else {
			isPaused = true;
		}
	}

	private void handleKeyboardInput() {
		//if the interface needs to respond to certain keyboard events
		if (keyboard.keyDown(80) && ! isPaused) {
			btnPauseRun_mouseClicked(null);	
		}
		if (keyboard.keyDown(79) && isPaused ) {
			btnPauseRun_mouseClicked(null);
		}
	}

	class DrawPanel extends JPanel {

		public void paintComponent(Graphics g)
		{	
			if (universe == null) {
				return;
			}
			
			SCREEN_HEIGHT = this.getHeight();
			SCREEN_WIDTH = this.getWidth();
			

			if (player1 != null && centreOnPlayer) {
				xOffset = - ((int) player1.getMinX() - (SCREEN_WIDTH / 2));
				yOffset = - ((int) player1.getMinY() - (SCREEN_HEIGHT / 2));     
			}
			else {
				xOffset = 0;
				yOffset = 0;
			}

			paintBackground(g, background);

			for (int i = 0; i < staticSprites.size(); i++) {
				StaticSprite staticSprite = staticSprites.get(i);
				g.drawImage(staticSprite.getImage(), (int)staticSprite.getMinX() + xOffset, (int)staticSprite.getMinY() + yOffset, (int)staticSprite.getWidth(), (int)staticSprite.getHeight(), null);
			}

			for (ActiveSprite activeSprite : activeSprites) {
				g.drawImage(activeSprite.getImage(), (int)activeSprite.getMinX() + xOffset, (int)activeSprite.getMinY() + yOffset, (int)activeSprite.getWidth(), (int)activeSprite.getHeight(), null);
				//if the sprite goes off the screen, reset it to the opposite side
				if (activeSprite.getMaxX() > SCREEN_WIDTH) {
					g.drawImage(activeSprite.getImage(), (int)activeSprite.getMinX() + xOffset - SCREEN_WIDTH, (int)activeSprite.getMinY() + yOffset, (int)activeSprite.getWidth(), (int)activeSprite.getHeight(), null);
				}
				else if (activeSprite.getMinX() < 0) {
					g.drawImage(activeSprite.getImage(), (int)activeSprite.getMinX() + xOffset + SCREEN_WIDTH, (int)activeSprite.getMinY() + yOffset, (int)activeSprite.getWidth(), (int)activeSprite.getHeight(), null);
				}
				
				if (activeSprite.getMaxY() > SCREEN_HEIGHT) {
					g.drawImage(activeSprite.getImage(), (int)activeSprite.getMinX() + xOffset, (int)activeSprite.getMinY() + yOffset - SCREEN_HEIGHT, (int)activeSprite.getWidth(), (int)activeSprite.getHeight(), null);
				}
				else if (activeSprite.getMinY() < 0) {
					g.drawImage(activeSprite.getImage(), (int)activeSprite.getMinX() + xOffset, (int)activeSprite.getMinY() + yOffset + SCREEN_HEIGHT, (int)activeSprite.getWidth(), (int)activeSprite.getHeight(), null);
				}
			}
			
			for (int i = 0; i < tailSprites.size(); i++) {
				TailSprite segment = (TailSprite) tailSprites.get(i);
				g.drawImage(segment.getImage(), (int)segment.getMinX() + xOffset, (int)segment.getMinY() + yOffset, (int)segment.getWidth(), (int)segment.getHeight(), null);
			}

		}

		private void paintBackground(Graphics g, Background background) {

			if ((g == null) || (background == null)) {
				return;
			}

			//what tile covers the top-left corner?
			int xTopLeft = - xOffset;
			int yTopLeft = - yOffset;
			int row = background.getRow(yTopLeft);
			int col = background.getCol(xTopLeft);
			Tile tile = null;

			boolean rowDrawn = false;
			boolean colDrawn = false;
			while (colDrawn == false) {
				while (rowDrawn == false) {
					tile = background.getTile(col, row);
					g.drawImage(tile.getImage(), tile.getMinX() + xOffset, tile.getMinY() + yOffset, tile.getWidth(), tile.getHeight(), null);
					//does the RHE of this tile extend past the RHE of the visible area?
					int rheTile = tile.getMinX() + xOffset + tile.getWidth();
					if (rheTile > SCREEN_WIDTH || tile.isOutOfBounds()) {
						rowDrawn = true;
					}
					else {
						col++;
					}
				}
				//does the bottom edge of this tile extend past the bottom edge of the visible area?
				int bottomEdgeTile = tile.getMinY() + yOffset + tile.getHeight();
				if (bottomEdgeTile > SCREEN_HEIGHT || tile.isOutOfBounds()) {
					colDrawn = true;
				}
				else {
					col = background.getCol(xTopLeft);
					row++;
					rowDrawn = false;
				}
			}
		}				
	}
	protected void this_windowClosing(WindowEvent e) {
		System.out.println("windowClosing()");
		stop = true;
		dispose();	
	}
}
