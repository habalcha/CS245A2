Name: Hermi Balcha
Date: December 10, 2020

Analysis of Six Degree's of Kevin Bacon:

This project has 2 implementation of finding the shortest path. One using connectActor() and another using connectShortPath()

connectActor() - for this function, I initially used the set intersection of the two actors. This gives me the shortest path
when path < 4 without having to call connectShortPath(). If the connection could not be found then, it calls connectShortPath()
and recursively checks the hashmap.

connectShortPath() - this is a Dijkstra's algorithm implementation of six degrees of Kevin Bacon and has a run time of
 O(|V|2 lg |V|).

 I left the code running connectShortPath() but they can be switched (one implementation is called on line 205 and the other on 208)

 To handle the JSON file I used org.json.simple.

 P.S: I was having trouble with the JSON file initially but I found the issue and the project should run with no errors.
