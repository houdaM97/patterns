package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
    }
    
    private ActorRef sm;
	
    public Client() {}
    
    public Client(ActorRef sm) {
        this.sm = sm;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef sm) {
		return Props.create(Client.class, () -> {
			return new Client(sm);
		});
	}

	static public class SendMessage {
        public Message m;
		public SendMessage(Message m) {
            this.m = m;
		}
    }

    static public class CreateSession {
        public CreateSession() {

        }
    }

    static public class EndSession {
        public ActorRef s;
        public EndSession(ActorRef s) {
            this.s = s;
        }
    }

    public void start(ActorRef s){
        s.tell(new Message("m1"), ActorRef.noSender());
        s.tell(new Message("m3"), ActorRef.noSender());
        sm.tell(new EndSession(s), ActorRef.noSender());
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof CreateSession){
            this.sm.tell(new CreateSession(), getSelf());
            log.info(getSelf().path().name()+" ask for creating a session.");
        }
        if (message instanceof ActorRef){
            start((ActorRef)message);
        }
        if(message instanceof Message){
            log.info("["+getSelf().path().name()+"] receives message: "+((Message)message).data+" from ["+ getSender().path().name() +"]");
        
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