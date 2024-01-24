import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // Configuração do logging para escrever em um arquivo
        try {
            FileHandler fileHandler = new FileHandler("log.txt");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cria uma instância do Timer
        Timer timer = new Timer();

        // Agenda a execução do código para ser repetida a cada 30 minutos
        timer.scheduleAtFixedRate(new AgendadorTarefa(logger), 0, 60 * 60 * 1000);
    }

    public static void executarCodigo() {
        try {
            String FILE_JSON = "C:\\Users\\pedro\\IdeaProjects\\Api Distribuicao\\src\\BODY.JSON";
            String url = "http://online.solucionarelj.com.br:9090/WebApiDistribuicoesV2/api/distribuicoes/BuscaNovasDistribuicoes";
            var client = HttpClient.newHttpClient();

            // Faz o request para a API
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofFile(Path.of(FILE_JSON)))
                    .header("Content-Type", "application/json")
                    .uri(URI.create(url))
                    .build();

            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(jsonResponse -> {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .create();
                        DadosApi[] dados = gson.fromJson(jsonResponse, DadosApi[].class);
                        return dados;
                    })
                    .thenAccept(dados -> {
                        InsertApi inserter = new InsertApi();

                        for (DadosApi dado : dados) {
                            inserter.inserir(dado);
                        }

                        if (inserter.inseridoComSucesso) {
                            logger.log(Level.INFO, "DADOS INSERIDOS COM SUCESSO");
                        } else {
                            logger.log(Level.INFO, "DADOS JÁ CADASTRADOS");
                        }
                    })
                    .exceptionally(ex -> {
                        logger.log(Level.SEVERE, "Erro ao executar o código", ex);
                        return null; // Suprimir a exceção para permitir que o Timer continue
                    })
                    .join();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao executar o código", e);
        }
    }

    private static String obterHoraAtualFormatada() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}

class AgendadorTarefa extends TimerTask {
    private final Logger logger;

    public AgendadorTarefa(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        Main.executarCodigo();
    }
}
