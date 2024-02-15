package application.tools;

import application.Class;
import application.Semester;
import application.Studies;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DatabaseManager {

    private static Connection connectToDatabase() {
        try {
            java.lang.Class.forName("org.h2.Driver");
            String pathToDatabase = Paths.get(System.getProperty("user.home"), ".uniGradesNotebook", "uni_grades").toString();
            return DriverManager.getConnection("jdbc:h2:" + pathToDatabase);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void createTablesIfNotExists() {

        Connection connection = DatabaseManager.connectToDatabase();

        try {
            Objects.requireNonNull(connection);
            Statement statement = connection.createStatement();

            String studiesTableSql = "CREATE TABLE IF NOT EXISTS studies " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255) not NULL UNIQUE)";

            statement.execute(studiesTableSql);

            String semesterTableSql = "CREATE TABLE IF NOT EXISTS semesters " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "studiesId Integer not NULL, " +
                    "name VARCHAR(255) not NULL, " +
                    "FOREIGN KEY (studiesId) REFERENCES studies(id) ON DELETE CASCADE, " +
                    "CONSTRAINT uniqueSemesterPerStudies UNIQUE (studiesId, name))";
            statement.execute(semesterTableSql);

            String classTableSql = "CREATE TABLE IF NOT EXISTS classes " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "semesterId Integer not NULL, " +
                    "name VARCHAR(255) not NULL, " +
                    "code VARCHAR(10) not NULL, " +
                    "ects Integer not NULL, " +
                    "grade Double not NULL, " +
                    "FOREIGN KEY (semesterId) REFERENCES semesters(id) ON DELETE CASCADE, " +
                    "CONSTRAINT uniqueClassPerSemester UNIQUE (semesterId, name))";

            statement.execute(classTableSql);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Studies> loadStudiesList() {
        ArrayList<Studies> studiesToLoad = new ArrayList<>();
        try {
            DatabaseManager.createTablesIfNotExists();

            String getStudiesSql = "SELECT name FROM studies";

            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(getStudiesSql);

            ResultSet result = statement.executeQuery();

            while(result.next()) {
                studiesToLoad.add(new Studies(result.getString("name")));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studiesToLoad;

    }

    public static boolean addStudies(Studies studiesToAdd) {
        try {
            String insertStudiesSql = "INSERT INTO studies (name) VALUES (?)";

            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(insertStudiesSql);
            statement.setString(1, studiesToAdd.getStudiesName());
            statement.executeUpdate();

            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addSemester(Semester semester, Integer studiesId) {
        try {
            String insertSemesterSql = "INSERT INTO semesters (name, studiesId) VALUES (?, ?)";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(insertSemesterSql);
            statement.setString(1, semester.getSemesterCode());
            statement.setInt(2, studiesId);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addClass(Class classToAdd, Integer semesterId) {
        try {
            String insertClassesSql = "INSERT INTO classes (semesterId, name, code, ects, grade) VALUES (?, ?, ?, ?, ?)";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(insertClassesSql);
            statement.setInt(1, semesterId);
            statement.setString(2, classToAdd.getClassName());
            statement.setString(3, classToAdd.getClassCode());
            statement.setInt(4, classToAdd.getClassEcts());
            statement.setDouble(5, classToAdd.getClassGrade());
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSemester(String newSemesterCode, Integer semesterId) {
        try {
            String updateSemesterSql = "UPDATE semesters SET name=? WHERE id=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(updateSemesterSql);
            statement.setString(1, newSemesterCode);
            statement.setInt(2, semesterId);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateClassName(Class classToUpdate, String newClassName, Integer semesterId) {
        try {
            String updateClassNameSql = "UPDATE classes SET name=? WHERE semesterId=? AND name=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(updateClassNameSql);
            statement.setString(1, newClassName);
            statement.setInt(2, semesterId);
            statement.setString(3, classToUpdate.getClassName());
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateClassCode(Class classToUpdate, String newClassCode, Integer semesterId) {
        try {
            String updateClassCodeSql = "UPDATE classes SET code=? WHERE semesterId=? AND name=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(updateClassCodeSql);
            statement.setString(1, newClassCode);
            statement.setInt(2, semesterId);
            statement.setString(3, classToUpdate.getClassName());
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateClassEcts(Class classToUpdate, Integer newClassEcts, Integer semesterId) {
        try {
            String updateClassEctsSql = "UPDATE classes SET ects=? WHERE semesterId=? AND name=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(updateClassEctsSql);
            statement.setInt(1, newClassEcts);
            statement.setInt(2, semesterId);
            statement.setString(3, classToUpdate.getClassName());
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateClassGrade(Class classToUpdate, Double newClassGrade, Integer semesterId) {
        try {
            String updateClassGradeSql = "UPDATE classes SET grade=? WHERE semesterId=? AND name=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(updateClassGradeSql);
            statement.setDouble(1, newClassGrade);
            statement.setInt(2, semesterId);
            statement.setString(3, classToUpdate.getClassName());
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteSemester(Integer semesterId) {
        try {
            String deleteSemesterSql = "DELETE FROM semesters WHERE id=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(deleteSemesterSql);
            statement.setInt(1, semesterId);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteStudies(String studiesName) {
        try {
            String deleteStudiesSql = "DELETE FROM studies WHERE name=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(deleteStudiesSql);
            statement.setString(1, studiesName);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteClass(String className, Integer semesterId) {
        try {
            String deleteClassSql = "DELETE FROM classes WHERE name=? AND semesterId=?";
            Connection connection = DatabaseManager.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(deleteClassSql);
            statement.setString(1, className);
            statement.setInt(2, semesterId);
            statement.executeUpdate();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Integer getStudiesId(Studies studies) throws SQLException {
        Connection connection = DatabaseManager.connectToDatabase();
        String getStudiesIdSql = "SELECT id FROM studies WHERE name=?";
        PreparedStatement statement = connection.prepareStatement(getStudiesIdSql);
        statement.setString(1, studies.getStudiesName());
        ResultSet result = statement.executeQuery();
        Integer studiesId = null;
        if (result.next()) {
            studiesId = result.getInt("id");
        }
        statement.close();
        connection.close();
        return studiesId;
    }

    public static Integer getSemesterId(Semester semester, Integer studiesId) throws SQLException {
        Connection connection = DatabaseManager.connectToDatabase();
        String getSemesterIdSql = "SELECT id FROM semesters WHERE name=? AND studiesId=?";
        PreparedStatement statement = connection.prepareStatement(getSemesterIdSql);
        statement.setString(1, semester.getSemesterCode());
        statement.setInt(2, studiesId);
        ResultSet result = statement.executeQuery();
        Integer semesterId = null;
        if (result.next()) {
            semesterId = result.getInt("id");
        }
        statement.close();
        connection.close();
        return semesterId;
    }

    public static void loadStudiesData(Studies studies) {
        try {
            Integer studiesId = DatabaseManager.getStudiesId(studies);
            Connection connection = DatabaseManager.connectToDatabase();
            String selectSemesterData = "SELECT * FROM semesters WHERE studiesId=? ORDER BY id";
            String selectClassData = "SELECT * FROM classes WHERE semesterId=? ORDER BY id";
            PreparedStatement statement = connection.prepareStatement(selectSemesterData);
            statement.setInt(1, studiesId);
            ResultSet semestersResult = statement.executeQuery();

            while (semestersResult.next()) {
                Semester newSemester = new Semester(semestersResult.getString("name"));
                Integer semesterId = semestersResult.getInt("id");
                statement = connection.prepareStatement(selectClassData);
                statement.setInt(1, semesterId);
                ResultSet classResult = statement.executeQuery();
                while(classResult.next()) {
                    newSemester.addClass(new Class(classResult.getString("name"),
                                                   classResult.getString("code"),
                                                   classResult.getInt("ects"),
                                                   classResult.getDouble("grade")));
                }
                studies.addSemester(newSemester);

            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
