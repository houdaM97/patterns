package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import demo.Sender.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Receiver() {}

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Receiver.class, () -> {
			return new Receiver();
		});
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Message){
            log.info(getSelf().path().name()+ " receives message: ' "+((Message)message).data+" ' from "+getSender().path().name());
        }
	}
	
	
}