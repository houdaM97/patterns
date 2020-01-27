package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.LinkedList;
import demo.Sender.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.HashMap;
import demo.Sender.CreateGroup;
import demo.Sender.SendMessage;

public class Multicaster extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public HashMap<Integer, LinkedList<ActorRef>> groups;

	// Empty Constructor
	public Multicaster() {
        groups = new HashMap<Integer, LinkedList<ActorRef>>();
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Multicaster.class, () -> {
			return new Multicaster();
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof CreateGroup){
            groups.put(1, ((CreateGroup)message).actors);
            log.info("The multicaster creates group " +((CreateGroup)message).id+" that includes : ");
            for(ActorRef a : ((CreateGroup)message).actors){
                log.info(a.path().name()+ " ");
            }
        }
        if(message instanceof SendMessage){
            int groupId = ((SendMessage)message).groupId;
            Message m = ((SendMessage)message).m;
            LinkedList<ActorRef> actors = groups.get(groupId);
            for (ActorRef a: actors){
                a.tell(m, getSender());
                log.info(getSelf().path().name()+" sends message:' "+m.data+" '' from "+ getSender().path().name()+" to "+a.path().name());
            }
            
        }
	}
	
	
}