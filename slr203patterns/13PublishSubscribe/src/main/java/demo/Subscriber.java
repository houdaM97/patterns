package demo;

import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import demo.Publisher.Message;
import demo.Topic.Subscribe;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Subscriber extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Subscriber() {}

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Subscriber.class, () -> {
			return new Subscriber();
		});
    }

    static public class LetsSubscribe {
        public ActorRef topic;
		public LetsSubscribe(ActorRef topic) {
            this.topic = topic;
		}
    }
    
    static public class Unsubscribe {
        public ActorRef topic;
		public Unsubscribe(ActorRef topic) {
            this.topic = topic;
		}
	}

	@Override
	public void onReceive(Object message) throws Throwable {
        if(message instanceof LetsSubscribe){
            ((LetsSubscribe)message).topic.tell(new Subscribe(getSelf()), getSelf());
            log.info(getSelf().path().name()+ " subscribes to topic: ' "+((LetsSubscribe)message).topic.path().name());
        }
        if(message instanceof Unsubscribe){
            ((Unsubscribe)message).topic.tell(new Subscribe(getSelf()), getSelf());
            log.info(getSelf().path().name()+ " subscribes to topic: ' "+((LetsSubscribe)message).topic.path().name());
        }
        if(message instanceof Message){
            log.info(getSelf().path().name()+ " receives message: ' "+((Message)message).data+" ' from "+getSender().path().name());
        }
	}
	
	
}