package application;

public class Class {
    private String className;
    private String classCode;
    private int classEcts;
    private Double classGrade;

    public Class(String name, String code, int ects, Double grade) {
        this.setClassName(name);
        this.setClassCode(code);
        this.setClassEcts(ects);
        this.setClassGrade(grade);
    }

    public Class(String name, String code, int ects) {
        this.setClassName(name);
        this.setClassCode(code);
        this.setClassEcts(ects);
    }

    public boolean isGraded() {
        return (this.classGrade != 0);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String newClassName) {
        this.className = newClassName;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String newClassCode) {
        this.classCode = newClassCode;
    }

    public int getClassEcts() {
        return classEcts;
    }

    public void setClassEcts(int newClassEcts) {
        this.classEcts = newClassEcts;
    }

    public Double getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(double newClassGrade) {
        this.classGrade = newClassGrade;
    }

    public Double getWeightedGrade() {
        return (this.classGrade * this.classEcts);
    }
}
