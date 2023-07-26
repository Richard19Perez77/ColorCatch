package radical.appwards.colorcatch.movements;

/**
 * A class that creates the different movable types for the radical.appwards.colorcatch.objects.
 * 
 * @author Rick
 * 
 */
public class MovableImplFactory {

	public Movable createMovableImpl(String type, int s) {
		type = type.toLowerCase();
		if (type.equals("leftdown"))
			return new MovableLeftDownImpl();
		else if (type.equals("rightdown"))
			return new MovableRightDownImpl();
		else if (type.equals("left"))
			return new MovableLeftImpl();
		else if (type.equals("right"))
			return new MovableRightImpl();
		else if (type.equals("down"))
			return new MovableStraightDownImpl();
		else if (type.equals("up"))
			return new MovableStraightUpImpl(s);
		else if (type.equals("path"))
			return new MovablePathImpl();
		else
			return null;
	}
}
