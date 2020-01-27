package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import java.util.Map;

import demo.Sender.Tache;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.HashMap;
import demo.Receiver.Finish;

public class LoadBalancer extends UntypedAbstractActor {

    // Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private int max_receivers;
    private int count = 0;
    private HashMap<ActorRef, Integer> sessions;
    
    // Empty Constructor
    public LoadBalancer() {}
	public LoadBalancer(int max_receivers) {
        this.max_receivers = max_receivers;
        sessions = new HashMap<ActorRef, Integer>();
        count = 0;
    }

	// Static function creating actor Props
	public static Props createActor(int max_receivers) {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer(max_receivers);
		});
	}
    

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof Tache){
            ActorRef session = null;
            int nbTasks = 1;
            if(sessions.size() < max_receivers){
                session = getContext().system().actorOf(Receiver.createActor(getSelf()), "session"+sessions.size());
                if(sessions.containsKey(session)){
                    nbTasks = sessions.get(session)+1;  
                }
            } else {
                int i=0;
                for (Map.Entry<ActorRef, Integer> entry : sessions.entrySet()) {
                    if(count == i){
                        session = entry.getKey();
                        nbTasks = entry.getValue()+1;
                    }
                    break;
                }
                count = (count +1)%sessions.size();
            }
            
            sessions.put(session, nbTasks);
            session.tell(message, getSelf());
            log.info("The loadBalancer creates session" +getSelf().path().name()+ " and send to it a task "+((Tache)message).id);
        }
        if (message instanceof Finish){
            ActorRef session = getSender();
            int nbTasks = 0;
            if(sessions.containsKey(session)){
                nbTasks = sessions.get(session) -1;
                sessions.put(session, nbTasks);
                if(nbTasks == 0){
                    session.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
                    sessions.remove(session);
                    log.info("The loadBalancer stops the session " +getSender().path().name());
        
                }
            } else {
                //error
                log.info(getSender().path().name()+" not found in the sessions list of " +getSelf().path().name());
        
            }
            
        }
	}
	
	
}