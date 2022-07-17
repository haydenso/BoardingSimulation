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

public class FlyingWingDisembarkingPassenger {

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
	private boolean movingToExit = false;
	private int segment;
	//private static int numOfNonSeatedPassengers = DataHolder.numOfNonSeatedPassengers;
	public int timeBeforeBoarding = 0;
	
	public int luggageTime = ProbabilityTimeGenerator.generateWeibullLuggageTime()/2;
	
	
	FlyingWingDisembarkingPassenger(ContinuousSpace<Object> space, 
				Grid<Object> grid, 
				int[] seatNumber,
				boolean carryonLuggage,
				float speed, int group, int segment) {
		
		this.space = space;
		this.grid = grid;
		this.seatNumber = seatNumber;
		this.carryonLuggage = carryonLuggage;
		this.speed = speed;
		this.group = group;
		this.segment = segment;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		if(!movingToExit) {
			int constant = DataHolder.numberOfSeatsInRow*2*0;
			switch(group) {
			case 1:
				disembarkingProcedure();
				break;
			case 2:
				if (DataHolder.numOfGroup1PassengersLeft <= constant) {
					disembarkingProcedure();
				}
				break;
			case 3:
				if (DataHolder.numOfGroup2PassengersLeft <= constant) {
					disembarkingProcedure();
				}
				break;
			default:
				if (DataHolder.numOfGroup3PassengersLeft <= constant) {
					disembarkingProcedure();
				}
				break;
				
			}
		}
		else {
			boolean someoneinfront = false;
			GridPoint pt = grid.getLocation(this);
			for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()-1)) {
				if (obj instanceof FlyingWingDisembarkingPassenger) {
					someoneinfront = true;
					break;
				}
			}
			if(!someoneinfront) {
				
				boolean someonecoming2 = false;
				if(pt.getY() % 6 == 4) {
					for(Object obj : grid.getObjectsAt(pt.getX()+1, pt.getY()-1)) {
						if (obj instanceof FlyingWingDisembarkingPassenger) {
							FlyingWingDisembarkingPassenger fwp = (FlyingWingDisembarkingPassenger) obj;

							if(fwp.seatNumber[0] < this.seatNumber[0]) {
								
								someonecoming2 = true;
								break;
							}
								
							
						}
					}
				}
				
				if (!someonecoming2) {
					moveDown();

				}

			}
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
		
		//Context<Object> context = ContextUtils.getContext(this);
		GridPoint pt = grid.getLocation(this);
		
		//Walk if possible otherwise stay still
		if (pt.getX() == 0) {
			movingToExit = true;
			return;
		}
		
			
			
			
		
		boolean someoneinfront = false;

		for(Object obj : grid.getObjectsAt(pt.getX()-1, pt.getY())) {
			if (obj instanceof FlyingWingDisembarkingPassenger) {
				someoneinfront = true;
				break;
			}
		}
		
		if (!someoneinfront) {
			boolean someonecoming = false;

			if (pt.getX() == 1) {

				for(Object obj : grid.getObjectsAt(pt.getX()-1, pt.getY()+1)) {
					if (obj instanceof FlyingWingDisembarkingPassenger) {
						FlyingWingDisembarkingPassenger fwp = (FlyingWingDisembarkingPassenger) obj;

						if(fwp.seatNumber[0] <= this.seatNumber[0]) {
							
							someonecoming = true;
							break;
						}
							
						
					}
				}
			}
			if(!someonecoming) {
				NdPoint myPoint = space.getLocation(this);
				space.moveByVector(this, speed, Math.PI, 0);
				myPoint = space.getLocation(this);
				grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			}
			
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
			if (obj instanceof FlyingWingDisembarkingPassenger) {
				someoneinfront = true;
				break;
			}
		}
		if (!someoneinfront) {
			
			
			boolean someoneinaisle = false;
			if (pt.getY()+val == 3) {
				for(Object obj : grid.getObjectsAt(pt.getX()+1, pt.getY()+val)) {
					if (obj instanceof FlyingWingDisembarkingPassenger) {
						FlyingWingDisembarkingPassenger dpas = (FlyingWingDisembarkingPassenger) obj;
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
						if (obj instanceof FlyingWingDisembarkingPassenger) {
							FlyingWingDisembarkingPassenger dpas = (FlyingWingDisembarkingPassenger) obj;
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
		if (pt.getY() == 3+6*segment) {
			inAisle = false;
		}
		
	}
	
	public void moveDown() {
		//only move if we are not already in the grid location
		
			Context<Object> context = ContextUtils.getContext(this);
			NdPoint myPoint = space.getLocation(this);
			space.moveByVector(this, speed, -Math.PI/2, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			if((int) myPoint.getY() <= 3) {
				context.remove(this);
				switch (group) {
				case 1:
					DataHolder.numOfGroup1PassengersLeft -= 1;
					//System.out.println(DataHolder.numOfGroup1PassengersLeft);
					break;
				case 2:
					DataHolder.numOfGroup2PassengersLeft -= 1;
					//System.out.println(DataHolder.numOfGroup1PassengersLeft);

					break;
				case 3:
					DataHolder.numOfGroup3PassengersLeft -= 1;
					//System.out.println(DataHolder.numOfGroup1PassengersLeft);

					break;
				default:
					DataHolder.numOfGroup4PassengersLeft -= 1;
					//System.out.println(DataHolder.numOfGroup1PassengersLeft);

					break;
				
				}
				
				if (context.size() == 0) {
					RunEnvironment.getInstance().endRun();
					
					
				}
				//Record this removal in DataHolder
				return;
			}
		
		
		
		
		
		
		
		
	}
		
	
	public double getTick() {
		
		return RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}
}
