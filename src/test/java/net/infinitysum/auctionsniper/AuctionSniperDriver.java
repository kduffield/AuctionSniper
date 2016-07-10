package net.infinitysum.auctionsniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import net.infinitysum.auctionsniper.ui.MainWindow;

import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;

/**
 * Created by Kevin on 29/06/2016.
 */
public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMillis){
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME),
                showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis,100));
    }

    /* public void showsSniperStatus(String statusText){
         new JLabelDriver(this,named(Main.SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
     }
     */
    public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        System.out.println("Checking that status is showing status of itemId = " + itemId +
                " lastPrice = " + lastPrice +
                " lastBid = " + lastBid +
                " statusText = " + statusText);
        JTableDriver table = new JTableDriver(this);
        table.hasRow(
                matching(withLabelText(itemId),
                        withLabelText(valueOf(lastPrice)),
                        withLabelText(valueOf(lastBid)),
                        withLabelText(statusText)
                )
        );
    }

    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"), withLabelText("Last Bid"), withLabelText("State")));
    }
}
