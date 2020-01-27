package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.SecondActor.LetsJoin;
import demo.FirstActor.Start;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class BroadcastProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");
        final ActorRef a2 = system.actorOf(SecondActor.createActor(broadcaster), "a2");
        final ActorRef a3 = system.actorOf(ThirdActor.createActor(broadcaster), "a3");
		//we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef a1 = system.actorOf(FirstActor.createActor(broadcaster), "a1");
        
        a2.tell(new LetsJoin(), ActorRef.noSender());
        a3.tell(new LetsJoin(), ActorRef.noSender());
        a1.tell(new Start(), ActorRef.noSender());

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
		Thread.sleep(5000);
	}
}
