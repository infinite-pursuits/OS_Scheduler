import java.util.*;
import java.io.*;

public class RR {
	
	ArrayList<Process> unstarted_processes = new ArrayList<Process>();
	ArrayList<Process> terminated_processes = new ArrayList<Process>();
	ArrayList<Process> ready_processes = new ArrayList<Process>();
	ArrayList<Process> blocked_processes = new ArrayList<Process>();
	ArrayList<Process> all_processes = new ArrayList<Process>();
	
	Udri udri = new Udri();
	Boolean verbose;
	int total_IO=0;
	int total_CPU=0;
	Process current_process=null;
	int quantum;
	
	public RR(ArrayList<Process> all_processes,Boolean verbose,int quantum) {
		this.all_processes=all_processes;
		this.verbose=verbose;
		this.quantum=quantum;
		System.out.print("\nThe original input was: "+all_processes.size()+" ");
		for(Process p: all_processes)
		{
			unstarted_processes.add(p);
			System.out.print("\t"+p.A+" "+p.B+" "+p.C+" "+p.IO+"\t");
		}
		System.out.println(" ");
		System.out.print("The (sorted) input is:  "+all_processes.size()+" ");
		Collections.sort(unstarted_processes,Process.arrival_time_comp );
		
		Collections.sort(all_processes,Process.arrival_time_comp );
		
		for(Process p: unstarted_processes)
			System.out.print("\t"+p.A+" "+p.B+" "+p.C+" "+p.IO+"\t");
		
		System.out.println(" ");
		if(this.verbose){
			System.out.println("\nThis detailed printout gives the state and remaining burst for each process\n");
		}
		
		run();
	}
	
	public void run()
	{		
		int cycle=0;
		current_process=null;
		Process temp=null;
		ArrayList<Process> temp2= new ArrayList<>();
		
		while(!(terminated_processes.size()==all_processes.size()))
		{	if(this.verbose)
				verbose_print(cycle);
		
		
		if(current_process!=null)
		{	int flag=0,flag2=0;
						int rcpubv=current_process.rcpub;
						int rcpuv=current_process.rcpu;
						if(rcpubv>rcpuv)
							rcpubv=rcpuv;
						
						current_process.rcpub=rcpubv;
						current_process.rcpu=rcpuv;
						current_process.rcpub--;
						current_process.rcpu--;
						current_process.quant--;
						if(current_process.quant==0 && current_process.rcpub>0)
						{
							flag2=1;
							current_process.state="ready";
							temp2.add(current_process);
						}
						if(current_process.rcpub==0 && current_process.rcpu>0)
						{
							current_process.gen_IO_burst(udri);
							current_process.state="blocked";
							flag=1;
						}
						if(current_process.rcpub==0 && current_process.rcpu==0)
						{
							current_process.state="terminated";
							current_process.finish_time=cycle;
							if (!terminated_processes.contains(current_process))
								terminated_processes.add(current_process);
							current_process=null;
						}
						if(flag==1||flag2==1)
							current_process=null;
							
						total_CPU++;
					
		}
		
		
		// Filling ready with arriving processes
		if (unstarted_processes.size()>0){
			Iterator<Process> it = unstarted_processes.iterator();
			while(it.hasNext())
			{	
				Process p=it.next();
					if  (p.A<=cycle)
					{
						p.state="ready";
						if (!ready_processes.contains(p))
							temp2.add(p);
					}
					
				}
	
			unstarted_processes.removeAll(temp2);
		}
		
		
		if (blocked_processes.size()>0)
		{		
			Iterator<Process> it = blocked_processes.iterator();
			while(it.hasNext())
			{	
				Process p=it.next();
				p.IO_time++;
				p.riob--;
				if(p.riob==0)
				{
					p.state="ready";
					if (!ready_processes.contains(p))
						temp2.add(p);
				}	
			}
			blocked_processes.removeAll(temp2);
			total_IO++;
		}
		Collections.sort(temp2,Process.arrival_time_comp );
		ready_processes.addAll(temp2);
		temp2.clear();
		
		if(current_process==null) {
			if(ready_processes.size()>0)
			{current_process = ready_processes.remove(0);
			current_process.state="running";
			current_process.rcpub=(current_process.rcpub==0) ? current_process.gen_CPU_burst(udri) : current_process.rcpub;
			
			current_process.quant = quantum;
		}
		}
		
		
		Iterator<Process> it = all_processes.iterator();
		while (it.hasNext()){
			Process p=(Process)it.next();
			if ((p.state).equals("blocked")){
		if (!blocked_processes.contains(p))
			blocked_processes.add(p);
		}
			}
		
		
		cycle++;
	}	
		System.out.println("\n\nThe scheduling algorithm used was RR\n");
		
		details_summary(cycle-1);
	}
	
	public void verbose_print(int cycle)
	{
		System.out.println("");
		System.out.print("Before cycle\t"+cycle+" :");
		Iterator<Process> it = all_processes.iterator();
		while (it.hasNext())
		{	Process p=(Process)it.next();
			if((p.state).equals("running"))
				System.out.print("\t"+p.state+" "+p.rcpub+" ");
			else if((p.state).equals("blocked"))
				System.out.print("\t"+p.state+" "+p.riob+" ");
			else
				System.out.print("\t"+p.state+" "+0+" ");
		}
	}
	
	public void details_summary(int f)
	{
		float total_wait_time=0;
		float total_turnaround_time=0;
		float total_finish_time=f;
		int np=all_processes.size();
		Iterator<Process> it = all_processes.iterator();
		int i=0;
		while(it.hasNext())
		{	
			Process p=(Process)it.next();
			p.turn_around_time=p.finish_time-p.A;
			p.waiting_time=p.turn_around_time-p.IO_time-p.C;
			total_wait_time=total_wait_time+p.waiting_time;
			total_turnaround_time=total_turnaround_time+p.turn_around_time; 
			System.out.println("\nProcess "+i+":"+"\n\t(A,B,C,IO) = "+"("+p.A+","+p.B+","+p.C+","+p.IO+")");
			System.out.println("\tFinishing Time: "+p.finish_time);
			System.out.println("\tTurnaround Time: "+p.turn_around_time);
			System.out.println("\tI/O Time: "+p.IO_time);
			System.out.println("\tWaiting Time: "+p.waiting_time);
			i++;
		}
		System.out.println("\nSummary Data:");
		System.out.println("\tFinishing time: "+(int)total_finish_time);
		System.out.println("\tCPU Utilization: "+total_CPU/total_finish_time);
		System.out.println("\tI/O Utilization: "+total_IO/total_finish_time);
		System.out.println("\tThroughput: "+np*100/total_finish_time+" processes per hundred cycles");
		System.out.println("\t"+"Average Turnaround Time: "+total_turnaround_time/np);
		System.out.println("\tAverage Waiting time: "+total_wait_time/np);
		
	}
}
