package team3_Sub_3;

import java.io.*;
import java.util.*;

public class Construct_Knowledge_Graph {

    static ArrayList<String> words = new ArrayList<>(Arrays.asList("brother of" , "sister of", "mother of" , "father of" , "wife of" , "husband of", "daughter of", "son of"));
    static Graph graph;

    static {
        try {
            graph = new Graph(count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Link{
        int vertex;
        String subject;
        String predicate;
        String object;
        int isDead;

        public Link(int vertex,String subject, String predicate, String object, int isDead){
            this.vertex = vertex;
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
            this.isDead = isDead;
        }
    }

    static class Graph {
        int vertices;
        LinkedList<Link>[] list;

        Graph(int vertices){
            this.vertices = vertices;
            list = new LinkedList[vertices];
            //initialize adjacency Links for all the vertices
            for (int i = 0; i <vertices ; i++) {
                list[i] = new LinkedList<>();
            }
        }

        public void addLink(int vertex,String subject, String predicate, String object, int isDead) {
            Link link = new Link(vertex,subject, predicate, object, isDead);
            list[vertex].addFirst(link); //for directed graph
        }

        public int printGraph(){
            int dead_count = 0;
            for (int i = 0; i <vertices ; i++) {
                LinkedList<Link> listt = list[i];
                for (int j = 0; j <listt.size() ; j++) {
                    //System.out.println("Character-" + listt.get(j).subject + " predicate " + listt.get(j).predicate + " object " +  listt.get(j).object);
                    if(listt.get(j).isDead == 0){
                        dead_count++;
                    }
                }
            }
            System.out.println("DC: " + dead_count);
            return dead_count;
        }

        /*Extracts the Gender from the Knowledge Graph*/
        public void PrintGender() throws IOException {
            int dead_count = 1;
            BufferedReader reader = new BufferedReader(new FileReader("data/names.txt"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/KG_Gender.run"));
            BufferedWriter writee = new BufferedWriter(new FileWriter("data/GenderCsv.csv"));
            writee.write("Character Name,Gender\n");
            String line = "";
            boolean f = true;
            while ((line = reader.readLine()) != null) {
                if(f){
                    f = false;
                    continue;
                }
                String[] nnchar = line.split(" ");


                boolean flag1 = true;

                for (int i = 0; i < vertices; i++) {
                    LinkedList<Link> listt = list[i];
                    for (int j = 0; j < listt.size(); j++) {
                        //System.out.println("Character-" + listt.get(j).subject + " predicate " + listt.get(j).predicate + " object " + listt.get(j).object);
                        //dead_count++;
                        if(line.equals(listt.get(j).subject)){
                            if(listt.get(j).predicate.equals("son of") || listt.get(j).predicate.equals("brother of") || listt.get(j).predicate.equals("father of") || listt.get(j).predicate.equals("husband of") || listt.get(j).predicate.equals("is male")){
                                //System.out.println("Name: " + line + " Gender: 1");
                                writer.write("0 0 "+line.replaceAll(" ","_")+" "+ dead_count + " 1 team_3\n");
                                writee.write(line + ",1" + "\n");
                                dead_count++;
                                flag1 = false;
                            }
                            else if(listt.get(j).predicate.equals("daughter of") || listt.get(j).predicate.equals("sister of") || listt.get(j).predicate.equals("mother of") || listt.get(j).predicate.equals("wife of") || listt.get(j).predicate.equals("is female")){
                                //System.out.println("Name: " + line + " Gender: 0");
                                writer.write("0 0 "+line.replaceAll(" ","_")+" "+ dead_count + " 0 team_3\n");
                                writee.write(line + ",0" + "\n");
                                dead_count++;
                                flag1 = false;
                            }
                            else{
                                //System.out.println("Name: " + line + " Gender: 1");
                                writer.write("0 0 "+line.replaceAll(" ","_") +" "+ dead_count + " 1 team_3\n");
                                writee.write(line + ",1" + "\n");
                                dead_count++;
                                flag1 = false;
                            }

                        }

                        break;
                    }
                }
                if(flag1){
                    //System.out.println("Name: " + line + " Gender: 1");
                    writer.write("0 0 "+line.replaceAll(" ","_") +" "+ dead_count + " 1 team_3\n");
                    writee.write(line + ",1" + "\n");
                    dead_count++;
                }


            }
            System.out.println("DC: " + dead_count);
            writer.close();
            writee.close();
            Remove_Duplicates();
        }

    }

    /*Removes any duplicates if any in the Gender runfile created*/
    public static void Remove_Duplicates() throws IOException {

        String filePath = "data/KG_Gender.run";
        //String[] input = null;
        //Instantiating the Scanner class
        Scanner sc = new Scanner(new File(filePath));
        //Instantiating the FileWriter class
        FileWriter writer = new FileWriter("data/KG_Gender_Runfile.run");
        //Instantiating the Set class
        Set set = new HashSet();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] inputt = input.split(" ");
            if(set.add(inputt[2])) {
                writer.append(input+"\n");
            }
        }
        writer.flush();
        System.out.println("Contents added............");

    }

    /*Creates Triples and constructs the Knowledge Graph*/
    public static void Create_Triples() throws IOException {

        FileInputStream input = new FileInputStream("data/data.properties");
        HashMap<String, String> ldapContent = new HashMap<String, String>();
        Properties prop = new Properties();

        prop.load(input);
        int vertex = 0;
        for(String key : prop.stringPropertyNames()) {
            boolean flag = false;
            String name = key;
            String[] namee = name.split(" ");
            String[] text = prop.get(key).toString().split("[.]");
            for(int i = 0;i<text.length;i++){
                String[] textt = text[i].split("[,]");
                for(int j = 0;j<textt.length;j++){
                    for(String word : words){
                        if(textt[j].contains(word)){
                            String line = textt[j].substring(textt[j].indexOf(word));
                            String[] linee = line.split(" ");
                            //System.out.println("Subject: " + name);
                            //System.out.println("Predicate: " + word);
                            if(namee.length == 1){
                                //System.out.println("Object: " + linee[2]);
                                graph.addLink(vertex,name,word,linee[2],0);
                                flag = true;
                            }
                            else if(namee.length == 2){
                                if(namee[1].contains("(")){
                                    //System.out.println("Object: " + linee[2]);
                                    graph.addLink(vertex,name,word,linee[2],0);
                                    flag = true;
                                }
                                else{
                                    if(linee[2].equals("Lord") || linee[2].equals("Lady") || linee[2].equals("King") || linee[2].equals("Queen")){
                                        //System.out.println("Object: " + linee[3] + " " + namee[1]);
                                        graph.addLink(vertex,name,word,(linee[3] + " " + namee[1]),0);
                                        flag = true;
                                    }
                                    else{
                                        //System.out.println("Object: " + linee[2] + " " + namee[1]);
                                        graph.addLink(vertex,name,word,(linee[2] + " " + namee[1]),0);
                                        flag = true;
                                    }

                                }
                            }
                            else{
                                if(linee[2].equals("Lord") || linee[2].equals("Lady") || linee[2].equals("King") || linee[2].equals("Queen")){
                                    //System.out.println("Object: " + linee[3] + " " + namee[1]);
                                    graph.addLink(vertex,name,word,(linee[3] + " " + namee[1]),0);
                                    flag = true;
                                }
                                else{
                                    //System.out.println("Object: " + linee[2] + " " + namee[1]);
                                    graph.addLink(vertex,name,word,(linee[2] + " " + namee[1]),0);
                                    flag = true;
                                }

                            }

                        }

                    }
                }
            }
            if(!flag){
                boolean flagg = false;
                String[] tex = prop.get(key).toString().split("[.]");
                for(int i = 0;i<tex.length;i++){
                    String[] texx = tex[i].split("[,]");
                    for(int j = 0;j<texx.length;j++){
                        if(texx[j].contains("He") || texx[j].contains("he") || texx[j].contains("him") || texx[j].contains("Him") || texx[j].contains("His") || texx[j].contains("his")){
                            graph.addLink(vertex,name,"is male","in GoT",0);
                            flagg = true;
                            break;
                        }
                        else if(texx[j].contains("She") || texx[j].contains("she") || texx[j].contains("her") || texx[j].contains("Her") || texx[j].contains("Her's") || texx[j].contains("her's")){
                            graph.addLink(vertex,name,"is female","in GoT",0);
                            flagg = true;
                            break;
                        }
                        else{
                            graph.addLink(vertex,name,"NA","in GoT",0);
                            flagg = true;
                            break;
                        }
                    }
                    if(flagg){
                        break;
                    }
                }
            }
            vertex++;
        }

    }

    /*Prints the extracted gender for each character*/
    public static void Display_Gender() throws IOException {
        graph.PrintGender();
    }

    public static int count() throws IOException {
        int count = 0;

        FileInputStream input = new FileInputStream("data/data.properties");
        Properties prop = new Properties();

        prop.load(input);
        for(String key : prop.stringPropertyNames()){

            count++;

        }
        //System.out.println(count);
        return count;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Constructing Knowledge Graph....");
        Create_Triples();
        System.out.println("Knowledge Graph Constructed.");
        System.out.println("Extracting Gender....");
        Display_Gender();
        System.out.println("Gender Extracted.");
    }
}