import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ThatSquareBuildingGame extends JPanel {

	//private static final long serialVersionUID = -8715353373678321308L;

	private final Point[][][] Tetraminos = {
			// I-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O-Piece
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S-Piece
			/*
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},*/
			
			// T-Piece
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z-Piece
			/*
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			},*/
			//Elbow Piece
			{
				{ new Point(0, 0), new Point(1, 0), new Point(0, 1) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(0, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1) }
			},

			// Alien Ship Piece
			{
				{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(2, 1)},
				{new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2)},
				{new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)},
				{new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(0, 2), new Point(1, 2)}
			}

	};
	
	private final Color[] tetraminoColors = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.pink, Color.magenta,
		new Color(204,153,255)
	};

	/*private int [] BlockSizes = {
		4,4,4,4,4,4,4,3
	};*/
	
	private Point pieceOrigin;
	private int currentPiece;
	private int rotation;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

	private long score;
	private Color[][] well;
	
	// Creates a border around the well and initializes the dropping piece
	private void init() {
		well = new Color[24][24];
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				if (i == 0 || j == 22) {
					well[i][j] = Color.GRAY;
				} else {
					well[i][j] = Color.BLACK;
				}
			}
		}
		newPiece();
	}
	
	// Put a new, random piece into the dropping position
	public void newPiece() {

		
		if(!collidesAt(5,2,rotation)){
			pieceOrigin = new Point(5,2);
		}
		else{

			int newX = ThreadLocalRandom.current().nextInt(2, 21);
			int newY = ThreadLocalRandom.current().nextInt(2, 21);
			int tolerance = 100;
			int attempts = 0;
			while(collidesAt(newX,newY,rotation) && availableSpace() && attempts<tolerance ){
				//System.out.println("for sure stuck here");
				newX = ThreadLocalRandom.current().nextInt(2, 21);
				newY = ThreadLocalRandom.current().nextInt(2, 21);
				attempts++;
			}
			boolean gameOver = false;
			if (attempts >= tolerance){

				for (int i = 0; i < 23 ;i++ ) {
					for (int j = 0; j <23 ; j++ ) {

						if(!collidesAt(i,j,rotation)){
							pieceOrigin = new Point(i,j);
						}
					}
				}
				gameOver = true;
			}
			if (gameOver == true){
				System.out.println("game over");
				System.exit(0);
			}


			pieceOrigin = new Point(newX,newY);
		}

		rotation = 0;
		if (nextPieces.isEmpty()) {
			Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
			Collections.shuffle(nextPieces);
		}
		currentPiece = nextPieces.get(0);
		nextPieces.remove(0);

	}
	
	// Collision test for the dropping piece
	private boolean collidesAt(int x, int y, int rotation) {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			//System.out.print(well[p.x+x][p.y+y]+"\n");
			if (well[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	
	//Get number of blocks of current piece -- used for rotation
	/*public int getBlocks(){
		return BlockSizes[currentPiece];
	}*/

	
	// Rotate the piece clockwise or counterclockwise
	public void rotate(int i) {
		//int mod = getBlocks();
		int newRotation = (rotation + i) % 4;
		if (newRotation < 0) { 
			newRotation = 3;
		}
		//System.out.print(!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation));
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
			rotation = newRotation;
		}
		repaint();
	}


	// Move the piece left or right
	public void move(int i) {
		if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation) && (i == -1 || i == 1) ) {
			pieceOrigin.x += i;	
		}
		repaint();
	}
	
	// Drops the piece one line or fixes it to the well if it can't drop
	public void dropDown() {
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y +1, rotation)) {
			pieceOrigin.y += 1;
		} 	
		repaint();
	}

	public void moveUp(){
		if (!collidesAt(pieceOrigin.x, pieceOrigin.y - 1, rotation)) {
			pieceOrigin.y -= 1;
		} 	
		repaint();

	}

	//return x and y origin of where piece will be randomly placed
	private boolean availableSpace(){
		
		for (int i = 0; i < 23 ;i++ ) {
			for (int j = 0; j <23 ; j++ ) {

				if(!collidesAt(i,j,rotation)){
				//	System.out.println("true");
					return true;
				}
			}
		}
		System.out.println("LOST");
		return false;

	}

	
	// Make the dropping piece part of the well, so it is available for
	// collision detection.
	public void fixToWell() {
		for (Point p : Tetraminos[currentPiece][rotation]) {
			well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[currentPiece];
		}
		clear();
		newPiece();
	}
	

	public void deleteSquare(int max_i, int max_j, int max_of_s){
		score += max_of_s;
		for(int i = max_i; i > max_i - max_of_s; i--)
       		{
           		for(int j = max_j; j > max_j - max_of_s; j--)
            	{

                	well[i][j] = Color.BLACK;
            	}  
       		}
	}
	
	// Clear completed rows from the field and award score according to
	// the number of simultaneously cleared rows.
	public boolean MaxSubSquare(int M[][])
    {
        int i,j;
        int R = M.length;         //no of rows in M[][]
        int C = M[0].length;     //no of columns in M[][]
        int S[][] = new int[R][C];     
         
        int max_of_s, max_i, max_j; 
       
        /* Set first column of S[][]*/
        for(i = 0; i < R; i++)
            S[i][0] = M[i][0];
       
        /* Set first row of S[][]*/   
        for(j = 0; j < C; j++)
            S[0][j] = M[0][j];
           
        /* Construct other entries of S[][]*/
        for(i = 1; i < R; i++)
        {
            for(j = 1; j < C; j++)
            {
                if(M[i][j] == 1) 
                    S[i][j] = Math.min(S[i][j-1],Math.min(S[i-1][j], S[i-1][j-1])) + 1;
                else
                    S[i][j] = 0;
            }    
        }     
        
        /* Find the maximum entry, and indexes of maximum entry 
             in S[][] */
        max_of_s = S[0][0]; max_i = 0; max_j = 0;
        for(i = 0; i < R; i++)
        {
            for(j = 0; j < C; j++)
            {
                if(max_of_s < S[i][j])
                {
                    max_of_s = S[i][j];
                    max_i = i; 
                    max_j = j;
                }        
            }                 
        }     
         
		//use this to know what entries to replace to BLACK in deleteRow - obviously change name

       
       // System.out.printf("%d %d %d \n", max_i ,max_j, max_of_s);  
        //Don't hardcode size -- make user adjustable setting for inc/dec difficulty
	    if (max_of_s > 3) {
	    	
	    	deleteSquare(max_i,max_j,max_of_s);
	    	return true;
	    }
	    else{
	    	return false;
	    }
    }  





	public void clear() {
		
		int[][] M = new int[24][24];
              
        //turn the entire board into a binary array 
        //Use MaxSubSquare to clear largest block
        for (int j = 0; j< 23 ; j++ ) {
        	for (int i = 0; i < 23 ;i++ ) {
        		if (well[i][j] != Color.GRAY && well[i][j] != Color.BLACK && well[i][j] != Color.WHITE) {
        			M[i][j] = 1;

        		}
        		else{
        			M[i][j] = 0;
        		}      		
        	}
        }
        
		MaxSubSquare(M);

	}
	
	// Draw the falling piece
	private void drawPiece(Graphics g) {		
		g.setColor(tetraminoColors[currentPiece]);
		for (Point p : Tetraminos[currentPiece][rotation]) {
			g.fillRect((p.x + pieceOrigin.x) * 26, 
					   (p.y + pieceOrigin.y) * 26, 
					   25, 25);
		}
	}
	
	@Override 
	public void paintComponent(Graphics g)
	{
		// Paint the well
		g.fillRect(0, 0, 26*23, 26*23);
		for (int i = 0; i < 23; i++) {
			for (int j = 0; j < 23; j++) {
				g.setColor(well[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		
		// Display the score
		g.setColor(Color.WHITE);
		g.drawString("" + score, 19*12, 25);
		
		// Draw the currently falling piece
		drawPiece(g);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("That Square Building Game");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(26*23+25, 26*23+25);
		f.setVisible(true);
		final ThatSquareBuildingGame game = new ThatSquareBuildingGame();
		game.init();
		f.add(game);
		
		final double [] last = new double[1];
		last[0] = System.nanoTime();
		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					game.moveUp();
					break;
				case KeyEvent.VK_DOWN:
					game.dropDown();
					break;
				case KeyEvent.VK_LEFT:
					game.move(-1);
					break;
				case KeyEvent.VK_RIGHT:
					game.move(+1);
					break;
				case KeyEvent.VK_SPACE:
					game.rotate(+1);
					break;
				 
				case KeyEvent.VK_Q:
					game.fixToWell();
					last[0] = System.nanoTime();
					game.score += 1;
					break;
				}
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		




		new Thread() {
			@Override public void run() {
				int count = 0;
				while (true) {
					if( ((System.nanoTime() - last[0]) / 1000000000.0) > 5.0 && count < 24 ){
						game.dropDown();
						count++;
						/*game.force();
						try {
							Thread.sleep(5000);
						} catch ( InterruptedException e ) {}*/
					}
					else if(count>=24) {
						game.fixToWell();
						last[0] = System.nanoTime();
						count = 0;
					}
					
				}
				
				
			}
		}.start();
	}
}