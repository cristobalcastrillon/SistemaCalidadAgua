import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import java.util.LinkedList;
import java.util.List;


// Código modificado de: https://zguide.zeromq.org/docs/chapter5/
public class LastValueCache {
    public static void main(String[] args)
    {
        try (ZContext context = new ZContext()) {
            Socket frontend = context.createSocket(SocketType.SUB);
            frontend.bind("tcp://*:5556");
            Socket backend = context.createSocket(SocketType.XPUB);
            backend.bind("tcp://*:5557");

            //  Subscribe to every single topic from publisher
            frontend.subscribe(ZMQ.SUBSCRIPTION_ALL);

            //  Store last instance of each topic in a cache
            List<String> cache = new LinkedList<>();

            Poller poller = context.createPoller(2);
            poller.register(frontend, Poller.POLLIN);
            poller.register(backend, Poller.POLLIN);

            //  .split main poll loop
            //  We route topic updates from frontend to backend, and we handle
            //  subscriptions by sending whatever we cached, if anything:
            while (true) {
                if (poller.poll(1000) == -1)
                    break; //  Interrupted

                //  Any new topic data we cache and then forward
                if (poller.pollin(0)) {
                    String publishedData = frontend.recvStr();

                    if (publishedData == null)
                        break;

                    cache.add(publishedData);
                    backend.send(publishedData.getBytes());
                }
                //  .split handle subscriptions
                //  When we get a new subscription, we pull data from the cache:
                if (poller.pollin(1)) {
                    String cachedData = backend.recvStr();

                    System.out.println("Sending cached data...");
                    backend.send(cachedData);
                }
            }
            // TODO: Find out if this is the correct usage of the following methods.
            frontend.close();
            backend.close();
            context.destroy();
        }
    }
}
