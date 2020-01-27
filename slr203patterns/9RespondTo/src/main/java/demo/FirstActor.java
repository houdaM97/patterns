package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
    
    static public int count = 0;
    
    static public class Request {
        public final int id;
        public final String data;
        public final ActorRef sendTo;
	
		public Request(String data, ActorRef sendTo) {
            this.id = count++;
            this.data = data;
            this.sendTo = sendTo;
		}
    }

	

	private ActorRef actorRef_b;
    private ActorRef actorRef_c;

	public FirstActor() {}

	public FirstActor(ActorRef a_b, ActorRef a_c) {
        this.actorRef_b = a_b;
        this.actorRef_c = a_c;
		log.info("I was linked to actor reference {}", this.actorRef_b);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef a_b, ActorRef a_c) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(a_b, a_c);
		});
	}

	static public class Start {
	
		public Start() {

		}
	}

    public void start(){
        if(this.actorRef_b != null){
            actorRef_b.tell(new Request("req1", actorRef_c), getSelf());
            log.info("["+getSelf().path().name()+"] sends request to ["+ this.actorRef_b.path().name() +"]");
		    
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