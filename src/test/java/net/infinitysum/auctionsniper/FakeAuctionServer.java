package net.infinitysum.auctionsniper;

import net.infinitysum.auctionsniper.ui.Main;
import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Kevin on 03/07/2016.
 */
public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";


    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(Chat chat, boolean createdLocally) {
                currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });

    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
        /*assertThat(currentChat.getParticipant(), equalTo(sniperId));
        messageListener.receivesAMessage(
                equalTo(String.format("SQLVersion: 1.1; Command: BID; Price: %d;",bid))
        );*/
        receivesAMessageMatching(sniperId, equalTo(format(Main.BID_COMMAND_FORMAT, bid)));
    }

    private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException {
        messageListener.receivesAMessage(messageMatcher);
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException {
        System.out.println("Reporting price for bidder[" + bidder + "]");
        currentChat.sendMessage(
                format("SOLVersion: 1.1; Event: PRICE;"
                                + "CurrentPrice: %d; Increment: %d; Bidder: %s",
                        price, increment, bidder)
        );

    }
}

