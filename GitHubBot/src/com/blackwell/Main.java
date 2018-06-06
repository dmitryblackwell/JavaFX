package com.blackwell;

import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) throws IOException, ParseException {
        Thread monitor = new GitMonitor("dmitryblackwell",
                TwitterKeys.ConsumerKey,
                TwitterKeys.ConsumerSecret,
                TwitterKeys.AccessToken,
                TwitterKeys.AccessTokenSecret);

        ((GitMonitor) monitor).update();
        monitor.start();




    }

    private static void allTweetDelete(){
        ConfigurationBuilder builder = new ConfigurationBuilder();

        // put in TwitterKeys your tokens
        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterKeys.ConsumerKey)
                .setOAuthConsumerSecret(TwitterKeys.ConsumerSecret)
                .setOAuthAccessToken(TwitterKeys.AccessToken)
                .setOAuthAccessTokenSecret(TwitterKeys.AccessTokenSecret);

        TwitterFactory factory = new TwitterFactory(builder.build());
        Twitter twitter = factory.getInstance();

        try {

            Paging paging = new Paging(1, 200);
            List<Status> statuses = twitter.getUserTimeline("GeorgeMcBerry",  paging);
            statuses.size(); // !!! do not delete - without this nothing working
            ArrayList<Long> statusesId = new ArrayList<>();
            for(Status s : statuses)
                statusesId.add(s.getId());

            for(int i=0; i<statusesId.size(); ++i) {
                twitter.destroyStatus(statusesId.get(i));
                System.out.println("Tweets deleted: " + i + "/" + statusesId.size());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
