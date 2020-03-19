package inventory.service;

import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing adding parts")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceTest {

    String name = "screw";
    String companyName= "CompanyX";
    int min = 0;
    int max= 100;
    private Logger logger = Logger.getLogger(InventoryServiceTest.class.getName());
    String filename = "data/test_items.txt";

    InventoryService initService(){
        try {
            InventoryRepository repo = new InventoryRepository(filename);
            return new InventoryService(repo);
        } catch (RepoException e) {
            logger.info(e.getMessage());
            return null;
        }
    }
    InventoryService service = initService();

    @AfterEach
    @Timeout(5)
    void removePart(){
        ClassLoader classLoader = InventoryRepository.class.getClassLoader();
        URL resource = classLoader.getResource(filename);
        File file = new File(resource.getFile());

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("");
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @Tag("good")
    @RepeatedTest(2)
    void addPartGoodInStock() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 10, 5, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    @Order(2)
    @Tag("bad")
    @RepeatedTest(2)
    void addPartBadInStock() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 10, -500, min, max, companyName));
    }
    @Test
    @Order(3)
    @Tag("good")
    @RepeatedTest(2)
    void addPartGoodPrice() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 5.5, 20, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    @Order(4)
    @Tag("bad")
    @RepeatedTest(2)
    void addPartBadPrice() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, -500, 20, min, max, companyName));
    }

    @Test
    @Order(5)
    @Tag("good")
    @RepeatedTest(2)
    void addPartGoodInStockBVA() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 10, 0, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    @Order(6)
    @Tag("bad")
    @RepeatedTest(2)
    void addPartBadInStockBVA() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 10, -1, min, max, companyName));
    }
    @Test
    @Order(7)
    @Tag("good")
    @RepeatedTest(2)
    void addPartGoodPriceBVA() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 0.01, 20, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    @Order(8)
    @Tag("bad")
    @RepeatedTest(2)
    void addPartBadPriceBVA() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 0, 20, min, max, companyName));
    }
}