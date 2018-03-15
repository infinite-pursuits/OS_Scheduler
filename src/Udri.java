import java.io.*;
import java.util.*;

public class Udri {
	
	ArrayList<Integer> numbers = new ArrayList<Integer>();
	int index = 0;

	public Udri(){
		try {
			String fname = "random-numbers.txt";
			Scanner sc = new Scanner(new File(fname));
			while(sc.hasNextInt()){
			    numbers.add(sc.nextInt());
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
	}

	public int obtain_X(){
		int X = numbers.get(index);
		index=index+1;
		return X;
	}

	public void reset(){
		index = 0;
	}

}
