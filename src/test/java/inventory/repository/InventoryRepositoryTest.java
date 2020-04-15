package inventory.repository;

import inventory.model.OutsourcedPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class InventoryRepositoryTest {

    InventoryRepository repo;

    @BeforeEach
    void setup(){
        try {
            repo = new InventoryRepository();
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addGoodPart() {
        InventoryRepository spiedRepo = spy(repo);
        OutsourcedPart p = new OutsourcedPart(1,"asga",2,20,0,20,"Asgas");
        try {
            Mockito.doNothing().when(spiedRepo).writeAll();
            assertDoesNotThrow(() ->spiedRepo.addPart(p));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addBadPart() {
        InventoryRepository spiedRepo = spy(repo);
        OutsourcedPart p = new OutsourcedPart(1,"asga",2,-20,0,20,"Asgas");
        try {
            Mockito.doNothing().when(spiedRepo).writeAll();
            assertDoesNotThrow(() ->spiedRepo.addPart(p));
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

}