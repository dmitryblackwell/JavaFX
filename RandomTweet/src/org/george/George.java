package org.george;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class George extends Thread {

    class Book{
        String name;
        String author;
        String url;
        Book(String name, String author, String url) {
            this.name = name;
            this.author = author;
            this.url = url;
        }
    }

    private Twitter twitter;
    private Random R = new Random();
    private static final String PATH = "RandomTweet/resources/";
    private Book[] books = {
            new Book("Перси Джексон и море Чудовищ","Рик Риордан", PATH + "Riordan_Rik_Persi_Djekson.txt"),
            new Book("Герой нашего времени","Михаил Лермонтов",PATH + "Lermotov_Hero_Of_Our_Time.txt"),
            new Book("Овод","Войнич Этель Лилиан", PATH + "Ovod.txt"),
            new Book( "Над пропастью во ржи","Джером Д. Сэлинджер", PATH + "NadPropustue.txt"),
            new Book( "Великий Гэтсби","Фрэнсис Скотт Фицджеральд", PATH + "velikiy-getsbi.txt")
    };


    George(){
        ConfigurationBuilder builder = new ConfigurationBuilder();

        // put in TwitterKeys your tokens
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterKeys.ConsumerKey)
                .setOAuthConsumerSecret(TwitterKeys.ConsumerSecret)
                .setOAuthAccessToken(TwitterKeys.AccessToken)
                .setOAuthAccessTokenSecret(TwitterKeys.AccessTokenSecret);

        TwitterFactory factory = new TwitterFactory(builder.build());
        twitter = factory.getInstance();
    }

    private String getBookText(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(fileName), "UTF-8");
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(System.lineSeparator());
            }
            return sb.toString().trim();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    private String getRandomPhrase(String text){
        String[] phrases = text.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=[.?!])\\s");
        String result;
        do{
            result = phrases[R.nextInt(phrases.length)];
        }while (result.isEmpty());

        return result.trim();
    }


    private void tweet(String tweet){
        try {
            twitter.updateStatus(tweet);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){

            int bookNo = R.nextInt(books.length);
            String phrase = getRandomPhrase(getBookText(books[bookNo].url));
            String msg = "\"" + phrase + "\"\n" +
                    books[bookNo].author + ". " + books[bookNo].name + ".";
            tweet(phrase);

            System.out.println(msg + "\n~~~~~~~~~~~~\n");
            try {
                sleep(10*60*1000); // every 10 minutes
            } catch (InterruptedException e) {
                return;
            }
            if(isInterrupted())
                return;

        }
    }
}
