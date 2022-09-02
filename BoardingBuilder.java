package boarding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.SimUtilities;
import repast.simphony.engine.schedule.*;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.*;
import java.util.stream.Collectors;


public class BoardingBuilder implements ContextBuilder<Object> {
	
	//public int[] seatedPassengersInRow = new int[numberOfRows];
	private int numberOfRows = DataHolder.numberOfRows;
	private int numberOfSeatsInRow = DataHolder.numberOfSeatsInRow;
	
	@Override
	public Context build(Context<Object> context) {
		
		
		// TODO Auto-generated method stub
		context.setId("Boarding");
		
		// Get parameters
		Parameters params = RunEnvironment.getInstance().getParameters();
		int numberOfSegmentsFlyingWing = params.getInteger("numberOfSegmentsFlyingWing");
		int time = params.getInteger("zz");
		int groups = params.getInteger("numberOfGroups");
		int entropy = params.getInteger("entropy");
		int method = params.getInteger("method");
		float stoppingPenalty = params.getFloat("stoppingPenalty");
		//stoppingPenalty = (float) 0.5;
		DataHolder.numberOfSegmentsFlyingWing = numberOfSegmentsFlyingWing;
		DataHolder.setComputedProperties();
	
		int aircraft = 1; // 0 = NA, 1 = FW, 2 = TETA (still have to choose appropriate boarding method)
		ContinuousSpace space;
		Grid grid;
		
		switch (aircraft) {
		case 0:
			//NARROW AIRCRAFT
			space = generateNarrowAircraftSpace(context);
			grid = generateNarrowAircraftGrid(context);
			break;
		case 1:
			//FLYING WING AIRCRAFT
			space = generateFlyingWingAircraftSpace(context);
			grid = generateFlyingWingAircraftGrid(context);
			break;
		case 2:
			//TETA AIRCRAFT
			space = generateTETAAircraftSpace(context);
			grid = generateTETAAircraftGrid(context);
			break;
			
		default:
			//NARROW AIRCRAFT
			space = generateNarrowAircraftSpace(context);
			grid = generateNarrowAircraftGrid(context);
			break;
		}
		
		

		
		
		
		
		/*ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), numberOfRows+1, 2*numberOfSeatsInRow+1);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, numberOfRows+1, 2*numberOfSeatsInRow+1));*/
		
		//Create passengers
		/*List<Passenger> passengers = new ArrayList<Passenger>();
		
		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				for (int a = 0; a < numberOfSeatsInRow; a++) {
					int seat[] = {i,e,a};
					Random rd = new Random();
					float speed = (float) (0.2 + rd.nextFloat()*(0.5));
					passengers.add(new Passenger(space, grid, seat, 
							rd.nextBoolean(), 
							speed, 1
							));
					DataHolder.seatedPassengersInRow[i][e][a] = 0;

					
				}
			}
			
		}*/
	
	//NARROW AIRCRAFT
		//Random boarding
		//randomBoarding(context, space, grid, time);
		
		//Random but same boarding
		//frontToBackOneByOne(context, space, grid, time);
		
		//Group boarding
		//groupBoarding(context, space, grid, time);
		
		//Seat boarding
		//seatBoarding(context, space, grid, time);
		
		//Slow-fast boarding
		//slowFastBoarding(context, space, grid);
		
		//Steffen boarding
		//steffenBoarding(context, space, grid, time, entropy);
		
		//P4R
		//p4rBoarding(context, space, grid);
		
		//Disembarking
		
		//(Semi-)Random disembarking
		//randomDisembarking(context, space, grid);
		
		//Front-to-bakc disembarking
		//frontToBackDisembarking(context, space, grid);
		
		// Scalable Group Boarding
		//groupBoardingScalable(context, space, grid, time, groups);
		
//FLYING WING AIRCRAFT
		
	//Boarding
		switch(method) {
		case 0:
			// Parallel Sequential Steffen
			steffenBoardingFlyingWing(context, space, grid, time, stoppingPenalty, entropy);

			//randomBoardingFlyingWing(context, space, grid, time);
			break;
		case 1:
			// Parallel Sequential WMA Random
			parallelSeatRandomBoardingFlyingWing(context, space, grid, time);

			//groupBoardingFlyingWing(context, space, grid, time);
			break;

		case 2:
			// 
			parallelSteffenBoardingFlyingWing(context, space, grid, time);
			//randomParallelSeatBoardingFlyingWing(context, space, grid, time);
			break;

		case 3:
			// Segmented WMA boarding
			parallelBTFBoardingFlyingWing(context, space, grid, time);

			//sequentialSteffenBoardingFlyingWing(context, space, grid, time, stoppingPenalty);
			break;

		case 4:
			//Steffen Boarding
			parallelFTBBoardingFlyingWing(context, space, grid, time);

			//steffenBoardingFlyingWing(context, space, grid, time, stoppingPenalty, entropy);
			break;

		case 5: 
			//Segmented boarding
			parallelWMABoardingFlyingWing(context, space, grid, time);
			
			//sequentialRandomBoardingFlyingWing(context, space, grid, time);
			break;

		case 6:
			//Segmented boarding
			//randomParallelWMARandomBoardingFlyingWing(context, space, grid, time);
			parallelFrontToBackInToOut(context, space, grid, time);
			break;

			
		case 7:
			//Parallel segments boarding
			//parallelSteffenBoardingFlyingWing(context, space, grid, time);
			parallelRandomBoardingFlyingWing(context, space, grid, time);

			break;

		case 8:
			// Segmented front to back amw
			//parallelFrontToBackInToOut(context, space, grid, time);
			sequentialSteffenBoardingFlyingWing(context, space, grid, time, stoppingPenalty);
			break;

		case 9:
			// Parallel seat boarding
			randomBoardingFlyingWing(context, space, grid, time);
			//parallelSeatRandomBoardingFlyingWing(context, space, grid, time);
			break;

		/*case 10:
			// Parallel group boarding
			randomBoardingFlyingWing(context, space, grid, time);
			//parallelBTFBoardingFlyingWing(context, space, grid, time);
			break;
		case 11: 
			parallelFTBBoardingFlyingWing(context, space, grid, time);
			break;
		case 12:
			parallelWMABoardingFlyingWing(context, space, grid, time);
			break;
		case 13:
			parallelRandomBoardingFlyingWing(context, space, grid, time);
			break;*/
		default:
			//Random boarding
			randomBoardingFlyingWing(context, space, grid, time);
			break;

		}
		
		
		
		
		
		
		
		

		
		
		
		
		
		
		
		
		
		
		
		
	//Disembarking
		
		//Random disembarking
		//randomDisembarkingFlyingWing(context, space, grid);
		
		//Segment disembarking
		//segmentedDisembarkingFlyingWing(context, space, grid);
		
//TETA AIRCRAFT
	
	//Boarding
		
		//Random boarding
		//randomBoardingTETA(context, space, grid);
		
		//WMA Boarding
		//seatBoardingTETA(context, space, grid);
		
		//Steffen boarding
		//steffenBoardingTETA(context, space, grid);
		
	//Disembarking
		//randomDisembarkingTETA(context, space, grid);
		
		///Only add this for boarding (not TETA)
		/*for (Object obj : context) {
			NdPoint pt = new NdPoint(0.4,3.4);
			space.moveTo(obj, pt.getX(), pt.getY());
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
			
		}*/
		
		///Add this instead for corner boarding on the narrow aircraft
		Random rd = new Random();
		for (Object obj : context) {
			NdPoint pt = new NdPoint(rd.nextDouble(),rd.nextDouble());
			space.moveTo(obj, pt.getX(), pt.getY());
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
			
		}
		
		switch (aircraft) {
		case 0:
			// Add for narrow aircraft
			context.add(new NarrowAircraftIterator());
			break;
		case 1:
			// Add for Flying Wing
			context.add(new FlyingWingIterator());
			break;
		default:
			break;
		}
		
		

		

		return context;
	}
	
