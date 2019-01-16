package rsocket.poc.news;

import java.io.Serializable;

public class News implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String title;
    
    private String body;

    public News(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }
    
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "News [title=" + title + ", body=" + body + "]";
    }
    
}
