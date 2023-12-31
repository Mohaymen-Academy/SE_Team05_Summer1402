# About

This project is a mini "Full-Text Search" library that receives your data and searches through it fast.

# Features

You can search for some words through an enormous amount of files and texts in O(1) time. Also, this project supports
using optional searches and excluding results from final search results. The project supports setting custom tokenizer,
normalizer, and stop list. You can add all your files to a folder to be searched through. The project supports edge
n-gram for better search results as well. The result of the search will be sorted based on the relative occurrences of
the query. The default processing attributes:

- Tokenizer: SimpleTokenizer in opennlp library (default), Edge n-gram Tokenizer
- Normalizer: PortStemmer of opennlp library
- Stop list: list of regularly used marks
- Files Dataset: Software Books

# Usage

1. Create an object from the Application class
2. Configure the parameters or leave them as default:
    1. Set your normalizer/tokenizer by creating a class implementing its interface and set the object in setNormalizer
       method.
    2. Set your stopWords by reading a file or setting it as an array.
    3. Set your dataset folder path by the addByFolder method (Set your folder path after setting attributes).
3. Use the search method to search your query in the data set.

   Sample query:

   `get help +illness +disease -cough`

4. You can also use runInConsole to input queries from the console.

# Example

```java
import ir.shelmos_search.ShelmosSearch;
import ir.shelmos_search.language.Normalizer;
import ir.shelmos_search.language.Tokenizer;

public class Main {
    public static void main(String[] args) {
        new ShelmosSearch()
                .setNormalizer(new MyNormalizer())
                .setTokenizer(new MyTokenizer())
                .setStopWordsByFile("./src/main/resources/Stop Words.txt")
                .addDocsByFolder("./src/main/resources/Software Books Dataset/")
                .addDoc("Custom Doc", "goal have get compile")
                .runInConsole();
    }
}

class MyNormalizer implements Normalizer {
    @Override
    public String normalize(String token) {
        return token.toUpperCase();
    }
}

class MyTokenizer implements Tokenizer {
    @Override
    public String[] tokenize(String text) {
        return text.split("\\s");
    }
}
```
