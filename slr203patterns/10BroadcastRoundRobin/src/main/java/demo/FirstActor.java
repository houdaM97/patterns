package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
	}
	
	// Actor reference
	private ActorRef broadcaster;
	
	public FirstActor() {}

	public FirstActor(ActorRef broadcaster) {
        this.broadcaster = broadcaster;
		log.info("I was linked to broadcaster {}", this.broadcaster);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(broadcaster);
		});
	}

	static public class Start {
	
		public Start() {

		}
	}

    public void start(){
        if(this.broadcaster != null){
            broadcaster.tell(new Message("m"), getSelf());
            log.info("["+getSelf().path().name()+"] sends message to ["+ this.broadcaster.path().name() +"]");
		    
        }
    }

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start){
			start();
		}
	}


	/**
	 * alternative for AbstractActor
	 * @Override
	public Receive createReceive() {
		return receiveBuilder()
				// When receiving a new message containing a reference to an actor,
				// Actor updates his reference (attribute).
				.match(ActorRef.class, ref -> {
					this.actorRef = ref;
					log.info("Actor reference updated ! New reference is: {}", this.actorRef);
				})
				.build();
	}
	 */
}