package umd.twittertools.eval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import umd.twittertools.data.Tweet;
import umd.twittertools.data.TweetSet;

import com.google.common.collect.Table;

public class Evaluation {
	
	public static double P_RANK (int qid, TweetSet tweetSet, 
			Table<Integer, Long, Integer> qrels, int rank) throws IOException {
		// Compute Precision by query
		double P30 = 0;
		rank = Math.min(rank, tweetSet.size());
		for (int i = 0; i < rank; i++) {
			Tweet tweet = tweetSet.getTweet(i);
			if (qrels.contains(qid, tweet.getId())) {
				P30 += 1.0 / rank;
			}
		}
//    List<String> lines = new ArrayList<String>();
//    Path file = Paths.get("logging/temp");
//    for (int i = 0; i < tweetSet.size(); i++){
//      Tweet tweet = tweetSet.getTweet(i);
//      lines.add(Long.toString(qid) + " Q0 " + Long.toString(tweet.getId()) + " 0 " + Double.toString(tweet.getTMScore()) + " KDE");
//    }
//    Files.write(file, lines, Charset.forName("UTF-8"));
//    Runtime rt = Runtime.getRuntime();
//    String[] commands = {"util/trec_eval-9.0.5/trec_eval", "data/qrels.txt", "logging/temp"};
//    Process proc = rt.exec(commands);
//
//    BufferedReader stdInput = new BufferedReader(new
//        InputStreamReader(proc.getInputStream()));
//
//    BufferedReader stdError = new BufferedReader(new
//        InputStreamReader(proc.getErrorStream()));
//    System.out.println("Here is the standard output of the command:\n");
//    String s = null;
//    while ((s = stdInput.readLine()) != null) {
//      System.out.println(s);
//    }
//
//    // read any errors from the attempted command
//    System.out.println("Here is the standard error of the command (if any):\n");
//    while ((s = stdError.readLine()) != null) {
//      System.out.println(s);
//    }
//    System.out.println(P30);
//    System.exit(0);
    return P30;
	}
	
	public static double MAP (int qid, TweetSet tweetSet, 
			Table<Integer, Long, Integer> qrels, Map<Integer, Integer> numrels) {
		int TP = 0;
		double AP = 0;
		for (int i = 0; i < tweetSet.size(); i++) {
			Tweet tweet = tweetSet.getTweet(i);
			if (qrels.contains(qid, tweet.getId())) {
				TP += 1;
				AP += TP * 1.0 / (i+1);
			}
		}
		return AP / numrels.get(qid);
	}
}
