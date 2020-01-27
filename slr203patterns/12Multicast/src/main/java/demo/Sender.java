package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.LinkedList;


public class Sender extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
	}
	
	// Actor reference
	private ActorRef multicaster;
	
	public Sender() {}

	public Sender(ActorRef multicaster) {
        this.multicaster = multicaster;
		log.info("I was linked to multicaster {}", this.multicaster);
	}

	// Static function creating actor Props
	public static Props createActor(ActorRef multicaster) {
		return Props.create(Sender.class, () -> {
			return new Sender(multicaster);
		});
	}

	static public class SendMessage {
        public Message m;
        public int groupId;
		public SendMessage(Message m, int groupId) {
            this.m = m;
            this.groupId = groupId;
		}
    }
    
    static public class CreateGroup {
        public int id;
        public LinkedList<ActorRef> actors;
		public CreateGroup(int id, LinkedList<ActorRef> as) {
            this.id = id;
            this.actors = new LinkedList<ActorRef>(as);
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {
	
        if(message instanceof CreateGroup){
            this.multicaster.tell(((CreateGroup)message), getSelf());
            log.info(getSelf()+" sends request to create group "+((CreateGroup)message).id);
        }
        if(message instanceof SendMessage){
			multicaster.tell(((SendMessage)message), getSelf());
            log.info("["+getSelf().path().name()+"] sends message to ["+ this.multicaster.path().name() +"]");
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