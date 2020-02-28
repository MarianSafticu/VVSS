package inventory.service;

import inventory.model.*;
import inventory.repository.InventoryRepository;
import inventory.repository.RepoException;
import inventory.validator.PartValidator;
import inventory.validator.IValidator;
import inventory.validator.ProductValidator;
import inventory.validator.ValidatorException;
import javafx.collections.ObservableList;

public class InventoryService {

    private InventoryRepository repo;
    private IValidator<Part> partValidator = new PartValidator();
    private IValidator<Product> productValidator = new ProductValidator();

    public InventoryService(InventoryRepository repo){
        this.repo =repo;
    }


    public void addInhousePart(String name, double price, int inStock, int min, int  max, int partDynamicValue) throws ServiceException{
        InhousePart inhousePart = new InhousePart(repo.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        try{
            partValidator.validate(inhousePart);
            repo.addPart(inhousePart);
        }catch (ValidatorException | RepoException e){
            throw new ServiceException("The part could not be added!\n"+e.getMessage());
        }
    }

    public void addOutsourcePart(String name, double price, int inStock, int min, int  max, String partDynamicValue) throws ServiceException{
        OutsourcedPart outsourcedPart = new OutsourcedPart(repo.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        try{
            partValidator.validate(outsourcedPart);
            repo.addPart(outsourcedPart);
        }catch (ValidatorException | RepoException e){
            throw new ServiceException("The part could not be added!\n"+e.getMessage());
        }
    }

    public void addProduct(String name, double price, int inStock, int min, int  max, ObservableList<Part> addParts) throws ServiceException{
        Product product = new Product(repo.getAutoProductId(), name, price, inStock, min, max, addParts);
        try{
            productValidator.validate(product);
            repo.addProduct(product);
        }catch (ValidatorException | RepoException e){
            throw new ServiceException("The product could not be added!\n"+e.getMessage());
        }
    }

    public ObservableList<Part> getAllParts() {
        return repo.getAllParts();
    }

    public ObservableList<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public Part lookupPart(String search) throws ServiceException{
        try {
            return repo.lookupPart(search);
        }catch (RepoException e){
            throw new ServiceException(e.getMessage());
        }
    }

    public Product lookupProduct(String search) throws ServiceException {
        try{
           return repo.lookupProduct(search);
        }catch (RepoException e){
            throw new ServiceException(e.getMessage());
        }

    }

    public void updateInhousePart(int partIndex, int partId, String name, double price, int inStock, int min, int max, int partDynamicValue) throws ServiceException {
        try {
            InhousePart inhousePart = new InhousePart(partId, name, price, inStock, min, max, partDynamicValue);
            partValidator.validate(inhousePart);
            repo.updatePart(partIndex, inhousePart);
        } catch (RepoException | ValidatorException e){
            throw new ServiceException("The part could not be updated!\n"+e.getMessage());
        }
    }

    public void updateOutsourcedPart(int partIndex, int partId, String name, double price, int inStock, int min, int max, String partDynamicValue) throws ServiceException {
        try {
            OutsourcedPart outsourcedPart = new OutsourcedPart(partId, name, price, inStock, min, max, partDynamicValue);
            partValidator.validate(outsourcedPart);
            repo.updatePart(partIndex, outsourcedPart);
        }catch (RepoException | ValidatorException e){
            throw new ServiceException("The part could not be updated!\n"+e.getMessage());
        }
    }

    public void updateProduct(int productIndex, int productId, String name, double price, int inStock, int min, int max, ObservableList<Part> addParts) throws ServiceException {
        try {
            Product product = new Product(productId, name, price, inStock, min, max, addParts);
            productValidator.validate(product);
            repo.updateProduct(productIndex, product);
        }catch (RepoException | ValidatorException e){
            throw new ServiceException("The product could not be updated!%n"+e.getMessage());
        }
    }

    public void deletePart(Part part) throws ServiceException {
        try {
            repo.deletePart(part);
        }catch (RepoException e){
            throw new ServiceException("The product could not be updated!%n"+e.getMessage());
        }
    }

    public void deleteProduct(Product product) throws ServiceException {
        try {
            repo.deleteProduct(product);
        }catch (RepoException e){
            throw new ServiceException("The product could not be updated!\n"+e.getMessage());
        }
    }

}
