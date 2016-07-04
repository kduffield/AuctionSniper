package net.infinitysum.auctionsniper;

import net.infinitysum.auctionsniper.ui.Main;
import net.infinitysum.auctionsniper.ui.MainWindow;

import static net.infinitysum.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;

/**
 * Created by Kevin on 29/06/2016.
 */
public class ApplicationRunner {
    public static final String SNIPER_ID= "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@localhost/Auction";
    private AuctionSniperDriver driver;


    public void startBiddingIn(final FakeAuctionServer auction){
        Thread thread = new Thread("Test Application"){
            @Override
            public void run(){
                try{
                    Main.main(XMPP_HOSTNAME,SNIPER_ID,SNIPER_PASSWORD,auction.getItemId());

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(Main.STATUS_JOINING);

    }

    public void showsSniperHasLostAuction(){
        driver.showsSniperStatus(Main.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }

    public void stop(){
        if(driver != null){
            driver.dispose();
        }
    }

}
