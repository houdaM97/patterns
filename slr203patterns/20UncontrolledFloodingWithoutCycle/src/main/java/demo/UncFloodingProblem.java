package demo;

import java.util.HashMap;
import java.util.LinkedList;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Actor.KnownActors;
import demo.Actor.Message;



/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class UncFloodingProblem {

	public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("system");
        
        int nb_actors = 5;
        int[][] adjMatrix = {{0,1,1,0,0},{0,0,0,1,0},{0,0,0,1,0},{0,0,0,0,1},{0,1,0,0,0}};
        LinkedList<ActorRef> actors = new LinkedList<ActorRef>();
        HashMap<ActorRef, LinkedList<ActorRef>> topology = new HashMap<ActorRef, LinkedList<ActorRef>>();
        
        for(int i=0; i < nb_actors; i++){
            ActorRef a = system.actorOf(Actor.createActor(), "a"+i);
            actors.add(a);
        }
        
        for(int i=0; i < nb_actors; i++){
            LinkedList<ActorRef> knownActors = new LinkedList<ActorRef>();
            for(int j=0; j<nb_actors; j++){
                if(adjMatrix[i][j]==1){
                    knownActors.add(actors.get(j));
                }
            }
            topology.put(actors.get(i), knownActors);
            actors.get(i).tell(new KnownActors(knownActors), ActorRef.noSender());
            Thread.sleep(1000);
        }
        actors.get(0).tell(new Message(0), ActorRef.noSender());
        

	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(10000);
	}
}