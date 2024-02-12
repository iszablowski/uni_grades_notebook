package application;

import java.util.ArrayList;
import java.util.Objects;

public class Studies {
    private ArrayList<Semester> semesters = new ArrayList<>();
    private String studiesName;

    public Studies(String studiesName) {
        this.studiesName = studiesName;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public String getStudiesName() {
        return studiesName;
    }

    public void setStudiesName(String studiesName) {
        this.studiesName = studiesName;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(Semester newSemester) {
        semesters.add(newSemester);
    }

    public void removeSemester(Semester semesterToRemove) {
        semesters.remove(semesterToRemove);
    }

    public Semester getSemesterByCode(String semesterCode) {
        for (Semester semester: semesters) {
            if (Objects.equals(semester.getSemesterCode(), semesterCode)) {
                return semester;
            }
        }
        return null;
    }

    public int getStudiesEcts() {
        int studiesEcts = 0;
        for (Semester semester: semesters) {
            studiesEcts += semester.getSemesterEcts();
        }
        return studiesEcts;
    }

    public double getStudiesCumulativeAverage(Semester semesterToEndOn) {
        double studiesWeightedGradesSum = 0;
        int studiesEcts = 0;
        for (Semester semester: semesters) {
            studiesEcts += semester.getSemesterEctsToAverage();
            studiesWeightedGradesSum += semester.getSemesterWeightedGradesSum();
            if (semester == semesterToEndOn) {
                break;
            }
        }
        return Math.round(studiesWeightedGradesSum/studiesEcts * 100d)/100d;
    }
}
