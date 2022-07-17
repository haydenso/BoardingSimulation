package boarding;

import java.util.Random;

public class ProbabilityTimeGenerator {

	public static int generateWeibullLuggageTime() {
		Random rd = new Random();
		double p = rd.nextDouble();
		int val = (int) (5*rd.nextDouble());
		
		if (p<0.13) {
			return val; //0-5
			
		}
		else if (p<0.36) {
			return(5+val); //5-10
		}
		else if (p<0.59) {
			return 10+val; //10-15
		}
		else if (p<0.75) {
			return 15+val; //15-20
		}
		else if (p<0.87) {
			return 20+val; //20-25
		}
		else if (p<0.93) {
			return 25+val; //25-30
		}
		else if (p<0.97) {
			return 30+val; //30-35
		}
		else if (p<0.99) {
			return 35+val; //35-40
		} 
		
		return 40+val; //40-45
		
	}
	
}
