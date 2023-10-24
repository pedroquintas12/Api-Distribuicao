import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String FILE_JSON = "C:\\Users\\pedro\\IdeaProjects\\Api Distribuicao\\src\\BODY.JSON";
        String url = "http://online.solucionarelj.com.br:9090/WebApiDistribuicoesV2/api/distribuicoes/BuscaNovasDistribuicoes";
        var client = HttpClient.newHttpClient();

        //faz o request para a API
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofFile(Path.of(FILE_JSON)))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .uri(URI.create(url))
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

        }
    }
