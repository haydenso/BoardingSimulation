package boarding;

public class DataHolder {
	
	//Constants
	public static int aisleSeatingTime = 5;
	public static int middleSeatingTime = 7;
	public static int unobstructedSeatingTime = 1;
	public static double meanWalkingSpeed = 0.75;
	public static double deviationOfWalkingSpeed = 0.25;
	public static double alphaLuggageTime = 1.7;
	public static double betaLuggageTime = 16;
	public static int numberOfRows = 33;
	public static int numberOfSeatsInRow = 3;
	public static int timeBetweenBoarders = 5;
	public static int[] primes = {2,3,5,7,11,13,17,19,23,29,31,37};
	
	public static int[][][] seatedPassengersInRow = new int[numberOfRows][2][3];
	public static int NAInitialNumberOfPassengers = numberOfRows*numberOfSeatsInRow*2-numberOfSeatsInRow;
	public static int numOfNonSeatedPassengers = NAInitialNumberOfPassengers;
	public static int numOfBoardedPAssengers = 0;
	
	public static int numOfGroup1PassengersLeft = 0; // Will be given value during runtime
	public static int numOfGroup2PassengersLeft = 0;
	public static int numOfGroup3PassengersLeft = 0;
	public static int numOfGroup4PassengersLeft = 0; // Will be given value during runtime
	public static int numOfGroup5PassengersLeft = 0;
	public static int numOfGroup6PassengersLeft = 0;
	public static int numOfGroup7PassengersLeft = 0; // Will be given value during runtime
	public static int numOfGroup8PassengersLeft = 0;
	public static int numOfGroup9PassengersLeft = 0;
	public static int numOfGroup10PassengersLeft = 0; // Will be given value during runtime
	public static int numOfGroup11PassengersLeft = 0;
	public static int numOfGroup12PassengersLeft = 0;
	public static int numOfGroup13PassengersLeft = 0;
	
	public static int firstGroupLimit = 11;
	public static int secondGroupLimit = 22;

	//FLYING WING
	public static int numberOfRowsFlyingWing = 14;
	public static int numberOfSeatsInRowFlyingWing = 3;
	public static int numberOfSegmentsFlyingWing = 1;
	public static int[][][][] seatedPassengersInRowFlyingWing = new int[numberOfSegmentsFlyingWing][numberOfRowsFlyingWing][2][numberOfSeatsInRowFlyingWing];

	public static int firstGroupLimitFlyingWing = 5;
	public static int secondGroupLimitFlyingWing = 10;
	
	public static int numOfNonSeatedPassengersFlyingWing = (numberOfSegmentsFlyingWing-1)*numberOfRowsFlyingWing*numberOfSeatsInRowFlyingWing*2+2*(numberOfRowsFlyingWing-3)*numberOfSeatsInRowFlyingWing;
	public static int FWnumOfBoardedPassengers= 0;

	
	
	//TETA
	public static int numberOfRowsTETA = 40;
	//public static int numberOfSeatsInRowTETA1 = 2;
	public static int numberOfSeatsInRowTETA = 3;

	public static int numberOfSegmentsTETA = 2;
	public static int[][][][] seatedPassengersInRowTETA = new int[numberOfSegmentsTETA][numberOfRowsTETA][2][2];

	public static int firstGroupLimitTETA = 5;
	public static int secondGroupLimitTETA = 10;
	
	public static int numOfNonSeatedPassengersTETA = (numberOfSeatsInRowTETA+2*(numberOfSeatsInRowTETA-1))*35+(numberOfSeatsInRowTETA-1)*3*3-3;

	// Trackers
	public static int numberOfCollissions = 0;
	public static int timeSpentWaiting = 0;


}
