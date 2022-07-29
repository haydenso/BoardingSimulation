package boarding;

import java.util.Comparator;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;

import repast.simphony.util.ContextUtils;
import java.util.stream.*;




public class FlyingWingIterator {
	
	Comparator<FlyingWingPassenger> idComparator = new Comparator<FlyingWingPassenger>() {
		  @Override
		  public int compare(FlyingWingPassenger fwp1, FlyingWingPassenger fwp2) {
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
			stream = context.getObjectsAsStream(FlyingWingPassenger.class).sorted((fwp1, fwp2) -> ((Integer) ((FlyingWingPassenger) fwp2).boardingID).compareTo(((Integer) ((FlyingWingPassenger) fwp1).boardingID)));


		}
		stream.forEach(fwp -> ((FlyingWingPassenger) fwp).step());
		
		
		
	}
	
	
	
}
