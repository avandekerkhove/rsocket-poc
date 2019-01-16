package rsocket.poc.news;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public interface NewsMapper {

    public static byte[] toByte(News news) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(news);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
    
    public static News fromByteBuffer(ByteBuffer byteBuffer) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer.array());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (News) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
    
}