	public void frontToBackOneByOne(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		//Create passengers
		List<Passenger> passengers = new ArrayList<Passenger>();

		for (int i = 0; i < numberOfRows; i++) {
			int row = numberOfRows - 1 - i;
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {row,e,a};
						//Random rd = new Random();
						
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1;
						passengers.add(new Passenger(space, grid, seat, 
								true, 
								speed, 1
								));
						
						
						DataHolder.seatedPassengersInRow[row][e][a] = 0;

						
					}
				}
				
			}
			
		}
		
		//SimUtilities.shuffle(passengers, RandomHelper.getUniform());
		// Put passengers in pseudo-random order
		
		int ind = 0;
		Random rd = new Random();	
		int totalgrouptime = 0;
		int id = 0;
		for(Passenger pas : passengers) {
			pas.boardingID = id;
			pas.timeBeforeBoarding = totalgrouptime;
			context.add(pas);
			id += 1;
			totalgrouptime += time;
			ind++;
		}
		DataHolder.numOfGroup1PassengersLeft = passengers.size();
	}
	
	public void randomBoarding(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		
		//Create passengers
		List<Passenger> passengers = new ArrayList<Passenger>();
		
		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1;
						passengers.add(new Passenger(space, grid, seat, 
								true, 
								speed, 1
								));
						
						
						DataHolder.seatedPassengersInRow[i][e][a] = 0;

						
					}
				}
				
			}
			
		}
		
		SimUtilities.shuffle(passengers, RandomHelper.getUniform());
		int ind = 0;
		Random rd = new Random();
		int totalgrouptime = 0;
		for(Passenger pas : passengers) {
			pas.boardingID = ind;
			pas.timeBeforeBoarding = totalgrouptime;
			context.add(pas);
			totalgrouptime += time;
			ind++;
		}
		
		DataHolder.numOfGroup1PassengersLeft = passengers.size();

		
		
	}
	
	public void groupBoarding(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		
		//Create passengers
		List<Passenger> groupA = new ArrayList<Passenger>();
		List<Passenger> groupB = new ArrayList<Passenger>();
		List<Passenger> groupC = new ArrayList<Passenger>();
		
		
		float obedienceconstant = (float) (3.0/2.0);
		float probofearly = (float) 0.0;
		float proboflate = (float) 0.0;
		float probofdisobedience = (float) 0.0;
		float lugprob = (float) 1.0;
		//Group A
		for (int i = 0; i < DataHolder.firstGroupLimit; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						float speed = (float) 1.0;
						float dec = rd.nextFloat();
						boolean lug = false;
						if (dec <= lugprob) {
							lug = true;
						}
						//Obliging added

						float obdec = rd.nextFloat();
						int timing = 1; //0 = early, 1 = on time, 2 = late
						if (obdec < probofdisobedience*probofearly*obedienceconstant) { //only early
							timing = 0;
						}
						
						//Full obedience now
						timing = 1;
						switch (timing) {
						
						case 0:
							//early
							if (rd.nextBoolean()) {
								groupC.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 1
										));
							}
							else {
								groupB.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 2
										));
							}
							break;
							
						case 1:
							//On time
							groupA.add(new Passenger(space, grid, seat, 
									lug, 
									speed, 1
									));
							
								
							break;
						default: //late, should not happen
							
							break;
							
						}
						DataHolder.seatedPassengersInRow[i][e][a] = 0;

						
					}
				}
				
			}
			
		}
		//Group B
		for (int i = DataHolder.firstGroupLimit; i < DataHolder.secondGroupLimit; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						float dec = rd.nextFloat();
						boolean lug = false;
						if (dec <= lugprob) {
							lug = true;
						}
						float speed = (float) 1;
						
	//Obliging added
						float obdec = rd.nextFloat();
						int timing = 1; //0 = early, 1 = on time, 2 = late
						if (obdec < probofdisobedience*probofearly*obedienceconstant) { // early
							timing = 0;
						}
						else if (obdec < probofdisobedience*obedienceconstant) {
							timing = 2; //late
						}
						//Full obedience
						timing = 1;
						//System.out.println(timing);
						switch (timing) {
						
							case 0:
								//early
								groupC.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 1
										));
								
								
								break;
								
							case 1:
								//On time
								groupB.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 2
										));
								
									
								break;
							default: //late
								groupA.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 3
										));
								break;
							
						}
					DataHolder.seatedPassengersInRow[i][e][a] = 0;
		
					}
				}
				
			}
			
		}
		//Group C
		for (int i = DataHolder.secondGroupLimit; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						float dec = rd.nextFloat();
						boolean lug = false;
						if (dec <= lugprob) {
							lug = true;
						}
						float speed = (float) 1;
						float obdec = rd.nextFloat();
						int timing = 1; //0 = early, 1 = on time, 2 = late
						if (obdec < probofdisobedience*proboflate*obedienceconstant) { //only late
							timing = 2;
						}
						//Full obedience
						timing = 1;
						switch (timing) {
						
							case 0:
								//should not happen
								break;
								
							case 1:
								//On time
								groupC.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 3
										));
								//System.out.println(groupC.size());
									
								break;
							default: //late
								if (rd.nextBoolean()) {
									groupA.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 3
											));
								}
								else {
									groupB.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 2
											));
								}
								break;
							
						}
						
						
						DataHolder.seatedPassengersInRow[i][e][a] = 0;

						
					}
				}
				
			}
		
		}
		SimUtilities.shuffle(groupA, RandomHelper.getUniform());
		
		Random rd = new Random();
		int totalgrouptime = 0;
		int ind = 0;
		for(Passenger pas : groupA) {
			pas.boardingID = ind;
			pas.timeBeforeBoarding = totalgrouptime;
			ind += 1;
			context.add(pas);
			totalgrouptime += time;
			
		}
		
		SimUtilities.shuffle(groupB, RandomHelper.getUniform());
		
		//totalgrouptime = 0;
		
		for(Passenger pas : groupB) {
			pas.boardingID = ind;
			pas.timeBeforeBoarding = totalgrouptime;
			ind += 1;
			context.add(pas);
			totalgrouptime += time;

		}
		
		SimUtilities.shuffle(groupC, RandomHelper.getUniform());
		
		//totalgrouptime = 0;
		for(Passenger pas : groupC) {
			pas.boardingID = ind;
			ind += 1;
			pas.timeBeforeBoarding = totalgrouptime;
			context.add(pas);
			totalgrouptime += time;
		}
		
		
		DataHolder.numOfGroup1PassengersLeft = groupA.size();
		DataHolder.numOfGroup2PassengersLeft = groupB.size();
		DataHolder.numOfGroup3PassengersLeft = groupC.size();
		

			
		
	}
	
	public void seatBoarding(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
	//Create passengers
		List<Passenger> groupW = new ArrayList<Passenger>();
		List<Passenger> groupM = new ArrayList<Passenger>();
		List<Passenger> groupA = new ArrayList<Passenger>();
		
		//Random factors
		float obedienceconstant = (float) (3.0/2.0);
		float probofearly = (float) 0.0;
		float proboflate = (float) 0.0;
		float probofdisobedience = (float) 0.0;
		float lugprob = (float) 1.0;
		
		//WMA
		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i == numberOfRows - 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {numberOfRows - 1 - i,e,a};
						Random rd = new Random();
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1.0;
						boolean lug = false;
						float dec = rd.nextFloat();
						if (dec <= lugprob) {
							lug = true;
						}
						
						switch (a) {
						case 0: 
							//Disobedience, Aisle
							float obdec = rd.nextFloat();
							int timing = 1; //0 = early, 1 = on time, 2 = late
							if (obdec < probofdisobedience*probofearly*obedienceconstant) { //only early
								timing = 0;
							}
							//System.out.println(timing);
							switch (timing) {
							
							case 0:
								//early
								if (rd.nextBoolean()) {
									groupW.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 1
											));
								}
								else {
									groupM.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 2
											));
								}
								break;
								
							case 1:
								//On time
								groupA.add(new Passenger(space, grid, seat, 
										lug, 
										speed, 3
										));
										break;
									
							default: //late, should not happen
								
								break;
								
							}
							break;
						case 1: 
							//Disobedience, Middle
							obdec = rd.nextFloat();
							timing = 1; //0 = early, 1 = on time, 2 = late
							if (obdec < probofdisobedience*probofearly*obedienceconstant) { // early
								timing = 0;
							}
							else if (obdec < probofdisobedience*obedienceconstant) {
								timing = 2; //late
							}
							switch (timing) {
							
								case 0:
									//early
									groupW.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 1
											));
									
									
									break;
									
								case 1:
									//On time
									groupM.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 2
											));
											break;
									
										
								default: //late
									groupA.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 3
											));
									break;
								
							}
							
							
							break;
						default: 
							//Disobedience, Window
							obdec = rd.nextFloat();
							timing = 1; //0 = early, 1 = on time, 2 = late
							if (obdec < probofdisobedience*proboflate*obedienceconstant) { //only late
								timing = 2;
							}
							switch (timing) {
							
								case 0:
									//should not happen
									break;
									
								case 1:
									//On time
									groupW.add(new Passenger(space, grid, seat, 
											lug, 
											speed, 1
											));
										
									break;
								default: //late
									if (rd.nextBoolean()) {
										groupA.add(new Passenger(space, grid, seat, 
												lug, 
												speed, 3
												));
									}
									else {
										groupM.add(new Passenger(space, grid, seat, 
												lug, 
												speed, 2
												));
									}
									break;
								
							}
							
							break;
						}
						
						DataHolder.seatedPassengersInRow[numberOfRows - 1 - i][e][a] = 0;

						
					}
				}
				
			}
			
		}
		
		//SimUtilities.shuffle(groupW, RandomHelper.getUniform());
		
		int ind = 0;
		//int timeBetweenGroups = groupA.size()*5;
		Random rd = new Random();
		int totalgrouptime = 0;
		
		for(Passenger pas : groupW) {
			pas.boardingID = ind;
			pas.timeBeforeBoarding = totalgrouptime;
			context.add(pas);
			totalgrouptime += time;
			ind++;
		}
		
		//SimUtilities.shuffle(groupM, RandomHelper.getUniform());
		
		for(Passenger pas : groupM) {
			pas.timeBeforeBoarding = totalgrouptime;
			pas.boardingID = ind;
			context.add(pas);
			totalgrouptime += time;

			ind++;
		}
		
		//SimUtilities.shuffle(groupA, RandomHelper.getUniform());
		
		for(Passenger pas : groupA) {
			pas.timeBeforeBoarding = totalgrouptime;
			pas.boardingID = ind;
			context.add(pas);
			totalgrouptime += time;

			ind++;
		}
		DataHolder.numOfGroup1PassengersLeft = groupW.size();
		DataHolder.numOfGroup2PassengersLeft = groupM.size();
		DataHolder.numOfGroup3PassengersLeft = groupA.size();
		
		


	}
	
	
	public void slowFastBoarding(Context<Object> context, ContinuousSpace space, Grid grid) {
		
		List<Passenger> groupA = new ArrayList<Passenger>();
		List<Passenger> groupB = new ArrayList<Passenger>();
		List<Passenger> groupC = new ArrayList<Passenger>();		
		
		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						Passenger passenger = new Passenger(space, grid, seat, 
								rd.nextBoolean(), 
								speed, 1);
								
						if (passenger.luggageTime < 10) {
							passenger.group = 1;
							groupA.add(passenger);
						}
						else if (passenger.luggageTime < 20) {
							passenger.group = 2;
							groupB.add(passenger);
						}
						else {
							passenger.group = 3;
							groupC.add(passenger);
						}
						
						
						
						DataHolder.seatedPassengersInRow[i][e][a] = 0;

						
					}
				}
				
			}
			
		}
		
		
		SimUtilities.shuffle(groupA, RandomHelper.getUniform());
		
		int ind = 0;
		//int timeBetweenGroups = groupA.size()*5;
		Random rd = new Random();
		int totalAtime = 0;
		int totalMtime = 0;
		int totalWtime = 0;
		for(Passenger pas : groupA) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalAtime;
			context.add(pas);
			totalAtime += time;
			ind++;
		}
		
		SimUtilities.shuffle(groupB, RandomHelper.getUniform());
		
		ind = 0;
		for(Passenger pas : groupB) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalMtime;
			context.add(pas);
			totalMtime += time;

			ind++;
		}
		
		SimUtilities.shuffle(groupC, RandomHelper.getUniform());
		
		ind = 0;
		for(Passenger pas : groupC) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalWtime;
			context.add(pas);
			totalWtime += time;

			ind++;
		}
		DataHolder.numOfGroup1PassengersLeft = groupA.size();
		DataHolder.numOfGroup2PassengersLeft = groupB.size();
		DataHolder.numOfGroup3PassengersLeft = groupC.size();
		
		System.out.println(groupA.size());
		System.out.println(groupB.size());
		System.out.println(groupC.size());

	}
	
	public void steffenBoarding(Context<Object> context, ContinuousSpace space, Grid grid, int time, int entropy) {

		
		List<Passenger> groupLWE = new ArrayList<Passenger>();
		List<Passenger> groupLWO = new ArrayList<Passenger>();
		List<Passenger> groupLME = new ArrayList<Passenger>();	
		List<Passenger> groupLMO = new ArrayList<Passenger>();
		List<Passenger> groupLAE = new ArrayList<Passenger>();
		List<Passenger> groupLAO = new ArrayList<Passenger>();
		List<Passenger> groupRWE = new ArrayList<Passenger>();
		List<Passenger> groupRWO = new ArrayList<Passenger>();
		List<Passenger> groupRME = new ArrayList<Passenger>();	
		List<Passenger> groupRMO = new ArrayList<Passenger>();
		List<Passenger> groupRAE = new ArrayList<Passenger>();
		List<Passenger> groupRAO = new ArrayList<Passenger>();	
		
		//Even
		for (int i = 0; i < numberOfRows; i+=2) {
			for(int e = 0; e < 2; e++) {
				if (!(i == numberOfRows - 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						
						int seat[] = {numberOfRows - 1 - i,e,a};
						Random rd = new Random();
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1.0;
						switch(a) {
						case 0:
							if(e == 0) {
								groupRAE.add(new Passenger(space, grid, seat, 
										true, 
										speed, 9));
							}
							else {
								groupLAE.add(new Passenger(space, grid, seat, 
										true, 
										speed, 10));
							}
							break;
						case 1:
							if(e == 0) {
								groupRME.add(new Passenger(space, grid, seat, 
										true, 
										speed, 5)); 
							}
							else {
								groupLME.add(new Passenger(space, grid, seat, 
										true, 
										speed, 6)); 
							}
							
							break;
						default:
							if(e == 0) {
								groupRWE.add(new Passenger(space, grid, seat, 
										true, 
										speed, 1));
							}
							else {
								groupLWE.add(new Passenger(space, grid, seat, 
										true, 
										speed, 2));
							}
							
							break;
						}
					
						
						
						
						
						DataHolder.seatedPassengersInRow[numberOfRows - 1 - i][e][a] = 0;

						
					}
				}
				
				
			}
			
		}
		//Odd
		for (int i = 1; i < numberOfRows; i+=2) {
			for(int e = 0; e < 2; e++) {
				if (!(i < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						
						int seat[] = {numberOfRows - 1 - i,e,a};
						Random rd = new Random();
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1.0;
						switch(a) {
						case 0:
							if(e == 0) {
								groupRAO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 11));
							}
							else {
								groupLAO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 12));
							}
							break;
						case 1:
							if(e == 0) {
								groupRMO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 7)); 
							}
							else {
								groupLMO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 8)); 
							}
							
							break;
						default:
							if(e == 0) {
								groupRWO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 3));
							}
							else {
								groupLWO.add(new Passenger(space, grid, seat, 
										true, 
										speed, 4));
							}
							
							break;
						}
					
					
					
					
					
					DataHolder.seatedPassengersInRow[numberOfRows - 1 - i][e][a] = 0;

					
					}
				}
				
			}
			
		}
		
		/*SimUtilities.shuffle(groupRWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAO, RandomHelper.getUniform());
*/
		List<Passenger> grouplist = new ArrayList<Passenger>();
		grouplist.addAll(groupRWE);
		grouplist.addAll(groupLWE);
		grouplist.addAll(groupRWO);
		grouplist.addAll(groupLWO);
		grouplist.addAll(groupRME);
		grouplist.addAll(groupLME);
		grouplist.addAll(groupRMO);
		grouplist.addAll(groupLMO);
		grouplist.addAll(groupRAE);
		grouplist.addAll(groupLAE);
		grouplist.addAll(groupRAO);
		grouplist.addAll(groupLAO);

		// Switch passengers
		Random rd = new Random();

		for(int idx = 0; idx < entropy; idx++) {
			int i_1 = rd.nextInt(grouplist.size());
			int i_2 = rd.nextInt(grouplist.size());
			Passenger p1 = grouplist.get(i_1);
			grouplist.set(i_1, grouplist.get(i_2));
			grouplist.set(i_2,  p1);
		}
		
		int ind = 0;
		int totalgrouptime = 0;
		//for(List<Passenger> fwlist : grouplist) {
			
		for(Passenger fwpas : grouplist) {
			fwpas.boardingID = ind;
			fwpas.timeBeforeBoarding = totalgrouptime;
			ind += 1;
			totalgrouptime += time;
			context.add(fwpas);
		}
		//}
		
		DataHolder.numOfGroup1PassengersLeft = groupRWE.size();
		DataHolder.numOfGroup2PassengersLeft = groupLWE.size();
		DataHolder.numOfGroup3PassengersLeft = groupRWO.size();
		DataHolder.numOfGroup4PassengersLeft = groupLWO.size();
		DataHolder.numOfGroup5PassengersLeft = groupRME.size();
		DataHolder.numOfGroup6PassengersLeft = groupLME.size();
		DataHolder.numOfGroup7PassengersLeft = groupRMO.size();
		DataHolder.numOfGroup8PassengersLeft = groupLMO.size();
		DataHolder.numOfGroup9PassengersLeft = groupRAE.size();
		DataHolder.numOfGroup10PassengersLeft = groupLAE.size();
		DataHolder.numOfGroup11PassengersLeft = groupRAO.size();
		DataHolder.numOfGroup12PassengersLeft = groupLAO.size();
		
		/*System.out.println(DataHolder.numOfGroup1PassengersLeft);
		System.out.println(DataHolder.numOfGroup2PassengersLeft);
		System.out.println(DataHolder.numOfGroup3PassengersLeft);
		System.out.println(DataHolder.numOfGroup4PassengersLeft);
		System.out.println(DataHolder.numOfGroup5PassengersLeft);
		System.out.println(DataHolder.numOfGroup6PassengersLeft);
		System.out.println(DataHolder.numOfGroup7PassengersLeft);
		System.out.println(DataHolder.numOfGroup8PassengersLeft);
		System.out.println(DataHolder.numOfGroup9PassengersLeft);
		System.out.println(DataHolder.numOfGroup10PassengersLeft);
		System.out.println(DataHolder.numOfGroup11PassengersLeft);
		System.out.println(DataHolder.numOfGroup12PassengersLeft);*/
		
		
	}
	
	
	public void p4rBoarding(Context<Object> context, ContinuousSpace space, Grid grid) {
		
		List<Passenger> PW = new ArrayList<Passenger>();
		List<Passenger> fourW = new ArrayList<Passenger>();
		List<Passenger> RW = new ArrayList<Passenger>();
		List<Passenger> PM = new ArrayList<Passenger>();
		List<Passenger> fourM = new ArrayList<Passenger>();
		List<Passenger> RM = new ArrayList<Passenger>();
		List<Passenger> PA = new ArrayList<Passenger>();
		List<Passenger> fourA = new ArrayList<Passenger>();
		List<Passenger> RA = new ArrayList<Passenger>();

		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				if (!(i == numberOfRows - 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {numberOfRows - 1 - i,e,a};
						Random rd = new Random();
						
						//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						float speed = (float) 1.0;

						Passenger pas = new Passenger(space, grid, seat, true, speed, 1);
						boolean prime = IntStream.of(DataHolder.primes).anyMatch(x -> x == seat[0]);
						
						if (prime) {
							switch (seat[2]) {
							case 2:
								pas.group = 1;

								PW.add(pas);

								break;
							case 1:
								pas.group = 4;

								PM.add(pas);

								break;
							default:
								pas.group = 7;

								PA.add(pas);

								break;
							}
						}
						else if (seat[0] % 4 == 0) {
							switch (seat[2]) {
							case 2:
								pas.group = 2;

								fourW.add(pas);

								break;
							case 1:
								pas.group = 5;

								fourM.add(pas);

								break;
							default:
								pas.group = 8;

								fourA.add(pas);

								break;
							}
						}
						else {
							switch (seat[2]) {
							case 2:
								pas.group = 3;

								RW.add(pas);

								break;
							case 1:
								pas.group = 6;

								RM.add(pas);

								break;
							default:
								pas.group = 9;

								RA.add(pas);

								break;
							};
						}
						DataHolder.seatedPassengersInRow[numberOfRows - 1 - i][e][a] = 0;

						
					}
				}
				
			}
			
		}
		List<List<Passenger>> grouplist = new ArrayList<List<Passenger>>();
		grouplist.add(PW);
		grouplist.add(fourW);
		grouplist.add(RW);
		grouplist.add(PM);
		grouplist.add(fourM);
		grouplist.add(RM);
		grouplist.add(PA);
		grouplist.add(fourA);
		grouplist.add(RA);
		int ind = 0;
		int totalgrouptime = 0;

		for (List<Passenger> list : grouplist) {
			//SimUtilities.shuffle(list, RandomHelper.getUniform());
			//Random rd = new Random();
			
			for(Passenger pas : list) {
				int time = 3;
				pas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				pas.boardingID = ind;
				context.add(pas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup1PassengersLeft = PW.size();
		DataHolder.numOfGroup2PassengersLeft = fourW.size();
		DataHolder.numOfGroup3PassengersLeft = RW.size();
		DataHolder.numOfGroup4PassengersLeft = PM.size();
		DataHolder.numOfGroup5PassengersLeft = fourM.size();
		DataHolder.numOfGroup6PassengersLeft = RM.size();
		DataHolder.numOfGroup7PassengersLeft = PA.size();
		DataHolder.numOfGroup8PassengersLeft = fourA.size();
		DataHolder.numOfGroup9PassengersLeft = RA.size();
		
		System.out.println(DataHolder.numOfGroup1PassengersLeft);
		System.out.println(DataHolder.numOfGroup2PassengersLeft);
		System.out.println(DataHolder.numOfGroup3PassengersLeft);
		System.out.println(DataHolder.numOfGroup4PassengersLeft);
		System.out.println(DataHolder.numOfGroup5PassengersLeft);
		System.out.println(DataHolder.numOfGroup6PassengersLeft);
		System.out.println(DataHolder.numOfGroup7PassengersLeft);
		System.out.println(DataHolder.numOfGroup8PassengersLeft);
		System.out.println(DataHolder.numOfGroup9PassengersLeft);
		System.out.println(DataHolder.numOfGroup10PassengersLeft);
		System.out.println(DataHolder.numOfGroup11PassengersLeft);
		System.out.println(DataHolder.numOfGroup12PassengersLeft);
		
		
		

	}
	
	public void randomDisembarking(Context<Object> context, ContinuousSpace space, Grid grid) {
		
		List<Passenger> passengers = new ArrayList<Passenger>();
		
		for (int i = 0; i < numberOfRows; i++) {
			for(int e = 0; e < 2; e++) {
				boolean skip = false;
				if (i == 0 && e == 0) {
					skip = true;
				}
				if (!skip) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						DisembarkingPassenger dpas = new DisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, 1);
						context.add(dpas);
						int val = 1;
						if (seat[1] == 1) {
							val = -1;
						}
						NdPoint pt = new NdPoint(0.4+seat[0]+1, 3.4+(seat[2]+1)*val);
						space.moveTo(dpas, pt.getX(), pt.getY());
						grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY());
						
						DataHolder.seatedPassengersInRow[i][e][a] = 1;

						
					}
				}
				
			}
			
		}
		

		SimUtilities.shuffle(passengers, RandomHelper.getUniform());
		int ind = 0;
		for(Passenger pas : passengers) {
			pas.timeBeforeBoarding = ind*5;
			context.add(pas);
			ind++;
		}
		
		
		
	}
	
