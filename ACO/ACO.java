package ACO;

import java.util.*;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

/**
 * Ant colony optimization algorithm, used to solve the problem that the task is assigned to the virtual machine in the shortest time
 * @author Gavrila
 */
public class ACO {
	public class position{
		public int vm;
		public int task;
		public position(int a, int b){//coordinates of ????????????????????
			vm = a;
			task = b;
		}
	}
	private List<Ant> ants;//Defining ant colony
	private int antcount;//Number of ants
	private int Q = 100;//#############why is dis q for??
	private double[][] pheromone;//Pheromone matrix
	private double[][] Delta;//Total pheromone increment
	private int VMs;//Number of virtual machines
	private int tasks;
	public position[] bestTour;//Best solution
	private double bestLength;//Optimal solution length (time scale)
	private List<? extends Cloudlet> cloudletList;
	private List<? extends Vm> vmList;
	/**
	 * Initialization matrix
* @param antNum is the number of ants to be used in the system which will assign besttour[nooftasks] all as(-1,-1),pheromone[vms][tasks] as 0.1,ant array n starting (vm,task)
* for each ant viz 5 here
	 */
	public void init(int antNum, List<? extends Cloudlet> list1, List<? extends Vm> list2){
		//cloudletList = new ArrayList<? extends Cloudlet>;
		cloudletList = list1;
		vmList = list2;
		antcount = antNum;
		ants = new ArrayList<Ant>(); 
		VMs = vmList.size();
		tasks = cloudletList.size();
		pheromone = new double[VMs][tasks];
		Delta = new double[VMs][tasks];
		bestLength = 1000000;
		//Initialize the pheromone matrix
		for(int i=0; i<VMs; i++){
			for(int j=0; j<tasks; j++){
				pheromone[i][j] = 0.1;
			}
		}
		bestTour = new position[tasks];
		for(int i=0; i<tasks; i++){
			bestTour[i] = new position(-1, -1);
		}
		//Randomly placed ants
        for(int i=0; i<antcount; i++){  
            ants.add(new Ant());  
            ants.get(i).RandomSelectVM(cloudletList, vmList);//goes to ant.java where it assigns one cloudlet to one vmso by d end we would hv assigned 5 cloudlets to 
            //5 vms that will be d starting position of the ants which are 5 in number these starting position is recorded in tour matrix,,
            //in tbu matrix firstvm would be there for now whatever i hv got
        }
	}
	/**
	 * ACO operation process
* @param maxgen maximum number of iterations of ACO
	 */
	public void run(int maxgen){
		for(int runTime=0; runTime<maxgen; runTime++){
			System.out.println(runTime+"th time：####################################################");
			//The process of each ant moving
			for(int i=0; i<antcount; i++){
				for(int j=1; j<tasks; j++){	
					ants.get(i).SelectNextVM(pheromone);
				}         

			}//for each ant next vm and task would be selected
			/*
			 * for each ant there will be a tour around vm  n come with max time taken in ant's journey*/
			for(int i=0; i<antcount; i++){
				System.out.println("number"+i+"ants");
				ants.get(i).CalTourLength();//going to ant.java
				System.out.println("#"+i+"An ant's journey："+ants.get(i).tourLength);
				ants.get(i).CalDelta();//going to ant.java
				if(ants.get(i).tourLength<bestLength){  
					//  Keep optimal path
	                bestLength = ants.get(i).tourLength;  
	                System.out.println("#"+runTime+"th time:" + "#"+i+"Ant find new solutions："+bestLength);   
	                for(int j=0;j<tasks;j++){  
	                	bestTour[j].vm = ants.get(i).tour.get(j).vm;
	                    bestTour[j].task = ants.get(i).tour.get(j).task;
	                } 
	                //Update pheromone to find the optimal solution
	                for(int k=0; k<VMs; k++){
	                	for(int j=0; j<tasks; j++){
	                		pheromone[k][j] = pheromone[k][j] + Q/bestLength;
	                	}
	                }  
				}
			}
			UpdatePheromone();//Update pheromone for each way
				
			//Re-set ant randomly
			for(int i=0;i<antcount;i++){  
				ants.get(i).RandomSelectVM(cloudletList, vmList);  
		    }  	
		}
	}
	/** 
     * Update the pheromone matrix
     */  
	public void UpdatePheromone(){
		double rou=0.5;  //evaporation included
        for(int k=0; k<antcount; k++){
        	for(int i=0; i<VMs; i++){
        		for(int j=0; j<tasks; j++){
        			Delta[i][j] += ants.get(k).delta[i][j];
        		}
        	}
        }
        
        for(int i=0; i<VMs; i++){
        	for(int j=0; j<tasks; j++){
        		pheromone[i][j] = (1-rou)*pheromone[i][j] + Delta[i][j];
        	}
        }  
	}
	/** 
     * Output the results of running the program
     */  
    public void ReportResult(){  
        System.out.println("The optimal path length is"+bestLength);
        for(int j=0; j<tasks; j++)
        {
        	System.out.println(bestTour[j].task+"Assigned to："+bestTour[j].vm);
        }

        System.out.println("Best Totalcost:");
    }  	
}
