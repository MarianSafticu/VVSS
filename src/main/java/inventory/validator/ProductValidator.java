package inventory.validator;

import inventory.model.Product;

public class ProductValidator implements IValidator<Product> {

    @Override
    public void validate(Product product) throws ValidatorException {
        String err = "";
        if (product.getPrice() < 0.01){
            err += "Price must be bigger than 0.01!\n";
        }
        if(product.getName().equals("")){
            err += "Name of the product cannot be empty!\n";
        }
        if(product.getInStock() < 0){
            err += "Number of products in stock cannot be negative!\n";
        }
        if(product.getMin() < 0){
            err += "The minimum number of products cannot be negative!\n";
        }
        if(product.getMax() < 0){
            err += "The maximum number of products cannot be negative!\n";
        }
        if(product.getMin() > product.getMax()){
            err += "The minimum number of products must be less or equal with the maximum number of products!\n";
        }
        if (err.length() > 0){
            throw new ValidatorException(err);
        }
    }
}
