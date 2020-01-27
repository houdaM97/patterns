package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.FirstActor.LetsJoin;
import demo.FirstActor.LetsSendM;
import demo.FirstActor.LetsUnjoin;
import demo.Merger.Message;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class ConvergeCastProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef root = system.actorOf(Root.createActor(), "root");
        final ActorRef merger = system.actorOf(Merger.createActor(root), "merger");
        final ActorRef a1 = system.actorOf(FirstActor.createActor(merger), "a1");
        final ActorRef a2 = system.actorOf(FirstActor.createActor(merger), "a2");
        //we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef a3 = system.actorOf(FirstActor.createActor(merger), "a3");
        
        a1.tell(new LetsJoin(), ActorRef.noSender());
        a2.tell(new LetsJoin(), ActorRef.noSender());
        a3.tell(new LetsJoin(), ActorRef.noSender());
        a1.tell(new LetsSendM(new Message("hi")), ActorRef.noSender());
        a2.tell(new LetsSendM(new Message("hi")), ActorRef.noSender());
        a3.tell(new LetsSendM(new Message("hi")), ActorRef.noSender());
        a3.tell(new LetsUnjoin(), ActorRef.noSender());
        a1.tell(new LetsSendM(new Message("hi2")), ActorRef.noSender());
        a2.tell(new LetsSendM(new Message("hi2")), ActorRef.noSender());

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
