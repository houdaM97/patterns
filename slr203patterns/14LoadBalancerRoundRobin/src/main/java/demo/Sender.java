package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Sender extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    private ActorRef loadBalancer;

    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
	}
	
	public Sender() {}

	public Sender(ActorRef loadBalancer) {
        this.loadBalancer = loadBalancer;
		log.info("I was linked to loadBalancer {}", this.loadBalancer);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef loadBalancer) {
		return Props.create(Sender.class, () -> {
			return new Sender(loadBalancer);
		});
	}

	static public class SendMessage {
        public Message m;
		public SendMessage(Message m) {
            this.m = m;
		}
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof SendMessage){
			loadBalancer.tell(((SendMessage)message).m, getSelf());
            log.info("["+getSelf().path().name()+"] sends message to ["+ this.loadBalancer.path().name() +"]");
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