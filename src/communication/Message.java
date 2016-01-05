package communication;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
	static public int TEXT = 1;
	static public int VOICE_INVITATION = 2;
    static public int VIDEO_INVITATION = 3;
	static public int HANDSHAKE = 4;
    static public int LINK = 5;
    static public int VOICE_INVITATION_RESPONSE = 6;
    static public int VIDEO_INVITATION_RESPONSE = 7;
    static public int VIDEO_SERVER_READY = 8;
    static public int VOICE_SERVER_READY = 9;
    static public int RESOLUTION_CONTROL = 10;

    static public String ACCEPT = "ACCEPT";
    static public String DECLINE = "DECLINE";
	
	private int messageType;
	private ArrayList<String> messageDesIDList;
	private String messageSrcID;
	private String messageContent;
	private String messageTimeStamp;

    public Message() {}

    public void setMessageType(int messageType) { this.messageType = messageType; }
    public void setMessageSrcID(String id) { this.messageSrcID = id; }
    public void setMessageDesIDList(ArrayList<String> desIDList) {this.messageDesIDList = desIDList; };
    public void setMessageTimeStamp(String timeStamp) {this.messageTimeStamp = timeStamp; }
    public void setMessageContent(String content) {this.messageContent = content; }

    public int getMessageType() { return this.messageType; }
    public String getMessageSrcID() { return this.messageSrcID; }
    public ArrayList<String> getMessageDesIDList() { return this.messageDesIDList; }
    public String getMessageContent() { return this.messageContent; }
    public String getMessageTimeStamp() { return this.messageTimeStamp; }
}
