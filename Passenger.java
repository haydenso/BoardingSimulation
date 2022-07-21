package boarding;


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


public class Passenger {

		private ContinuousSpace<Object> space;
		private Grid<Object> grid;
		public int seatNumber[];
		private boolean carryonLuggage;
		private int timeSpentOnLuggage = 0;
		private float startingPenalty = (float) 0.0; // 1 is the lowest now with randomSeed = 1. Lower than 1 and people will move instantly behind each other, not good, but could of course be tried. Starting Penalty default should be 1.
		private int waitingToStart = 2;
		private int timeWaited = 0;
		private boolean doingLuggage = false;
		private float speed;
		private boolean seated = false;
		private boolean gettingIntoSeat = false;
		private int timeSpentOnGettingIntoSeat = 0;
		private int seatingTime = 0;
		public int group;
		private boolean firstTimeMoving = true;
		private float cornerCoefficient = (float) 1;
		private boolean justEnteredCorner = true;
		public int boardingID = 0;
		//private static int numOfNonSeatedPassengers = DataHolder.numOfNonSeatedPassengers;
		public int timeBeforeBoarding = 0;
		private boolean readyToStart = false;
		private boolean standingAround = false;
		private double averageWaitingTime;
		private int spacesGap = 1;
		
		//This is for more stochastic boarding (for more general procedure)

		//public int luggageTime = ProbabilityTimeGenerator.generateWeibullLuggageTime();
		
		//This is for completely deterministic boarding (for experiments)
		public int luggageTime = 10;

