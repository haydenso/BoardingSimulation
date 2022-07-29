package boarding;

import java.util.Random;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;

public class ProbabilityTimeGenerator {
	
	public static int num = 0;
	public static int total_lugtime = 0;

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
	
	public static int generateGaussianLuggageTime() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		double deviation = params.getDouble("lugDeviation");
		double mean = params.getDouble("lugMean");
		RandomHelper randomHelper = new RandomHelper();
		
		int lugtime = randomHelper.createNormal(mean, deviation).nextInt();
		if (lugtime < 0) {
			//System.out.println("Asymmetry");
			lugtime = 0;
		}
		//System.out.println(lugtime);
		total_lugtime += lugtime;
		num += 1;
		System.out.println((double) total_lugtime/(double) num);
		//System.out.println(total_lugtime);
		//System.out.println(num);
		return lugtime;
	}
	
}
