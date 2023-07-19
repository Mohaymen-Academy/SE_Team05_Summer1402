import opennlp.tools.stemmer.PorterStemmer;

public class DefaultNormalizer implements Normalizer {
    public String normalize(String token) {
        PorterStemmer stemmer = new PorterStemmer();
        String lowered = token.toLowerCase();
        return stemmer.stem(lowered);
    }
}
