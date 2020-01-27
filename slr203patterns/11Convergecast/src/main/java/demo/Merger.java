package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.LinkedList;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Merger extends UntypedAbstractActor {

    static public class Message {
        public final String data;
 
		public Message(String data) {
            this.data = data;
		}
    }

    public Message mToSend; 

    // Logger attached to actor
    public int count = 0;
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static class Join{
        public ActorRef a;
        public Join(ActorRef a){
            this.a = a;
        }
    }

    public static class Unjoin{
        public ActorRef a;
        public Unjoin(ActorRef a){
            this.a = a;
        }
    }

    private ActorRef root;
    public LinkedList<ActorRef> actors;

    public Merger() {}
	// Empty Constructor
	public Merger(ActorRef root) {
        actors = new LinkedList<ActorRef>();
        mToSend = new Message("");
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef root) {
		return Props.create(Merger.class, () -> {
			return new Merger(root);
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Message){
            if(count == actors.size()){
                root.tell((Message)message, getSelf());
                //log.info(getSelf().path().name()+" sends message from children to "+root.path().name());
                count = 0;
            } else {
                count = count +1;
                mToSend = new Message(mToSend.data+"\n"+((Message)message).data);
                //log.info(getSelf().path().name()+" receives a message from "+getSender().path().name()+ " and waits for the others' messages");
            }
        }
        if (message instanceof Join){
            actors.add(((Join)message).a);
            //log.info(getSelf().path().name()+" confirms that " +((Join)message).a.path().name()+" joins the convergecast");
        }
        if (message instanceof Unjoin){
            actors.remove(((Unjoin)message).a);
            //log.info(getSelf().path().name()+" confirms that " +((Unjoin)message).a.path().name()+" unjoins the convergecast");
        
        }
        
	}
	
	
}