import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.TimeZone;
import java.util.UUID;

public class InsertApi {

    public void inserir (DadosApi dados)  {
        UUID codigoLocalizacao = UUID.randomUUID();
        String codigoLocalizacaoString = codigoLocalizacao.toString();
        LocalDate dataAtual = LocalDate.now();

        try {

        Connection connection = Conexao.getInstance().getConnection();

            //verifica se os dado ja existe no banco de dados
            String selectSql = "SELECT * FROM apidistribuicao.processo WHERE codProcesso = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, dados.getCodProcesso());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("O registro já existe para codProcesso " + dados.getCodProcesso());
            } else {
                //insere o dado no banco, caso ele não exista
                String sql = "INSERT INTO apidistribuicao.processo(codProcesso, codEscritorio, numeroProcesso, instancia," +
                        " tribunal, siglaSistema, comarca, orgaoJulgador, tipoDoProcesso, dataAudiencia, dataDistribuicao, valorDaCausa," +
                        " magistrado, cidade, uf, nomePesquisado, data_insercao, LocatorDB) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setInt(1, dados.getCodProcesso());
                statement.setInt(2, dados.getCodEscritorio());
                statement.setString(3, dados.getNumeroProcesso());
                statement.setInt(4, dados.getInstancia());
                statement.setString(5, dados.getTribunal());
                statement.setString(6, dados.getSiglaSistema());
                statement.setString(7, dados.getComarca());
                statement.setString(8, dados.getOrgaoJulgador());
                statement.setString(9, dados.getTipoDoProcesso());
                if (dados.getDataAudiencia() != null) {
                    statement.setDate(10, new java.sql.Date(dados.getDataAudiencia().getTime()));
                } else {
                    statement.setNull(10, 0);
                }
                statement.setDate(11, new Date(dados.getDataDistribuicao().getTime()));
                statement.setString(12, dados.getValorDaCausa());
                statement.setString(13, dados.getMagistrado());
                statement.setString(14, dados.getCidade());
                statement.setString(15, dados.getUf());
                statement.setString(16, dados.getNomePesquisado());
                statement.setDate(17, Date.valueOf(dataAtual));
                statement.setString(18, codigoLocalizacaoString);
                System.out.println(("DADO INSERIDO PARA O ESCRITORIO: " + dados.getCodEscritorio()));

                statement.execute();
            }


        connection.close();
      }catch (Exception e){
        e.printStackTrace();
     }
        }

}
