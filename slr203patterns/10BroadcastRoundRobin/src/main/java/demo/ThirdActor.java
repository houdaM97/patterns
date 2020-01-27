package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import demo.FirstActor.Message;
import akka.actor.ActorRef;
import demo.Broadcaster.Join;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.SecondActor.LetsJoin;

public class ThirdActor extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public ActorRef broadcaster;

    public ThirdActor() {}

	// Empty Constructor
	public ThirdActor(ActorRef broadcaster) {
        this.broadcaster = broadcaster;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(ThirdActor.class, () -> {
			return new ThirdActor(broadcaster);
		});
    }
    
    
    public void join(){
        this.broadcaster.tell(new Join(getSelf()), getSelf());
        log.info(getSelf().path().name()+" joins the "+ this.broadcaster.path().name());
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof LetsJoin){
			join();
		}
        if(message instanceof Message){
            log.info(getSelf().path().name()+ " receives message from "+getSender().path().name());
		
        }
	}
	
	
}