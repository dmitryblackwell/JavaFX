package com.blackwell;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class GitMonitor extends Thread {
    private String gitLogin;
    private Twitter twitter;
    private JSONParser parser = new JSONParser();
    private Map<String, Integer> repos = new HashMap<>();

    //https://api.github.com/users/dmitryblackwell/repos
    //https://api.github.com/repos/dmitryblackwell/JavaFX/commits
    private static final String API_GIT = "https://api.github.com/";
    private static final String USERS = "users";
    private static final String REPOS = "repos";
    private static final String COMMIT = "commit";
    private static final String COMMITS = "commits";
    private static final String COMMIT_TO = "Commit to ";


    GitMonitor(String gitLogin, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.gitLogin = gitLogin;

        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory factory = new TwitterFactory(builder.build());
        twitter = factory.getInstance();
    }

    public void update(){
        try {
            checkForRepos();
            getNotTrackedCommits();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void sendTweet(String tweet){
        try {
            twitter.updateStatus(tweet);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private synchronized void checkForRepos() throws IOException, ParseException {
        URL url = new URL(API_GIT + USERS +"/"+ gitLogin +"/"+ REPOS);

        JSONArray reposArray = getJsonFileFromURL(url);

        if (reposArray.size() != repos.size()) {
            for (Object repo : reposArray) {
                JSONObject repObj = (JSONObject) repo;
                String repoName = repObj.get("name").toString();
                if(!repos.containsKey(repoName))
                    repos.put(repoName, 0);
            }
        }
    }

    private synchronized List<String> getNotTrackedCommits() throws IOException, ParseException {
        List<String> notTrackedCommits = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : repos.entrySet()){
            URL url = new URL(API_GIT + REPOS +"/"+ gitLogin +"/"+ entry.getKey() +"/"+ COMMITS);
            JSONArray jsonArray = getJsonFileFromURL(url);
            int difference = jsonArray.size() - entry.getValue();
            for(int i=0; i<difference; ++i){
                JSONObject commit = (JSONObject) jsonArray.get(i);
                JSONObject commitData = (JSONObject) commit.get(COMMIT);
                String commitMessage = (String) commitData.get("message");

                String message = COMMIT_TO +"#"+ entry.getKey() + "\n\n" +
                        commitMessage;
                notTrackedCommits.add(message);
            }
            entry.setValue(entry.getValue()+difference);
        }
        return notTrackedCommits;
    }

    private JSONArray getJsonFileFromURL(URL url) throws IOException, ParseException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ( (line = in.readLine()) != null)
            sb.append(line);

        return  (JSONArray) parser.parse(sb.toString());
    }

    public void sendAll(){
        try {
            repos.clear();
            checkForRepos();
            List<String> notTrackedCommits = getNotTrackedCommits();
            for(String message : notTrackedCommits)
                //System.out.println(message+"\n~~~~~~~~~~~~~~~~~~\n");
                sendTweet(message);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){

            try {

//                checkForRepos();
//                List<String> notTrackedCommits = getNotTrackedCommits();
//                for(String message : notTrackedCommits)
//                    //System.out.println(message+"\n~~~~~~~~~~~~~~~~~~\n");
//                    sendTweet(message);

                Thread.sleep(70*1000);

                if (interrupted())
                    return;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
