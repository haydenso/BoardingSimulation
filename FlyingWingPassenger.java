package boarding;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;



public class FlyingWingPassenger {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public int seatNumber[];
	private boolean carryonLuggage;
	private int timeSpentOnLuggage = 0;
	public float startingPenalty = (float) 0.0; // 1 is the lowest now with randomSeed = 1. Lower than 1 and people will move instantly behind each other, not good, but could of course be tried. Starting Penalty default should be 1.
	private int waitingToStart = 20;
	private boolean doingLuggage = false;
	private float speed;
	private boolean seated = false;
	private boolean gettingIntoSeat = false;
	private int timeSpentOnGettingIntoSeat = 0;
	private int seatingTime = 0;
	public int group;
	private boolean firstTimeMoving = true;
	private float cornerCoefficient = (float) 1.0;
	private boolean justEnteredCorner = true;
	public int boardingID = 0;
	//private static int numOfNonSeatedPassengers = DataHolder.numOfNonSeatedPassengers;
	public int timeBeforeBoarding = 0;
	
	// Flying Wing specific
	public int segment; //1, 2 or 3
	private boolean atSegment = false;
	
	private boolean readyToStart = false;
	private boolean standingAround = false;
	private double averageWaitingTime;
	private int spacesGap = 1;
	
	public int luggageTime = ProbabilityTimeGenerator.generateGaussianLuggageTime();
	//public int luggageTime = 15;
	
	public FlyingWingPassenger(ContinuousSpace<Object> space, 
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
		
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int spacesGap = params.getInteger("spacesGap");
		this.spacesGap = spacesGap;

		
		
	}
	
	
	
	//@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		if(timeBeforeBoarding <= 0) {
			boolean stay = true;
			//int constant = DataHolder.numberOfSeatsInRow*2*(DataHolder.numberOfRowsFlyingWing-1)*0;
			//System.out.println(DataHolder.FWnumOfBoardedPassengers);
			if ((DataHolder.FWnumOfBoardedPassengers) >= boardingID) {
				// Time to start boarding, just to make sure that they go in their respective order 
				//readyToStart = true;
				stay = false;
			}
			
			
			/*switch (group) {
			case 1:
				stay = false;
				break;
			case 2:
				if (DataHolder.numOfGroup1PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 3:
				if (DataHolder.numOfGroup2PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 4:
				if (DataHolder.numOfGroup3PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 5:
				if (DataHolder.numOfGroup4PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 6:
				if (DataHolder.numOfGroup5PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 7:
				if (DataHolder.numOfGroup6PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 8:
				if (DataHolder.numOfGroup7PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 9:
				if (DataHolder.numOfGroup8PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 10:
				if (DataHolder.numOfGroup9PassengersLeft <= constant) {
					stay = false;
				}
				break;
			case 11:
				if (DataHolder.numOfGroup10PassengersLeft <= constant) {
					stay = false;
				}
				break;
			
			default:
				if (DataHolder.numOfGroup11PassengersLeft <= constant) {
					stay = false;
				}
				break;
					
			}*/
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
		
