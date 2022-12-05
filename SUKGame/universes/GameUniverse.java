import java.util.ArrayList;

public class GameUniverse implements Universe {

	private long a = 0;
	private boolean complete = false;	
	private Background background = null;
	private Background background2 = null;
	private ArrayList<Background> backgrounds = null;
	private DisplayableSprite player1 = null;
	private DisplayableSprite boss = null;
	private DisplayableSprite boss2 = null;
	private ArrayList<DisplayableSprite> sprites = new ArrayList<DisplayableSprite>();
	private double xCenter = 0;
	private double yCenter = 0;
	private static final double GROUND_Y = GameBackground.TILE_WIDTH * 2; 
	
	private ArrayList<DisplayableSprite> disposalList = new ArrayList<DisplayableSprite>();
	
	private DisplayableSprite floor = null;
	private DisplayableSprite wall = null;
	private DisplayableSprite wall2 = null;
	private DisplayableSprite platform = null;
	private DisplayableSprite platform2 = null;

	public GameUniverse () {

		background = new GameBackground();
		background.setShiftY(-600);
		background2 = new SUKBackground2();
		background2.setShiftY(0);

		floor = new BarrierSprite(-800,-10,800,0,false,0,200);
		wall = new BarrierSprite(0,0,4,1000,false,549,0 );
		wall2 = new BarrierSprite(0,0,4,1000,false,-549,0 );
		platform = new BarrierSprite(200,0,422,4,false,300,-35 );
		platform2 = new BarrierSprite(200,0,420,4,false,-300,-32 );
		
		backgrounds =new ArrayList<Background>();
		backgrounds.add(background);
		//backgrounds.add(background2);
		
		player1 = new Player(0, -300);
		boss = new Boss(150, 120);
	//	boss2 = new Boss(150, 120);
		
		sprites.add(player1);
		sprites.add(boss);
		sprites.add(floor);
		sprites.add(wall);
		sprites.add(wall2);
		sprites.add(platform);
		sprites.add(platform2);
		

	}

	public double getScale() {
		return 0.73;
	}	
	
	public double getXCenter() {
		return this.xCenter;
	}

	public double getYCenter() {
		return this.yCenter;
	}
	
	public void setXCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public void setYCenter(double yCenter) {
		this.yCenter = yCenter;
	}
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		complete = true;
	}

	@Override
	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}	
	public DisplayableSprite getPlayer1() {
		return player1;
	}
	
	public DisplayableSprite getBoss() {
		return boss;
	}
	
	public DisplayableSprite getBarrier(boolean wall) {
		if (wall) {
			return this.wall;
		}
		else {
			return this.floor;
		}
	}

	public ArrayList<DisplayableSprite> getSprites() {
		return sprites;
	}
		
	public boolean centerOnPlayer() {
		return false;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		
		a = actual_delta_time;

		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		disposeSprites();

		for (int i = 0; i < sprites.size(); i++) {
			DisplayableSprite sprite = sprites.get(i);
			sprite.update(this, keyboard, actual_delta_time);
    	}

	}
	
	 protected void disposeSprites() {
	        
	    	//collect a list of sprites to dispose
	    	//this is done in a temporary list to avoid a concurrent modification exception
			for (int i = 0; i < sprites.size(); i++) {
				DisplayableSprite sprite = sprites.get(i);
	    		if (sprite.getDispose() == true) {
	    			disposalList.add(sprite);
	    		}
	    	}
			
			//go through the list of sprites to dispose
			//note that the sprites are being removed from the original list
			for (int i = 0; i < disposalList.size(); i++) {
				DisplayableSprite sprite = disposalList.get(i);
				sprites.remove(sprite);
				System.out.println("Remove: " + sprite.toString());
	    	}
			
			//clear disposal list if necessary
	    	if (disposalList.size() > 0) {
	    		disposalList.clear();
	    	}
	    }


	public String toString() {
		return String.format("");
	}	

}
