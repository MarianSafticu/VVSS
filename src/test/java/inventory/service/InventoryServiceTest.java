package inventory.service;

import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    String name = "screw";
    String companyName= "CompanyX";
    int min = 0;
    int max= 100;

    String filename = "data/test_items.txt";

    InventoryRepository repo;
    {
        try {
            repo = new InventoryRepository(filename);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
    InventoryService service = new InventoryService(repo);

    @AfterEach
    void RemovePart(){

        ClassLoader classLoader = InventoryRepository.class.getClassLoader();
        URL resource = classLoader.getResource(filename);
        File file = new File(resource.getFile());

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("");
        } catch (IOException e) {
        }
    }

    @Test
    void addPartGoodInStock() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 10, 5, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    void addPartBadInStock() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 10, -500, min, max, companyName));
    }
    @Test
    void addPartGoodPrice() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 5.5, 20, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    void addPartBadPrice() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, -500, 20, min, max, companyName));
    }

    @Test
    void addPartGoodInStockBVA() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 10, 0, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    void addPartBadInStockBVA() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 10, -1, min, max, companyName));
    }
    @Test
    void addPartGoodPriceBVA() {
        assertDoesNotThrow(() -> service.addOutsourcePart(name, 0.01, 20, min, max, companyName));
        assertDoesNotThrow(() -> service.lookupPart(name));
    }
    @Test
    void addPartBadPriceBVA() {
        assertThrows(ServiceException.class,() -> service.addOutsourcePart(name, 0, 20, min, max, companyName));
    }
}