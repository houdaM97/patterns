package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.Client.Message;
import demo.Client.SendMessage;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Session extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef client;

    public Session(ActorRef client) {
        this.client = client;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef client) {
		return Props.create(Session.class, () -> {
			return new Session(client);
		});
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Message){
            log.info(getSelf().path().name()+ " receives message: ' "+((Message)message).data+" ' from "+getSender().path().name());
            client.tell(new Message("m2"), getSelf());
            log.info(getSelf().path().name()+ " sends a message to "+client.path().name());
        
        }
        if(message instanceof SendMessage){
            client.tell(((SendMessage)message).m, getSelf());
            log.info(getSelf().path().name()+ " sends a message to "+client.path().name());
        }
	}
	
	
}