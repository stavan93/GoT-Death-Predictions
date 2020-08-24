package team3_Sub_3;


import java.io.*;

public class Final_MergeCSV {

    public static void Merge_Train_CSV() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("data/Final_Predictions/FeatureSet_Train.csv"));

        BufferedReader reader2 = new BufferedReader(new FileReader("data/Final_Predictions/A_Train.csv"));
        
        BufferedReader reader3 = new BufferedReader(new FileReader("data/Final_Predictions/EntityLinked_FeatureTrain.csv"));

        BufferedWriter writer = new BufferedWriter(new FileWriter("data/Final_Predictions/All_FeatureSet_Train_final.csv"));

        String line = "";
        boolean flag = true;
        int count = 1;
        writer.write("No.,Character Name,Death,Mention Count,Mention Length,Mention Frequency,Avg. Positive Sentiment,Avg. Negative Sentiment,Gender,Weapon,Longevity,Killer,Married,Nobility,Battle Winner,Gender2,Popularity,Gender1,TFIDF\n");



        while((line = reader.readLine()) != null){
            if(flag){
                System.out.println(line);
                System.out.println(reader2.readLine());
                System.out.println(reader3.readLine());
                flag = false;
                continue;
            }
            String[] linee = line.split(",");
            String[] line2 = reader2.readLine().split(",");
            String[] line3 = reader3.readLine().split(",");
            writer.append(linee[0]).append(",").append(linee[1]).append(",").append(linee[2]).append(",").append(linee[3]).append(",").append(linee[4]).append(",").append(linee[5]).append(",").append(linee[6]).append(",").append(linee[7]).append(",").append(linee[8]).append(",").append(line2[2]).append(",").append(line2[3]).append(",").append(line2[4]).append(",").append(line2[5]).append(",").append(line2[6]).append(",").append(line2[7]).append(",").append(line2[8]).append(line3[2]).append(",").append(line3[3]).append(",").append(line3[5]).append(",").append(line3[6]).append("\n");
            count++;
        }
        writer.close();
        //writer1.close();
        System.out.println(count);

    }

    public static void Merge_Test_CSV() throws IOException {


        BufferedReader reader1 = new BufferedReader(new FileReader("data/Final_Predictions/FeatureSet_Test.csv"));

        BufferedReader reader3 = new BufferedReader(new FileReader("data/Final_Predictions/A_Test.csv"));
        
        BufferedReader reader2 = new BufferedReader(new FileReader("data/Final_Predictions/EntityLinked_FeatureTest.csv"));

        BufferedWriter writer1 = new BufferedWriter(new FileWriter("data/Final_Predictions/All_FeatureSet_Test_final.csv"));
        String linee = "";
        boolean flag = true;
        int count = 1;
        writer1.write("No.,Character Name,Death,Mention Count,Mention Length,Mention Frequency,Avg. Positive Sentiment,Avg. Negative Sentiment,Gender,Weapon,Longevity,Killer,Married,Nobility,Battle Winner,Gender2,Popularity,Gender1,TFIDF\n");


        while((linee = reader1.readLine()) != null){
            if(flag){
                System.out.println(reader3.readLine());
                System.out.println(reader2.readLine());
                flag = false;
                continue;
            }
            String[] line1 = linee.split(",");
            String[] line3 = reader3.readLine().split(",");
            String[] line2 = reader2.readLine().split(",");
            writer1.append(line1[0]).append(",").append(line1[1]).append(",").append(line1[2]).append(",").append(line1[3]).append(",").append(line1[4]).append(",").append(line1[5]).append(",").append(line1[6]).append(",").append(line1[7]).append(",").append(line1[8]).append(",").append(line3[2]).append(",").append(line3[3]).append(",").append(line3[4]).append(",").append(line3[5]).append(",").append(line3[6]).append(",").append(line3[7]).append(",").append(line3[8]).append(line2[2]).append(",").append(line2[3]).append(",").append(line2[5]).append(",").append(line2[6]).append("\n");
            count++;
        }
        writer1.close();
        System.out.println(count);

    }

    public static void main(String[] args) throws IOException {
        Merge_Train_CSV();
        Merge_Test_CSV();
    }

}