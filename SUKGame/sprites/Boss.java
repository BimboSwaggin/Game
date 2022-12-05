 import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.util.Random;


public class Boss implements DisplayableSprite, Health {
	
	private static final int PERIOD_LENGTH = 200;			
	private static final int IMAGES_IN_CYCLE = 9;
	
	private long elapsedTime = 0; 
	private long buffer = 0; 
	private long previousTime = 0;

	private static Image[] images;
	private double centerX = 0;
	private double centerY = 0;
	private double width = 160;
	private double height = 160;
	private boolean dispose = false;	
	boolean attackCollides = false;
	boolean isAttacking = false;
	boolean hit = false;
	double leftOrRight;
	int health = 250;
		
	private int velocity = 90;
	private double velocityX = 90;
	private double velocityY = 90;

	
	private Direction direction = Direction.RIGHT;
	private enum Direction { RIGHT(0), LEFT(1), ATK1_RIGHT(3), ATK1_LEFT(2), ATK2_LEFT(4), ATK2_RIGHT(5) ;
		private int value = 0;
		private Direction(int value) {
			this.value = value; 
		} 
	};
	
	public Boss(double centerX, double centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		
		if (images == null) {
			try {
				images = new Image[54];
				for (int i = 0; i < 54; i++) {
					String path = String.format("res/BossRes/Boss%d.png",i);
					images[i] = ImageIO.read(new File(path));
				}
			}
			catch (IOException e) {
				System.err.println(e.toString());
			}		
		}	
		
	}
	
	
	

	public Image getImage() {
		//calculate how many periods of 200 milliseconds have elapsed since this sprite was instantiated?
		long period = elapsedTime / PERIOD_LENGTH;
		//calculate which image (aka 'frame') of the sprite animation should be shown out of the cycle of images
		int image = (int) (period % IMAGES_IN_CYCLE);
		//calculate index into array of all images. this is an arbitrary value, depending on how the image files are ordered
		int index = direction.value * IMAGES_IN_CYCLE + image;
						
		return images[index];
	}
	
	public double getMinX() {
		return centerX - (width / 2);
	}

	public double getMaxX() {
		return centerX + (width / 2);
	}

	public double getMinY() {
		return centerY - (height / 2);
	}

	public double getMaxY() {
		return centerY + (height / 2);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getCenterX() {
		return centerX;
	};

	public double getCenterY() {
		return centerY;
	};
	
	
	public boolean getDispose() {
		return dispose;
	}
	
	public double getFacingDirection() {
		return leftOrRight;
	}

	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
	
	public boolean getVisible() {
		return true;
	}
	
	private void setDirection(boolean attack, int move) {
		
		
	if (attack) {
		if (move == 1) {
			if (leftOrRight < 0) {
				this.direction = direction.ATK1_LEFT;
			}
			else {
				this.direction = direction.ATK1_RIGHT;
			}
		}
		else if (move == 2) {
			if (leftOrRight < 0) {
				this.direction = direction.ATK2_LEFT;
			}
			else {
				this.direction = direction.ATK2_RIGHT;
			}
			
		}
//		else if (move == 3) {
//			if (leftOrRight > 0) {
//				this.direction = direction.LEFT;
//			}
//			else {
//				this.direction = direction.LEFT;
//			}
//		}
		}
	else {
		if (leftOrRight > 0) {
			this.direction = direction.LEFT;
		}
		else if (leftOrRight < 0) {
			this.direction = direction.RIGHT;
			}
		else {
			this.direction = direction.RIGHT;
			}
		}
		
	} 

	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time) {
		
		
		
		elapsedTime += actual_delta_time;
		double deltaX = actual_delta_time * 0.001 * velocityX + 1;
		double deltaY = actual_delta_time * 0.001 * velocityY;
		boolean proximity = checkProximityWithOtherSprite(universe.getSprites(), deltaX, deltaY);
		attackCollides = checkCollision(universe.getSprites(), deltaX, deltaY);
		if (isAttacking) {
			attacking(universe, deltaX, deltaY);
		}
		
		
		double relativeX = this.centerX - universe.getPlayer1().getCenterX();
		
		leftOrRight = 1 * Math.signum(velocity);
		
		if (hit) {
			if (elapsedTime - buffer > 500) {
				health -= 5;
				buffer = elapsedTime;
			}
			hit = false;
		}
		
		if (proximity == false) {
			if (relativeX < 1) { 
				velocity = 90;
				this.centerX += actual_delta_time * 0.001 * velocity;
				setDirection(false, 0);
			}
			else if (relativeX > -1) {
				velocity = -90;
				this.centerX += actual_delta_time * 0.001 * velocity;
				setDirection(false, 0);
			}
		}
		else {

			
			System.out.println(attackCollides);
			if (elapsedTime-previousTime >= 1800) {
				isAttacking = true;
				previousTime = elapsedTime;
				}
				
			}
		if (health <= 0) {
			setDispose(true);
		}
		
		}
	private boolean checkProximityWithOtherSprite(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		boolean close = false;
		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof Player) {
				double distanceX = this.centerX - sprite.getCenterX();
				double distanceY = this.centerY - sprite.getCenterY();
				double radialDistance = Math.sqrt((distanceY * distanceY) + (distanceX * distanceX));
				if (radialDistance <= 80) {
					close = true;
				}
				else {
					close = false;
				}
			}
		}
		return close;
	}
	
	private boolean checkCollision(ArrayList<DisplayableSprite> sprites, double deltaX, double deltaY) {
		boolean colliding = false;

		for (DisplayableSprite sprite : sprites) {
			if (sprite instanceof Player) {
				
				
				if (CollisionDetection.pixelBasedOverlaps(this, sprite, deltaX, deltaY)) {
					colliding = true;
					break;					
				}
			}
		}		
		return colliding;	
	}


	private void attacking(Universe universe, double deltaX, double deltaY) { 
		Random rand = new Random();
		int move = rand.nextInt(3);
			
			if (move == 1) { // Ground pound
				setDirection(true, move);
			}
			
			else if (move == 2) { // Sweep
				setDirection(true, move);
			}
		
//				else if (move == 3) { // BFLB
//					setDirection(true, move);
//				}
			
			universe.getPlayer1().isHit(attackCollides);
			isAttacking = false;
			

	}

	@Override
	public boolean isHit(boolean hit) {
		this.hit = hit;
		return this.hit;
	}




	@Override
	public int getHealth() {
		return health;
	}




	@Override
	public void setDontMove(boolean DontMove) {
		// TODO Auto-generated method stub
		
	}
}
