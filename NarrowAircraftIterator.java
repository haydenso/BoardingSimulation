package boarding;

import java.util.Comparator;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import java.util.stream.*;




public class NarrowAircraftIterator {
	
	Comparator<Passenger> idComparator = new Comparator<Passenger>() {
		  @Override
		  public int compare(Passenger fwp1, Passenger fwp2) {
			 Integer i2 = fwp1.boardingID;
			 Integer i1 = fwp2.boardingID;
			 return i2.compareTo(i1);
		  }
		}; 
	private boolean setUp = false;
	private Stream<Object> stream;
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		if (!setUp) {
			Context<Object> context = ContextUtils.getContext(this);
			stream = context.getObjectsAsStream(Passenger.class).sorted((p1, p2) -> ((Integer) ((Passenger) p2).boardingID).compareTo(((Integer) ((Passenger) p1).boardingID)));
			setUp = true;

		}
		stream.forEach(fwp -> ((Passenger) fwp).step());

		
		
	}
	
}
