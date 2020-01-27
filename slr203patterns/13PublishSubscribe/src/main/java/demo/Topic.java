package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.LinkedList;
import demo.Publisher.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.Publisher.SendMessage;
import demo.Subscriber.Unsubscribe;

public class Topic extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public LinkedList<ActorRef> group;

	// Empty Constructor
	public Topic() {
        group = new LinkedList<ActorRef>();
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
		});
	}
    
    static public class Subscribe {
        public ActorRef actor;
		public Subscribe(ActorRef a) {
            this.actor = a;
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof Subscribe){
            group.add(((Subscribe)message).actor);
            log.info("The topic add "+((Subscribe)message).actor+" in group of " +getSelf().path().name());
        }
        if (message instanceof Unsubscribe){
            if(group.contains(getSender())){
                group.remove(getSender());
                log.info("The topic removes "+getSender()+" in topic " +getSelf().path().name());
        
            } else {
                log.info(getSender().path().name()+" not found in subscribers list of " +getSelf().path().name());
        
            }
        }
        if(message instanceof SendMessage){
            Message m = ((SendMessage)message).m;
            LinkedList<ActorRef> actors = group;
            for (ActorRef a: actors){
                a.tell(m, getSender());
                log.info(getSelf().path().name()+" sends message:' "+m.data+" '' from "+ getSender().path().name()+" to "+a.path().name());
            }
            
        }
	}
	
	
}