		if (pt.getY() == this.segment*DataHolder.numberOfSeatsInRowFlyingWing*2+DataHolder.numberOfSeatsInRowFlyingWing+this.segment) {
			atSegment = true;
			normalProcedure();
		}
		
		
		else {
			/*//Check if someone in front
			boolean someoneinfront = false;
			for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+1)) {
				if (obj instanceof FlyingWingPassenger) {
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
			}*/
			
				boolean someoneinfront = false;
				for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY()+1)) {
					if (obj instanceof FlyingWingPassenger) {
						someoneinfront = true;
						if (!standingAround) {
							standingAround = true;
							DataHolder.numberOfCollissions += 1;
						}
						DataHolder.timeSpentWaiting += 1;
						waitingToStart = 0;
						break;
					}
				}
				
				/*if (pt_1.getY() == 2 && pt_1.getX() == 0 && justEnteredCorner) {
					
					float newval = (float)(speed * cornerCoefficient);
					speed = newval;
					justEnteredCorner = false;
					
				}*/

				if (pt.getY() == segment*(DataHolder.numberOfSeatsInRowFlyingWing*2+1)+DataHolder.numberOfSeatsInRowFlyingWing-1 && pt.getX() == 0 && justEnteredCorner) {
					
					float newval = (float)(speed * cornerCoefficient);
					speed = newval;
					justEnteredCorner = false;
					
				}
				if (pt.getY() == segment*(DataHolder.numberOfSeatsInRowFlyingWing*2+1)+DataHolder.numberOfSeatsInRowFlyingWing-1 && pt.getX() == 0) {
					if (!someoneinfront) {
						someoneinfront = someoneStowingInFront(pt.getX()-1, pt.getY()+1);
					}
				}
				
				
				
				if(someoneinfront) {
					//Do nothing (add stress factor?)
					//DataHolder.numberOfCollissions += 1;
					waitingToStart = 0;
				}
				else {
					
					
					// If starting penalty is over:
					if (waitingToStart >= startingPenalty) {
						//move forward
						
						
						// Just move forward normally
						//move forward
						//System.out.println("Normal: " + speed);
						moveForward(Math.PI/2);
						
						standingAround = false;
						return;
						
						
					}
					else {
						if (1 + waitingToStart - startingPenalty > 0) {
							System.out.println(waitingToStart);
							// Wait a little bit
							float orval = speed;
							//System.out.println("First: " + speed);
							speed = (1 + waitingToStart-startingPenalty)*speed;
							//System.out.println("Second: " + speed);
							//move forward
							moveForward(Math.PI/2);
							speed = orval;
							standingAround = false;


							
							
						}
						
						waitingToStart++;
						

						
					}
					
				}
			
		}
	}
	
	
	
	//Normal movement
	public void normalProcedure() {
		if (!seated) {
			if(timeBeforeBoarding <= 0) {
				
				// Reser to non-corner pace and register leaving corner
				GridPoint pt_1 = grid.getLocation(this); 

				if (pt_1.getY() == segment*(DataHolder.numberOfSeatsInRowFlyingWing*2+1)+DataHolder.numberOfSeatsInRowFlyingWing && pt_1.getX() == 1 && !justEnteredCorner){
					//Done from corner
				  
					float oldval = (float)(speed/cornerCoefficient);
					speed = oldval;
					justEnteredCorner = true;
					
				}
				//Check if doing luggage
				if(doingLuggage) {
					if (timeSpentOnLuggage > luggageTime) {
						//Remove from grid, keep track of where passenger is seated
						doingLuggage = false;
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
							/*timeSpentOnLuggage++;
							doingLuggage = true;
							return;*/
							
							checkStowingPossibilities(pt.getX(), pt.getY());

						}
						else {
							//Remove from grid, keep track of where passenger is seated
							//move to seat
							gettingIntoSeat = true;
							calculateSeatingTime();

						}
					}
					else {
						//Check if someone in front
						boolean someoneinfront = someoneStowingInFront(pt.getX(), pt.getY());
						
						if(someoneinfront) {
							waitingToStart = 0;
						} 
						else {
							
							if (waitingToStart == 1) {
								//DataHolder.numberOfCollissions += 1;
							}
							
							// If starting penalty is over:
							if (waitingToStart >= startingPenalty) {
								//move forward
								
								// Just move forward normally
								//System.out.println("Normal: " + speed);

								moveForward(0.0);
								standingAround = false;
								
							}
							else {
								if (1 + waitingToStart - startingPenalty > 0) {
									// Wait a little bit
									float orval = speed;
									//System.out.println("First: " + speed);
									speed = (1 + waitingToStart-startingPenalty)*speed;
									//System.out.println("Second: " + speed);
									moveForward(0.0);
									speed = orval; 
									standingAround = false;
									

									
								}
								
								waitingToStart++;
								//timeWaited += 1;

								
							}
							
						}
					}
				}
			}
			else {
				timeBeforeBoarding--;
			}
		}
	}
	
	public void moveForward(Double angle) {
		//only move if we are not already in the grid location
		
		NdPoint myPoint = space.getLocation(this);
		space.moveByVector(this, speed, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		if ((int)myPoint.getY() == 1) {
			updateFirstTimeMoving();

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
			default:
				DataHolder.numOfGroup12PassengersLeft -= 1;
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
		DataHolder.seatedPassengersInRowFlyingWing[segment][seatNumber[0]][seatNumber[1]][seatNumber[2]] = 1;
		DataHolder.numOfNonSeatedPassengersFlyingWing -= 1;
		
		//System.out.println(luggageTime);
		if (DataHolder.numOfNonSeatedPassengersFlyingWing == 0) {
			DataHolder.numOfNonSeatedPassengersFlyingWing = (DataHolder.numberOfSegmentsFlyingWing-1)*DataHolder.numberOfRowsFlyingWing*DataHolder.numberOfSeatsInRowFlyingWing*2+2*(DataHolder.numberOfRowsFlyingWing-3)*DataHolder.numberOfSeatsInRowFlyingWing;
			averageWaitingTime = ((double) (DataHolder.timeSpentWaiting))/((double) DataHolder.numberOfCollissions);

			//Reset data trackers
			DataHolder.FWnumOfBoardedPassengers = 0;
			DataHolder.timeSpentWaiting = 0;
			ProbabilityTimeGenerator.num = 0;
			ProbabilityTimeGenerator.total_lugtime = 0;
			
			RunEnvironment.getInstance().endRun();
			
		}
		
	}
	
	public void checkStowingPossibilities(int x, int y) {
			boolean infrontempty = true;
			boolean behindempty = true;
			if(spacesGap > 1) {
				// If bigger than one, figure out all the possible combinations of the number
				// Number of possibilities is always n+1 (e.g 5 has 6 possibilities)
				// The following fills up combinations with arrays that contain the the spaces in front and spaces behind 
				//int[][] combinations = getCombinations();
				

				// For each [front, back] in combinations array, set that as the check for front and back
				
				int dist_to_check = (int) Math.ceil(((double)spacesGap)/2.0);
				//int backcheck = combinations[i][1];		
				//However for each space we need to check all the spaces up to it, hence calling for another for loop
				// If the number is 0, there is no point in check
				int emptySpacesAround = 0;
				if(dist_to_check != 0) {
					front_outer:
					for(int i = 1; i<=dist_to_check; i++) {
						for(Object obj : grid.getObjectsAt(x+i, y)) {
							if (obj instanceof Passenger) {
								infrontempty = false;
								break front_outer;
								//Check space in front
								//At that instant if both are True, then proceed with stowage
							}
						}
						emptySpacesAround += 1;
					}
				}
				
				if(dist_to_check != 0) {
					back_outer:
					for(int i=1; i<=dist_to_check; i++) {
						for(Object obj : grid.getObjectsAt(x-i, y)) {
							if (obj instanceof Passenger) {
								behindempty = false;
								break back_outer;
								//Check space behind
								//At that instant if both are True, then proceed with stowage
							}
						}
						emptySpacesAround += 1;

					}
				}

				
				

				if (emptySpacesAround >= spacesGap) {
					//Do luggage
					timeSpentOnLuggage++;
					doingLuggage = true;
					return;

				}
			
			}
			// if spacesGap == 1, just check front once and back once;
			else if(spacesGap == 1) {
				for(Object obj : grid.getObjectsAt(x+spacesGap, y)) {
					if (obj instanceof Passenger) {
						infrontempty = false;
						break;
						//Check space in front
						//At that instant if both are True, then proceed with stowage
					}
				}
				for(Object obj : grid.getObjectsAt(x-spacesGap, y)) {
					if (obj instanceof Passenger) {
						behindempty = false;
						break;
						//Check space behind
						//At that instant if both are True, then proceed with stowage
					}
				}
				if (infrontempty || behindempty) {
					//Do luggage
					timeSpentOnLuggage++;
					doingLuggage = true;
					return;

				}
			}
			else {
				timeSpentOnLuggage++;
				doingLuggage = true;
				return;
			}
	}
	
	public boolean someoneStowingInFront(int x, int y) {
		//Check if someone in front
		if (x == -1) {
			//System.out.println(x);

		}
		int firstSpacesInFrontToCheck = (int) Math.ceil(((double) spacesGap)/2.0) - 1;
		if (spacesGap == 0) {
			firstSpacesInFrontToCheck = 0;
		}
		boolean someoneinfront = false;
		boolean spacesBetweenUsed = false;
		boolean infrontDoingLuggage = false;
		for(int i = 0; i <= firstSpacesInFrontToCheck; i++) {
			for(Object obj : grid.getObjectsAt(x+i+1, y)) {
				if (obj instanceof FlyingWingPassenger) {
					someoneinfront = true;
					waitingToStart = 0;
					break;
				}
			}
		}
		
		if (!someoneinfront) {
			int spacesInFrontToCheck = (int) Math.ceil(((double) spacesGap)/2.0);
			int spaceBetween = 0;
			outerloop:
			for(int i = 1; i <= spacesInFrontToCheck; i++) {
				for(Object obj : grid.getObjectsAt(x+i+1, y)) {
					if (obj instanceof FlyingWingPassenger) {
						if(((FlyingWingPassenger) obj).seatNumber[0]+1 == x+i+1) {
							infrontDoingLuggage = true;
							spaceBetween = i;
						}
						
						break outerloop;
					}
				}
			}
			// Think this fixes WWL overchecking
			//System.out.println("Space between: " + spaceBetween);
			//System.out.println("Space in front To Check: " + spacesInFrontToCheck);
			if (infrontDoingLuggage) {
				if (spacesGap % 2 == 0) {
					if (spaceBetween == spacesInFrontToCheck) {
						someoneinfront = true;
					}
				} 
				else {
					
					if (spaceBetween == spacesInFrontToCheck - 1) {
						someoneinfront = true;
					}
					else if (spaceBetween == spacesInFrontToCheck) {
						//Now check if the person is relying on space between you and them
						int emptySpaceInFrontOfStower = 0;
						second_outerloop:
						for(int i = 1; i <= spacesGap; i++) {
							for(Object obj : grid.getObjectsAt(x+spaceBetween + 1 + i, y)) {
								if (obj instanceof FlyingWingPassenger) {
									
									
									break second_outerloop;
								}
							}
							emptySpaceInFrontOfStower += 1;
						} 
						if (emptySpaceInFrontOfStower+spaceBetween>spacesGap) {
							//Gucci, go ahead one more step
						}
						else {
							someoneinfront = true;
						}
					}
					else {
						someoneinfront = true;
					}
					
				}
					
				
			}
		}
		return someoneinfront;
	}
	
	private void updateFirstTimeMoving() {
		if (firstTimeMoving) {
			/*switch (group) {
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
			default:
				DataHolder.numOfGroup12PassengersLeft -= 1;
				break;
			}*/
			
			DataHolder.FWnumOfBoardedPassengers += 1;
			firstTimeMoving = false;
		}
	}
	
	
	public void calculateSeatingTime() {
		if (seatNumber[2] == 0) {
			seatingTime = 1;
		}
		else {
			int[] peopleSeatedInRow = DataHolder.seatedPassengersInRowFlyingWing[segment][seatNumber[0]][seatNumber[1]];
			int aisleSeat = peopleSeatedInRow[0];
			int middleSeat = peopleSeatedInRow[1];

			seatingTime = aisleSeat*5+middleSeat*7+1*(aisleSeat+middleSeat)+1;
		
			//System.out.println(seatingTime);
		}
		
		//seatingTime = 1;
		
		//System.out.println(seatingTime);
		
		
	}
}
