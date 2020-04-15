package inventory.service;

import inventory.model.OutsourcedPart;
import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ServiceTest {

    String name = "screw";
    String companyName= "CompanyX";
    int min = 0;
    int max= 100;
    private Logger logger = Logger.getLogger(InventoryServiceTest.class.getName());
    String filename = "data/test_items.txt";
    InventoryRepository repo;
    InventoryService service;

    @BeforeEach
    void setup(){
        repo = mock(InventoryRepository.class);
        service = new InventoryService(repo);
    }

    @Test
    void addPartGoodInStockMock() {
        OutsourcedPart outsourcedPart = new OutsourcedPart(repo.getAutoPartId(), "a", 1, 1, 1, 1, "a");
        try {
            Mockito.doNothing().when(repo).addPart(outsourcedPart);
            assertDoesNotThrow(() -> service.addOutsourcePart("a", 1, 1, 1, 1, "a"));
            assertDoesNotThrow(() -> service.lookupPart("a"));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addPartBadInStockMock() {
        Mockito.when(repo.getAutoPartId()).thenReturn(2);
        OutsourcedPart outsourcedPart = new OutsourcedPart(repo.getAutoPartId(),"a", 1, 1, -1, 1, "a");
        try {
            Mockito.doThrow(new RepoException()).when(repo).addPart(outsourcedPart);
            assertThrows(ServiceException.class,() -> service.addOutsourcePart("a", 1, 1, -1, 1, "a"));
        } catch (RepoException e) {
            e.printStackTrace();
        }

    }
}
