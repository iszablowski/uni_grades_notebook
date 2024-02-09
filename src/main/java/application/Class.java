package application;

public class Class {
    private String className;
    private String classCode;
    private int classEcts;
    private double classGrade;

    public Class(String name, String code, int ects, double grade) {
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

    public double getClassGrade() {
        return classGrade;
    }

    public void setClassGrade(double newClassGrade) {
        this.classGrade = newClassGrade;
    }

    public double getWeightedGrade() {
        return (this.classGrade * this.classEcts);
    }
}
