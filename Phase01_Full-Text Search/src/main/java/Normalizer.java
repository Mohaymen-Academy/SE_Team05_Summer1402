import opennlp.tools.stemmer.PorterStemmer;

public class Normalizer implements INormalizer {
    public String normalize(String token) {
        PorterStemmer stemmer = new PorterStemmer();
        token = token.toLowerCase();
        return stemmer.stem(token);
    }
}
