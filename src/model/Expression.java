package model;

import enums.ArithmeticOperation;
import enums.NumberFormat;
import exception.UnexpectedResultException;
import service.RomeArabicConverter;

import static enums.NumberFormat.ROMAN;

public record Expression(int firstNum, int secondNum, ArithmeticOperation operation, NumberFormat format){
    public String execute(boolean calcAsInteger) {
        double result = switch (operation) {
            case PLUS -> firstNum + secondNum;
            case MINUS -> firstNum - secondNum;
            case DIVIDE -> (double) firstNum / secondNum;
            case MULTIPLY -> firstNum * secondNum;
        };
        return setResult(result, calcAsInteger);
    }
    private String setResult(double d, boolean calcAsInteger) {
        return switch (format) {
            case ARABIC -> calcAsInteger ? String.valueOf((int) d) : String.valueOf(d);
            case ROMAN -> RomeArabicConverter.convert((int) validate(d));
        };
    }

    private double validate(double d) {
        if (d < 1 || Math.rint(d) != d && format.equals(ROMAN)) {
            throw new UnexpectedResultException("Result in Roman format must be natural and greater than 0.");
        } return d;
    }

}
