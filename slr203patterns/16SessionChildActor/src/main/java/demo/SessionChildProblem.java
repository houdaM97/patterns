package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Client.CreateSession;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class SessionChildProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef sm = system.actorOf(SessionManager.createActor(), "sessionManager");
    
        final ActorRef client = system.actorOf(Client.createActor(sm), "client1");
        
        client.tell(new CreateSession(), ActorRef.noSender());
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