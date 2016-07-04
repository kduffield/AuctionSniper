package net.infinitysum.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static java.lang.String.format;
import static net.infinitysum.auctionsniper.ui.Main.BID_COMMAND_FORMAT;
import static net.infinitysum.auctionsniper.ui.Main.JOIN_COMMAND_FORMAT;

/**
 * Created by Kevin on 04/07/2016.
 */
public class XMPPAuction implements Auction {

    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
