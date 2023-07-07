package model;

import enums.ArithmeticOperation;
import enums.NumberFormat;
import exception.UnexpectedResultException;
import service.RomeArabicConverter;

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
            case ROMAN -> RomeArabicConverter.convert(tryCastToIntOverZero(d));
        };
    }

    private int tryCastToIntOverZero(double d) {
        if (d < 1) {
            throw new UnexpectedResultException("Result in Roman format must be natural and greater than 0.");
        } return ((int) d);
    }

}
