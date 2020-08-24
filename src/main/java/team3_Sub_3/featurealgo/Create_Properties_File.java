package team3_Sub_3;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Create_Properties_File {

    public static HashMap<String,String> data = new HashMap<>();
    public static Properties properties = new Properties();

    /*Parses through the cbor file and creates a properties file of
    character names and paragraph text*/
    public static void Create_Properties_File() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("data/names.txt"));

        String line = "";
        while((line = reader.readLine()) != null){

            final String pagesFile = "data/got.cbor";
            final FileInputStream fileInputStream = new FileInputStream(new File(pagesFile));

            for(Data.Page page: DeserializeData.iterableAnnotations(fileInputStream)) {
                boolean flag = false;
                int count = 0;
                String text = "";
                if(line.equals(page.getPageName())){
                    for(Data.Page.SectionPathParagraphs linee : page.flatSectionPathsParagraphs()){
                        if(count > 5){
                            break;
                        }
                        text = linee.getParagraph().getTextOnly();
                        count++;
                        flag = true;
                    }

                    if(flag) {
                        data.put(page.getPageName(),text);
                        properties.put(page.getPageName(),text);
                        System.out.println(page.getPageName() + " Added.");
                        break;
                    }

                }

            }

        }
        properties.store(new FileOutputStream("dat.properties"), null);

    }

    /*Counts the total number of entries in the properties file*/
    public static void Count() throws IOException {
        int count = 0;
        if(data.isEmpty()){
            Create_Properties_File();
        }

        for (HashMap.Entry<String,String> entry : data.entrySet()) {
            count++;
        }

        System.out.println(count);

    }

    public static void main(String[] args) throws IOException {

        Count();

    }

}