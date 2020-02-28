package inventory.validator;

import inventory.model.Part;

public class PartValidator implements IValidator<Part> {

    @Override
    public void validate(Part part) throws ValidatorException {
        String errorMessage = "";

        if(part.getName().equals("")) {
            errorMessage += "A name has not been entered. ";
        }
        if(part.getPrice() < 0.01) {
            errorMessage += "The price must be greater than 0. ";
        }
        if(part.getInStock() < 0) {
            errorMessage += "Inventory level must be greater than 0. ";
        }
        if(part.getMin() > part.getMax()) {
            errorMessage += "The Min value must be less than the Max value. ";
        }
        if(part.getInStock() < part.getMin()) {
            errorMessage += "Inventory level is lower than minimum value. ";
        }
        if(part.getInStock() > part.getMax()) {
            errorMessage += "Inventory level is higher than the maximum value. ";
        }
        if (errorMessage.length() > 0){
            throw new ValidatorException(errorMessage);
        }
    }
}
