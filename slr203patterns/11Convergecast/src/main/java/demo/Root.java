package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.Merger.Message;


public class Root extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	public Root() {}

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Root.class, () -> {
			return new Root();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message){
			log.info("["+getSelf().path().name()+"] receives message : "+ ((Message)message).data +" from ["+ getSender().path().name() +"]");   
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