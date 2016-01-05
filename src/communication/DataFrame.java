package communication;

import java.io.Serializable;

/**
 * Created by jiaxing song on 2015/11/4.
 * Transfer the data between for voice and video chat.
 */
public class DataFrame implements Serializable{
    private byte[] bytes;
    private int length;

    public DataFrame(byte[] bytes) {
        this.bytes = bytes;
    }

    public DataFrame(byte[] bytes, int length) {
        this.bytes = bytes;
        this.length = length;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public int getLength() { return length; }
}
