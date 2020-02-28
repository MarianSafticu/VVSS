package inventory.validator;

import inventory.model.Product;

public class ProductValidator implements IValidator<Product> {

    @Override
    public void validate(Product product) throws ValidatorException {
        String errorMessage = "";
        double sumOfParts = 0.00;
        for (int i = 0; i < product.getAssociatedParts().size(); i++) {
            sumOfParts += product.getAssociatedParts().get(i).getPrice();
        }
        if (product.getName().equals("")) {
            errorMessage += "A name has not been entered. ";
        }
        if (product.getMin() < 0) {
            errorMessage += "The inventory level must be greater than 0. ";
        }
        if (product.getMin() < 0.01) {
            errorMessage += "The price must be greater than $0. ";
        }
        if (product.getMin() > product.getMax()) {
            errorMessage += "The Min value must be less than the Max value. ";
        }
        if(product.getInStock() < product.getMin()) {
            errorMessage += "Inventory level is lower than minimum value. ";
        }
        if(product.getInStock() > product.getMax()) {
            errorMessage += "Inventory level is higher than the maximum value. ";
        }
        if (product.getAssociatedParts().isEmpty()) {
            errorMessage += "Product must contain at least 1 part. ";
        }
        if (sumOfParts > product.getPrice()) {
            errorMessage += "Product price must be greater than cost of parts. ";
        }
        if (errorMessage.length() > 0){
            throw new ValidatorException(errorMessage);
        }
    }
}
