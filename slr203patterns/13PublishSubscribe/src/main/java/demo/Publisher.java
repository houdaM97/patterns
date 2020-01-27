package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Publisher extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
	}
	
	// Actor reference
	
	public Publisher() {}

	/*public Publisher(ActorRef topic) {
        this.topic = topic;
		log.info("I was linked to topic {}", this.topic);
	}*/

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Publisher.class, () -> {
			return new Publisher();
		});
	}

	static public class SendMessage {
        public Message m;
        public ActorRef topic;
		public SendMessage(Message m, ActorRef topic) {
            this.m = m;
            this.topic = topic;
		}
    }

	@Override
	public void onReceive(Object message) throws Throwable {

        if(message instanceof SendMessage){
			((SendMessage)message).topic.tell(((SendMessage)message).m, getSelf());
            log.info("["+getSelf().path().name()+"] publishes a message in ["+ ((SendMessage)message).topic.path().name() +"]");
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