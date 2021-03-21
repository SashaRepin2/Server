import java.sql.*;

public class DataBase {

    private Connection conn;


    public DataBase() {
        try{
            this.conn = connectionDb();

            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connectionDb() throws SQLException {
        String url = "jdbc:sqlite:SQLiteDataBase/users.db";
        Connection connection = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established");
        return connection;
    }

    public void createTable() throws SQLException {
        String sqlTable = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY_KEY,\n"
                + " login text NOT NULL,\n"
                + " password text NOT NULL\n"
                + ");";

        System.out.println("Table has been created");

        try (Statement statement = conn.createStatement()) {
            statement.execute(sqlTable);
        }
    }

    public void insert(int id, String login, String password) throws SQLException {
        String sql = "INSERT INTO users(id, login, password) VALUES(?,?,?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,login);
            preparedStatement.setString(3,password);
            preparedStatement.executeUpdate();

            id++;
        }
    }
}
