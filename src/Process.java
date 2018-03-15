import java.util.*;
import java.io.*;

public class Process {
	int A ; //arrival time
	int C ; // cpu time needed
	int rcpu ; //remaining cpu time needed
	int riob; //remaining io burst time
	int rcpub; //remaining cpu burst time
	int B; //cpu burst limit
	int total_cpu_time;
	int turn_around_time;
	int finish_time;
	int waiting_time;
	int IO_time;
	int IO; //io burst limit
	String state;
	int ID;
	int quant;
	
	public Process(int a,int b, int c, int io, int id){
		A=a;
		B=b;
		C=c;
		IO=io;
		ID=id;
		rcpu=c;
		quant=IO_time=waiting_time=rcpub=riob=total_cpu_time=turn_around_time=finish_time=0;
		state="unstarted";
	}
	
	public Process(Process ref){
		A=ref.A;
		B=ref.B;
		C=ref.C;
		IO=ref.IO;
		ID=ref.ID;
		rcpu=ref.rcpu;
		quant=IO_time=waiting_time=rcpub=riob=total_cpu_time=turn_around_time=finish_time=0;
		state="unstarted";
	}
	
	public void terminate(int cycle)
	{
		finish_time = cycle;
		turn_around_time = finish_time-A;
		state = "terminated";
	}
	
	public int gen_IO_burst(Udri udri)
	{	riob= ((udri.obtain_X())%IO)+1;
		return riob;
	}
	
	public int gen_CPU_burst(Udri udri)
	{	this.rcpub = ((udri.obtain_X())%B)+1;
		return rcpub;
	}
	
	public static Comparator<Process> arrival_time_comp = new Comparator<Process>(){
	      public  int compare(Process p1, Process p2){
	            int comp = p1.A - p2.A;
	            if(comp == 0){
	                return p1.ID - p2.ID;
	            }
	            else{
	                return comp;
	            }
	        }
	    };
	    
		public static Comparator<Process> rcpu_time_comp = new Comparator<Process>(){
		      public  int compare(Process p1, Process p2){
		            int comp = p1.rcpu - p2.rcpu;
		            if(comp == 0){
		                return p1.ID - p2.ID;
		            }
		            else{
		                return comp;
		            }
		        }
		    };

}

