package demo;

import akka.actor.Props;

import java.util.LinkedList;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Actor extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private LinkedList<ActorRef> knownActors;

    public Actor() {
        this.knownActors = new LinkedList<ActorRef>();
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
    }

    static public class KnownActors {
        public LinkedList<ActorRef> actors;
 
        public KnownActors(LinkedList<ActorRef> actors) {
            this.actors = new LinkedList<ActorRef>(actors);
        }
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof KnownActors){
            this.knownActors = ((KnownActors)message).actors;
            log.info(getSelf().path().name()+ " receives the known actors :");
            for(ActorRef a : this.knownActors){
                log.info(a.path().name()+ " ");
            }
        }
	}
	
	
}