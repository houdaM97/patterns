package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.LinkedList;
import demo.Sender.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.Sender.SendMessage;
import demo.Receiver.Join;
import demo.Receiver.Unjoin;

public class LoadBalancer extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    LinkedList<ActorRef> group;
    public int count;

	// Empty Constructor
	public LoadBalancer() {
        group = new LinkedList<ActorRef>();
        count = 0;
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof Join){
            group.add(getSender());
            log.info("The loadBalancer adds " +getSender().path().name());
        }
        if (message instanceof Unjoin){
            if(group.contains(getSender())){
                group.remove(getSender());
                log.info("The loadBalancer removes " +getSender().path().name());
        
            } else {
                log.info(getSender().path().name()+" not found in the group list of " +getSelf().path().name());
        
            }
            }
        if(message instanceof SendMessage){
            Message m = ((SendMessage)message).m;
            LinkedList<ActorRef> actors = group;
            ActorRef a = actors.get(count);
            count = (count + 1)%actors.size();
            a.tell(m, getSender());
            log.info(getSelf().path().name()+" sends message:' "+m.data+" '' from "+ getSender().path().name()+" to "+a.path().name());
            
        }
	}
	
	
}