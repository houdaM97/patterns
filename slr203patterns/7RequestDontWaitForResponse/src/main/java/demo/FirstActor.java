package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.SecondActor.Response;

public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
    static public int count = 0;
    
    static public class Request {
        public final int id;
        public final String data;
        public final ActorRef replyTo;
	
		public Request(String data, ActorRef replyTo) {
            this.id = count++;
            this.data = data;
            this.replyTo = replyTo;
		}
    }

	private ActorRef actorRef;

	public FirstActor() {}

	public FirstActor(ActorRef actorRef) {
		this.actorRef = actorRef;
		log.info("I was linked to actor reference {}", this.actorRef);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef actorRef) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef);
		});
	}

	static public class Start {
	
		public Start() {

		}
	}

    public void start(){
        if(this.actorRef != null){
            actorRef.tell(new Request("req1", getSelf()), getSelf());
            log.info("["+getSelf().path().name()+"] sends request 1 to ["+ this.actorRef.path().name() +"]");
		    actorRef.tell(new Request("req2", getSelf()), getSelf());
            log.info("["+getSelf().path().name()+"] sends request 2 to ["+ this.actorRef.path().name() +"]");
		    
        }
    }

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start){
			start();
		}
		if(message instanceof Response){
		log.info("["+getSelf().path().name()+"] received response from ["+ getSender().path().name() +" to request "+ ((Response) message).id +"]");
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