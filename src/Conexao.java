import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Conexao {

    private static Conexao conexao;

    public static Conexao getInstance(){
        if(conexao == null){
            conexao = new Conexao();
        }
        return conexao;

    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://26.191.28.12:3306/apidistribuicao","pedro","123456");
     }
     public static void main(String[]args){
        try {
            System.out.println(getInstance().getConnection());

        }catch (Exception e){
            e.printStackTrace();
        }
     }


}
