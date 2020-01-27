package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Sender.Start;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class ElasticLBProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        int max_receivers = 2;
        final ActorRef lb = system.actorOf(LoadBalancer.createActor(max_receivers), "lb");
    
        final ActorRef sender = system.actorOf(Sender.createActor(lb), "a");
        
        sender.tell(new Start(), ActorRef.noSender());
        //Do we have to wait until the session is created?
        
        
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