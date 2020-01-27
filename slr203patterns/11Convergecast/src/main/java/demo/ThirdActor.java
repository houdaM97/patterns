package demo;

import akka.actor.Props;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.Merger.Join;
import demo.Merger.Unjoin;
import scala.concurrent.ExecutionContext;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.FirstActor.LetsJoin;
import demo.FirstActor.LetsUnjoin;
import demo.FirstActor.LetsSendM;

public class ThirdActor extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    

    private ActorRef merger;

    public ThirdActor() {}
	// Empty Constructor
	public ThirdActor(ActorRef merger) {
        this.merger = merger;
    }

	// Static function creating actor Props
	public static Props createActor(ActorRef merger) {
		return Props.create(ThirdActor.class, () -> {
			return new ThirdActor(merger);
		});
    }
    
    public void join(){
        this.merger.tell(new Join(getSelf()), getSelf());
        log.info(getSelf().path().name()+" joins the convergecast");
    }

    public void unjoin(){
        this.merger.tell(new Unjoin(getSelf()), getSelf());
        log.info(getSelf().path().name()+" unjoins the convergecast");
    }

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof LetsJoin){
            join();
            Thread.sleep(1000);
        }
        if(message instanceof LetsUnjoin){
            unjoin();
            Thread.sleep(1000);
		}
        if(message instanceof LetsSendM){
            this.merger.tell((LetsSendM)message, getSelf());
            log.info(getSelf().path().name()+ " sends message to "+this.merger.path().name());
            Thread.sleep(1000);
        }
	}
	
	
}