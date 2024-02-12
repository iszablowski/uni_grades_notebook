package application;

import java.util.ArrayList;
import java.util.Objects;

public class Semester {
    private String semesterCode;
    private ArrayList<Class> semesterClasses;

    public Semester(String newSemesterCode) {
        semesterClasses = new ArrayList<Class>();
        this.setSemesterCode(newSemesterCode);
    }

    public String getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(String newSemesterCode) {
        semesterCode = newSemesterCode;
    }

    public ArrayList<Class> getSemesterClasses() {
        return semesterClasses;
    }

    public void setSemesterClasses(ArrayList<Class> newSemesterClasses) {
         semesterClasses = newSemesterClasses;
    }

    public void addClass(Class newClass) {
        semesterClasses.add(newClass);
    }

    public int getSemesterPlannedEcts() {
        int semesterEcts = 0;
        for (Class uniClass: semesterClasses) {
            semesterEcts += uniClass.getClassEcts();
        }
        return semesterEcts;
    }

    public int getSemesterEctsToAverage() {
        int semesterEcts = 0;
        for (Class uniClass: semesterClasses) {
            if (uniClass.isGraded()) {
                semesterEcts += uniClass.getClassEcts();
            }
        }
        return semesterEcts;
    }

    public int getSemesterEcts() {
        int semesterEcts = 0;
        for (Class uniClass: semesterClasses) {
            if (uniClass.isGraded() && uniClass.getClassGrade() != 2) {
                semesterEcts += uniClass.getClassEcts();
            }
        }
        return semesterEcts;
    }

    public double getSemesterWeightedGradesSum() {
        double semesterWeightedGradesSum = 0;
        for (Class uniClass: semesterClasses) {
            semesterWeightedGradesSum += uniClass.getWeightedGrade();
        }
        return  semesterWeightedGradesSum;
    }

    public double getSemesterAverage() {
        return Math.round(this.getSemesterWeightedGradesSum()/this.getSemesterEctsToAverage() * 100d)/100d;
    }

    public Class getClassByName(String className) {
        for (Class uniClass: semesterClasses) {
            if (Objects.equals(uniClass.getClassName(), className)) {
                return uniClass;
            }
        }
        return null;
    }
}
