package demo;

import akka.actor.Props;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import demo.Merger.Message;
import demo.Merger.Join;
import demo.Merger.Unjoin;
import scala.concurrent.ExecutionContext;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor {

    // Logger attached to actor
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef merger;

    public FirstActor() {
    }

    // Empty Constructor
    public FirstActor(ActorRef merger) {
        this.merger = merger;
    }

    // Static function creating actor Props
    public static Props createActor(ActorRef merger) {
        return Props.create(FirstActor.class, () -> {
            return new FirstActor(merger);
        });
    }

    public static class LetsJoin {

        public LetsJoin() {

        }
    }

    public static class LetsSendM {

        public Message m;

        public LetsSendM(Message m) {
            this.m = m;
        }
    }

    public static class LetsUnjoin {

        public LetsUnjoin() {

        }
    }

    public void join() {
        this.merger.tell(new Join(getSelf()), getSelf());
        log.info(getSelf().path().name() + " joins the convergecast");
        //the scheduler does'nt work
        //ActorSystem r = getContext().system();
        //r.scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system(), ActorRef.noSender());
    }

    public void unjoin() {
        this.merger.tell(new Unjoin(getSelf()), getSelf());
        log.info(getSelf().path().name() + " unjoins the convergecast");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof LetsJoin) {
            join();
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        
        }
        if(message instanceof LetsUnjoin){
            unjoin();
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        
		}
        if(message instanceof LetsSendM){
            this.merger.tell((LetsSendM)message, getSelf());
            log.info(getSelf().path().name()+ " sends message to "+this.merger.path().name());
            getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
        }
	}
	
	
}