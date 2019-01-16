package rsocket.poc.news;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class NewsApplication {

    public static void main(String[] args) {
        
        var newsProducer = new NewsProducer();
        var newsConsumer = new NewsConsumer();
        
        // socket acceptor - used on server side when a request is done
        // we do stream interaction mode thanks to requestStream method
        SocketAcceptor socketAcceptor = (setup, sendingSocket) -> {
            return Mono.just(new AbstractRSocket() {

                @Override
                public Flux<Payload> requestStream(Payload payload) {
                    return newsProducer.streamNews()
                            .map(NewsMapper::toByte)
                            .map(DefaultPayload::create)
                            ;
                }
                
            });
        };
        
        // server
        RSocketFactory.receive()
            .acceptor(socketAcceptor)
            .transport(TcpServerTransport.create("localhost", 7000))
            .start()
            .subscribe();
        
        // creating socket for client
        RSocket clientSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create("localhost", 7000))
                .start()
                .block();
        
        // client
        clientSocket
            .requestStream(DefaultPayload.create("hi"))
            .map(Payload::getData)
            .map(NewsMapper::fromByteBuffer)  // deserialize payload to business object
            .doOnNext(newsConsumer::readNews) // business service call
            .take(5)
            .doFinally(signalType -> clientSocket.dispose())
            .then()
            .block();
        
    }
    
}
