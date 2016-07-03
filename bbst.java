import java.util.*;
import java.io.*;
 
public class bbst{
	public static void main(String [] args){ 
		String file = args[0];
		int[] K = null;
		int[] V = null;
		int size = 0;
		
		// reads the input file and store key,value pairs
		String line = null;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			line = bufferedReader.readLine();
			size = Integer.parseInt(line);
			
			K = new int[size];
			V = new int[size];
			
            for(int i=0;i<size;i++){
				line= bufferedReader.readLine();
                String[] temp = line.split(" ");
				K[i] = Integer.parseInt(temp[0]);
				V[i] = Integer.parseInt(temp[1]);
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {}
        catch(IOException ex) {}  
		
		// Initialize RedBlackTree
		RedBlackTree T = new RedBlackTree();
		T.Head = T.initialize(0,0,size-1,K,V);
		 
		 
		// Interactively deals with commands 
		Scanner sc = new Scanner(System.in); 
		String s = sc.next();
		
		while(!s.equals("quit")){
			if(s.equals("increase")){
				int ID = sc.nextInt();
				int m = sc.nextInt();
				T.increase(ID,m);
			}
			else if(s.equals("reduce")){
				int ID = sc.nextInt();
				int m = sc.nextInt();
				T.reduce(ID,m);
			}
			else if(s.equals("count")){
				int ID = sc.nextInt();
				T.count(ID);
			}
			else if(s.equals("inrange")){
				int ID1 = sc.nextInt();
				int ID2 = sc.nextInt();
				T.inrange(ID1,ID2);
			}
			else if(s.equals("next")){
				int ID = sc.nextInt();
				T.next(ID);
			}
			else if(s.equals("previous")){
				int ID = sc.nextInt();
				T.previous(ID);
			}
			s = sc.next();
		}
		
	}	
}