package demo;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import demo.Client.CreateSession;
import demo.Client.EndSession;

public class SessionManager extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Empty Constructor
	public SessionManager() {
    }

	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof CreateSession){
            ActorRef session = getContext().system().actorOf(Session.createActor(getSender()), "session1");
            getSender().tell(session, getSelf());
            log.info(getSelf().path().name()+" creates the session :" +session.path().name());
        }
        if (message instanceof EndSession){
            ((EndSession)message).s.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
            log.info(getSelf().path().name()+" stoppes the session :" +((EndSession)message).s.path().name());
        }
        
	}
	
	
}