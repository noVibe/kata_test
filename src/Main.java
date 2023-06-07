
import service.CalcService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input = reader.readLine();
            CalcService c = new CalcService().setRestriction(10);
            c.calculate(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

