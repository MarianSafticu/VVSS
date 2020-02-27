package inventory.validator;

import inventory.model.Part;

public class PartValidator implements IValidator<Part> {

    @Override
    public void validate(Part part) throws ValidatorException {
        String err = "";
        if (part.getPrice() < 0.01){
            err += "Price must be bigger than 0.01!\n";
        }
        if(part.getName().equals("")){
            err += "Name of the part cannot be empty!\n";
        }
        if(part.getInStock() < 0){
            err += "Number of parts in stock cannot be negative!\n";
        }
        if(part.getMin() < 0){
            err += "The minimum number of parts cannot be negative!\n";
        }
        if(part.getMax() < 0){
            err += "The maximum number of parts cannot be negative!\n";
        }
        if(part.getMin() > part.getMax()){
            err += "The minimum number of parts must be less or equal with the maximum number of parts!\n";
        }
        if (err.length() > 0){
            throw new ValidatorException(err);
        }
    }
}
