package net.infinitysum.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.HashMap;
import java.util.Map;

import static net.infinitysum.auctionsniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static net.infinitysum.auctionsniper.AuctionEventListener.PriceSource.FromSniper;

/**
 * Created by Kevin on 04/07/2016.
 */
public class AuctionMessageTranslator implements MessageListener {
    private final String sniperID;
    private final AuctionEventListener listener;


    public AuctionMessageTranslator(String sniperID, AuctionEventListener listener) {
        this.sniperID = sniperID;
        this.listener = listener;
    }

    public void processMessage(Chat chat, Message message) {

        AuctionEvent event = AuctionEvent.from(message.getBody());
        //  HashMap<String, String> event = unpackEventFrom(message);
        String eventType = event.type();
        if ("CLOSE".equals(eventType)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(eventType)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperID));
        }

    }


    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<String, String>();

        public String type() {

            return get("Event");
        }

        public int currentPrice() {
            return getInt("CurrentPrice");
        }

        public int increment() {
            return getInt("Increment");
        }

        private int getInt(String fieldName) {

            return Integer.parseInt(get(fieldName));
        }

        private String get(String fieldName) {
            return fields.get(fieldName);
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }
            return event;
        }

        static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }

        public AuctionEventListener.PriceSource isFrom(String sniperID) {
            System.out.println("Checking price source for sniperID[" + sniperID + "] against bidder[" + bidder() + "]");
            return sniperID.equals(bidder()) ? FromSniper : FromOtherBidder;
        }

        private String bidder() {
            return get("Bidder");
        }
    }

}
