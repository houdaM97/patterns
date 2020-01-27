package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.Sender.Tache;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef lb;

    public Receiver(ActorRef lb) {
        this.lb = lb;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef lb) {
		return Props.create(Receiver.class, () -> {
			return new Receiver(lb);
		});
    }

    static public class Finish {
        public final int id;;
 
		public Finish(int id) {
            this.id = id;
		}
    }
    
    public void executeTask(int id) throws InterruptedException {
        Thread.sleep(1000);
        this.lb.tell(new Finish(id), getSelf());
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Tache){
            executeTask(((Tache)message).id);
            log.info(getSelf().path().name()+ " receives task: ' "+((Tache)message).id+" ' from "+getSender().path().name());
        }
	}
	
	
}