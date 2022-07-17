package boarding;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class TETAPassenger {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public int seatNumber[];
	private boolean carryonLuggage;
	private int timeSpentOnLuggage = 0;
	private boolean doingLuggage = false;
	private float speed;
	private boolean seated = false;
	private boolean gettingIntoSeat = false;
	private int timeSpentOnGettingIntoSeat = 0;
	private int seatingTime = 0;
	public int group;
	private boolean firstTimeMoving = true;
	public int timeBeforeBoarding = 0;
	private int segment; //1, 2 or 3
	private boolean atSegment = false;
	private int entrance;
	
	
	public int luggageTime = ProbabilityTimeGenerator.generateWeibullLuggageTime();
	
	public TETAPassenger(ContinuousSpace<Object> space, 
			Grid<Object> grid, 
			int[] seatNumber,
			boolean carryonLuggage,
			float speed, int group, int segment, int entrance) {
		this.space = space;
		this.grid = grid;
		this.seatNumber = seatNumber;
		this.carryonLuggage = carryonLuggage;
		this.speed = speed;
		this.group = group;
		this.segment = segment;
		this.entrance = entrance;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		if(timeBeforeBoarding <= 0) {
			boolean stay = true;
			
			switch (group) {
			case 1:
				stay = false;
				break;
			case 2:
				if (DataHolder.numOfGroup1PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 3:
				if (DataHolder.numOfGroup2PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 4:
				if (DataHolder.numOfGroup3PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 5:
				if (DataHolder.numOfGroup4PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 6:
				if (DataHolder.numOfGroup5PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 7:
				if (DataHolder.numOfGroup6PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 8:
				if (DataHolder.numOfGroup7PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 9:
				if (DataHolder.numOfGroup8PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 10:
				if (DataHolder.numOfGroup9PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 11:
				if (DataHolder.numOfGroup10PassengersLeft == 0) {
					stay = false;
				}
				break;
			case 12:
				if (DataHolder.numOfGroup11PassengersLeft == 0) {
					stay = false;
				}
				break;
			
			default:
				if (DataHolder.numOfGroup12PassengersLeft == 0) {
					stay = false;
				}
				break;
					
			}
			if (!stay) {
				if(atSegment) {
					//Do normal passenger procedure
					normalProcedure();
				}
				else {
					moveToSegment();
				}
			}
			
		}
		else {
			timeBeforeBoarding -= 1;
		}
		
	}
	
	public void moveToSegment() {
		GridPoint pt = grid.getLocation(this);
		//Check if at row
		if (pt.getY() == this.segment*4+2) {
			atSegment = true;
			normalProcedure();
		}
		else {
			//Check if someone in front
			boolean someoneinfront = false;
			for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+1)) {
				if (obj instanceof TETAPassenger) {
					someoneinfront = true;
					break;
				}
			}
			if(someoneinfront) {
				//Do nothing (add stress factor?)

			}
			else {
				//move forward
				moveForwardY();
			}
		}
	}
	
	
	
	//Normal movement
	public void normalProcedure() {
		if (!seated) {
			if(timeBeforeBoarding <= 0) {
				//Check if doing luggage
				if(doingLuggage) {
					if (timeSpentOnLuggage > luggageTime) {
						//Remove from grid, keep track of where passenger is seated
						doingLuggage = false;
						Context<Object> context = ContextUtils.getContext(this);
						gettingIntoSeat = true;
						calculateSeatingTime();
					}
					else {
						//Do luggage
						timeSpentOnLuggage++;
						return;
					}
				}
				else if (gettingIntoSeat) {
					
					if (timeSpentOnGettingIntoSeat > seatingTime) {
						//Remove from grid, keep track of where passenger is seated
						gettingIntoSeat = false;
						//Context<Object> context = ContextUtils.getContext(this);
						moveToSeat();
						
					}
					else {
						//Do luggage
						timeSpentOnGettingIntoSeat++;
						return;
					}
				}
				else {
					
					
					GridPoint pt = grid.getLocation(this);
					//Check if at row
					if (pt.getX() == this.seatNumber[0]+1) {
						if (this.carryonLuggage) {
							//Do luggage
							timeSpentOnLuggage++;
							doingLuggage = true;
							return;
						}
						else {
							//Remove from grid, keep track of where passenger is seated
							Context<Object> context = ContextUtils.getContext(this);
							//move to seat
							gettingIntoSeat = true;
							calculateSeatingTime();

						}
					}
					else {
						//Check if someone in front
						boolean someoneinfront = false;
						for(Object obj : grid.getObjectsAt(pt.getX()+1+2*(-entrance), pt.getY())) {
							if (obj instanceof TETAPassenger) {
								someoneinfront = true;
								break;
							}
						}
						if(someoneinfront) {
							//Do nothing (add stress factor?)

						}
						else {
							//move forward
							moveForward();
						}
					}
				}
			}
			else {
				timeBeforeBoarding--;
			}
		}
	}
	
	public void moveForward() {
		//only move if we are not already in the grid location
		
		NdPoint myPoint = space.getLocation(this);
		space.moveByVector(this, speed, entrance*Math.PI, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		if (firstTimeMoving) {
			switch (group) {
			case 1:
				DataHolder.numOfGroup1PassengersLeft -= 1;
				break;
			case 2:
				DataHolder.numOfGroup2PassengersLeft -= 1;
				break;
			case 3:
				DataHolder.numOfGroup3PassengersLeft -= 1;
				break;
			case 4:
				DataHolder.numOfGroup4PassengersLeft -= 1;
				break;
			case 5:
				DataHolder.numOfGroup5PassengersLeft -= 1;
				break;
			case 6:
				DataHolder.numOfGroup6PassengersLeft -= 1;
				break;
			case 7:
				DataHolder.numOfGroup7PassengersLeft -= 1;
				break;
			case 8:
				DataHolder.numOfGroup8PassengersLeft -= 1;
				break;
			case 9:
				DataHolder.numOfGroup9PassengersLeft -= 1;
				break;
			case 10:
				DataHolder.numOfGroup10PassengersLeft -= 1;
				break;
			case 11:
				DataHolder.numOfGroup11PassengersLeft -= 1;
				break;
			case 12:
				DataHolder.numOfGroup12PassengersLeft -= 1;
				break;
			default:
				DataHolder.numOfGroup13PassengersLeft -= 1;
				break;
			}
			firstTimeMoving = false;
		}
		
		
	}
	
	public void moveForwardY() {
		//only move if we are not already in the grid location
		
		NdPoint myPoint = space.getLocation(this);
		space.moveByVector(this, speed, Math.PI/2, 0);
		
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		if (firstTimeMoving) {
			switch (group) {
			case 1:
				DataHolder.numOfGroup1PassengersLeft -= 1;
				break;
			case 2:
				DataHolder.numOfGroup2PassengersLeft -= 1;
				break;
			case 3:
				DataHolder.numOfGroup3PassengersLeft -= 1;
				break;
			case 4:
				DataHolder.numOfGroup4PassengersLeft -= 1;
				break;
			case 5:
				DataHolder.numOfGroup5PassengersLeft -= 1;
				break;
			case 6:
				DataHolder.numOfGroup6PassengersLeft -= 1;
				break;
			case 7:
				DataHolder.numOfGroup7PassengersLeft -= 1;
				break;
			case 8:
				DataHolder.numOfGroup8PassengersLeft -= 1;
				break;
			case 9:
				DataHolder.numOfGroup9PassengersLeft -= 1;
				break;
			case 10:
				DataHolder.numOfGroup10PassengersLeft -= 1;
				break;
			case 11:
				DataHolder.numOfGroup11PassengersLeft -= 1;
				break;
			case 12:
				DataHolder.numOfGroup12PassengersLeft -= 1;
				break;
			default:
				DataHolder.numOfGroup13PassengersLeft -= 1;
				break;
			}
			firstTimeMoving = false;
		}
		
		
	}
	
	public void moveToSeat() {
		NdPoint myPoint = space.getLocation(this);
		double angle = Math.PI/2.0;
		if (seatNumber[1] == 1) {
			angle = -angle;
		}
		
		space.moveByVector(this, seatNumber[2] + 1, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		
		seated = true;
		//Record the seating
		DataHolder.seatedPassengersInRowTETA[segment][seatNumber[0]][seatNumber[1]][seatNumber[2]] = 1;
		DataHolder.numOfNonSeatedPassengersTETA -= 1;
		
		if (DataHolder.numOfNonSeatedPassengersTETA == 0) {
			DataHolder.numOfNonSeatedPassengersTETA = (DataHolder.numberOfSeatsInRowTETA+2*(DataHolder.numberOfSeatsInRowTETA-1))*35+(DataHolder.numberOfSeatsInRowTETA-1)*3*3-3;
			RunEnvironment.getInstance().endRun();
			
		}
	}
	
	public void calculateSeatingTime() {
		if (seatNumber[2] == 0) {
			seatingTime = 1;
		}
		else {
			int[] peopleSeatedInRow = DataHolder.seatedPassengersInRowTETA[segment][seatNumber[0]][seatNumber[1]];
			int aisleSeat = peopleSeatedInRow[0];
			int middleSeat = peopleSeatedInRow[1];

			seatingTime = aisleSeat*5+middleSeat*7+1*(aisleSeat+middleSeat)+1;
		
		}
		
		
		
	}
}
