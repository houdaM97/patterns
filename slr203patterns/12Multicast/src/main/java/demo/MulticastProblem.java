package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Sender.CreateGroup;
import demo.Sender.Message;
import demo.Sender.SendMessage;
import java.util.LinkedList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class MulticastProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef multicaster = system.actorOf(Multicaster.createActor(), "multicaster");
        final ActorRef r1 = system.actorOf(Receiver.createActor(), "r1");
        final ActorRef r2 = system.actorOf(Receiver.createActor(), "r2");
        //we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef r3 = system.actorOf(Receiver.createActor(), "r3");
        final ActorRef sender = system.actorOf(Sender.createActor(multicaster), "sender");
        
        LinkedList<ActorRef> group1 = new LinkedList<ActorRef>();
        LinkedList<ActorRef> group2 = new LinkedList<ActorRef>();

        group1.add(r1);
        group1.add(r2);
        group2.add(r2);
        group2.add(r3);

        sender.tell(new CreateGroup(1, group1), ActorRef.noSender());
        sender.tell(new CreateGroup(2, group2), ActorRef.noSender());
        sender.tell(new SendMessage(new Message("hello"), 1), ActorRef.noSender());
        sender.tell(new SendMessage(new Message("world"), 2), ActorRef.noSender());
        
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
