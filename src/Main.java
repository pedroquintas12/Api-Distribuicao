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
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
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
                        System.out.println("DADOS INSERIDOS COM SUCESSO");
                    } else {
                        System.out.println("DADOS JÁ CADASTRADOS");
                    }
                })
                .join();
    }
}
