package mechanics;

public class PlacementRegion extends Body {

	public static final double X_DEV = 187500000*Univ.m;
	public static final double Y_DEV = 375000000*Univ.m;
	public static final double LOCATION = -300000000*Univ.m;
	
	
	
	public static PlacementRegion placementRegion(double t, Battlefield field, boolean host) {
		if (host)	return new PlacementRegion(LOCATION, 0, t, field);
		else		return new PlacementRegion(-LOCATION, 0, t, field);
	}
	
	
	
	private PlacementRegion(double x0, double y0, double t0, Battlefield field) {
		super(x0, y0, 0, 0, t0, field);
	}
	
	
	@Override
	public String spriteName() {
		return "rectangle";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double[] res = {0, X_DEV/150,Y_DEV/300, 1};
		return res;
	}
	
	
	public boolean contains(double x, double y) {
		return Math.abs(x-this.xValAt(0)) < X_DEV && Math.abs(y-this.yValAt(0)) < Y_DEV;
	}

}
