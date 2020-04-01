package inventory.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    Inventory inv = new Inventory();

    @AfterEach
    void clearInventory(){
        inv = new Inventory();
    }

    @Test
    void searchEmptyString() {
        assertNull(inv.lookupPart(""));
    }
    @Test
    void searchGoodPartByName(){
        inv.addPart(new InhousePart(2,"screw",10.0,2, 0,10, 12));
        assertNotNull(inv.lookupPart("screw"));
    }
    @Test
    void searchGoodPartById(){
        inv.addPart(new InhousePart(2,"screw",10.0,2, 0,10, 12));
        assertNotNull(inv.lookupPart("2"));
    }
    @Test
    void searchNonExistentPart() {
        inv.addPart(new InhousePart(2,"screw",10.0,2, 0,10, 12));
        assertNull(inv.lookupPart("scrwe"));
    }
}