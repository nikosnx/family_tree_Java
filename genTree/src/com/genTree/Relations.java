package com.genTree;

import java.io.*;
import java.util.*;

public class Relations {
    private final Map<String, Member> persons = new HashMap<>(); //need map to get persons info
    private final Set<Relation> relations = new HashSet<>(); //no need for key

    public void ImportDataFromFile(String path) throws IOException  {
        try (
                FileInputStream is = new FileInputStream(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
                BufferedReader bf = new BufferedReader(new InputStreamReader(bufferedInputStream))
        ) {
            String line;
            int lineNumber = 0;

            while ( (line = bf.readLine()) != null) {
                lineNumber++;

                if(line.isEmpty())
                    continue;

                final String[] split = Arrays.stream(line.split(",")).map(String::trim).toArray(String[]::new);

                switch (split.length) {
                    case 2: //means content is name & gender
                        persons.put(Tools.format(split[0]), new Member(split[0], split[1]));
                        break;
                    case 3: //means content is name, basic relation & related person
                        relations.add(new Relation(split[0], split[1], split[2]));
                        break;
                    default:
                        throw new IllegalArgumentException(
                                String.format("Invalid line formation %d: %s", lineNumber, line));
                }
            }
            connectBasic();
        }
    }
    //implement relations based on given data from the csv
    private void connectBasic() {
        for (Relation relation : relations) {
            Member member1 = persons.get(relation.getPerson1());
            Member member2 = persons.get(relation.getPerson2());

            switch (relation.getRelType()) {
                case "mother": //continue to father
                case "father": //connect child with parent, specify mother/father in member "setParent" function
                    member2.setParent(member1, relation.getRelType());
                    member1.setChildren(member2);
                    break;
                case "husband": //connect couple
                    member1.setSpouse(member2);
                    member2.setSpouse(member1);
                    break;
                default:
                    throw new IllegalArgumentException(
                            String.format("%s is an unexpected relation", relation.getRelType()));
            }
        }
    }

    public void writeToTxtSorted() {
        //new Arraylist needed in order to sort by name
        List<Member> persons = new ArrayList(this.persons.values());
        Collections.sort(persons, new SortByName());
        //write names to file
        try (
                FileWriter w = new FileWriter("namesgender.txt");
                BufferedWriter b = new BufferedWriter(w)
        ) {

            for (Member m : persons) {
                b.write(m.getName() + ", " + (m.getGender() == 1 ? "man" : "woman"));
                b.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToDot() {

        List<Relation> relations = new ArrayList(this.relations);
        try (
                FileWriter w = new FileWriter("relations.dot");
                BufferedWriter b = new BufferedWriter(w)
        ) {
            String line = "\"%s\" -> \"%s\" [label=\"%s\"]; ";
            String hLine = "{rank = same; \"%s\"; \"%s\"}";
            b.write("digraph HouseBaratheon {");
            b.newLine();
            b.write("rankdir=TD;");
            b.newLine();
            b.write("size=8.5");   // ""
            b.newLine();
            b.write("node [shape = rectangle] [color=black];");
            b.newLine();
            for (Relation r : relations) {
                b.write(String.format(line, r.getPerson1(), r.getPerson2(), r.getRelType()));
                b.newLine();
                if (Objects.equals(r.getRelType(), "husband")) {
                    b.write(String.format(hLine, r.getPerson1(), r.getPerson2()));
                    b.newLine();
                }
            }
            b.write("}");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public String getUserInput() {
        String name1, name2;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the full name of the first person: ");
        name1 = Tools.format(Tools.convertToLowerCaseTrim(s.nextLine()));
        System.out.println("Enter the full name of the second person: ");
        name2 = Tools.format(Tools.convertToLowerCaseTrim(s.nextLine()));
        return String.format("%s , %s", name1, name2);

    }

    public void printRelations(String fullName) {


        final String[] split = Arrays.stream(fullName.split(",")).map(String::trim).toArray(String[]::new);
        Member member1 = persons.get(split[0]);
        Member member2 = persons.get(split[1]);
        String result = Relation.getRelation(member1, member2);

        System.out.println(String.format("%s is %s of %s", member1.getName(), result, member2.getName()));
    }


}
