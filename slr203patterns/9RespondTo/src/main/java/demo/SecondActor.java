package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import demo.Intermediate.Response;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor {

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	

	// Empty Constructor
	public SecondActor() {}

	// Static function that creates an actor Props
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
    }
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Response){
            log.info(getSelf()+ " receives a response : "+ ((Response)message).data +" from "+getSender());
        }
	}
	
	
}