public void groupBoardingScalable(Context<Object> context, ContinuousSpace space, Grid grid, int time, int groups) {
		
		//Create passengers
		List<List < Passenger> > listOfGroupsOfPassengers = new ArrayList< List < Passenger> >();
		for(int idx = 0; idx < groups; idx++) {
			listOfGroupsOfPassengers.add(new ArrayList<Passenger>());
		}
		//groups = 3
		//numOfRowsFW = 14
		//numOfRowsInGroup = 16/3 = 5
		// Group 1: 0,1,2,3,4
		// Group 2: 5,6,7,8,9
		// Group 3: 10,11,12,13,14
		// Group 4: 15,16
		int numOfRowsInGroup = (int) Math.ceil((double) (DataHolder.numberOfRows)/groups);
		for (int i = 0; i < DataHolder.numberOfRows; i++) {
			int row = DataHolder.numberOfRows - 1 - i;
			for(int e = 0; e < 2; e++) {
				if (!(row < 1 && e == 0)) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int group = groups - 1 - row/numOfRowsInGroup;
						if(group < 0) {
							group = 0;
						}
						int seat[] = {row,e,a};
						Random rd = new Random();
						float speed = (float) 1;
						Passenger pas = new Passenger(space, grid, seat, 
								true, 
								speed, 1);
						
						listOfGroupsOfPassengers.get(group).add(pas);
						
						DataHolder.seatedPassengersInRow[row][e][a] = 0;

						
					}
				}
				
			}
			
		}
		int totalgrouptime = 0;
		int ind = 0;
		for(List<Passenger> pass : listOfGroupsOfPassengers) {
			SimUtilities.shuffle(pass, RandomHelper.getUniform());
			for(Passenger pas : pass) {
				pas.boardingID = ind;
				pas.timeBeforeBoarding = totalgrouptime;
				ind += 1;
				context.add(pas);
				totalgrouptime += time;
				
			}
		}
		
		
			
		
	}
	
	public void frontToBackDisembarking(Context<Object> context, ContinuousSpace space, Grid grid) {
		List<DisembarkingPassenger> groupA = new ArrayList<DisembarkingPassenger>();
		List<DisembarkingPassenger> groupB = new ArrayList<DisembarkingPassenger>();
		List<DisembarkingPassenger> groupC = new ArrayList<DisembarkingPassenger>();
		
		for (int i = 0; i < numberOfRows; i++) {
			
			for(int e = 0; e < 2; e++) { //Adjust for asymmetric plane design (Narrow Flight)
				boolean skip = false;
				if (i == 0 && e == 0) {
					skip = true;
				}
				if (!skip) {
					for (int a = 0; a < numberOfSeatsInRow; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						//context.add(dpas);
						if (i<DataHolder.firstGroupLimit) {
							DisembarkingPassenger dpas = new DisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, 1);

							dpas.group = 1;
							groupA.add(dpas);
						}
						else if (i<DataHolder.secondGroupLimit) {
							DisembarkingPassenger dpas = new DisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, 2);

							groupB.add(dpas);
						}
						else {
							DisembarkingPassenger dpas = new DisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, 3);

							groupC.add(dpas);
						}
						
						
						
						DataHolder.seatedPassengersInRow[i][e][a] = 1;

						
					}
				}
				
			}
			
		}
		
		SimUtilities.shuffle(groupA, RandomHelper.getUniform());
		SimUtilities.shuffle(groupB, RandomHelper.getUniform());
		SimUtilities.shuffle(groupC, RandomHelper.getUniform());
		
		Random rd = new Random();
		int ind = 0;
		for(DisembarkingPassenger dpas : groupA) {
			
			context.add(dpas);
			int val = 1;
			if (dpas.seatNumber[1] == 1) {
				val = -1;
			}
			NdPoint pt = new NdPoint(0.4+dpas.seatNumber[0]+1, 3.4+(dpas.seatNumber[2]+1)*val);
			space.moveTo(dpas, pt.getX(), pt.getY());
			grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY());
			System.out.println(dpas.group);

			//totalBtime += time;

		}
		for(DisembarkingPassenger dpas : groupB) {
			
			context.add(dpas);
			int val = 1;
			if (dpas.seatNumber[1] == 1) {
				val = -1;
			}
			NdPoint pt = new NdPoint(0.4+dpas.seatNumber[0]+1, 3.4+(dpas.seatNumber[2]+1)*val);
			space.moveTo(dpas, pt.getX(), pt.getY());
			grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY());
			System.out.println(dpas.group);
			//totalBtime += time;

		}
		for(DisembarkingPassenger dpas : groupC) {
			
			context.add(dpas);
			int val = 1;
			if (dpas.seatNumber[1] == 1) {
				val = -1;
			}
			NdPoint pt = new NdPoint(0.4+dpas.seatNumber[0]+1, 3.4+(dpas.seatNumber[2]+1)*val);
			space.moveTo(dpas, pt.getX(), pt.getY());
			grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY());
			System.out.println(dpas.group);

			//totalBtime += time;

		}
		
		DataHolder.numOfGroup1PassengersLeft = groupA.size();
		DataHolder.numOfGroup2PassengersLeft = groupB.size();
		DataHolder.numOfGroup3PassengersLeft = groupC.size();


		
		
	}
	
	public void randomBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		//Create passengers
		List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = 1;
								fwpassengers.add(new FlyingWingPassenger(space, grid, seat, 
										true, 
										speed, 1, s
										));
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengers, RandomHelper.getUniform());
		int ind = 0;
		int totalgrouptime = 0;
		Random rd = new Random();
		for(FlyingWingPassenger pas : fwpassengers) {
			
			pas.timeBeforeBoarding = totalgrouptime;
			pas.boardingID = ind;
			totalgrouptime += time;
			context.add(pas);
			ind++;
		}
	}
	
	
	public void groupBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		List<FlyingWingPassenger> fwpassengersA = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersB = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersC = new ArrayList<FlyingWingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = (float) 1;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, speed, 1, s);
								if(i<DataHolder.firstGroupLimitFlyingWing) {
									fwpas.group = 3;
									fwpassengersA.add(fwpas);
								}
								else if(i<DataHolder.secondGroupLimitFlyingWing) {
									fwpas.group = 2;
									fwpassengersB.add(fwpas);

								}
								else {
									fwpas.group = 1;
									fwpassengersC.add(fwpas);

								}
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersB, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersC, RandomHelper.getUniform());

		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengersA);
		grouplist.add(fwpassengersB);
		grouplist.add(fwpassengersC);

		
		int ind = 0;
		Random rd = new Random();
		int totalgrouptime = 0;

		for(List<FlyingWingPassenger> fwlist : grouplist) {
			for(FlyingWingPassenger fwpas : fwlist) {
				fwpas.timeBeforeBoarding = totalgrouptime;
				fwpas.boardingID = ind;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup3PassengersLeft = fwpassengersA.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengersB.size();
		DataHolder.numOfGroup1PassengersLeft = fwpassengersC.size();

	}
	
	
	public void randomParallelSeatBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		List<FlyingWingPassenger> fwpassengersW = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersM = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersA = new ArrayList<FlyingWingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								int row = numberOfSeatsInRow - 1 - a;
								int seat[] = {i,e,row};
								
								float speed = (float) 1;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, speed, 1, s);
								if(a == 0) {
									fwpas.group = 3;
									fwpassengersA.add(fwpas);
								}
								else if(a==1) {
									fwpas.group = 2;
									fwpassengersM.add(fwpas);

								}
								else {
									fwpas.group = 1;
									fwpassengersW.add(fwpas);

								}
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][row] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersM, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersW, RandomHelper.getUniform());

		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengersA);
		grouplist.add(fwpassengersM);
		grouplist.add(fwpassengersW);

		
		int ind = 0;
		Random rd = new Random();
		int totalgrouptime = 0;

		for(List<FlyingWingPassenger> fwlist : grouplist) {
			for(FlyingWingPassenger fwpas : fwlist) {
				fwpas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				fwpas.boardingID = ind;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup3PassengersLeft = fwpassengersA.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengersM.size();
		DataHolder.numOfGroup1PassengersLeft = fwpassengersW.size();

	}
	/*
	public void steffenBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid) {
		List<FlyingWingPassenger> fwpassengersWE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersWO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersME = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersMO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersAE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersAO = new ArrayList<FlyingWingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = (float) (0.5 + rd.nextFloat()*(0.5));
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										rd.nextBoolean(), speed, 1, s);
								if(a == 0) {
									fwpas.group = 3;
									fwpassengersA.add(fwpas);
								}
								else if(a==1) {
									fwpas.group = 2;
									fwpassengersM.add(fwpas);

								}
								else {
									fwpas.group = 1;
									fwpassengersW.add(fwpas);

								}
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersM, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersW, RandomHelper.getUniform());

		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengersA);
		grouplist.add(fwpassengersM);
		grouplist.add(fwpassengersW);

		
		int ind = 0;
		Random rd = new Random();
		
		for(List<FlyingWingPassenger> fwlist : grouplist) {
			int totalgrouptime = 0;
			for(FlyingWingPassenger fwpas : fwlist) {
				int time = 1+rd.nextInt(4);
				fwpas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup3PassengersLeft = fwpassengersA.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengersM.size();
		DataHolder.numOfGroup1PassengersLeft = fwpassengersW.size();

	}*/
	
	public void steffenBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time, float stoppingPenalty, int entropy) {

		
		List<FlyingWingPassenger> groupLWE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupLWO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupLME = new ArrayList<FlyingWingPassenger>();	
		List<FlyingWingPassenger> groupLMO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupLAE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupLAO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupRWE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupRWO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupRME = new ArrayList<FlyingWingPassenger>();	
		List<FlyingWingPassenger> groupRMO = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupRAE = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> groupRAO = new ArrayList<FlyingWingPassenger>();	
		
		//Even
		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i+=2) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && DataHolder.numberOfRowsFlyingWing - 1 - i < 3)) {
						skip = true;
					}
					else if((s==0 && e == 0 && DataHolder.numberOfRowsFlyingWing - 1 - i < 3)){
						skip = true;
					}
					if (!skip) {
						for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
							int seat[] = {DataHolder.numberOfRowsFlyingWing - 1 - i,e,a};
							Random rd = new Random();
							//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							float speed = (float) 1.0;
							switch(a) {
							case 0:
								if(e == 0) {
									groupRAE.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 9, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								else {
									groupLAE.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 10, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								break;
							case 1:
								if(e == 0) {
									groupRME.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 5, DataHolder.numberOfSegmentsFlyingWing-1-s)); 
								}
								else {
									groupLME.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 6, DataHolder.numberOfSegmentsFlyingWing-1-s)); 
								}
								
								break;
							default:
								if(e == 0) {
									groupRWE.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								else {
									groupLWE.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 2, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								
								break;
							}
							
							
							
							DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][DataHolder.numberOfRowsFlyingWing - 1 - i][e][a] = 0;

							
						}
					}
					
				}
				
			}
			//Odd
			for (int i = 1; i < DataHolder.numberOfRowsFlyingWing; i+=2) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && DataHolder.numberOfRowsFlyingWing - 1 - i < 3)) {
						skip = true;
					}
					else if((s==0 && e == 0 && DataHolder.numberOfRowsFlyingWing - 1 - i < 3)){
						skip = true;
					}
					if (!skip) {
						for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
							int seat[] = {DataHolder.numberOfRowsFlyingWing - 1 - i,e,a};
							Random rd = new Random();
							//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							float speed = (float) 1.0;

							switch(a) {
							case 0:
								if(e == 0) {
									groupRAO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 11, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								else {
									groupLAO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 12, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								break;
							case 1:
								if(e == 0) {
									groupRMO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 7, DataHolder.numberOfSegmentsFlyingWing-1-s)); 
								}
								else {
									groupLMO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 8, DataHolder.numberOfSegmentsFlyingWing-1-s)); 
								}
								
								break;
							default:
								if(e == 0) {
									groupRWO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 3, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								else {
									groupLWO.add(new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 4, DataHolder.numberOfSegmentsFlyingWing-1-s));
								}
								
								break;
							}
							
							
							
							DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][DataHolder.numberOfRowsFlyingWing - 1 - i][e][a] = 0;

							
						}
					}
					
				}
				
			}
		}
		
		
		
		/*SimUtilities.shuffle(groupLWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAO, RandomHelper.getUniform());*/

		List<FlyingWingPassenger>grouplist = new ArrayList<FlyingWingPassenger>();
		grouplist.addAll(groupRWE);
		grouplist.addAll(groupLWE);
		grouplist.addAll(groupRWO);
		grouplist.addAll(groupLWO);
		grouplist.addAll(groupRME);
		grouplist.addAll(groupLME);
		grouplist.addAll(groupRMO);
		grouplist.addAll(groupLMO);
		grouplist.addAll(groupRAE);
		grouplist.addAll(groupLAE);
		grouplist.addAll(groupRAO);
		grouplist.addAll(groupLAO);

		
		// Switch people around
		Random rd = new Random();

		for(int idx = 0; idx < entropy; idx++) {
			int i_1 = rd.nextInt(grouplist.size());
			int i_2 = rd.nextInt(grouplist.size());
			FlyingWingPassenger fwp1 = grouplist.get(i_1);
			grouplist.set(i_1, grouplist.get(i_2));
			grouplist.set(i_2,  fwp1);
		}
		
		int ind = 0;
		int totalgrouptime = 0;
		//for(List<FlyingWingPassenger> fwlist : grouplist) {
			
		for(FlyingWingPassenger fwpas : grouplist) {
			fwpas.boardingID = ind;
			fwpas.timeBeforeBoarding = totalgrouptime;
			fwpas.startingPenalty = stoppingPenalty;
			ind += 1;
			totalgrouptime += time;
			context.add(fwpas);
		}
		//}
		
		DataHolder.numOfGroup1PassengersLeft = groupRWE.size();
		DataHolder.numOfGroup2PassengersLeft = groupLWE.size();
		DataHolder.numOfGroup3PassengersLeft = groupRWO.size();
		DataHolder.numOfGroup4PassengersLeft = groupLWO.size();
		DataHolder.numOfGroup5PassengersLeft = groupRME.size();
		DataHolder.numOfGroup6PassengersLeft = groupLME.size();
		DataHolder.numOfGroup7PassengersLeft = groupRMO.size();
		DataHolder.numOfGroup8PassengersLeft = groupLMO.size();
		DataHolder.numOfGroup9PassengersLeft = groupRAE.size();
		DataHolder.numOfGroup10PassengersLeft = groupLAE.size();
		DataHolder.numOfGroup11PassengersLeft = groupRAO.size();
		DataHolder.numOfGroup12PassengersLeft = groupLAO.size();
		
		System.out.println(DataHolder.numOfGroup1PassengersLeft);
		System.out.println(DataHolder.numOfGroup2PassengersLeft);
		System.out.println(DataHolder.numOfGroup3PassengersLeft);
		System.out.println(DataHolder.numOfGroup4PassengersLeft);
		System.out.println(DataHolder.numOfGroup5PassengersLeft);
		System.out.println(DataHolder.numOfGroup6PassengersLeft);
		System.out.println(DataHolder.numOfGroup7PassengersLeft);
		System.out.println(DataHolder.numOfGroup8PassengersLeft);
		System.out.println(DataHolder.numOfGroup9PassengersLeft);
		System.out.println(DataHolder.numOfGroup10PassengersLeft);
		System.out.println(DataHolder.numOfGroup11PassengersLeft);
		System.out.println(DataHolder.numOfGroup12PassengersLeft);
		
		
	}
	public void sequentialSteffenBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time, float stoppingPenalty) {

		//Create passengers
		List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
		
		int ind = 0;
		int totalGroupTime = 0;
		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
			List<FlyingWingPassenger> fwpass =  new ArrayList<FlyingWingPassenger>();

				for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
					for(int e = 0; e < 2; e++) {
				
						for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i+=2) {

				
					
					
					
								boolean skip = false;
								int row = DataHolder.numberOfRowsFlyingWing - 1 - i;
								if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
									skip = true;
								}
								else if((s==0 && e == 0 && row < 3)){
									skip = true;
								}
								if(!skip) {
									int seat[] = {row,e, DataHolder.numberOfSeatsInRowFlyingWing-1-a};
									Random rd = new Random();
									
									//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
									float speed = (float) 1.0;
									FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
											);
									fwpas.startingPenalty = stoppingPenalty;
									fwpass.add(fwpas);
									
									
									DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][DataHolder.numberOfSeatsInRow-1-a] = 0;

							
						}
					}
					for (int i = 1; i < DataHolder.numberOfRowsFlyingWing; i+=2) {

							
							
							
							
						boolean skip = false;
						int row = DataHolder.numberOfRowsFlyingWing - 1 - i;
						if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
							skip = true;
						}
						else if((s==0 && e == 0 && row < 3)){
							skip = true;
						}
						if(!skip) {
							int seat[] = {row,e, DataHolder.numberOfSeatsInRowFlyingWing-1-a};
							Random rd = new Random();
							
							//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							float speed = (float) 1.0;
							FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
									true, 
									speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
									);
							fwpas.startingPenalty = stoppingPenalty;
							fwpass.add(fwpas);
							
							
							DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][DataHolder.numberOfSeatsInRow-1-a] = 0;

						
					}
				}
					//SimUtilities.shuffle(fwpass, RandomHelper.getUniform());

					
					
				
				}
			}
				
			for(FlyingWingPassenger fwpas : fwpass) {
				fwpas.timeBeforeBoarding = totalGroupTime;
				fwpas.boardingID = ind;
				totalGroupTime += time;
				context.add(fwpas);
				ind++;
				
			} 
		}
		
		
	}
	
	
	
	public void sequentialRandomBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		List<FlyingWingPassenger> fwpassengers0 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers1 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers2 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers3 = new ArrayList<FlyingWingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = (float) 1;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, speed, 1, s);
								if(s == 0) {
									fwpas.group = 4;
									fwpassengers0.add(fwpas);
								}
								else if(s==1) {
									fwpas.group = 3;
									fwpassengers1.add(fwpas);

								}
								else if(s==2) {
									fwpas.group = 2;
									fwpassengers2.add(fwpas);

								}
								else {
									fwpas.group = 1;
									fwpassengers3.add(fwpas);
								}
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengers0, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers1, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers2, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers3, RandomHelper.getUniform());

		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengers3);
		grouplist.add(fwpassengers2);
		grouplist.add(fwpassengers1);
		grouplist.add(fwpassengers0);

		
		int ind = 0;
		Random rd = new Random();
		int totalgrouptime = 0;

		for(List<FlyingWingPassenger> fwlist : grouplist) {
			for(FlyingWingPassenger fwpas : fwlist) {
				
				fwpas.timeBeforeBoarding = totalgrouptime;
				fwpas.boardingID = ind;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup4PassengersLeft = fwpassengers0.size();
		DataHolder.numOfGroup3PassengersLeft = fwpassengers1.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengers2.size();
		DataHolder.numOfGroup1PassengersLeft = fwpassengers3.size();

	}
	
	public void randomParallelWMARandomBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		List<FlyingWingPassenger> fwpassengers1 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers2 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers3 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers4 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers5 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers6 = new ArrayList<FlyingWingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = (float) 1;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, speed, 1, s);
								
								if(e==0) {
									if(a==2) {
										fwpas.group = 1;
										fwpassengers1.add(fwpas);
									}
									else if(a==1) {
										fwpas.group = 2;
										fwpassengers2.add(fwpas);
									}
									else {
										
										fwpas.group = 3;
										fwpassengers3.add(fwpas);
										
									}
								}
								else {
									if(a==2) {
										fwpas.group = 4;
										fwpassengers4.add(fwpas);
									}
									else if(a==1) {
										fwpas.group = 5;
										fwpassengers5.add(fwpas);
									}
									else {
										
										fwpas.group = 6;
										fwpassengers6.add(fwpas);
										
									}
								}
								
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengers1, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers2, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers3, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers4, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers5, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers6, RandomHelper.getUniform());
		
		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengers1);
		grouplist.add(fwpassengers2);
		grouplist.add(fwpassengers3);
		grouplist.add(fwpassengers4);
		grouplist.add(fwpassengers5);
		grouplist.add(fwpassengers6);

		
		int ind = 0;
		Random rd = new Random();
		int totalgrouptime = 0;

		for(List<FlyingWingPassenger> fwlist : grouplist) {
			for(FlyingWingPassenger fwpas : fwlist) {
				fwpas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				fwpas.boardingID = ind;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup1PassengersLeft = fwpassengers1.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengers2.size();
		DataHolder.numOfGroup3PassengersLeft = fwpassengers3.size();
		DataHolder.numOfGroup4PassengersLeft = fwpassengers4.size();
		DataHolder.numOfGroup5PassengersLeft = fwpassengers5.size();
		DataHolder.numOfGroup6PassengersLeft = fwpassengers6.size();

	}
	
	public void parallelSteffenBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		//Create passengers
		List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
		
		int ind = 0;
		int totalGroupTime = 0;
				
		for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
			for(int e = 0; e < 2; e++) {
				

				
					
				
					for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i+=2) {
							for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
								boolean skip = false;
								int row = DataHolder.numberOfRowsFlyingWing - 1 - i;
								if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
									skip = true;
								}
								else if((s==0 && e == 0 && row < 3)){
									skip = true;
								}
								if(!skip) {
									int seat[] = {row,e, DataHolder.numberOfSeatsInRowFlyingWing-1-a};
									Random rd = new Random();
									
									//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
									float speed = (float) 1.0;
									FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
											true, 
											speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
											);
									fwpassengers.add(fwpas);
									fwpas.boardingID = ind;
									fwpas.timeBeforeBoarding = totalGroupTime;
									totalGroupTime += time;
									context.add(fwpas);
									ind += 1;
									
									DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][DataHolder.numberOfSeatsInRow-1-a] = 0;

							
						}
					}
							
						
					}
					for (int i = 1; i < DataHolder.numberOfRowsFlyingWing; i+=2) {
						for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
							boolean skip = false;
							int row = DataHolder.numberOfRowsFlyingWing - 1 - i;
							if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
								skip = true;
							}
							else if((s==0 && e == 0 && row < 3)){
								skip = true;
							}
							if(!skip) {
								int seat[] = {row,e, DataHolder.numberOfSeatsInRowFlyingWing-1-a};
								Random rd = new Random();
								
								//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
								float speed = (float) 1.0;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, 
										speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
										);
								fwpassengers.add(fwpas);
								fwpas.boardingID = ind;
								fwpas.timeBeforeBoarding = totalGroupTime;
								totalGroupTime += time;
								context.add(fwpas);
								ind += 1;
								
								DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][DataHolder.numberOfSeatsInRow-1-a] = 0;

						
							}
						}	
						
					}
					
			}
		}			
		
		
		/*SimUtilities.shuffle(fwpassengers, RandomHelper.getUniform());
		int ind = 0;
		int totalgrouptime = 0;
		int time = 3;
		Random rd = new Random();
		//Only hard-coded 11 segments here, don't know how to make this variable but is surely possible with Java
		int [] segmentTotalTimes = new int[DataHolder.numberOfSegmentsFlyingWing];
		
		for(FlyingWingPassenger pas : fwpassengers) {
			
			int segment = pas.segment;
			
			
			
			pas.timeBeforeBoarding = segment*time+(segmentTotalTimes[segment])*time*DataHolder.numberOfSegmentsFlyingWing;
			pas.boardingID = segment + segmentTotalTimes[segment]*DataHolder.numberOfSegmentsFlyingWing;
			totalgrouptime += time;
			context.add(pas);
			ind++;
			segmentTotalTimes[segment] += 1;

		}*/
	}
	/*
	public void ULTRASegmentedBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid) {
		List<FlyingWingPassenger> fwpassengers1 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers2 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers3 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers4 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers5 = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengers6 = new ArrayList<FlyingWingPassenger>();

		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengers1);
		grouplist.add(fwpassengers2);
		grouplist.add(fwpassengers3);
		grouplist.add(fwpassengers4);
		grouplist.add(fwpassengers5);
		grouplist.add(fwpassengers6);
		
		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if((s==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
						if(!skip) {
							for (int a = 0; a < numberOfSeatsInRow; a++) {
								
								int seat[] = {i,e,a};
								Random rd = new Random();
								
								float speed = (float) (0.5 + rd.nextFloat()*(0.5));
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										rd.nextBoolean(), speed, 1, s);
								
								if(e==0) {
									if(a==2) {
										fwpas.group = 4-s;
										
										grouplist[4-s].add(fwpas);
									}
									else if(a==1) {
										fwpas.group = 2;
										fwpassengers2.add(fwpas);
									}
									else {
										
										fwpas.group = 3;
										fwpassengers3.add(fwpas);
										
									}
								}
								else {
									if(a==2) {
										fwpas.group = 4;
										fwpassengers4.add(fwpas);
									}
									else if(a==1) {
										fwpas.group = 5;
										fwpassengers5.add(fwpas);
									}
									else {
										
										fwpas.group = 6;
										fwpassengers6.add(fwpas);
										
									}
								}
								
								DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 0;

								
							}
						}
					
				}
			}
			
			
		}
		
		SimUtilities.shuffle(fwpassengers1, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers2, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers3, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers4, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers5, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengers6, RandomHelper.getUniform());
		
		

		
		int ind = 0;
		Random rd = new Random();
		
		for(List<FlyingWingPassenger> fwlist : grouplist) {
			SimUtilities.shuffle(grouplist, RandomHelper.getUniform());

			int totalgrouptime = 0;
			for(FlyingWingPassenger fwpas : fwlist) {
				int time = 1+rd.nextInt(4);
				fwpas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
			}
		}
		
		
		DataHolder.numOfGroup1PassengersLeft = fwpassengers1.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengers2.size();
		DataHolder.numOfGroup3PassengersLeft = fwpassengers3.size();
		DataHolder.numOfGroup4PassengersLeft = fwpassengers4.size();
		DataHolder.numOfGroup5PassengersLeft = fwpassengers5.size();
		DataHolder.numOfGroup6PassengersLeft = fwpassengers6.size();

	}*/
	
	public void parallelFrontToBackInToOut(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		//Create passengers
				List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
				
				int ind = 0;
				int totalGroupTime = 0;
						
				
						
						for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i+=1) {
							for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
								int wma = a;
								for(int e = 0; e < 2; e++) {

									for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
										boolean skip = false;
										int row = i;
										if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
											skip = true;
										}
										else if((s==0 && e == 0 && row < 3)){
											skip = true;
										}
										if(!skip) {
											int seat[] = {row,e, wma};
											Random rd = new Random();
											
											//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
											float speed = (float) 1.0;
											FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
													true, 
													speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
													);
											fwpassengers.add(fwpas);
											
											fwpas.boardingID = ind;
											fwpas.timeBeforeBoarding = totalGroupTime;
											totalGroupTime += time;
											context.add(fwpas);
											ind += 1;
											
											DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][a] = 0;

									
								}
							}
									
								
							}
							
					}
				}		
	}
	
	public void parallelSeatRandomBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {

		//Create passengers
		List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
		
		int ind = 0;
		int totalGroupTime = 0;
		
			for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
				for(int e = 0; e < 2; e++) {
				

					for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {

					
					List<FlyingWingPassenger> fwpass =  new ArrayList<FlyingWingPassenger>();
					
					for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i+=1) {
							boolean skip = false;
							int row = DataHolder.numberOfRowsFlyingWing - 1 - i;
							if((s==DataHolder.numberOfSegmentsFlyingWing-1 && e == 1 && row < 3)) {
								skip = true;
							}
							else if((s==0 && e == 0 && row < 3)){
								skip = true;
							}
							if(!skip) {
								int seat[] = {row,e, DataHolder.numberOfSeatsInRowFlyingWing-1-a};
								Random rd = new Random();
								
								//float speed = (float) (0.5 + rd.nextFloat()*(0.5));
								float speed = (float) 1.0;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, 
										speed, 1, DataHolder.numberOfSegmentsFlyingWing-1-s
										);
								fwpass.add(fwpas);
								
								
								DataHolder.seatedPassengersInRowFlyingWing[DataHolder.numberOfSegmentsFlyingWing-1-s][row][e][DataHolder.numberOfSeatsInRow-1-a] = 0;

						
						}
					}
					SimUtilities.shuffle(fwpass, RandomHelper.getUniform());

					
					for(FlyingWingPassenger fwpas : fwpass) {
						fwpas.timeBeforeBoarding = totalGroupTime;
						fwpas.boardingID = ind;
						totalGroupTime += time;
						context.add(fwpas);
						ind++;
						
					} 
				
				}
			}
		}			
	}
	
	public void parallelBTFBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		
			int ind = 0;
			int totalgrouptime = 0;

			int numberOfGroups = 3;
			List<List<List<FlyingWingPassenger> > > masterList = new ArrayList<List<List<FlyingWingPassenger> > >();

			
			List<Integer> rows = IntStream.range(0, DataHolder.numberOfRowsFlyingWing).boxed().collect(Collectors.toList());
			List<Integer> seatsInRow = IntStream.range(0, DataHolder.numberOfSeatsInRowFlyingWing).boxed().collect(Collectors.toList());

			SimUtilities.shuffle(rows, RandomHelper.getUniform());
			SimUtilities.shuffle(seatsInRow, RandomHelper.getUniform());

		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
			List<FlyingWingPassenger> fwpassengersA = new ArrayList<FlyingWingPassenger>();
			List<FlyingWingPassenger> fwpassengersB = new ArrayList<FlyingWingPassenger>();
			List<FlyingWingPassenger> fwpassengersC = new ArrayList<FlyingWingPassenger>();
			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					
					for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
						
							
							


							int segment = DataHolder.numberOfSegmentsFlyingWing - 1 - s;
							
							boolean skip = false;

							if((segment==0 && e == 1 && i < 3)) {
								skip = true;
							}
							else if((segment==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
								skip = true;
							}
							if(!skip) {

								int seatInRow = DataHolder.numberOfSeatsInRowFlyingWing - 1 - a;
								
								int seat[] = {i,e,seatInRow};
								Random rd = new Random();
								
								float speed = (float) 1;
								FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
										true, speed, 1, segment);
								if(i<DataHolder.firstGroupLimitFlyingWing) {
									fwpassengersA.add(fwpas);
								}
								else if(i<DataHolder.secondGroupLimitFlyingWing) {
									fwpassengersB.add(fwpas);

								}
								else {
									fwpassengersC.add(fwpas);

								}
								DataHolder.seatedPassengersInRowFlyingWing[segment][i][e][seatInRow] = 0;

								
							}
						}
					
				}
			}
			SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
			SimUtilities.shuffle(fwpassengersB, RandomHelper.getUniform());
			SimUtilities.shuffle(fwpassengersC, RandomHelper.getUniform());
			List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
			grouplist.add(fwpassengersC);
			grouplist.add(fwpassengersB);
			grouplist.add(fwpassengersA);
			masterList.add(grouplist);

			
		}
			
			
		List<List<FlyingWingPassenger>> groupAs = new ArrayList<List<FlyingWingPassenger>>();
		List<List<FlyingWingPassenger>> groupBs = new ArrayList<List<FlyingWingPassenger>>();
		List<List<FlyingWingPassenger>> groupCs = new ArrayList<List<FlyingWingPassenger>>();

			
			Random rd = new Random();
			for(int outer_idx = 0; outer_idx < numberOfGroups; outer_idx++) {
				for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
					List<FlyingWingPassenger> fwpass = masterList.get(inner_idx).get(outer_idx);
					switch (outer_idx) {
					case 0:
						groupAs.add(fwpass);
						break;
					case 1:
						groupBs.add(fwpass);
						break;
					default:
						groupCs.add(fwpass);
						break;
					}
					
					
					
					
					
				}
			}
			for(int outer_idx = 0; outer_idx < groupAs.get(0).size(); outer_idx++) {
				for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
					FlyingWingPassenger fwpas = groupAs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
					
				}
			}
			for(int outer_idx = 0; outer_idx < groupBs.get(0).size(); outer_idx++) {
				for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
					FlyingWingPassenger fwpas = groupBs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
					
				}
			}
			for(int outer_idx = 0; outer_idx < groupCs.get(DataHolder.numberOfSegmentsFlyingWing/2).size(); outer_idx++) {
				for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
					if (outer_idx < groupCs.get(inner_idx).size()) {
						FlyingWingPassenger fwpas = groupCs.get(inner_idx).get(outer_idx);
						fwpas.timeBeforeBoarding = totalgrouptime;
						fwpas.boardingID = ind;
						totalgrouptime += time;
						context.add(fwpas);
						ind++;
					}
					
					
				}
			}
			
			

	}
	
	public void parallelFTBBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		
		int ind = 0;
		int totalgrouptime = 0;

		int numberOfGroups = 3;
		List<List<List<FlyingWingPassenger> > > masterList = new ArrayList<List<List<FlyingWingPassenger> > >();

		
		List<Integer> rows = IntStream.range(0, DataHolder.numberOfRowsFlyingWing).boxed().collect(Collectors.toList());
		List<Integer> seatsInRow = IntStream.range(0, DataHolder.numberOfSeatsInRowFlyingWing).boxed().collect(Collectors.toList());

		SimUtilities.shuffle(rows, RandomHelper.getUniform());
		SimUtilities.shuffle(seatsInRow, RandomHelper.getUniform());

	for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
		List<FlyingWingPassenger> fwpassengersA = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersB = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersC = new ArrayList<FlyingWingPassenger>();
		for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
			for(int e = 0; e < 2; e++) {
				
				for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
					
						
						


						int segment = DataHolder.numberOfSegmentsFlyingWing - 1 - s;
						
						boolean skip = false;

						if((segment==0 && e == 1 && i < 3)) {
							skip = true;
						}
						else if((segment==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
							skip = true;
						}
						if(!skip) {

							int seatInRow = DataHolder.numberOfSeatsInRowFlyingWing - 1 - a;
							
							int seat[] = {i,e,seatInRow};
							Random rd = new Random();
							
							float speed = (float) 1;
							FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
									true, speed, 1, segment);
							if(i>=DataHolder.secondGroupLimitFlyingWing) {
								fwpassengersA.add(fwpas);
							}
							else if(i>DataHolder.firstGroupLimitFlyingWing) {
								fwpassengersB.add(fwpas);

							}
							else {
								fwpassengersC.add(fwpas);

							}
							DataHolder.seatedPassengersInRowFlyingWing[segment][i][e][seatInRow] = 0;

							
						}
					}
				
			}
		}
		SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersB, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersC, RandomHelper.getUniform());
		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengersC);
		grouplist.add(fwpassengersB);
		grouplist.add(fwpassengersA);
		masterList.add(grouplist);

		
	}
		
		
	List<List<FlyingWingPassenger>> groupAs = new ArrayList<List<FlyingWingPassenger>>();
	List<List<FlyingWingPassenger>> groupBs = new ArrayList<List<FlyingWingPassenger>>();
	List<List<FlyingWingPassenger>> groupCs = new ArrayList<List<FlyingWingPassenger>>();

		
		Random rd = new Random();
		for(int outer_idx = 0; outer_idx < numberOfGroups; outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				List<FlyingWingPassenger> fwpass = masterList.get(inner_idx).get(outer_idx);
				switch (outer_idx) {
				case 0:
					groupAs.add(fwpass);
					break;
				case 1:
					groupBs.add(fwpass);
					break;
				default:
					groupCs.add(fwpass);
					break;
				}
				
				
				
				
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupAs.get(DataHolder.numberOfSegmentsFlyingWing/2).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				if (outer_idx < groupAs.get(inner_idx).size()) {
					FlyingWingPassenger fwpas = groupAs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
				}
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupBs.get(0).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				FlyingWingPassenger fwpas = groupBs.get(inner_idx).get(outer_idx);
				fwpas.timeBeforeBoarding = totalgrouptime;
				fwpas.boardingID = ind;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupCs.get(0).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				//if (outer_idx < groupCs.get(inner_idx).size()) {
					FlyingWingPassenger fwpas = groupCs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
				//}
				
				
			}
		}
		
		

}
	
	
public void parallelWMABoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
		
		int ind = 0;
		int totalgrouptime = 0;

		int numberOfGroups = 3;
		List<List<List<FlyingWingPassenger> > > masterList = new ArrayList<List<List<FlyingWingPassenger> > >();

		
		List<Integer> rows = IntStream.range(0, DataHolder.numberOfRowsFlyingWing).boxed().collect(Collectors.toList());
		List<Integer> seatsInRow = IntStream.range(0, DataHolder.numberOfSeatsInRowFlyingWing).boxed().collect(Collectors.toList());

		SimUtilities.shuffle(rows, RandomHelper.getUniform());
		SimUtilities.shuffle(seatsInRow, RandomHelper.getUniform());

	for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
		List<FlyingWingPassenger> fwpassengersA = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersB = new ArrayList<FlyingWingPassenger>();
		List<FlyingWingPassenger> fwpassengersC = new ArrayList<FlyingWingPassenger>();
		for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
			for(int e = 0; e < 2; e++) {
				
				for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
					
						
						


						int segment = DataHolder.numberOfSegmentsFlyingWing - 1 - s;
						
						boolean skip = false;

						if((segment==0 && e == 1 && i < 3)) {
							skip = true;
						}
						else if((segment==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
							skip = true;
						}
						if(!skip) {

							int seatInRow = DataHolder.numberOfSeatsInRowFlyingWing - 1 - a;
							
							int seat[] = {i,e,seatInRow};
							Random rd = new Random();
							
							float speed = (float) 1;
							FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
									true, speed, 1, segment);
							if(a==2) {
								fwpassengersA.add(fwpas);
							}
							else if(a==1) {
								fwpassengersB.add(fwpas);

							}
							else {
								fwpassengersC.add(fwpas);

							}
							DataHolder.seatedPassengersInRowFlyingWing[segment][i][e][seatInRow] = 0;

							
						}
					}
				
			}
		}
		SimUtilities.shuffle(fwpassengersA, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersB, RandomHelper.getUniform());
		SimUtilities.shuffle(fwpassengersC, RandomHelper.getUniform());
		List<List<FlyingWingPassenger>> grouplist = new ArrayList<List<FlyingWingPassenger>>();
		grouplist.add(fwpassengersC);
		grouplist.add(fwpassengersB);
		grouplist.add(fwpassengersA);
		masterList.add(grouplist);

		
	}
		
		
	List<List<FlyingWingPassenger>> groupAs = new ArrayList<List<FlyingWingPassenger>>();
	List<List<FlyingWingPassenger>> groupBs = new ArrayList<List<FlyingWingPassenger>>();
	List<List<FlyingWingPassenger>> groupCs = new ArrayList<List<FlyingWingPassenger>>();

		
		Random rd = new Random();
		for(int outer_idx = 0; outer_idx < numberOfGroups; outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				List<FlyingWingPassenger> fwpass = masterList.get(inner_idx).get(outer_idx);
				switch (outer_idx) {
				case 0:
					groupAs.add(fwpass);
					break;
				case 1:
					groupBs.add(fwpass);
					break;
				default:
					groupCs.add(fwpass);
					break;
				}
				
				
				
				
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupAs.get(1).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				if (outer_idx < groupAs.get(inner_idx).size()) {

					FlyingWingPassenger fwpas = groupAs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
				}
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupBs.get(1).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				if (outer_idx < groupAs.get(inner_idx).size()) {
					FlyingWingPassenger fwpas = groupBs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
				}
				
			}
		}
		for(int outer_idx = 0; outer_idx < groupCs.get(1).size(); outer_idx++) {
			for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
				if (outer_idx < groupAs.get(inner_idx).size()) {
					FlyingWingPassenger fwpas = groupCs.get(inner_idx).get(outer_idx);
					fwpas.timeBeforeBoarding = totalgrouptime;
					fwpas.boardingID = ind;
					totalgrouptime += time;
					context.add(fwpas);
					ind++;
				}
								
				
			}
		}
		
		

	}

