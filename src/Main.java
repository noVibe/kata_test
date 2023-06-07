import exception.InvalidRomanNumberException;
import service.CalcService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Use 'stop' to stop");
            while (true) {
                String input = reader.readLine();
                if (input.matches("stop")) {
                    break;
                }
                try {
                    CalcService.calculate(input);
                } catch (InvalidRomanNumberException e) {
                    System.err.println(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
