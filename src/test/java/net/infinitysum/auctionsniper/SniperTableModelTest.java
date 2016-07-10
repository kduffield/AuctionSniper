package net.infinitysum.auctionsniper;

import net.infinitysum.auctionsniper.ui.SnipersTableModel;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static net.infinitysum.auctionsniper.AuctionSniper.SniperState.BIDDING;
import static net.infinitysum.auctionsniper.ui.SnipersTableModel.textFor;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Kevin on 09/07/2016.
 */
@RunWith(JMock.class)
public class SniperTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Before
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableModel.Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        context.checking(new Expectations() {{
            one(listener).tableChanged(with(aRowChangedEvent()));
        }});

        model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, BIDDING));
        assertColumnEquals(SnipersTableModel.Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(SnipersTableModel.Column.LAST_PRICE, 555);
        assertColumnEquals(SnipersTableModel.Column.LAST_BID, 666);
        assertColumnEquals(SnipersTableModel.Column.SNIPER_STATE, textFor(BIDDING));
    }

    private void assertColumnEquals(SnipersTableModel.Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    @Test
    public void setsUpColumnHeadings() {
        for (SnipersTableModel.Column column : SnipersTableModel.Column.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }

}