public void parallelRandomBoardingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid, int time) {
	
	int ind = 0;
	int totalgrouptime = 0;

	int numberOfGroups = 3;
	List<List<List<FlyingWingPassenger> > > masterList = new ArrayList<List<List<FlyingWingPassenger> > >();
	List < List < FlyingWingPassenger> > groupList = new ArrayList < List < FlyingWingPassenger> >();
	

	for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
		
		List<FlyingWingPassenger> fwpassengers = new ArrayList<FlyingWingPassenger>();
		
		
		for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
			for(int e = 0; e < 2; e++) {
				
				for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
				
					int segment = DataHolder.numberOfSegmentsFlyingWing - 1 - s;
					
					boolean skip = false;

					if((segment==0 && e == 1 && i < 3)) {
						skip = true;
					}
					else if((segment==DataHolder.numberOfSegmentsFlyingWing-1 && e == 0 && i < 3)){
						skip = true;
					}
					if(!skip) {

						int seatInRow = DataHolder.numberOfSeatsInRowFlyingWing - 1 - a;
						
						int seat[] = {i,e,seatInRow};
						Random rd = new Random();
						
						float speed = (float) 1;
						FlyingWingPassenger fwpas = new FlyingWingPassenger(space, grid, seat, 
								true, speed, 1, segment);
						fwpassengers.add(fwpas);
						DataHolder.seatedPassengersInRowFlyingWing[segment][i][e][seatInRow] = 0;

						
					}
				}
			
			}
		}
		SimUtilities.shuffle(fwpassengers, RandomHelper.getUniform());
	
	
	
		groupList.add(fwpassengers);

	
	}
	List < List < FlyingWingPassenger> > groupListDimensionalized = new ArrayList < List < FlyingWingPassenger> >();

	
	for(int outer_idx = 0; outer_idx < groupList.get(1).size(); outer_idx++) {
		for(int inner_idx = 0; inner_idx < DataHolder.numberOfSegmentsFlyingWing; inner_idx++) {
			if (outer_idx < groupList.get(inner_idx).size()) { 
				FlyingWingPassenger fwpas = groupList.get(inner_idx).get(outer_idx);
				fwpas.timeBeforeBoarding = totalgrouptime;
				fwpas.boardingID = ind;
				totalgrouptime += time;
				context.add(fwpas);
				ind++;
			}
		}
	}

	
	

}

	public void randomDisembarkingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid) {
		
		
		
		for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
			for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
				for(int e = 0; e < 2; e++) {
					boolean skip = false;
					if (s == 0 && e == 1 && i < 3 || s == 3 && e == 0 && i < 3) {
						skip = true;
					}
					if (!skip) {
						for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
							int seat[] = {i,e,a};
							Random rd = new Random();
							
							float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							FlyingWingDisembarkingPassenger dpas = new FlyingWingDisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, 1, s);
							context.add(dpas);
							int val = 1;
							if (seat[1] == 1) {
								val = -1;
							}
							NdPoint pt = new NdPoint(0.4+seat[0]+1, 3.4+(seat[2]+1)*val);
							space.moveTo(dpas, pt.getX(), pt.getY()+6*s);
							grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY()+6*s);
							
							DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 1;

							
						}
					}
					
				}
				
			
			}	
		}
		
		

		
}

	public void segmentedDisembarkingFlyingWing(Context<Object> context, ContinuousSpace space, Grid grid) {
	
	List<FlyingWingDisembarkingPassenger> seg0 = new ArrayList<FlyingWingDisembarkingPassenger>();
	List<FlyingWingDisembarkingPassenger> seg1 = new ArrayList<FlyingWingDisembarkingPassenger>();
	List<FlyingWingDisembarkingPassenger> seg2 = new ArrayList<FlyingWingDisembarkingPassenger>();
	List<FlyingWingDisembarkingPassenger> seg3 = new ArrayList<FlyingWingDisembarkingPassenger>();

	for(int s = 0; s < DataHolder.numberOfSegmentsFlyingWing; s++) {
		for (int i = 0; i < DataHolder.numberOfRowsFlyingWing; i++) {
			for(int e = 0; e < 2; e++) {
				boolean skip = false;
				if (s == 0 && e == 1 && i < 3 || s == 3 && e == 0 && i < 3) {
					skip = true;
				}
				if (!skip) {
					for (int a = 0; a < DataHolder.numberOfSeatsInRowFlyingWing; a++) {
						int seat[] = {i,e,a};
						Random rd = new Random();
						
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						FlyingWingDisembarkingPassenger dpas = new FlyingWingDisembarkingPassenger(space, grid, seat, rd.nextBoolean(), speed, s+1, s);
						switch (s) {
						case 0:
							seg0.add(dpas);
							break;
						case 1:
							seg1.add(dpas);
							break;
						case 2:
							seg2.add(dpas);
							break;
						default:
							seg3.add(dpas);
							break;
						}
						context.add(dpas);
						int val = 1;
						if (seat[1] == 1) {
							val = -1;
						}
						NdPoint pt = new NdPoint(0.4+seat[0]+1, 3.4+(seat[2]+1)*val);
						space.moveTo(dpas, pt.getX(), pt.getY()+6*s);
						grid.moveTo(dpas, (int)pt.getX(), (int)pt.getY()+6*s);
						
						DataHolder.seatedPassengersInRowFlyingWing[s][i][e][a] = 1;

						
					}
				}
				
			}
			
		
	}
		
	DataHolder.numOfGroup1PassengersLeft = seg0.size();
	DataHolder.numOfGroup2PassengersLeft = seg1.size();
	DataHolder.numOfGroup3PassengersLeft = seg2.size();
	DataHolder.numOfGroup4PassengersLeft = seg3.size();

}
	

	
	
	
	
}
	
	
	
	public void randomBoardingTETA(Context<Object> context, ContinuousSpace space, Grid grid) {
		//Create passengers
		List<TETAPassenger> fwpassengers = new ArrayList<TETAPassenger>();
		List<TETAPassenger> buspassengers = new ArrayList<TETAPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsTETA; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsTETA; i++) {
				for(int e = 0; e < 2; e++) {
					int max = 2;
					int entr = 0;
					if (s == 1 && e == 1) {
							max = 1;
					}
					if (i == 18 && s == e) {
						max = 0;
					}
					 if (s==0 && e==0 && i < 3) {
						max = 1;
					}
					
					 if (i == 17) {
						max = 0;
					}
					 if (i == 3) {
						 max = 0;
						 
					 }
					 if (i<3) {
						 entr = 1;
					 }
					
					for (int a = 0; a < max; a++) {
						
						int seat[] = {i,e,a};
						Random rd = new Random();
						if (i > 19) {
							entr = 1;
						}
						
						//System.out.println(entr);
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						TETAPassenger tpas = new TETAPassenger(space, grid, seat, 
								rd.nextBoolean(), 
								speed, 2, s, entr
								);
						if(i<3) {
							tpas.group = 1;
							buspassengers.add(tpas);
						}
						else {
							fwpassengers.add(tpas);
						}
						
						context.add(tpas);
						NdPoint pt = new NdPoint(4.4,0.0);
						if (i<3) {
							space.moveTo(tpas, pt.getX(), pt.getY());
							grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
						}
						else {
							if (entr == 0) {
								space.moveTo(tpas, pt.getX(), pt.getY());
								grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
							
							}
							else {
								space.moveTo(tpas, pt.getX()+37, pt.getY());
								grid.moveTo(tpas, (int)pt.getX()+37, (int)pt.getY());
							}
						}
							
						
						
						
						DataHolder.seatedPassengersInRowTETA[s][i][e][a] = 0;

						
					}
						
					
				}
			}
			
			
		}
		
			
			
			
		SimUtilities.shuffle(buspassengers, RandomHelper.getUniform());

		
		SimUtilities.shuffle(fwpassengers, RandomHelper.getUniform());
		int totalgrouptime = 0;
		Random rd = new Random();
		for(TETAPassenger pas : fwpassengers) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		totalgrouptime = 0;
		for(TETAPassenger pas : buspassengers) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		
		DataHolder.numOfGroup1PassengersLeft = buspassengers.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengers.size();

	}

	
	public void seatBoardingTETA(Context<Object> context, ContinuousSpace space, Grid grid) {
		//Create passengers
		List<TETAPassenger> buspassengers = new ArrayList<TETAPassenger>();

		List<TETAPassenger> passengersW = new ArrayList<TETAPassenger>();

		List<TETAPassenger> passengersA = new ArrayList<TETAPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsTETA; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsTETA; i++) {
				for(int e = 0; e < 2; e++) {
					int max = 2;
					int entr = 0;
					if (s == 1 && e == 1) {
							max = 1;
					}
					if (i == 18 && s == e) {
						max = 0;
					}
					 if (s==0 && e==0 && i < 3) {
						max = 1;
					}
					
					 if (i == 17) {
						max = 0;
					}
					 if (i == 3) {
						 max = 0;
						 
					 }
					 if (i<3) {
						 entr = 1;
					 }
					
					for (int a = 0; a < max; a++) {
						
						int seat[] = {i,e,a};
						Random rd = new Random();
						if (i > 19) {
							entr = 1;
						}
						
						//System.out.println(entr);
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						TETAPassenger tpas = new TETAPassenger(space, grid, seat, 
								rd.nextBoolean(), 
								speed, 1, s, entr
								);
						if (i < 3) {
							
							buspassengers.add(tpas);
						}
						else {
							switch(a) {
							case 0:
								tpas.group = 3;
								passengersA.add(tpas);
								break;
							default:
								tpas.group = 2;

								passengersW.add(tpas);
								break;

							}
						}
						
						context.add(tpas);
						NdPoint pt = new NdPoint(4.4,0.0);
						if (i<3) {
							space.moveTo(tpas, pt.getX(), pt.getY());
							grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
						}
						else {
							if (entr == 0) {
								space.moveTo(tpas, pt.getX(), pt.getY());
								grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
							
							}
							else {
								space.moveTo(tpas, pt.getX()+37, pt.getY());
								grid.moveTo(tpas, (int)pt.getX()+37, (int)pt.getY());
							}
						}
						
						DataHolder.seatedPassengersInRowTETA[s][i][e][a] = 0;

						
					}
						
					
				}
			}
			
			
		}
		
			
			
			
		
		SimUtilities.shuffle(buspassengers, RandomHelper.getUniform());

		SimUtilities.shuffle(passengersW, RandomHelper.getUniform());
		SimUtilities.shuffle(passengersA, RandomHelper.getUniform());

		int totalgrouptime = 0;
		Random rd = new Random();
		for(TETAPassenger pas : passengersW) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		totalgrouptime = 0;
		for(TETAPassenger pas : passengersA) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		totalgrouptime = 0;
		for(TETAPassenger pas : buspassengers) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		
		DataHolder.numOfGroup1PassengersLeft = buspassengers.size();
		DataHolder.numOfGroup2PassengersLeft = passengersW.size();
		DataHolder.numOfGroup3PassengersLeft = passengersA.size();

	}
	
	public void steffenBoardingTETA(Context<Object> context, ContinuousSpace space, Grid grid) {
		
		List<TETAPassenger> buspassengers = new ArrayList<TETAPassenger>();

		List<TETAPassenger> groupLWE = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupLWO = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupLME = new ArrayList<TETAPassenger>();	
		List<TETAPassenger> groupLMO = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupLAE = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupLAO = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupRWE = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupRWO = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupRME = new ArrayList<TETAPassenger>();	
		List<TETAPassenger> groupRMO = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupRAE = new ArrayList<TETAPassenger>();
		List<TETAPassenger> groupRAO = new ArrayList<TETAPassenger>();	
		
		//Even
		for(int s = 0; s < DataHolder.numberOfSegmentsTETA; s++) {
			for (int i = 0; i < DataHolder.numberOfRowsTETA; i+=2) {
				for(int e = 0; e < 2; e++) {
					int max = 2;
					int entr = 0;
					if (s == 1 && e == 1) {
							max = 1;
					}
					if (i == 18 && s == e) {
						max = 0;
					}
					 if (s==0 && e==0 && i < 3) {
						max = 1;
					}
					
					 if (i == 17) {
						max = 0;
					}
					 if (i == 3) {
						 max = 0;
						 
					 }
					 if (i<3) {
						 entr = 1;
					 }
						for (int a = 0; a < max; a++) {
							int seat[] = {i,e,a};
							Random rd = new Random();
							if (i > 19) {
								entr = 1;
							}
							float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							TETAPassenger tpas = new TETAPassenger(space, grid, seat, 
									rd.nextBoolean(), 
									speed, 1, s, entr);
							if (i<3) {
								buspassengers.add(tpas);
							}
							else {
								switch(a) {
								case 0:
									if(e == 0) {
										tpas.group = 10;
										groupRAE.add(tpas);
									}
									else {
										tpas.group = 11;
										groupLAE.add(tpas);
									}
									break;
								case 1:
									if(e == 0) {
										tpas.group = 6;

										groupRME.add(tpas); 
									}
									else {
										tpas.group = 7;

										groupLME.add(tpas); 
									}
									
									break;
								default:
									if(e == 0) {
										tpas.group = 2;

										groupRWE.add(tpas);
									}
									else {
										tpas.group = 3;

										groupLWE.add(tpas);
									}
									
									break;
								}
							}
							
							context.add(tpas);
							NdPoint pt = new NdPoint(4.4,0.0);
							if (i<3) {
								space.moveTo(tpas, pt.getX(), pt.getY());
								grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
							}
							else {
								if (entr == 0) {
									space.moveTo(tpas, pt.getX(), pt.getY());
									grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
								
								}
								else {
									space.moveTo(tpas, pt.getX()+37, pt.getY());
									grid.moveTo(tpas, (int)pt.getX()+37, (int)pt.getY());
								}
							}
							
							
							DataHolder.seatedPassengersInRowTETA[s][i][e][a] = 0;

							
						}
					
					
				}
				
			}
			//Odd
			for (int i = 1; i < DataHolder.numberOfRowsTETA; i+=2) {
				for(int e = 0; e < 2; e++) {
					int max = 2;
					int entr = 0;
					if (s == 1 && e == 1) {
							max = 1;
					}
					if (i == 18 && s == e) {
						max = 0;
					}
					 if (s==0 && e==0 && i < 3) {
						max = 1;
					}
					
					 if (i == 17) {
						max = 0;
					}
					 if (i == 3) {
						 max = 0;
						 
					 }
					 if (i<3) {
						 entr = 1;
					 }
						for (int a = 0; a < max; a++) {
							int seat[] = {i,e,a};
							Random rd = new Random();
							if (i > 19) {
								entr = 1;
							}
							float speed = (float) (0.5 + rd.nextFloat()*(0.5));
							TETAPassenger tpas = new TETAPassenger(space, grid, seat, 
									rd.nextBoolean(), 
									speed, 1, s, entr);
							if (i < 3) {
								buspassengers.add(tpas);
							}
							else {
								switch(a) {
								case 0:
									if(e == 0) {
										tpas.group = 12;
										groupRAO.add(tpas);
									}
									else {
										tpas.group = 13;

										groupLAO.add(tpas);
									}
									break;
								case 1:
									if(e == 0) {
										tpas.group = 8;

										groupRMO.add(tpas); 
									}
									else {
										tpas.group = 9;

										groupLMO.add(tpas); 
									}
									
									break;
								default:
									if(e == 0) {
										tpas.group = 4;

										groupRWO.add(tpas);
									}
									else {
										tpas.group = 5;

										groupLWO.add(tpas);
									}
									
									break;
								}
							}
							context.add(tpas);
							NdPoint pt = new NdPoint(4.4,0.0);
							if (i<3) {
								space.moveTo(tpas, pt.getX(), pt.getY());
								grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
							}
							else {
								if (entr == 0) {
									space.moveTo(tpas, pt.getX(), pt.getY());
									grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
								
								}
								else {
									space.moveTo(tpas, pt.getX()+37, pt.getY());
									grid.moveTo(tpas, (int)pt.getX()+37, (int)pt.getY());
								}
							}
							
							
							
							
							DataHolder.seatedPassengersInRowTETA[s][i][e][a] = 0;

							
						}
					
					
				}
				
			}
		}
		
		
		
		SimUtilities.shuffle(groupLWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRWE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLWO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRME, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLMO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAO, RandomHelper.getUniform());
		SimUtilities.shuffle(groupRAE, RandomHelper.getUniform());
		SimUtilities.shuffle(groupLAO, RandomHelper.getUniform());
		SimUtilities.shuffle(buspassengers, RandomHelper.getUniform());

		List<List<TETAPassenger>> grouplist = new ArrayList<List<TETAPassenger>>();
		grouplist.add(groupLWE);
		grouplist.add(groupLWO);
		grouplist.add(groupRWE);
		grouplist.add(groupRWO);
		grouplist.add(groupLME);
		grouplist.add(groupLMO);
		grouplist.add(groupRME);
		grouplist.add(groupRMO);
		grouplist.add(groupLAE);
		grouplist.add(groupLAO);
		grouplist.add(groupRAE);
		grouplist.add(groupRAO);
		grouplist.add(buspassengers);

		
		int ind = 0;
		Random rd = new Random();
		
		for(List<TETAPassenger> fwlist : grouplist) {
			int totalgrouptime = 0;
			for(TETAPassenger fwpas : fwlist) {
				int time = 1+rd.nextInt(4);
				fwpas.timeBeforeBoarding = totalgrouptime;
				totalgrouptime += time;
				context.add(fwpas);
			}
		}
		
		DataHolder.numOfGroup2PassengersLeft = groupRWE.size();
		DataHolder.numOfGroup3PassengersLeft = groupLWE.size();
		DataHolder.numOfGroup4PassengersLeft = groupRWO.size();
		DataHolder.numOfGroup5PassengersLeft = groupLWO.size();
		DataHolder.numOfGroup6PassengersLeft = groupRME.size();
		DataHolder.numOfGroup7PassengersLeft = groupLME.size();
		DataHolder.numOfGroup8PassengersLeft = groupRMO.size();
		DataHolder.numOfGroup9PassengersLeft = groupLMO.size();
		DataHolder.numOfGroup10PassengersLeft = groupRAE.size();
		DataHolder.numOfGroup11PassengersLeft = groupLAE.size();
		DataHolder.numOfGroup12PassengersLeft = groupRAO.size();
		DataHolder.numOfGroup13PassengersLeft = groupLAO.size();
		DataHolder.numOfGroup1PassengersLeft = buspassengers.size();


	}
	
	public void randomDisembarkingTETA(Context<Object> context, ContinuousSpace space, Grid grid) {
		//Create passengers
		List<TETADisembarkingPassenger> fwpassengers = new ArrayList<TETADisembarkingPassenger>();
		List<TETADisembarkingPassenger> buspassengers = new ArrayList<TETADisembarkingPassenger>();

		for(int s = 0; s < DataHolder.numberOfSegmentsTETA; s++) {

			for (int i = 0; i < DataHolder.numberOfRowsTETA; i++) {
				for(int e = 0; e < 2; e++) {
					int max = 2;
					int entr = 0;
					if (s == 1 && e == 1) {
							max = 1;
					}
					if (i == 18 && s == e) {
						max = 0;
					}
					 if (s==0 && e==0 && i < 3) {
						max = 1;
					}
					
					 if (i == 17) {
						max = 0;
					}
					 if (i == 3) {
						 max = 0;
						 
					 }
					 if (i<3) {
						 entr = 1;
					 }
					
					for (int a = 0; a < max; a++) {
						
						int seat[] = {i,e,a};
						Random rd = new Random();
						if (i > 19) {
							entr = 1;
						}
						
						//System.out.println(entr);
						float speed = (float) (0.5 + rd.nextFloat()*(0.5));
						TETADisembarkingPassenger tpas = new TETADisembarkingPassenger(space, grid, seat, 
								rd.nextBoolean(), 
								speed, 2, s, entr
								);
						if(i<3) {
							tpas.group = 1;
							buspassengers.add(tpas);
						}
						else {
							fwpassengers.add(tpas);
						}
						
						context.add(tpas);
						int val = 1;
						if (tpas.seatNumber[1] == 1) {
							val = -1;
						}
						NdPoint pt = new NdPoint(0.4+seat[0]+1, 2.4+4*s+(seat[2]+1)*val);
						space.moveTo(tpas, pt.getX(), pt.getY());
						grid.moveTo(tpas, (int)pt.getX(), (int)pt.getY());
							
						
						
						
						DataHolder.seatedPassengersInRowTETA[s][i][e][a] = 0;

						
					}
						
					
				}
			}
			
			
		}
		
			
			
			
		SimUtilities.shuffle(buspassengers, RandomHelper.getUniform());

		
		SimUtilities.shuffle(fwpassengers, RandomHelper.getUniform());
		int totalgrouptime = 0;
		Random rd = new Random();
		for(TETADisembarkingPassenger pas : fwpassengers) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		totalgrouptime = 0;
		for(TETADisembarkingPassenger pas : buspassengers) {
			int time = 1 + rd.nextInt(4);
			pas.timeBeforeBoarding = totalgrouptime;
			totalgrouptime += time;
			//context.add(pas);
		}
		
		DataHolder.numOfGroup1PassengersLeft = buspassengers.size();
		DataHolder.numOfGroup2PassengersLeft = fwpassengers.size();

	}
	
	

	public ContinuousSpace generateNarrowAircraftSpace(Context<Object> context) {
		//Generate narrow aircraft space
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), numberOfRows+1+10, 2*numberOfSeatsInRow+1);
		
		return space;
		
	}
	
	public Grid generateNarrowAircraftGrid(Context<Object> context) {
		//Generate narrow aircraft grid
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, numberOfRows+1+10, 2*numberOfSeatsInRow+1));
		
		return grid;
		
	}
	public ContinuousSpace generateFlyingWingAircraftSpace(Context<Object> context) {
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), DataHolder.numberOfRowsFlyingWing+1+10, 2*DataHolder.numberOfSeatsInRowFlyingWing+(DataHolder.numberOfSegmentsFlyingWing-1)*2*DataHolder.numberOfSeatsInRowFlyingWing+DataHolder.numberOfSegmentsFlyingWing);
		
		return space;
	}
	
	public Grid generateFlyingWingAircraftGrid(Context<Object> context) {
		
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, DataHolder.numberOfRowsFlyingWing+1+10, 2*DataHolder.numberOfSeatsInRowFlyingWing+(DataHolder.numberOfSegmentsFlyingWing-1)*2*DataHolder.numberOfSeatsInRowFlyingWing+DataHolder.numberOfSegmentsFlyingWing));
		return grid;
	}
	
	public ContinuousSpace generateTETAAircraftSpace(Context<Object> context) {
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), DataHolder.numberOfRowsTETA+2, 9);
		
		return space;
	}
	
	public Grid generateTETAAircraftGrid(Context<Object> context) {
		
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, DataHolder.numberOfRowsTETA+2, 9));
		return grid;
	}
}
