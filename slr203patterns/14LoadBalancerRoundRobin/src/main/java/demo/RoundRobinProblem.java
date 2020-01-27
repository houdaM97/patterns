package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Sender.Message;
import demo.Sender.SendMessage;
import demo.Receiver.Unjoin;
import demo.Receiver.Join;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class RoundRobinProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef lb = system.actorOf(LoadBalancer.createActor(), "loadBalancer");
    
        final ActorRef c = system.actorOf(Receiver.createActor(lb), "c");
        final ActorRef b = system.actorOf(Receiver.createActor(lb), "b");
        //we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef a = system.actorOf(Sender.createActor(lb), "a");
        
        b.tell(new Join(), ActorRef.noSender());
        c.tell(new Join(), ActorRef.noSender());
        a.tell(new SendMessage(new Message("m1")), ActorRef.noSender());
        a.tell(new SendMessage(new Message("m2")), ActorRef.noSender());
        a.tell(new SendMessage(new Message("m3")), ActorRef.noSender());
        c.tell(new Unjoin(), ActorRef.noSender());
        a.tell(new SendMessage(new Message("m4")), ActorRef.noSender());
        
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