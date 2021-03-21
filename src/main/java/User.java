public class User {
    // Поля
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public User()
    {
        password = null;
        login = null;
    }

    public User(String login,String password){
        this.login = login;
        this.password = password;
    }

}
