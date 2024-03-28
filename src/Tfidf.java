import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Tfidf {

    public double tf(String comment, String term) {
        String[] words = comment.replaceAll("\\.", " ").replaceAll("\\p{Punct}", "").split("\\s+");
        int termFrequency = 0;
        for (String word : words) {
            if (term.equalsIgnoreCase(word)) {
                termFrequency++;
            }
        }
        System.out.println("FREQ: " + termFrequency);
        System.out.println("LEN: " + words.length);
        System.out.println("TF: " + (double) termFrequency / words.length);

        return (double) termFrequency / words.length;
    }

    public double idf(List<List<String>> docs, String term) {
        double n = 1;
        for (List<String> doc : docs) {
            String[] words = String.valueOf(doc).replaceAll("\\.", " ").replaceAll("\\p{Punct}", "").split("\\s+");
            for (String word : words) {
                if (term.equalsIgnoreCase(word)){
                    n++;
                    break;
                }
            }
        }
        System.out.println("n: " + n);
        System.out.println("Doc size(): " + docs.size());
        System.out.println("IDF: " + Math.log(docs.size() / n));
        return Math.log(docs.size() / n);
    }

    public double tfIdf(String doc, List<List<String>> docs, String term) {
        LocalDateTime tempoInicial = LocalDateTime.now();
        double tf = tf(doc, term);
        LocalDateTime tempoFinal = LocalDateTime.now();
        System.out.println("Tempo de cálculo do TF: " + Duration.between(tempoInicial, tempoFinal).toMillis()+"ms");
        tempoInicial = LocalDateTime.now();
        double idf = idf(docs, term);
        tempoFinal = LocalDateTime.now();
        System.out.println("Tempo de cálculo do IDF: " + Duration.between(tempoInicial, tempoFinal).toSeconds()+"s");
        return tf * idf;
    }

    public static void main(String[] args) {

        LocalDateTime tempoInicial = LocalDateTime.now();

        Path path = Paths.get("C:\\Users\\usuario\\Documents\\Lucas\\Estudos\\IMD\\Engsoft 2024.1\\Concorrente\\Dataset\\reviews.csv");
        List<List<String>> documents = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> doc = new ArrayList<>();
                for (String token : tokens) {
                    doc.add(token.replaceAll("^\"|\"$", "").trim());
                }
                documents.add(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalDateTime tempoFinal = LocalDateTime.now();

        System.out.println("Tempo de leitura do dataset: " + Duration.between(tempoInicial, tempoFinal).toSeconds()+"s");

        Tfidf calculator = new Tfidf();

        System.out.println("DOC 1: " + documents.get(1));

        tempoInicial = LocalDateTime.now();
        double tfidf = calculator.tfIdf(String.valueOf(documents.get(1)), documents, "content");
        tempoFinal = LocalDateTime.now();
        System.out.println("Tempo de cálculo do TF-IDF: " + Duration.between(tempoInicial, tempoFinal).toSeconds()+"s");
        System.out.println("TF-IDF = " + tfidf);
    }
}
