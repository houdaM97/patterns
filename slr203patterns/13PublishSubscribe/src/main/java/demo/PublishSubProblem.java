package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import demo.Publisher.Message;
import demo.Publisher.SendMessage;
import demo.Subscriber.LetsSubscribe;
import demo.Subscriber.Unsubscribe;;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class PublishSubProblem {

	public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("system");
        final ActorRef topic1 = system.actorOf(Topic.createActor(), "topic1");
        final ActorRef topic2 = system.actorOf(Topic.createActor(), "topic2");
    
        final ActorRef a = system.actorOf(Subscriber.createActor(), "a");
        final ActorRef b = system.actorOf(Subscriber.createActor(), "b");
        //we create the actor a1 of type FirstActor and gives a2 as a reference during the construction
        final ActorRef c = system.actorOf(Subscriber.createActor(), "c");
        final ActorRef publisher1 = system.actorOf(Publisher.createActor(), "publisher1");
        final ActorRef publisher2 = system.actorOf(Publisher.createActor(), "publisher2");

        a.tell(new LetsSubscribe(topic1), ActorRef.noSender());
        b.tell(new LetsSubscribe(topic1), ActorRef.noSender());
        b.tell(new LetsSubscribe(topic2), ActorRef.noSender());
        c.tell(new LetsSubscribe(topic2), ActorRef.noSender());
        publisher1.tell(new SendMessage(new Message("hello"), topic1), ActorRef.noSender());
        publisher2.tell(new SendMessage(new Message("world"), topic2), ActorRef.noSender());
        a.tell(new Unsubscribe(topic1), ActorRef.noSender());
        publisher1.tell(new SendMessage(new Message("hello"), topic1), ActorRef.noSender());
        
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
