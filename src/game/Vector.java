package game;

public class Vector {
	
	private double magnitude;
	private double direction;
	private double upperBound;
	private double lowerBound;
	
	public double getMagnitude() {
		return magnitude;
	}

	public double getDirection() {
		return direction;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	Vector(int xDiff, int yDiff) {
		this.magnitude = Vector.hypotenuse(xDiff, yDiff);
		this.direction = Vector.angleMeasure(xDiff, yDiff);
		
		//TODO: Doc all this up a bit
		int xHalves = Math.abs(xDiff) * 2 + 1;
		int	yHalves = Math.abs((Math.abs(yDiff) - 1) * 2 + 1);
		
		if(yDiff == 0) {
			xHalves = Math.abs((Math.abs(xDiff) - 1) * 2 + 1);
		}
		
		double baseAngle = 90;
		if(xDiff != 0) {
			baseAngle = Math.abs(Math.toDegrees(Math.atan((double) yDiff/xDiff)));
		}
		
		double threshold = Math.abs(baseAngle) - Math.toDegrees(Math.atan((double) yHalves/xHalves));
		
		if(yDiff == 0) {
			threshold = Math.toDegrees(Math.atan((double) yHalves/xHalves));
		}
		
		this.upperBound = (this.direction + threshold) % 360;
		this.lowerBound = (((this.direction - threshold) % 360) + 360) % 360;
	}
	
	public static double hypotenuse(int width, int height) {
		return Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
	}
	
	public static double angleMeasure(int width, int height) {
		int quadrant = 0;
		if(width > 0) {
			if(height > 0) { quadrant = 2; }
			else if(height < 0) { quadrant = 3; }
		} else if(width < 0) {
			if(height > 0) { quadrant = 1; }
			else if(height < 0) { quadrant = 4; }
		}
		
		double baseAngle = 90;
		if(width != 0) {
			baseAngle = Math.abs(Math.toDegrees(Math.atan((double) height/width)));
		}	
		
		//Note: baseAngle should only be 0 when the quadrant is 0.
		//(i.e. when point is on axis, baseAngle should be 0)
		switch(quadrant) {
		case 1:
			return baseAngle;
		case 2: 
			return 180 - baseAngle;
		case 3:
			return 180 + baseAngle;
		case 4:
			return 360 - baseAngle;
		case 0:
			if(width == 0) {			
				if(height > 0) { return 90; }
				else if(height < 0) { return 270; }
			} else if(height == 0) {
				if(width > 0) { return 180; }
				else if(width < 0) { return 0; }
			}
			break;
		}
		
		return -1;
	}
	
	public String toString() {
		return magnitude + ", " + direction + ", " + lowerBound + ", " + upperBound;
	}
}
