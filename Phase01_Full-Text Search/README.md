# About

This project is a mini "Full Text Search" library which receives your data, and searches through it fast.

# Usage

1. Create an object from Application class
2. Configure the parameters or leave them as default:
   1. Set your normalizer by creating a class implementing Normalizer interface and set the object in setNormalizer method.
   2. Set your tokenizer by creating a class implementing Tokenizer interface and set the object in setTokenizer method.
   3. Set your stop-words by setStopWords method.
   4. Set your dataset folder path by addByFolder method (Set your folder path after setting attributes).
3. Run init method to create dictionary for you.
4. Use search method to search your query in data set.

   Sample query:

   `get help +illness +disease -cough`

5. you can also use runInConsole to input queries from console.

# Example

```java
public class Main {
   public static void main(String[] args) {
      new Application()
              .setNormalizer(new MyNormalizer())
              .setTokenizer(new MyTokenizer())
              .setStopWordsByFile("./src/main/resources/stopWords.txt")
              .addDocsByFolder("./src/main/resources/Software Books Dataset/")
              .addDoc("Ali", "goal have get compile")
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
      return text.split("\\s+");
   }
}
```
