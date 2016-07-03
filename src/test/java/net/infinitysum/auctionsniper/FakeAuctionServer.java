package net.infinitysum.auctionsniper;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Kevin on 03/07/2016.
 */
public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD="auction";


    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId){
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public  void startSellingItem() throws XMPPException{
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN,itemId),AUCTION_PASSWORD,AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(Chat chat, boolean createdLocally) {
                currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });

    }

    public String getItemId(){
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper()throws InterruptedException{
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws XMPPException{
        currentChat.sendMessage(new Message());
    }

    public void stop(){
        connection.disconnect();
    }
}
