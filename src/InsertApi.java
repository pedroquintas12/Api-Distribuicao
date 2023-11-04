import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertApi {

    public void inserir (DadosApi dados) {
    try {


        Connection connection = Conexao.getInstance().getConnection();

        String sql = "INSERT INTO apidistribuicao.processo(codProcesso,codEscritorio) VALUES(?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, dados.getCodProcesso());
        statement.setInt(2, dados.getCodEscritorio());

        statement.execute();
        connection.close();
      }catch (Exception e){
        e.printStackTrace();
    }
    }

}
