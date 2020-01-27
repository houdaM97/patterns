package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.FirstActor.Message;
import demo.Broadcaster.Join;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    

    private ActorRef broadcaster;

    public SecondActor() {}
	// Empty Constructor
	public SecondActor(ActorRef broadcaster) {
        this.broadcaster = broadcaster;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor(broadcaster);
		});
    }
    
    public static class LetsJoin {
	
		public LetsJoin() {

		}
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