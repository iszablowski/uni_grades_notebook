package application.tools;

import javafx.util.converter.IntegerStringConverter;

public class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {}
        return null;
    }

    @Override
    public Integer fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {}
        return -1;
    }
}
