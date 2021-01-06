
//javac -cp  \lib\json-simple-1.1.1.jar A2.java
import java.lang.String;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  // handle errors
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner; // Read text files
import java.util.HashMap;
//import org.json.simple;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class A2 {
    // create a hashmap to store actor data
    // stores it as key = actor name and all the actors they are connected to as a set (sets bc we won't have any duplicates)
    // has add, put and remove functions
    Map<String, Set<String>> actor_list = new HashMap<>();


    public void A2() {} // end A2



    // check if the hashmap connects the two actors
    public boolean isConnected(String actor1, String actor2){

        // get set of actors connected to actor1
        Set<String> act1 = actor_list.get(actor1);
        if(act1.contains(actor2)) {
            return true; // is a neighbor
        }return false;
    } // end isConnected



    // get a list of all the actors connected to the given actor
    public Set<String> getActors(String actor){
        return actor_list.get(actor);
    } // end getActors



    //connect actor1 with actor2 by adding them to each others set
    public void AddEdge(String actor1, String actor2){
        // get values / set for both actors
        Set<String> act1 = actor_list.get(actor1);
        Set<String> act2 = actor_list.get(actor2);

        // add each actor to the others set
        act1.add(actor2);
        act2.add(actor1);

    } // end addEdge



    // check if actor is already in actor_list
    public boolean isKey(String actor){
        if(actor_list.containsKey(actor)){
            return true;
        }
        else{
            return false;
        }
    } // end isKey


    // read the json movie cast file
    static void readJson(String fileName, A2 a2) throws Exception{
        System.out.println("Adding cast information to hashmap...");

        String splitBy = ",\"";

        BufferedReader  reader = new BufferedReader(new FileReader(fileName));
        String read = "";

        try {
            read = reader.readLine();
            while ((read = reader.readLine()) != null) {
                //blank casts
                if(read.contains("[],[]")){
                    read = reader.readLine();
                }
                // split the current line into individual entries
                String[] movieCast = read.split(splitBy);
                String cast = "";

                // json file has "" c "" instead of "c"
                // change all instances of "" c "" for
                if(movieCast[1].contains("[{"))
                    cast = movieCast[1].replace("\"\"","\"");

                else if(movieCast[2].contains("[{"))
                    cast = movieCast[2].replace("\"\"","\"");


                // json parser to break up json text
                JSONParser parser = new JSONParser();

                // json array to store parsed json file
                JSONArray castArray;

                try{
                    castArray  = (JSONArray) parser.parse(cast); // parse cast information and add to array

                }catch(Exception e){

                    // reassign and split read
                    read = reader.readLine();
                    movieCast = read.split(splitBy);
                    cast = "";

                    // check if ""c"" occurs
                    if(movieCast[1].contains("[{"))
                        cast = movieCast[1].replace("\"\"","\"");

                    // add new cast line to array
                    castArray  = (JSONArray) parser.parse(cast);
                }

                // go through array and add values to hashmap
                for(Object arr_val : castArray){

                    JSONObject act = (JSONObject) arr_val;

                    String act1 = ((String) act.get("name")).toLowerCase(); // get value under name key

                    //checks if key name is already in the hashmap
                    if(!a2.isKey(act1))
                        a2.actor_list.put(act1, new HashSet<>());

                    // for each object in castArray, loop through the rest of the names
                    // create a new hashmap entry if it doesn't exist
                    // connect all of them to the first array value

                    for(Object co_cast : castArray){

                        JSONObject co_cast_val = (JSONObject) co_cast;
                        String act2 = ((String) co_cast_val.get("name")).toLowerCase();

                        // check if key already exists in map
                        if(!a2.isKey(act2))
                            a2.actor_list.put(act2, new HashSet<>());

                        // connect / add edge between the two actors
                        a2.AddEdge(act1, act2);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    } // end readJson



    public static void main(String[] args) throws Exception {

        // file name given as argument
        String fileName = args[0]; //"tmdb_5000_credits.csv";
        A2 a2 = new A2();

        String name1 = null;
        String name2 = null;
        String name_1_c = null;
        String name_2_c = null;

        // read file and add actors and casts to hashmap
        // this might fail
        A2.readJson(fileName, a2);

        // get user input for the two actors to connect
        Scanner check_actors  = new Scanner(System.in);
        System.out.print("Actor 1 name: ");

        // check if actor is in out hashmap
        name_1_c = check_actors.nextLine();
        name1 = name_1_c.toLowerCase();
        if(!a2.isKey(name1)){
            System.out.print("No such actor.\n");
        } else {
            // get actor 2
            System.out.print("Actor 2 name: ");

            name_2_c = check_actors.nextLine();
            name2 = name_2_c.toLowerCase();
            if(!a2.isKey(name2)){
                System.out.print("No such actor.\n");
                name2 = null; // to stop further execution
            }
        }
        check_actors.close();

        // find path if both name1 and name2 are in the hashmap
        if (name1 != null && name2 != null){

            System.out.println("\nFinding shortest path: ");
            // get a linked list of the path between actor1 and actor2

            // used the intersection of sets to find shortest path when path needs less than 3 connections
            // Kevin Bacon has an average connection of 3.18 (wekipedia) so most of the searches are less than 3 connections
            //LinkedList connectionPath = a2.connectActors(name1, name2);

            // uses Dijkstra’s algorithm to find the shortest path
            LinkedList connectionPath = a2.connectShortPath(name1, name2);

            if(connectionPath == null){
                System.out.println("No path was found");

            } else {

                String path = "";

                for(int i = 0; i < connectionPath.size(); i++){
                    if ( i == 0){
                        String name = (String) connectionPath.get(i);
                        String f_name = name.split(" ")[0];
                        f_name = f_name.substring(0, 1).toUpperCase() + f_name.substring(1);
                        String l_name = name.split(" ")[1];
                        l_name = l_name.substring(0, 1).toUpperCase() + l_name.substring(1);
                        path += f_name + " " + l_name;
                    } else {
                        String name = (String) connectionPath.get(i);
                        String f_name = name.split(" ")[0];
                        f_name = f_name.substring(0, 1).toUpperCase() + f_name.substring(1);
                        String l_name = name.split(" ")[1];
                        l_name = l_name.substring(0, 1).toUpperCase() + l_name.substring(1);
                        path += " --> " + f_name + " " + l_name;
                    }
                }
                System.out.println("Path between " + name_1_c + " and " + name_2_c + ": \n");
                System.out.print(path);
            }
        }
    } // end main



    // connectActors uses set properties to make search faster
    // similar to running connectShortPath(String actor1, String actor2)
    public LinkedList connectActors(String actor1, String actor2){
        LinkedList<String> visited = new LinkedList<String>(); // holds values that have already been visited
        LinkedList<String> path = new LinkedList<String>(); // path to connect the two actors

        // if we are connecting the same actor, return the actors name
        if (actor1.equals(actor2)) {
            path.add((String) actor1);
            return path;

        } else {

            // get actor set for both actors
            Set<String> act1 = actor_list.get(actor1);
            Set<String> act2 = actor_list.get(actor2);

            // get the intersection of these two actor sets
            Set<String> intersection = new HashSet<String>(act1);
            //System.out.println(intersection);
            intersection.retainAll(act2);

            // add the first actor to path
            path.add(actor1);

            // if there is no interection - there is no direct connection between these two actors
            // find shortest path from their actors list
            if (intersection.size() == 0){
                LinkedList<String> shortPath = shortestPath(actor1, actor2, 4, visited); // set entries to 4
                if(shortPath!=null && shortPath.size() > 0){
                    for (int i = 0; i < shortPath.size(); i++){
                        path.add(shortPath.get(i));
                    }
                    return path;
                }
            } else {

                // if the actors are directly connected to each other
                if (intersection.contains(actor2)){
                    path.add(actor2);
                    return path;
                }

                // if they are connected with the same individual
                if (intersection.size() > 0){
                    Iterator<String> i = intersection.iterator();
                    path.add(i.next());
                    path.add(actor2);
                    return path;
                }

            }
        }
        return null;
    } // end connectActors


    // function that finds the shortest path between two actors using Dijkstra’s algorithm // calls shortestPath
    // actor1 is the starting vector and algorithm finds the first connection between actor1 and actor2
    // we don't have cost/weight so we only care about the depth of the path
    public LinkedList connectShortPath(String actor1, String actor2){
        LinkedList<String> visited = new LinkedList<String>(); // holds values that have already been visited
        LinkedList<String> path = new LinkedList<String>(); // path to connect the two actors

        // if we are connecting the same actor, return the actors name
        if (actor1.equals(actor2)) {
            path.add((String) actor1);
            return path;
        } else {

            // add the first actor to path
            path.add(actor1);

            LinkedList<String> shortPath = shortestPath(actor1, actor2, 2, visited); // set entries to 4
            if(shortPath!=null && shortPath.size() > 0){
                for (int i = 0; i < shortPath.size(); i++){
                    path.add(shortPath.get(i));
                }
                return path;
            }
        }

        return null;
    } // end connectShortPath



    // shortest path implementation of Dijkstra’s algorithm
    public LinkedList<String> shortestPath(String actor1, String actor2, int entries, LinkedList<String> visited){

        // have already checked for paths with 1, 2 and 3 entries
        // find shortest path for 4, 5 and 6 entries

        while (entries < 7){
            LinkedList<String> path = new LinkedList<String>(); // path to connect the two actors

            // get actor set for both actors
            Set<String> act1 = actor_list.get(actor1);
            Set<String> act2 = actor_list.get(actor2);

            // iterate through the first actor set and find all the actors that are connected to actor 2
            Iterator itr = act1.iterator();
            while(itr.hasNext()){

                // actor connected to actor1
                String connAct = (String) itr.next();

                // check if this actor has been visited
                boolean hasValue = false;
                for (int j = 0; j  < visited.size(); j++){
                    if(connAct.equals(visited.get(j))){
                        hasValue = true;
                    }
                }

                // if this actor has not been visited
                if (hasValue == false){
                    visited.add(connAct); // add to visited list

                    // check if these two actors are connected;
                    // since
                    if(isConnected(connAct, actor2)){
                        path.add(connAct);
                        path.add(actor2);
                        return path;
                    } else {
                        path.add(actor2);
                        return path;
                    }
                }
            }

            // find next shortest path with path size entries + 1
            // restate itr to actor set of actor1
            itr = act1.iterator();
            while(itr.hasNext()){
                // actor connected to actor1
                String connAct = (String) itr.next();
                if (connAct != actor2){
                    return shortestPath(connAct, actor2, entries + 1, visited);
                } else {
                    return path;
                }

            }
        }
        return null;
    } // end ShortestPath;
    } // end A2




