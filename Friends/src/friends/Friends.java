package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		if(g == null) return null;
		if(!g.map.containsKey(p1) || !g.map.containsKey(p2)) return null;
		if(p1.equals(p2)) return null;
		int size = g.members.length;
		int[] visited = new int[size];
		for(int i = 0; i < size; i++) {
			visited[i] = -1;
		}
		int p1num = g.map.get(p1);
		int p2num = g.map.get(p2);
		visited[p2num] = p2num;
		
		Queue<Integer> personNumQ = new Queue<>();
		Friend ptr = g.members[p2num].first;
		
		while(ptr != null) {
			visited[ptr.fnum] = p2num;
			personNumQ.enqueue(ptr.fnum);
			ptr = ptr.next;
		}
		
		while(visited[p1num] == -1) {
			if(personNumQ.isEmpty()) break;
			int nextSearch = personNumQ.dequeue();
			ptr = g.members[nextSearch].first;
			int prevPerson = g.map.get(g.members[nextSearch].name);
			while(ptr != null) {
				int num = ptr.fnum;
				if(visited[num] == -1) {
					visited[num] = prevPerson;
					personNumQ.enqueue(num);
				}
				ptr = ptr.next;
			}
			if(personNumQ.isEmpty()) break;
		}
		
		if(visited[p1num] == -1) return null;
		
		int cur = p1num;
		ArrayList<String> path = new ArrayList<>();
		while(cur != p2num) {
			path.add(g.members[cur].name);
			cur = visited[cur];
		}
		path.add(g.members[cur].name);
		return path;
		
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		if(g == null) return null;
		if(g.members.length == 0) return null; //Check to make sure it isn't 0
		
		int size = g.members.length;
		boolean[] visited = new boolean[size];
		for(int i = 0; i < size; i++) {
			visited[i] = false;
		}
		Queue<Integer> peeps = new Queue<>();
		Queue<Integer> clique = new Queue<>();

		ArrayList<String> tempClique = new ArrayList<>();						//This will be the cliques that we add
		ArrayList<ArrayList<String>> allClique = new ArrayList<>();				//This will be the thing we return

		if(g.members[0].school != null && g.members[0].school.equals(school)) { //find out which thing to put the first item into
			clique.enqueue(0);
			tempClique.add(g.members[0].name);
		} else {
			peeps.enqueue(0);
		}
		
		Friend ptr = null;
		
		while(!peeps.isEmpty() || !clique.isEmpty()) { //Keep going while there's something. At the end of this loop, we make sure everything's been hit
			while(!clique.isEmpty()) { //First go through all the same school people to find the whole clique
				int num = clique.dequeue();
				ptr = g.members[num].first;
				while(ptr != null) {
					if(visited[ptr.fnum]) { //If we already completed a visit, there's no need to requeue & readd
						ptr = ptr.next;
						continue;
					}
					String perSchool = g.members[ptr.fnum].school;
					if(perSchool != null && perSchool.equals(school)) { //If this friend is in the same school, add to clique and add him to clique queue to check his friends
						tempClique.add(g.members[ptr.fnum].name);
						clique.enqueue(ptr.fnum);
					} else { //This friend isn't in the same school, but his friends might be, so add him to people queue and check his friends later
						peeps.enqueue(ptr.fnum);
					}
					ptr = ptr.next;
				}
				visited[num] = true; //We've visited all of the friends of this node, so we're done with it.
			}
			//If we reach here, we've finished making a clique. Add to list of cliques if there's actually something to add and reset it
			if(!tempClique.isEmpty()) {
				allClique.add(tempClique);
				tempClique = new ArrayList<>();
			}
				
			while(!peeps.isEmpty()) { //Since clique is empty, we'll go through peeps to search for the next clique
				int num = peeps.dequeue();
				ptr = g.members[num].first;
				while(ptr != null) {
					if(visited[ptr.fnum]) { //Visited = no new info
						ptr = ptr.next;
						continue;
					}
					String perSchool = g.members[ptr.fnum].school;
					if(perSchool != null && perSchool.equals(school)) { //Check if this node's friend is in the school. If it is, we add to clique queue and break
						clique.enqueue(ptr.fnum);
						tempClique.add(g.members[ptr.fnum].name);
						break; //We break because we don't want to accidentally add the same person twice by having 2 people enqueued in the same clique
					}
					peeps.enqueue(ptr.fnum); //Not related to the school, we'll check him later
					ptr = ptr.next;
				}
				if(!clique.isEmpty()) break; //See previous break. If we break, we aren't done with this node
				else visited[num] = true; //If we don't break, we can just keep moving on because we've visited all the friends
			}
			
			if(peeps.isEmpty() && clique.isEmpty() && !allTrue(visited)) { //If there are islands, we need a driver to check all nodes in a school
				for(int i = 0; i< visited.length; i++) {
					if(!visited[i]) { //This is the driver. Find first uncompleted node, add it to the queue again and start checking the unvisited friends
						String perSchool = g.members[i].school;
						if(perSchool != null && perSchool.equals(school)) {
							clique.enqueue(i);
							tempClique.add(g.members[i].name);
							break;
						}
							peeps.enqueue(i);
							break;
					}
				}
			}
		}
		
		if(allClique.isEmpty()) return null;
		/** COMPLETE THIS METHOD **/
		//Remove the Duplicates
		for(ArrayList<String> clq: allClique) {
			for(int i = 0; i<clq.size(); i++) {
				for(int j =0; j<clq.size()-1; j++) {
					if(i==j) continue;
					if(clq.get(i).equals(clq.get(j))) {
						clq.remove(j);
						j--;
					}
				}
			}
		}
		return allClique;
		
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		if(g.members.length <= 2) return null;				//Impossible for there to be any connectors, plus numIsland doesn't work for size 0 or 1;
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> connectorList = new ArrayList<>();
		int gNumOfIslands = numOfIslands(g, -1);
		if(gNumOfIslands == -1) {
			return null;
		}
		
		for(int i = 0; i < g.members.length; i++) {
			int removedNumOfIslands = numOfIslands(g, i);
			if(removedNumOfIslands > gNumOfIslands) {
				connectorList.add(g.members[i].name);
			}
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		if(connectorList.isEmpty()) return null;
		return connectorList;
		
	}
	
	/**
	 * Counts number of disjointed islands there are in Graph g
	 * @param g The graph whose islands will be determined
	 * @param a The number of the node that should be removed before determining number of islands. -1 if no nodes are to be removed
	 * @return The number of islands (with possibly 1 removed node). -1 if there is a problem with a
	 */
	private static int numOfIslands(Graph g, int a) { //Counts number of islands in graph g (disjoint pieces)
		if(a < -1 || a >= g.members.length) return -1;
		int numOfIslands = 0;
		//Create the visited array, since we are gonna use this method to check how many island exist when a node is "removed"
		//We do this by setting visited to true for the node we want to remove, that way we dont access any of its friends.
		boolean notDone = true;
		boolean[] visited = new boolean[g.members.length];
		for(int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		if(a != -1) visited[a] = true;
		
		Queue<Integer> people = new Queue<>();
		Friend ptr = null;
		
		while(notDone) {
			int num = 0;
			while(visited[num]) {
				num++;
			}
			people.enqueue(num);
			visited[num] = true;
			while(!people.isEmpty()) {
				int checkNum = people.dequeue();
				ptr = g.members[checkNum].first;
				while(ptr != null) {
					if(!visited[ptr.fnum]) {
						people.enqueue(ptr.fnum);
					}
					ptr = ptr.next;
				}
				visited[checkNum] = true;
			}
			numOfIslands++;
			
			if(allTrue(visited)) {
				notDone = false;
			}
		}
		
		return numOfIslands;
	}
	
	private static boolean allTrue(boolean[] arr) {
		for(boolean b:arr) {
			if(!b) return false;
		}
		return true;
	}
}

