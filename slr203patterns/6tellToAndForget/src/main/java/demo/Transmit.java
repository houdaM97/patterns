package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.FirstActor.Start;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class Transmit {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("system");
        final ActorRef a2 = system.actorOf(SecondActor.createActor(), "a2");
        final ActorRef t = system.actorOf(Transmitter.createTransmitter(), "t");
		//we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef a1 = system.actorOf(FirstActor.createActor(a2, t), "a1");
        
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
