import java.awt.Image;

public interface DisplayableSprite {

	public double getFacingDirection();
	
	public abstract Image getImage();
	
	public boolean getVisible();

	public double getMinX();

	public double getMaxX();

	public double getMinY();

	public double getMaxY();

	public double getHeight();

	public double getWidth();

	public double getCenterX();

	public double getCenterY();
	
	public boolean getDispose();
	
	public void setDispose(boolean dispose);

	public void update(Universe universe, KeyboardInput keyboard, long actual_delta_time);

	public abstract boolean isHit(boolean b);

	void setDontMove(boolean DontMove);
	
}