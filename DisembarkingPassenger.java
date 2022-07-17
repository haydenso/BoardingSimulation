package boarding;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

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

public class DisembarkingPassenger {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public int seatNumber[];
	private boolean carryonLuggage;
	private int timeSpentOnLuggage = 0;
	private boolean doingLuggage = false;
	private float speed;
	private boolean seated = true;
	private boolean gettingIntoSeat = false;
	private int timeSpentOnGettingIntoSeat = 0;
	private int seatingTime = 0;
	private boolean inAisle = true;
	public int group;
	private boolean firstTimeMoving = true;
	private boolean removed = false;
	//private static int numOfNonSeatedPassengers = DataHolder.numOfNonSeatedPassengers;
	public int timeBeforeBoarding = 0;
	
	public int luggageTime = ProbabilityTimeGenerator.generateWeibullLuggageTime()/2;
	
	
	DisembarkingPassenger(ContinuousSpace<Object> space, 
				Grid<Object> grid, 
				int[] seatNumber,
				boolean carryonLuggage,
				float speed, int group) {
		
		this.space = space;
		this.grid = grid;
		this.seatNumber = seatNumber;
		this.carryonLuggage = carryonLuggage;
		this.speed = speed;
		this.group = group;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		switch(group) {
		case 1:
			disembarkingProcedure();
			break;
		case 2:
			if (DataHolder.numOfGroup1PassengersLeft == 0) {
				disembarkingProcedure();
			}
			break;
		default:
			if (DataHolder.numOfGroup2PassengersLeft == 0) {
				disembarkingProcedure();
			}
			break;
			
		}
	}
	
	public void disembarkingProcedure() {
		
		if (!inAisle) {
			if (doingLuggage) {
				//Doing luggage
				if(timeSpentOnLuggage > luggageTime) {
					//Finished
					walkProcedure(); ///Walk if possible
				}
				else {
					timeSpentOnLuggage += 1;
				}
			}
			else {
				//Have luggage
				if (carryonLuggage) {
					doingLuggage = true;
					timeSpentOnLuggage += 1;
				}
				else {
					walkProcedure();
				}
				
			}
		}
		
		else {
			//Not doing luggage
			
			moveForwardInAisle();
				
			
			
		}
		
	}
	
	public void walkProcedure() {
		
		Context<Object> context = ContextUtils.getContext(this);
		GridPoint pt = grid.getLocation(this);
		
		//Walk if possible otherwise stay still
		if (pt.getX() == 0) {
			context.remove(this);
			switch (group) {
			case 1:
				DataHolder.numOfGroup1PassengersLeft -= 1;
				//System.out.println(DataHolder.numOfGroup1PassengersLeft);
				break;
			case 2:
				DataHolder.numOfGroup2PassengersLeft -= 1;
				System.out.println(DataHolder.numOfGroup1PassengersLeft);

				break;
			default:
				DataHolder.numOfGroup3PassengersLeft -= 1;
				//System.out.println(DataHolder.numOfGroup1PassengersLeft);

				break;
			
			}
			
			if (context.size() == 0) {
				RunEnvironment.getInstance().endRun();
				
				
			}
			//Record this removal in DataHolder
			return;
			
			
		}
		boolean someoneinfront = false;

		for(Object obj : grid.getObjectsAt(pt.getX()-1, pt.getY())) {
			if (obj instanceof DisembarkingPassenger) {
				someoneinfront = true;
				break;
			}
		}
		
		if (!someoneinfront) {
			NdPoint myPoint = space.getLocation(this);
			space.moveByVector(this, speed, Math.PI, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
		
	}	
	
	public void moveForwardInAisle() {
		
		
		Context<Object> context = ContextUtils.getContext(this);
		GridPoint pt = grid.getLocation(this);
		
		//Walk if possible otherwise stay still
		boolean someoneinfront = false;
		int val = 1;
		if (seatNumber[1] == 0) {
			val = -1;
		}
		for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+val)) {
			if (obj instanceof DisembarkingPassenger) {
				someoneinfront = true;
				break;
			}
		}
		if (!someoneinfront) {
			
			
			boolean someoneinaisle = false;
			if (pt.getY()+val == 3) {
				for(Object obj : grid.getObjectsAt(pt.getX()+1, pt.getY()+val)) {
					if (obj instanceof DisembarkingPassenger) {
						DisembarkingPassenger dpas = (DisembarkingPassenger) obj;
						if (!(dpas.luggageTime-dpas.timeSpentOnLuggage>1)) {
							someoneinaisle = true;
							break;
						}
						
					}
				}
				//Aisle privilege assortment
				
				if (!someoneinaisle) {
					boolean someoneopposite = false;
					for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+val*2)) {
						if (obj instanceof DisembarkingPassenger) {
							DisembarkingPassenger dpas = (DisembarkingPassenger) obj;
							if (dpas.seatNumber[2] < this.seatNumber[2]) {
								someoneopposite = true;
							}
							else if (dpas.seatNumber[2] == this.seatNumber[2]) {
								if (val == 1) {
									someoneopposite = true;
								}
							}
						}
					}
					if (!someoneopposite) {
						NdPoint myPoint = space.getLocation(this);
						space.moveByVector(this, speed, Math.PI/2.0*val, 0);
						myPoint = space.getLocation(this);
						grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
					}
				}
			}
			else {
				NdPoint myPoint = space.getLocation(this);
				space.moveByVector(this, speed, Math.PI/2.0*val, 0);
				myPoint = space.getLocation(this);
				grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			}
			
			
			
			
			//Upper-lower random assortment
			/*if (seatNumber[1] == 0) { //Upper side
				
				
				if (!someoneinaisle) {
					NdPoint myPoint = space.getLocation(this);
					space.moveByVector(this, speed, Math.PI/2.0*val, 0);
					myPoint = space.getLocation(this);
					grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
				}
			}
			
			else if(!someoneinaisle) { //Lower side, check only if aisle not occupied, but also check upper side
				boolean someoneinupper = false;
				for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+val*2)) {
					if (obj instanceof DisembarkingPassenger) {
						someoneinupper = true;
						break;
					}
				}
				if (!someoneinupper) {
					NdPoint myPoint = space.getLocation(this);
					space.moveByVector(this, speed, Math.PI/2.0*val, 0);
					myPoint = space.getLocation(this);
					grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
				}
				
			}*/
			
			
			
			
			
			
		}
	
		else {
			return;
		}
		pt = grid.getLocation(this);
		if (pt.getY() == 3) {
			inAisle = false;
		}
		
	}
		
	
	public double getTick() {
		
		return RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
}


