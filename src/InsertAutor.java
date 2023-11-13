import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class InsertAutor {
    public static boolean inseridoComSucesso;

    public void inserir(DadosApi dadosAutor) {

        Connection connection;
        try {

            connection = Conexao.getInstance().getConnection();

            //verifica se os dadosAutor ja existem no banco de dadosAutor
            String selectSql = "SELECT * FROM apidistribuicao.autor WHERE codPolo = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, dadosAutor.getCodPolo());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
            } else {
                //insere o dado no banco, caso ele não exista
                String sql = "INSERT INTO apidistribuicao.autor(codPolo, " +
                        "nome, descricaoTipoPolo, cnpj, cpf)" +
                        " VALUES (?,?,?,?,?)";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setInt(1,dadosAutor.getCodPolo());
                statement.setString(2,dadosAutor.getNome());
                statement.setString(3,dadosAutor.getDescricaoTipoPolo());
                statement.setString(4,dadosAutor.getCnpj());
                statement.setString(5,dadosAutor.getCpf());


                statement.execute();
                inseridoComSucesso = true;

            }

            connection.close();
        }catch (Exception e){
            e.printStackTrace();
    }
    }
    }

