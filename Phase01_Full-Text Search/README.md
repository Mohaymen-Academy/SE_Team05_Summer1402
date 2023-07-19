# About
This project is a mini text search library which you give your data, and it searches anything you want fast. 

# Usage

1. create an object from Application class
2. configure the parameters or leave as default:
   1. set your dataset folder path by setDataPathFolder method.
   2. set your normalizer by creating a class implementing Normalizer interface and set the object in setNormalizer method.
   3. set your tokenizer by creating a class implementing Tokenizer interface and set the object in setTokenizer method.
   4. set your stop-words by setStopWords method.
3. run init method to create dictionary for you.
4. use search method to search your query in data set.

    sample query:

    `get help +illness +disease -cough`
5. you can also use runInConsole to input queries from console.

# Example

```java
public class Main {
   public static void main(String[] args) {
      new Application()
              .setDataPathFolder("./src/main/resources/Software Books Dataset/")
              .setNormalizer(new MyNormalizer())
              .setTokenizer(new MyTokenizer())
              .setStopWords(new String[]{"f"})
              .init()
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
      return text.split(" +");
   }
}
```