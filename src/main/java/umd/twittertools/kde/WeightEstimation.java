package umd.twittertools.kde;

import umd.twittertools.data.Tweet;
import umd.twittertools.data.TweetSet;
import umontreal.iro.lecuyer.probdist.PowerDist;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

public class WeightEstimation {
	
	public static DoubleArrayList computeUniformWeights(TweetSet tweetset) {
		DoubleArrayList weights = new DoubleArrayList();
		for (int i = 0; i < tweetset.size(); i++) {
			weights.add(1.0/tweetset.size());
		}
		return weights;
	}

	public static DoubleArrayList computeScoreBasedWeights(TweetSet tweetset) {
		DoubleArrayList weights = new DoubleArrayList();
		double totalScore = 0;
		for (Tweet tweet : tweetset) {
//			totalScore += Math.pow(Math.E, tweet.getQlScore());
      totalScore += tweet.getQlScore();
		}
		for (Tweet tweet : tweetset) {
//			double tweetScore = Math.pow(Math.E, tweet.getQlScore());
      double tweetScore = tweet.getQlScore();
			weights.add(tweetScore/totalScore);
		}
		return weights;
	}

	public static DoubleArrayList computeRankBasedWeights(TweetSet tweetset) {
		DoubleArrayList weights = new DoubleArrayList();
		double mean = 0;
		for (Tweet tweet: tweetset) {
			mean += tweet.getRank() * 1.0 / tweetset.size();
		}
		double lambda = 1;// 1/mean;
		double totalRank = 0;
		for (Tweet tweet: tweetset) {
			totalRank += lambda * Math.pow(Math.E, -tweet.getRank());
		}
		for (Tweet tweet : tweetset) {
		  int rank = tweet.getRank();
      double tweetRank = lambda * Math.pow(Math.E, -tweet.getRank() * 1.0 / 5.0);
//      if (rank < 30)
//         tweetRank =1;
			weights.add(tweetRank/totalRank);
		}
		return weights;
	}

	public static DoubleArrayList computeFeedbackWeights(TweetSet tweetset, TweetSet feedbackSet) {
		DoubleArrayList weights = new DoubleArrayList();
//		DoubleArrayList scoreBasedWeights = computeScoreBasedWeights(tweetset);
//		double totalScore = 0;
//		for (Tweet tweet : tweetset) {
//			if (feedbackSet.contains(tweet)) {
//				totalScore += 1.0;
//			} else {
//				//totalScore += Math.pow(Math.E, tweet.getQlScore());
//        totalScore += tweet.getQlScore();
//			}
//		}
//
//		for (Tweet tweet : tweetset) {
//			if (feedbackSet.contains(tweet)) {
//				weights.add(1.0/totalScore);
//			} else {
//				//weights.add(Math.pow(Math.E, tweet.getQlScore())/totalScore);
//        weights.add(tweet.getQlScore()/totalScore);
//			}
//		}
		double mean = 0;
		for (Tweet tweet: tweetset) {
			mean += tweet.getRank() * 1.0 / tweetset.size();
		}
		double lambda = 1;//1/mean;
		double totalRank = 0;
		for (Tweet tweet: tweetset) {
      if (feedbackSet.contains(tweet)) {
        totalRank += 1;
      } else {
        totalRank += lambda * Math.pow(Math.E, -tweet.getRank());
      }
		}
		for (Tweet tweet : tweetset) {
			if (feedbackSet.contains(tweet)) {
				weights.add(1.0/ totalRank);
			} else {
				double tweetRank = lambda * Math.pow(Math.E, -tweet.getRank());
				weights.add(tweetRank / totalRank);
			}
		}
		return weights;
	}
	
}
