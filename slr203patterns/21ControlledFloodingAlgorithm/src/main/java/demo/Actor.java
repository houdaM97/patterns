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

    private int seqNumb = -1;

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

    static public class Message {
        public int id;
 
        public Message(int id) {
            this.id = id;
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
        if(message instanceof Message){
            if(((Message)message).id != seqNumb){
                this.seqNumb = ((Message)message).id;
                log.info(getSelf().path().name()+" receives a message from "+getSender().path().name());
                for(ActorRef a : this.knownActors){
                    a.tell(message, getSelf());
                    log.info(getSelf().path().name()+" sends a message to "+a.path().name());
                    Thread.sleep(1000);
                }
            } else {
                log.info(getSelf().path().name()+" drops a message from "+getSender().path().name());
                
            }
            
        }
	}
	
	
}