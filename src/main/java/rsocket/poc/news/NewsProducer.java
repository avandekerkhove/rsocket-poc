package rsocket.poc.news;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class NewsProducer {

    public Flux<News> streamNews() {
       return Flux.interval(Duration.ofSeconds(1))
               .map(tick -> new News("title news " + tick, "body news"));
    }
    
}
