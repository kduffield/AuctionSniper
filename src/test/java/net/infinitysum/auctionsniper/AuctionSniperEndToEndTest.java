package net.infinitysum.auctionsniper;

import org.junit.Test;

/**
 * Created by Kevin on 03/07/2016.
 */
public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other  bidder");
        application.hasShownSniperIsBidding();

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction.announceClosed();
        application.showsSniperHasLostAuction();

    }

}
