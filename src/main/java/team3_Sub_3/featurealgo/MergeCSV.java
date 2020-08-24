package team3_Sub_3;

import java.io.*;

public class MergeCSV {

    public static void Merge_CSV() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("data/character-deaths.csv"));
        BufferedReader reader1 = new BufferedReader(new FileReader("data/Mention_Stats.csv"));
        BufferedReader reader2 = new BufferedReader(new FileReader("data/Sentiment.csv"));
        BufferedReader reader3 = new BufferedReader(new FileReader("data/GenderCsv.csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/Final_Predictions/FeatureSet_Train.csv"));
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("data/Final_Predictions/FeatureSet_Test.csv"));
        String line = "";
        boolean flag = true;
        int count = 0;
        writer.write("No.,Character Name,Death,Mention Count,Mention Length,Mention Frequency,Avg. Positive Sentiment,Avg. Negative Sentiment,Gender\n");
        writer1.write("No,Character Name,Death,Mention Count,Mention Length,Mention Frequency,Avg. Positive Sentiment,Avg. Negative Sentiment,Gender\n");


        while((line = reader.readLine()) != null){
            if(flag){
                System.out.println(reader1.readLine());
                System.out.println(reader2.readLine());
                System.out.println(reader3.readLine());
                flag = false;
                continue;
            }
            String[] linee = line.split(",");
            String[] linee1 = reader1.readLine().split(",");
            String[] linee2 = reader2.readLine().split(",");
            String[] linee3 = reader3.readLine().split(",");
            if(count%2 == 0){
                if(linee[2].equals("")){
                    System.out.println(linee[0] + ",0" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2]);
                    writer.write(count + "," + linee[0] + ",0" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2] + "," + linee3[1] + "\n");
                    count++;
                }
                else{
                    System.out.println(linee[0] + ",1" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2]);
                    writer.write(count + "," + linee[0] + ",1" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2] + "," + linee3[1] + "\n");
                    count++;
                }
            }
            else{
                if(linee[2].equals("")){
                    System.out.println(linee[0] + ",0" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2]);
                    writer1.write(count + "," + linee[0] + ",0" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2] + "," + linee3[1] + "\n");
                    count++;
                }
                else{
                    System.out.println(linee[0] + ",1" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2]);
                    writer1.write(count + "," + linee[0] + ",1" + "," + linee1[1] + "," + linee1[2] + "," + linee1[3] + "," + linee2[1] + "," +linee2[2] + "," + linee3[1] + "\n");
                    count++;
                }
            }

        }
        writer.close();
        writer1.close();
        System.out.println(count);

    }

    public static void main(String[] args) throws IOException {
        Merge_CSV();
    }

}