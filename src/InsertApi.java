import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InsertApi {

    public boolean inseridoComSucesso;

    public void inserir(DadosApi dados) {
        UUID codigoLocalizacao = UUID.randomUUID();
        String codigoLocalizacaoString = codigoLocalizacao.toString();
        LocalDate dataAtual = LocalDate.now();


        try {
<<<<<<< Updated upstream

            Connection connection = Conexao.getInstance().getConnection();
=======
            connection = Conexao.getInstance().getConnection();
>>>>>>> Stashed changes

            // Verifica se os dados já existem no banco de dados
            String selectSql = "SELECT * FROM apidistribuicao.processo WHERE codProcesso = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, dados.getCodProcesso());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        // Insere o dado no banco, caso ele não exista
                        String sql = "INSERT INTO apidistribuicao.processo(codProcesso, codEscritorio, numeroProcesso, " +
                                "instancia, tribunal, siglaSistema, comarca, orgaoJulgador, tipoDoProcesso, dataAudiencia, " +
                                "dataDistribuicao, valorDaCausa, assuntos, magistrado, cidade, uf, nomePesquisado, data_insercao, LocatorDB) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                        try (PreparedStatement statementProcesso = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

<<<<<<< Updated upstream
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
                // Converter a lista de assuntos em uma string JSON e retira os caracteres "[" e ""
                List<String> assuntos = dados.getAssuntos();
                String assuntosJson = new Gson().toJson(assuntos).replaceAll("[\\[\\]\"]", "");
                statement.setString(13,assuntosJson);
                statement.setString(14, dados.getMagistrado());
                statement.setString(15, dados.getCidade());
                statement.setString(16, dados.getUf());
                statement.setString(17, dados.getNomePesquisado());
                statement.setDate(18, Date.valueOf(dataAtual));
                statement.setString(19, codigoLocalizacaoString);

                statement.execute();

                inseridoComSucesso = true;

            }


            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
=======
                            statementProcesso.executeUpdate();

                            // Obtém o ID do processo recém-inserido
                            try (ResultSet generatedKeys = statementProcesso.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int idProcesso = generatedKeys.getInt(1);

                                    // Insere os autores relacionados ao processo
                                    inserirAutores(connection, idProcesso, dados.getAutor());
                                    inserirReus(connection, idProcesso, dados.getReu());
                                    inserirAdv(connection, idProcesso, dados.getAdvogados());
                                    inserirOutrosEnvil(connection, idProcesso, dados.getOutrosEnvolvidos());
                                    inserirMovimentos(connection,idProcesso,dados.getMovimentos());
                                    inserirLink(connection,idProcesso,dados.getDocumentosIniciais());

                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    private void inserirAutores(Connection conn, int idProcesso, List<RetornoAutor> autores) throws SQLException {
        if (autores != null && !autores.isEmpty()) {
            String sql = "INSERT INTO apidistribuicao.processo_autor (ID_processo, codPolo, nome,descricaoTipoPolo,cnpj,cpf) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (RetornoAutor autor : autores) {
                    pstmt.setInt(1, idProcesso);
                    pstmt.setInt(2, autor.getCodPolo());
                    pstmt.setString(3, autor.getNome());
                    pstmt.setString(4, autor.getDescricaoTipoPolo());
                    pstmt.setString(5, autor.getCnpj());
                    pstmt.setString(6, autor.getCpf());

                    pstmt.executeUpdate();
                }
            }
>>>>>>> Stashed changes
        }

    }
<<<<<<< Updated upstream
}
=======

    private void inserirReus(Connection conn, int idProcesso, List<RetornoReu> Reus) throws SQLException {
        if (Reus != null && !Reus.isEmpty()) {
            String sql = "INSERT INTO apidistribuicao.processo_reu (ID_processo, codPolo, nome,descricaoTipoPolo,cnpj,cpf) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement statementReu = conn.prepareStatement(sql)) {
                for (RetornoReu Reu : Reus) {
                    statementReu.setInt(1, idProcesso);
                    statementReu.setInt(2, Reu.getCodPolo());
                    statementReu.setString(3, Reu.getNome());
                    statementReu.setString(4, Reu.getDescricaoTipoPolo());
                    statementReu.setString(5, Reu.getCnpj());
                    statementReu.setString(6, Reu.getCpf());
>>>>>>> Stashed changes

                    statementReu.executeUpdate();

                }
            }
        }
    }

    private void inserirAdv(Connection conn, int idProcesso, List<RetornoAdvogado> Advogados) throws SQLException {
        if (Advogados != null && !Advogados.isEmpty()) {
            String sql = "INSERT INTO apidistribuicao.processo_advogado (ID_processo, codPolo, nome,TipoPolo,oab,cnpj,cpf) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement statementAdv = conn.prepareStatement(sql)) {
                for (RetornoAdvogado adv : Advogados) {
                    statementAdv.setInt(1, idProcesso);
                    statementAdv.setInt(2, adv.getCodPolo());
                    statementAdv.setString(3, adv.getNome());
                    statementAdv.setString(4, adv.getTipoPolo());
                    statementAdv.setString(5, adv.getOab());
                    statementAdv.setString(6, adv.getCnpj());
                    statementAdv.setString(7, adv.getCpf());

                    statementAdv.executeUpdate();

                }
            }
        }
    }

    private void inserirOutrosEnvil(Connection conn, int idProcesso, List<RetornoOutrosEnvil> OutroEnvil) throws SQLException {
        if (OutroEnvil != null && !OutroEnvil.isEmpty()) {
            String sql = "INSERT INTO apidistribuicao.processo_outrosenvil (ID_processo, codPolo, nome,descricaoTipoPolo,cnpj,cpf) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement statementOutrosEnvil = conn.prepareStatement(sql)) {
                for (RetornoOutrosEnvil outros : OutroEnvil) {
                    statementOutrosEnvil.setInt(1, idProcesso);
                    statementOutrosEnvil.setInt(2, outros.getCodPolo());
                    statementOutrosEnvil.setString(3, outros.getNome());
                    statementOutrosEnvil.setString(4, outros.getDescricaoTipoPolo());
                    statementOutrosEnvil.setString(5, outros.getCnpj());
                    statementOutrosEnvil.setString(6, outros.getCpf());

                    statementOutrosEnvil.executeUpdate();

                }
            }
        }
    }
    private void inserirMovimentos(Connection conn, int idProcesso, List<RetornoMovimento> Movimentos) throws SQLException{
        if(Movimentos != null && !Movimentos.isEmpty()){
            String sql = "INSERT INTO apidistribuicao.processo_movimento (ID_processo, texto,data) VALUES (?,?,?)";
            try (PreparedStatement statementMovimentos = conn.prepareStatement(sql)){
                for (RetornoMovimento movimento : Movimentos){
                    statementMovimentos.setInt(1,idProcesso);
                    statementMovimentos.setString(2,movimento.getTexto());
                    if (movimento.getData() != null) {
                        statementMovimentos.setDate(3, new java.sql.Date(movimento.getData().getTime()));
                    } else {
                        statementMovimentos.setNull(3, 0);
                    }
                    statementMovimentos.executeUpdate();

                }
            }
        }
    }
    private void inserirLink (Connection conn, int idProcesso, List<RetornoDocIniciais> Link)throws SQLException{
        if (Link != null && !Link.isEmpty()){
            String sql = "INSERT INTO apidistribuicao.processo_link (ID_processo, link) VALUES (?,?)";
            try (PreparedStatement statementLink = conn.prepareStatement(sql)){
                for (RetornoDocIniciais link : Link){
                    statementLink.setInt(1,idProcesso);
                    statementLink.setString(2,link.getLinkDocInicial());

                    statementLink.executeUpdate();
                    inseridoComSucesso = true;
                }
            }

        }
    }
}