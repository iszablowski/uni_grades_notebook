package application.tools;

import application.Grades;

public class InputValidation {
    public static boolean isValidString(String string) {
        return !(string == null || string.trim().isEmpty());
    }

    public static boolean canBeParsedToInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidGrade(Double grade) {
        if (grade == 0) {
            return true;
        }
        for (double validGrade: Grades.getGrades()) {
            if (grade == validGrade) {
                return true;
            }
        }
        return false;
    }

    public static boolean isClassDataValid(String newClassName, String newClassCode, String newClassEcts, Double newClassGrade) {
        return (InputValidation.isValidString(newClassName) &&
                InputValidation.isValidString(newClassCode) &&
                InputValidation.canBeParsedToInteger(newClassEcts) &&
                InputValidation.isValidGrade(newClassGrade));
    }
}
