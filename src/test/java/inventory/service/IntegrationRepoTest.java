package inventory.service;

import inventory.model.OutsourcedPart;
import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class IntegrationRepoTest {
    InventoryRepository repo, spiedRepo;
    InventoryService service;
    @BeforeEach
    void setup(){
        try {
            repo = new InventoryRepository();
            spiedRepo = spy(repo);
            service = new InventoryService(spiedRepo);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addPartGoodInStockMock() {
        try {
            Mockito.doNothing().when(spiedRepo).writeAll();
            assertDoesNotThrow(() -> service.addOutsourcePart("a", 1, 1, 1, 1, "a"));
            assertDoesNotThrow(() -> service.lookupPart("a"));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addPartBadInStockMock() {
        try {
            Mockito.doNothing().when(spiedRepo).writeAll();
            assertThrows(ServiceException.class,() -> service.addOutsourcePart("a", 1, -1, 1, 1, "a"));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }


}
