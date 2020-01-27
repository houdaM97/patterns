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
    private ActorRef actorRef;
    private ActorRef transmitter;

	public FirstActor() {}

	public FirstActor(ActorRef actorRef, ActorRef transmitter) {
        this.actorRef = actorRef;
        this.transmitter = transmitter;
		log.info("I was linked to actor reference {}", this.actorRef);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef actorRef, ActorRef transmitter) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef, transmitter);
        });
        
    }
    static public class MyMessage {
		public final String data;
	
		public MyMessage(String data) {
			this.data = data;
		}
	}
	
	static public class Start {
	
		public Start() {

		}
	}
    
    public void start(String m){
        if(this.transmitter != null && this.actorRef != null){
            transmitter.tell(this.actorRef, getSelf());
            transmitter.tell(new MyMessage(m), getSelf());
            log.info("["+getSelf().path().name()+"] sends message to ["+ this.transmitter.path().name() +"]");
		    //log.info("Actor reference updated ! New reference is: {}", this.actorRef);
        
        }
    }

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start){
			start("hello");
		}
	}



}