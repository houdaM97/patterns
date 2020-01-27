package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.FirstActor.MyMessage;

public class Transmitter extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
    private ActorRef actorRef;

	public Transmitter() {}

	// Static function creating actor
	public static Props createTransmitter() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof ActorRef){
		this.actorRef = (ActorRef) message;
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		log.info("Actor reference updated ! New reference is: {}", this.actorRef);
        }
        if(message instanceof MyMessage){
            this.actorRef.tell((MyMessage) message, getSender());
            log.info("The message is send to the target reference : {}", this.actorRef);
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
