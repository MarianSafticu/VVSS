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
import static org.mockito.Mockito.spy;

public class IntegrationEntityTest {
    String name = "screw";
    String companyName = "CompanyX";
    int min = 0;
    int max = 100;
    private Logger logger = Logger.getLogger(InventoryServiceTest.class.getName());
    String filename = "data/test_items.txt";
    InventoryRepository repo, spiedRepo;
    InventoryService service;

    @BeforeEach
    void setup() {
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
            assertDoesNotThrow(() -> service.addOutsourcePart(name, 10, 5, min, max, companyName));
            assertDoesNotThrow(() -> service.lookupPart(name));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addPartBadInStockMock() {
        try {
            Mockito.doNothing().when(spiedRepo).writeAll();
            assertThrows(ServiceException.class, () -> service.addOutsourcePart(name, 10, -500, min, max, companyName));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }


}


