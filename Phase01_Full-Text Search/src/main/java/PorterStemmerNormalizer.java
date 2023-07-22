import opennlp.tools.stemmer.PorterStemmer;

public class PorterStemmerNormalizer implements Normalizer {
    private final PorterStemmer stemmer;

    public PorterStemmerNormalizer() {
        stemmer = new PorterStemmer();
    }

    public String normalize(String token) {
        String lowered = token.toLowerCase();
        return stemmer.stem(lowered);
    }
}
