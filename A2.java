//javac -cp  \lib\json-simple-1.1.1.jar A2.java
import java.lang.String;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;  // handle errors
import java.util.Scanner; // Read text files
import java.util.HashMap;
import org.json.simple;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class A2 {


    public void A2() {

    }

    // read file from provided directory
    public static Scanner read_file(String file_path) {
        try {
            File movies = new File(file_path); // read in csv file
            Scanner movie_reader = new Scanner(movies);

            // read in individual csv entries
            while (movie_reader.hasNextLine()) {
                String data = movie_reader.nextLine();
            }

            movie_reader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //////////////////////////////// assign cast and crew
//        parse_json(cast);
//        parse_json(crew);

    }

    public LinkedList neighbor(String actor){
        return new LinkedList(actor_list.get(actor));
    }

    //We utilize a HashMap to hold our adjacency List in this implementation of a graph
    Map<String, Set<String>> actor_list = new HashMap<>();

    public static void main(String[] args) throws Exception {
        //Read in CSV file by first splitting by commas, then parse the data formatted in JSON using JSON parse
        String splitByComma = ",\"";
        String fileName = args[0]; //"src/tmdb_5000_credits.csv";
        Assignment3 connection = new Assignment3();

        //tmdb_5000_credits.csv

        LinkedList listofNames = new LinkedList();
        BufferedReader  reader = new BufferedReader(new FileReader(fileName));
        String l = "";

        try {
            l = reader.readLine();
            while ((l = reader.readLine()) != null) {
                //blank casts
                if(l.contains("[],[]")){
                    l = reader.readLine();
                }
                //build String array that holds all the info
                String[] castinfo = l.split(splitByComma);
                String cast = "";

                //File had to be converted to a JSON Format, there were double quotes instead of single quotes. Replace doubles with singles
                if(castinfo[1].contains("[{"))
                    cast = castinfo[1].replace("\"\"","\"");
                    //checks for quotes within movie names
                else if(castinfo[2].contains("[{"))
                    cast = castinfo[2].replace("\"\"","\"");
                //builds the Parse and JSON array
                JSONParser parser = new JSONParser();
                JSONArray castArray;
                try{
                    castArray  = (JSONArray) parser.parse(cast);
                }catch(Exception e){ //catches Exceptions that were thrown because of weird data within the cast JSON
                    l = reader.readLine();
                    castinfo = l.split(splitByComma);
                    cast = "";
                    if(castinfo[1].contains("[{"))
                        cast = castinfo[1].replace("\"\"","\"");
                    castArray  = (JSONArray) parser.parse(cast);
                }

                //Now we add the values into our graph
                for(Object o : castArray){ //Go through each name in cast
                    JSONObject one = (JSONObject) o;
                    String name = ((String) one.get("name")).toLowerCase();
                    //checks if Name exists in Map
                    if(!connection.containsActor(name))
                        connection.addActor(name);
                    for(Object j : castArray){ //builds edge to cast member 1
                        JSONObject two = (JSONObject) j;
                        //convert everything to LowerCase so you do not have to worry about it within the PathFinders
                        String name2 = ((String) two.get("name")).toLowerCase();
                        if(!connection.containsActor(name2))
                            connection.addActor(name2);
                        connection.AddEdge(name, name2);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // getting the user's inputs
        getNames(connection);
    }

    // parse through json cast and crew
//    public static void parse_json(String[] cast) {
//        // parese json cast and crew entries
//        JSONParser parser = new JSONParser();
//        JSONArray castArray;
//        castArray = (JSONArray) parser.parse(cast);


    public LinkedList connect_actors(String actor1, String actor2) {
        LinkedList visited = new LinkedList(); // holds values that have already been visited
        LinkedList path = new LinkedList(); // path to connect the two actors

        // if we are connecting the same actor, return the actors name
        if (actor1.equals(actor2)) {
            path.add(actor1);
            return path;
        }


        for (String s : actor_list.keySet()) {
            if (s.equals(actor1)) {
                path.add(s);
                visited.add(s);
            }
        }
            path.add(actor1); // add the first
            while (!path.isEmpty()) {
                String name = path.remove();
                visited.add(name);
                if (isANeighbor(actor1, actor2)) {
                    for (String values : actor_list.keySet()) {
                        values = values.toLowerCase();
                        if (values.equals(actor2)) {
                            path.add(values);
                            visited.add(values);
                            return path;
                        }
                    }
                }
            }
    }

    public boolean isANeighbor(String actor1, String actor2){
        Set<String> act1 = actor_list.get(actor1);
        if(act1.contains(actor2)) {
            return true;
        }return false;
    }

    public LinkedList computePaths(String actor1, String actor2) {
        LinkedList visited = new LinkedList(); // holds values that have already been visited
        LinkedList path = new LinkedList();
        if (actor1.equals(actor2)) {
            path.add(actor1);
            return path;
        }for (String s : actor_list.keySet()) {
            if (s.equals(actor1)) {
                path.add(s);
                visited.add(s);
            } }
        path.add(actor1); // add the first
        while (!path.isEmpty()) {
            String name = path.remove();
            visited.add(name);
            if (isANeighbor(actor1, actor2)) {
                for (String values : actor_list.keySet()) {
                    values = values.toLowerCase();
                    if (values.equals(actor2)) {
                        path.add(values);
                        visited.add(values);
                        return path; } }
            }
            LinkedList neighbor = new LinkedList(neighbor(name)); /////////////issue here
            for(int i = 0; i < neighbor.size(); i++){
                path.add(name);
                path.add(neighbor.get(i));

                if(!visited.contains(neighbor.get(i))){
                    path.add(neighbor.get(i));
                    visited.add(neighbor.get(i));
                }
            }
        }
        return null;

    }

    //adds an Edge
    public void AddEdge(String actor1, String actor2){
        actor_list.get(actor2).add(actor1);
        actor_list.get(actor1).add(actor2);
    }

    public void addActor(String name){
        actor_list.put(name, new HashSet<>());
    }



    public boolean containsActor(String actor){
        if(actor_list.containsKey(actor)){
            return true;
        }
        else{
            return false;
        }
    }

    public void checkPaths(){}

    public void containsActor(){}



    public static void main (String[] args){
//        String file_path = args[0];
        read_file("tmdb_5000_credits.csv");
        LinkedList T = new LinkedList();
    }
}




