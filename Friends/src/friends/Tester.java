package friends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		System.out.print("Enter word file: ");
		String wordsFile = scan.nextLine();
		Scanner sc = new Scanner(new File(wordsFile));
		
		Graph g = new Graph(sc);
		
		/*		//Testing Shortest Path
		//while(true) {
			System.out.println("\nEnter Person 1: ");
			String p1 = scan.nextLine();
			System.out.println("Enter Person 2: ");
			String p2 = scan.nextLine();
		
			ArrayList<String> path = Friends.shortestChain(g, p1, p2);
			if(path == null) System.out.print("Null List (No connection)");
			else for(String p:path)System.out.print(p + " ");
		//}*/
		
		
/********************************************************************************************/
		
				//Testing Cliques
		
		System.out.println("Enter School: ");
		String school = scan.nextLine();
		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
		
		if(cliques == null) System.out.print("null");
		else {
			for(ArrayList<String> arr: cliques) {
				for(String person:arr) {
					System.out.print(person + " ");
				}
				System.out.println();
			}
		}
		
		
		
/************************************************************************************************/
		/*//Test for Connector
		System.out.println("Now printing all connectors: ");
		ArrayList<String> connectors = Friends.connectors(g);
		
		if(connectors == null) {
			System.out.println("null");
		} else {
			for(String e: connectors) {
				System.out.print(e + " ");
			}
			System.out.print("\n");
			System.out.println("\nDone!");
		}
	*/
	}
}
