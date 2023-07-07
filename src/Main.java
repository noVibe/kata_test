
import exception.InvalidInputException;
import exception.InvalidRomanNumberException;
import exception.UnexpectedResultException;
import service.CalcService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Use 'break' to stop");
            while (true) {
                String input = reader.readLine();
                if (input.equals("break")) {
                    break;
                }
                try {
                    CalcService c = new CalcService().setRestriction(10);
                    c.calculate(input);
                } catch (InvalidRomanNumberException | InvalidInputException | UnexpectedResultException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

