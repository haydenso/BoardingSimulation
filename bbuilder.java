package boarding;

//import java.util.ArrayList;
/*import java.util.List;
import java.util.Random;

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
import repast.simphony.util.SimUtilities;*/
/*
public class bbuilder implements ContextBuilder<Object> {
	public static int numberOfRows = 33;
	public static int numberOfSeatsInRow = 6;
	//public int seatedPassengersInRow[] = null;
	
	
	@Override
	public Context build(Context<Object> context) {
		// TODO Auto-generated method stub
		context.setId("Boarding");
	
		
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new SimpleCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), numberOfRows, 1);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), 
						true, numberOfRows, 1));
		
		List<Passenger> passengers = new ArrayList<Passenger>();
		
		for (int i = 0; i < numberOfRows; i++) {
			for (int a = 0; a < numberOfSeatsInRow; a++) {
				int seat[] = {i,a};
				Random rd = new Random();
				float speed = (float) (0.5 + rd.nextFloat()*(0.5));
				passengers.add(new Passenger(space, grid, seat, 
						rd.nextBoolean(), 
						speed
						));
				
			}
			//seatedPassengersInRow[i] = 0;
		}
		
		//Random boarding
		SimUtilities.shuffle(passengers, RandomHelper.getUniform());
		int ind = 0;
		for(Passenger pas : passengers) {
			pas.timeBeforeBoarding = ind*5;
			context.add(pas);
			ind++;
		}
		
		for (Object obj : context) {
			NdPoint pt = new NdPoint(0.4,0.4);
			space.moveTo(obj, pt.getX(), pt.getY());
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
			
		}
				
		return context;
	}
}*/
