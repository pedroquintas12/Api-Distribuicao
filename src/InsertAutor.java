import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;


public class InsertAutor {


    public boolean inseridoComSucesso;

    public void inserir(RetornoAutor dados) {
        UUID codigoLocalizacao = UUID.randomUUID();
        String codigoLocalizacaoString = codigoLocalizacao.toString();
        LocalDate dataAtual = LocalDate.now();
        DadosApi Processo= new DadosApi();

        try {

            Connection connection = Conexao.getInstance().getConnection();

            //verifica se os dados ja existem no banco de dados
            String selectSql = "SELECT * FROM apidistribuicao.processo_autor WHERE codPolo = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, dados.getCodPolo());
            ResultSet resultSet = selectStatement.executeQuery();

            String selectID = "SELECT ID_processo FROM apidistribuicao.processo WHERE codProcesso = ?";
            PreparedStatement selectIDStatement = connection.prepareStatement(selectID);
            selectIDStatement.setInt(1, Processo.getCodProcesso());
            ResultSet resultSetID = selectIDStatement.executeQuery();

            int idProcesso = 0;

            if (resultSetID.next()) {
                idProcesso = resultSetID.getInt(1);


                // Insere os dados na tabela processo_autor usando o ID do processo
                String sqlProcessoAutor = "INSERT INTO apidistribuicao.processo_autor(ID_processo, codPolo, nome, descricaoTipoPolo, cnpj, cpf) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement statementProcessoAutor = connection.prepareStatement(sqlProcessoAutor);

                // Configura os parâmetros para a tabela processo_autor
                statementProcessoAutor.setInt(1, idProcesso); // Use o ID do processo
                statementProcessoAutor.setInt(2, dados.getCodPolo());
                statementProcessoAutor.setString(3, dados.getNome());
                statementProcessoAutor.setString(4, dados.getDescricaoTipoPolo());
                statementProcessoAutor.setString(5, dados.getCnpj());
                statementProcessoAutor.setString(6, dados.getCpf());

                // Execute a inserção na tabela processo_autor
                statementProcessoAutor.execute();

                // Define que a inserção foi bem-sucedida
                inseridoComSucesso = true;

                // Fecha os recursos relacionados à inserção na tabela processo_autor
                statementProcessoAutor.close();

            }


            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

