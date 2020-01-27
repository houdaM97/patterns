package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.LinkedList;
import demo.FirstActor.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Broadcaster extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    

    public static class Join{
        public ActorRef a;
        public Join(ActorRef a){
            this.a = a;
        }
    }

    public LinkedList<ActorRef> actors;

	// Empty Constructor
	public Broadcaster() {
        actors = new LinkedList<ActorRef>();
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof Join){
            actors.add(((Join)message).a);
            log.info("The broadcaster confirm that " +((Join)message).a.path().name()+" joins the broadcast");
        }
        if(message instanceof Message){
            for (ActorRef a: actors){
                a.tell((Message)message, getSender());
                log.info(getSelf().path().name()+" sends message from "+ getSender().path().name()+" to "+a.path().name());
            }
            
        }
	}
	
	
}