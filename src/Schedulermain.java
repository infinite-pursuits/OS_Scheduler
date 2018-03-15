import java.util.*;
import java.io.*;

public class Schedulermain {
		
	public static void main(String[] args) throws IOException{
		Boolean verbose=false;
		String file="unread";
		ArrayList<Process> all_processes=new ArrayList<Process>();
		
		if(args.length==1)
			{verbose = false;
			file=args[0];
			}
		if(args.length==2)
			{verbose = true;
			file=args[1];
			}
		
		Scanner s=new Scanner(new File(file));
		
		int a,b,c,io;
		int np=s.nextInt();
		for(int i=0;i<np;i++)
		{
			a= s.nextInt();
			b= s.nextInt();
			c= s.nextInt();
			io=s.nextInt();
			Process p=new Process(a,b,c,io,i);
			all_processes.add(p);
		}
		
		int quantum=2;
		ArrayList<Process> uniprocesses=new ArrayList<Process>();
		Iterator<Process> it = all_processes.iterator();
		while (it.hasNext()){
			uniprocesses.add(new Process(it.next()));
		}
		Uniprogrammed uni=new Uniprogrammed(uniprocesses,verbose);
		
		ArrayList<Process> fcfsprocesses=new ArrayList<Process>();
		Iterator<Process> it1 = all_processes.iterator();
		while (it1.hasNext()){
			fcfsprocesses.add(new Process(it1.next()));
		}
		FCFS fcfs = new FCFS(fcfsprocesses,verbose);
		
		ArrayList<Process> rrprocesses=new ArrayList<Process>();
		Iterator<Process> it2 = all_processes.iterator();
		while (it2.hasNext()){
			rrprocesses.add(new Process(it2.next()));
		}
		RR rr = new RR(rrprocesses,verbose,quantum);
		
		ArrayList<Process> psjfprocesses=new ArrayList<Process>();
		Iterator<Process> it3 = all_processes.iterator();
		while (it3.hasNext()){
			psjfprocesses.add(new Process(it3.next()));
		}
		PSJF psjf= new PSJF(psjfprocesses,verbose);
	}
}
