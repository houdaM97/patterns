package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.FirstActor.Request;

public class Intermediate extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	

	public Intermediate() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Intermediate.class, () -> {
			return new Intermediate();
		});
    }
    
    static public class Response {
        public int id;
		public final String data;
	
		public Response(String data, int id) {
            this.id = id;
			this.data = data;
		}
    }

	@Override
	public void onReceive(Object request) throws Throwable {
		if (request instanceof Request) {
            ((Request) request).sendTo.tell(new Response("res to "+((Request) request).data, ((Request) request).id), getSelf());
            log.info("["+getSelf().path().name()+"] sends response of request to ["+ ((Request) request).sendTo.path().name() +"]");
		    
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