package demo;

import akka.actor.Props;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.Sender.Message;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    private ActorRef loadBalancer;

    public Receiver() {}

    public Receiver(ActorRef lb) {
        this.loadBalancer = lb;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef lb) {
		return Props.create(Receiver.class, () -> {
			return new Receiver(lb);
		});
    }

    public static class Join {
	
		public Join() {

		}
    }

    public static class Unjoin {
	
		public Unjoin() {

		}
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof Join){
            this.loadBalancer.tell(new Join(), getSelf());
            log.info(getSelf().path().name()+ " joins loadBalancer ");
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        }
        if(message instanceof Unjoin){
            this.loadBalancer.tell(new Unjoin(), getSelf());
            log.info(getSelf().path().name()+ " unjoins loadBalancer ");
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        
        }
        if(message instanceof Message){
            log.info(getSelf().path().name()+ " receives message: ' "+((Message)message).data+" ' from "+getSender().path().name());
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        }
	}
	
	
}