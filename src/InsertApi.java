import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;


public class InsertApi {


    public boolean inseridoComSucesso;

    public void inserir(DadosApi dados) {
        UUID codigoLocalizacao = UUID.randomUUID();
        String codigoLocalizacaoString = codigoLocalizacao.toString();
        LocalDate dataAtual = LocalDate.now();

        Connection connection = null;

        try {

            connection = Conexao.getInstance().getConnection();

            //verifica se os dados ja existem no banco de dados
            String selectSql = "SELECT * FROM apidistribuicao.processo WHERE codProcesso = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, dados.getCodProcesso());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
            } else {
                //insere o dado no banco, caso ele não exista
                String sql = "INSERT INTO apidistribuicao.processo(codProcesso, codEscritorio, numeroProcesso, " +
                        "instancia, tribunal, siglaSistema, comarca, orgaoJulgador, tipoDoProcesso, dataAudiencia, " +
                        "dataDistribuicao, valorDaCausa, assuntos, magistrado, cidade, uf, nomePesquisado, data_insercao, LocatorDB) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                PreparedStatement statementProcesso = connection.prepareStatement(sql);

                statementProcesso.setInt(1, dados.getCodProcesso());
                statementProcesso.setInt(2, dados.getCodEscritorio());
                statementProcesso.setString(3, dados.getNumeroProcesso());
                statementProcesso.setInt(4, dados.getInstancia());
                statementProcesso.setString(5, dados.getTribunal());
                statementProcesso.setString(6, dados.getSiglaSistema());
                statementProcesso.setString(7, dados.getComarca());
                statementProcesso.setString(8, dados.getOrgaoJulgador());
                statementProcesso.setString(9, dados.getTipoDoProcesso());
                if (dados.getDataAudiencia() != null) {
                    statementProcesso.setDate(10, new java.sql.Date(dados.getDataAudiencia().getTime()));
                } else {
                    statementProcesso.setNull(10, 0);
                }
                statementProcesso.setDate(11, new Date(dados.getDataDistribuicao().getTime()));
                statementProcesso.setString(12, dados.getValorDaCausa());
                // Converter a lista de assuntos em uma string JSON e retira os caracteres "[" e ""
                List<String> assuntos = dados.getAssuntos();
                String assuntosJson = new Gson().toJson(assuntos).replaceAll("[\\[\\]\"]", "");
                statementProcesso.setString(13, assuntosJson);
                statementProcesso.setString(14, dados.getMagistrado());
                statementProcesso.setString(15, dados.getCidade());
                statementProcesso.setString(16, dados.getUf());
                statementProcesso.setString(17, dados.getNomePesquisado());
                statementProcesso.setDate(18, Date.valueOf(dataAtual));
                statementProcesso.setString(19, codigoLocalizacaoString);


                statementProcesso.executeUpdate();
            }
                resultSet.close();
                selectStatement.close();

                connection.close();
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                // Fecha a conexão no bloco finally para garantir que ela seja fechada, mesmo em caso de exceção
                try {
                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


