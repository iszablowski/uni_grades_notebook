package application.tools;

import application.Class;
import application.Semester;
import application.Studies;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {

    private static Connection connectToDatabase() {
        try {
            java.lang.Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/test");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void createTablesIfNotExist() {

        Connection connection = DatabaseManager.connectToDatabase();

        try {
            Statement statement = connection.createStatement();

            String studiesTableSql = "CREATE TABLE IF NOT EXISTS studies " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(255) not NULL UNIQUE)";

            statement.execute(studiesTableSql);

            String semesterTableSql = "CREATE TABLE IF NOT EXISTS semesters " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "studiesId Integer not NULL, " +
                    "name VARCHAR(255) not NULL, " +
                    "FOREIGN KEY (studiesId) REFERENCES studies(id), " +
                    "CONSTRAINT uniqueSemesterPerStudies UNIQUE (studiesId, name))";
            statement.execute(semesterTableSql);

            String classTableSql = "CREATE TABLE IF NOT EXISTS classes " +
                    "(id Integer not NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "semesterId Integer not NULL, " +
                    "name VARCHAR(255) not NULL, " +
                    "code VARCHAR(10) not NULL, " +
                    "ects Integer not NULL, " +
                    "grade Double not NULL, " +
                    "FOREIGN KEY (semesterId) REFERENCES semesters(id), " +
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
            DatabaseManager.createTablesIfNotExist();

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
            DatabaseManager.createTablesIfNotExist();

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

    private static Integer getStudiesId(Studies studies) throws SQLException {
        Connection connection = DatabaseManager.connectToDatabase();
        String getStudiesIdSql = "SELECT id FROM studies WHERE name=?";
        PreparedStatement statement = connection.prepareStatement(getStudiesIdSql);
        statement.setString(1, studies.getStudiesName());
        ResultSet result = statement.executeQuery();
        Integer studiesId = null;
        if (result.next()) {
            studiesId = result.getInt("id");
        }
        return studiesId;
    }

    public static void loadStudiesData(Studies studies) {
        try {
            Integer studiesId = DatabaseManager.getStudiesId(studies);
            Connection connection = DatabaseManager.connectToDatabase();
            String selectSemesterData = "SELECT * FROM semesters WHERE studiesId=?";
            String selectClassData = "SELECT * FROM classes WHERE semesterId=?";
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
