package application;

import java.util.ArrayList;

public class Studies {
    private ArrayList<Semester> semesters;
    private String studiesName;

    public Studies(String studiesName) {
        semesters = new ArrayList<Semester>();
        this.studiesName = studiesName;
    }

    public Studies(String studiesName, ArrayList<Semester> semesters) {
        this.studiesName = studiesName;
        this.semesters = semesters;
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

    public int getStudiesEcts() {
        int studiesEcts = 0;
        for (Semester semester: semesters) {
            studiesEcts += semester.getSemesterEcts();
        }
        return studiesEcts;
    }

    public double getStudiesCumulativeAverage() {
        double studiesWeightedGradesSum = 0;
        for (Semester semester: semesters) {
            studiesWeightedGradesSum += semester.getSemesterWeightedGradesSum();
        }
        return Math.round(studiesWeightedGradesSum/this.getStudiesEcts() * 100d)/100d;
    }
}
