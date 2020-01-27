package demo;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import demo.FirstActor.Request;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	// Empty Constructor
	public SecondActor() {}

	// Static function that creates an actor Props
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
    }

    static public class Response {
        public final int id;
		public final String data;
	
		public Response(String data, int id) {
            this.id = id;
			this.data = data;
		}
    }
    

	@Override
	public void onReceive(Object request) throws Throwable {
        if(request instanceof Request){
            ((Request)request).replyTo.tell(new Response("res to "+((Request)request).data, ((Request)request).id), getSelf());
            log.info("we send a response to the request");
		
        }
	}
	
	
}