package net.infinitysum.auctionsniper;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.After;
import org.junit.Test;

import static net.infinitysum.auctionsniper.AuctionSniper.SniperState.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Kevin on 29/06/2016.
 */
public class AuctionSniperTest {
    private final String ITEM_ID = "item-54321";
    private final FakeAuctionServer auctionServer = new FakeAuctionServer(ITEM_ID);
    private final ApplicationRunner application = new ApplicationRunner();

    private final Mockery context = new Mockery();
    private final SniperListener sniperListener = context.mock(SniperListener.class);

    private final Auction auction = context.mock(Auction.class);

    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

    private final States sniperState = context.states("sniper");




    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(JOINING)));
        }});
        auctionServer.startSellingItem();
        application.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auctionServer.announceClosed();
        application.showsSniperHasLostAuction();

    }

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            one(sniperListener).sniperStateChanged(with(aSniperThatIs(LOST)));
        }});

        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        context.checking(new Expectations() {{
            one(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, BIDDING));
        }});

        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(BIDDING)));
            then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 135, 135, WINNING)
            );
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, AuctionEventListener.PriceSource.FromSniper);
    }


    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(BIDDING)));
            then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(LOST)));
            when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(with(any(SniperSnapshot.class)));
            then(sniperState.is("winning"));

            atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(WON)));
            when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    private Matcher<SniperSnapshot> aSniperThatIs(final AuctionSniper.SniperState state) {
        return new FeatureMatcher<SniperSnapshot, AuctionSniper.SniperState>(
                equalTo(state), "sniper that is ", "was") {
            protected AuctionSniper.SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }


    @After
    public void stopAuction() {
        auctionServer.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }
}