		public Passenger(ContinuousSpace<Object> space, 
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
		
		//@ScheduledMethod(start = 1, interval = 1)
		public void step() {
			
			
			if(timeBeforeBoarding <= 0) {
				int totalNumOfPassengersLeft = DataHolder.numOfGroup1PassengersLeft + DataHolder.numOfGroup2PassengersLeft+DataHolder.numOfGroup3PassengersLeft+DataHolder.numOfGroup4PassengersLeft+
						DataHolder.numOfGroup5PassengersLeft+DataHolder.numOfGroup6PassengersLeft+DataHolder.numOfGroup7PassengersLeft+DataHolder.numOfGroup8PassengersLeft+DataHolder.numOfGroup9PassengersLeft+DataHolder.numOfGroup10PassengersLeft
						+DataHolder.numOfGroup11PassengersLeft+DataHolder.numOfGroup12PassengersLeft+DataHolder.numOfGroup13PassengersLeft;
				//System.out.println("one " + totalNumOfPassengersLeft);
				//System.out.println(DataHolder.NAInitialNumberOfPassengers);
				System.out.println(DataHolder.numOfBoardedPAssengers);
				if ((DataHolder.numOfBoardedPAssengers) == boardingID) {
					// Time to start boarding, just to make sure that they go in their respective order 
					readyToStart = true;
				}
				
				if (readyToStart) {
					/*switch (group) {
					case 1:
						procedure();
						break;
					case 2:
						if (DataHolder.numOfGroup1PassengersLeft == 0) {
							procedure();
						}
						break;
					case 3:
						if (DataHolder.numOfGroup2PassengersLeft == 0) {
							procedure();
						}
						break;
					case 4:
						if (DataHolder.numOfGroup3PassengersLeft == 0) {
							procedure();
						}
						break;
					case 5:
						if (DataHolder.numOfGroup4PassengersLeft == 0) {
							procedure();
						}
						break;
					case 6:
						if (DataHolder.numOfGroup5PassengersLeft == 0) {
							procedure();
						}
						break;
					case 7:
						if (DataHolder.numOfGroup6PassengersLeft == 0) {
							procedure();
						}
						break;
					case 8:
						if (DataHolder.numOfGroup7PassengersLeft == 0) {
							procedure();
						}
						break;
					case 9:
						if (DataHolder.numOfGroup8PassengersLeft == 0) {
							procedure();
						}
						break;
					case 10:
						if (DataHolder.numOfGroup9PassengersLeft == 0) {
							procedure();
						}
						break;
					case 11:
						if (DataHolder.numOfGroup10PassengersLeft == 0) {
							procedure();
						}
						break;
					
					default:
						if (DataHolder.numOfGroup11PassengersLeft == 0) {
							procedure();
						}
						break;
							
					}*/
					
					procedure();
					
				}
				
			}
			else {
				timeBeforeBoarding--;
			}
			
			
			
			
			
		}
		
		
		
		public void procedure() {
			
			if (!seated) {
				if (boardingID == 20) {
					//System.out.println(waitingToStart);
				}
				
				if(timeBeforeBoarding <= 0) {
					
					//Check if walking up to aisle still

					GridPoint pt_1 = grid.getLocation(this); 
					if (pt_1.getY() < DataHolder.numberOfSeatsInRow) { // Still moving up to aisle
						boolean someoneinfront = false;
						for(Object obj : grid.getObjectsAt(pt_1.getX(), pt_1.getY()+1)) {
							if (obj instanceof Passenger) {
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
						
						/*if (pt_1.getY() == 2 && pt_1.getX() == 0) {
							float newval = (float)(speed * cornerCoefficient);
							speed = newval;
							
							for(Object obj : grid.getObjectsAt(pt_1.getX()+1, pt_1.getY()+1)) {
								if (obj instanceof Passenger) {
									someoneinfront = true;
									break;
								}
							}
						}*/
						if (pt_1.getY() == 2 && pt_1.getX() == 0 && justEnteredCorner) {
							
							float newval = (float)(speed * cornerCoefficient);
							speed = newval;
							justEnteredCorner = false;
							
						}
						if (pt_1.getY() == 2 && pt_1.getX() == 0) {
							if (!someoneinfront) {
								someoneinfront = someoneStowingInFront(pt_1.getX()-1, pt_1.getY()+1);
							}
						}
						
						
						
						
						if(someoneinfront) {
							//Do nothing (add stress factor?)
							//DataHolder.numberOfCollissions += 1;
						}
						else {
							
							if (waitingToStart == 1) {
								//DataHolder.numberOfCollissions += 1;
							}
							
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
								timeWaited += 1;

								
							}
							
						}
						return;
						
					}
					else if (pt_1.getY() == 3 && pt_1.getX() == 1 && !justEnteredCorner){
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
							return;
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
							




							/*int[] combinations = getCombinations();

							for(int i = 0; i < combinations.length; i++) {
								int[] combo = combinations[i];
								for(Object obj : grid.getObjectsAt(pt.getX()+(combo[0]+1), pt.getY())) {
								if (obj instanceof Passenger) {
									if(((Passenger) obj).doingLuggage) {
										infrontDoingLuggage = true;
									}
									break;
								}
								for(Object obj : grid.getObjectsAt(pt.getX()+(combo[0]+1), pt.getY())) {
								if (obj instanceof Passenger) {
									if(((Passenger) obj).doingLuggage) {
										spacesBetweenUsed = true;
									}
									break;
								}
							}
							}
							// Check if someone is stowing a luggage 2 spaces in front and someone in position 3 spaces 
							for(Object obj : grid.getObjectsAt(pt.getX()+(spacesGap+1), pt.getY())) {
								if (obj instanceof Passenger) {
									if(((Passenger) obj).doingLuggage) {
										infrontDoingLuggage = true;
									}
									break;
								}
							}
							for(Object obj : grid.getObjectsAt(pt.getX()+(spacesGap+2), pt.getY())) {
								if (obj instanceof Passenger) {
									twospacesAhead = true;
									break;
								}
							}
							
							//If someone is doing luggage in front and the three in front space is being used, then the empty space in front is useless
							if(infrontDoingLuggage && twospacesAhead) {
								someoneinfront = true;
								waitingToStart = 0;
							}
								*/
							else {
								
							}
							
							
							if(someoneinfront) {
								//Do nothing (add stress factor?)

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
									timeWaited += 1;

									
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
			updateFirstTimeMoving();
			
			
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
			DataHolder.seatedPassengersInRow[seatNumber[0]][seatNumber[1]][seatNumber[2]] = 1;
			DataHolder.numOfNonSeatedPassengers -= 1;
			//System.out.println(timeWaited);
			//System.out.println(luggageTime);
			if (DataHolder.numOfNonSeatedPassengers == 0) {
				//Print out tracker values
				//System.out.println(DataHolder.numberOfCollissions);
				averageWaitingTime = ((double) (DataHolder.timeSpentWaiting))/((double) DataHolder.numberOfCollissions);
				
				//Reset data trackers
				DataHolder.numOfNonSeatedPassengers = DataHolder.numberOfRows*DataHolder.numberOfSeatsInRow*2-DataHolder.numberOfSeatsInRow;
				DataHolder.numOfBoardedPAssengers = 0;
				DataHolder.timeSpentWaiting = 0;
				
				RunEnvironment.getInstance().endRun();
				
			}
		}
		
		public void calculateSeatingTime() {
			if (seatNumber[2] == 0) {
				seatingTime = 1;
			}
			else {
				int[] peopleSeatedInRow = DataHolder.seatedPassengersInRow[seatNumber[0]][seatNumber[1]];
				int aisleSeat = peopleSeatedInRow[0];
				int middleSeat = peopleSeatedInRow[1];

				seatingTime = aisleSeat*5+middleSeat*7+1*(aisleSeat+middleSeat)+1;
			
				
			}
			
			//System.out.println(seatingTime);
			
			
		}
		
		public int[][] getCombinations() {
			if (spacesGap % 2 == 1) {
				int[][]  combinations = new int[2][2];
				int index = 0;
				for(int i=0; i <= spacesGap; i++) {
					if (Math.abs(2*i - spacesGap)<=1) {
						int[] combo = new int[] {i, spacesGap - i};
						combinations[index] = combo;
						index += 1;

					}
					
				}
				return combinations;
			}
			else {
				int[][] combinations = new int[1][2];
				combinations[0] = new int[] {spacesGap/2, spacesGap/2};
				return combinations;
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

			boolean someoneinfront = false;
			boolean spacesBetweenUsed = false;
			boolean infrontDoingLuggage = false;
			for(int i = 0; i <= firstSpacesInFrontToCheck; i++) {
				for(Object obj : grid.getObjectsAt(x+i+1, y)) {
					if (obj instanceof Passenger) {
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
						if (obj instanceof Passenger) {
							if(((Passenger) obj).seatNumber[0]+1 == x+i+1) {
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
									if (obj instanceof Passenger) {
										
										
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
		
		public double getTick() {
			
			return RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		}
		public int getCollissions() {
			int val = DataHolder.numberOfCollissions;
			DataHolder.numberOfCollissions = 0;

			return val;
		}
		
		public double getAverageWaitingTime() {
			
			
			return averageWaitingTime;
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
				DataHolder.numOfBoardedPAssengers += 1;
				firstTimeMoving = false;
			}
		}
		
		
}


