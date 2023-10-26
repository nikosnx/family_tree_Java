package com.genTree;

import java.util.ArrayDeque;
import java.util.Deque;

public class Relation {
    private String person1;
    private String person2;
    private String relType;

    public Relation(String person1, String relation, String person2) {
        setPerson1(person1);
        setPerson2(person2);
        setRelType(relation);
    }
    //getters
    public String getPerson1() {
        return person1;
    }

    public String getPerson2() {
        return person2;
    }

    public String getRelType() {
        return relType;
    }

    public static String getRelation(Member member1, Member member2) {
        String relation = "Nothing"; //if none of the following is true, there is no relation

        if (member1.equals(member2)) //same person
            return "Same person!";

        if (isParent(member1, member2))
            return member1.getGender() == 0 ? "mother" : "father";

        if (isParent(member2, member1))
            return member1.getGender() == 0 ? "daughter" : "son";

        if (areSiblings(member1, member2))
            return member1.getGender() == 0 ? "sister" : "brother";

        if (areCousins(member1, member2))
            return "cousin";

        if (isCouple(member1, member2))
            return member1.getGender() == 0 ? "wife" : "husband";

        if (isGrandParent(member1, member2))
            return member1.getGender() == 0 ? "grandmother" : "grandfather";

        if (isGrandParent(member2, member1))
            return member1.getGender() == 0 ? "granddaughter" : "grandson";

        if (isAuntUncle(member2, member1))
            return member1.getGender() == 0 ? "niece" : "nephew";

        if (isAuntUncle(member1, member2))
            return member1.getGender() == 0 ? "aunt" : "uncle";

        return relation;
    }


    //setters
    public void setPerson1(String person) {
        this.person1 = Tools.format(person);
    }

    public void setPerson2(String person) {
        this.person2 = Tools.format(person);
    }

    public void setRelType(String relType) {
        this.relType = Tools.convertToLowerCaseTrim(relType);
    }


    @Override
    public String toString() {
        return person1 + " is " + relType + " of " + person2;
    }

    private static boolean isParent(Member member1, Member member2) {
        return member1.getChildren() != null && member1.getChildren().contains(member2);
    }

    private static boolean areSiblings(Member member1, Member member2) {
        Member mother = member1.getMother();
        Member father = member1.getFather();

        //include step siblings case
        if (mother != null && father == null) {
            if (mother.getSpouse() != null)
                father = mother.getSpouse();
        }
        else if (father != null && mother == null) {
            if ( father.getSpouse() != null)
                mother = father.getSpouse();
        }

        return ( mother != null  && mother.equals(member2.getMother())) || (father != null && father.equals(member2.getFather()));
    }

    private static boolean areCousins(Member member1, Member member2) {
        Member mother = member1.getMother();
        Member father = member1.getFather();
        Deque<Member> people = new ArrayDeque<>();

        //non-blood relation
        if (mother != null && father == null) {
            if (mother.getSpouse() != null)
                father = mother.getSpouse();
        }
        else if (father != null && mother == null) {
            if (father.getSpouse() != null)
                mother = father.getSpouse();
        }

        //Add mother's parents if she exists
        if (mother != null) {
            if (mother.getMother() != null)
                people.add(mother.getMother());

            if (mother.getFather() != null)
                people.add(mother.getFather());
        }

        //Add father's parent if he exists
        if (father != null) {
            if (father.getMother() != null)
                people.add(father.getMother());

            if (father.getFather() != null)
                people.add(father.getFather());
        }

        while (!people.isEmpty())  {
            Member grandparent = people.pop(); //from older to newer entries

            for (Member auntUncleCand : grandparent.getChildren()) {
                if (isParent(auntUncleCand, member1)) //is the actual parent
                    continue;

                if (auntUncleCand.getChildren().contains(member2)) //child of parent's brother/sister
                    return true;

                if (auntUncleCand.getSpouse() != null && auntUncleCand.getSpouse().getChildren().contains(member2)) // non-blood relation
                    return true;
            }
        }
        return false;
    }

    private static boolean isCouple(Member member1, Member member2) {
        return  member1.getSpouse()  != null && member1.getSpouse().equals(member2);
    }

    private static boolean isGrandParent(Member member1, Member member2) {
        Member mother = member2.getMother();
        Member father = member2.getFather();
        Deque<Member> grandparents = new ArrayDeque<Member>();
        //get grandparents of mother & father
        if (mother != null) {
            if (mother.getMother() != null)
                grandparents.add(mother.getMother());

            if (mother.getFather() != null)
                grandparents.add(mother.getFather());
        }

        if (father != null) {
            if (father.getMother() != null)
                grandparents.add(father.getMother());

            if (father.getFather() != null)
                grandparents.add(father.getFather());
        }
        //iterate the grandparents
        while (!grandparents.isEmpty()) {
            Member grandparent = grandparents.poll();
            //is the grandparent?
            if (grandparent.equals(member1))
                return true;
        }
        return false;
    }


    private static boolean isAuntUncle(Member member1, Member member2) {
        Member mother = member2.getMother();
        Member father = member2.getFather();
        Deque<Member> people = new ArrayDeque<Member>();

        // Add mother's existing parents if she exists
        if (null != mother) {
            if (null != mother.getMother())
                people.add(mother.getMother());

            if (null != mother.getFather())
                people.add(mother.getFather());
        }

        // Add father's existing parents if he exists
        if (null != father) {
            if (father.getMother() != null)
                people.add(father.getMother());

            if (father.getFather() != null)
                people.add(father.getFather());
        }

        //iterate grandparents
        while (!people.isEmpty()) {
            Member grandparent = people.pop();

            for (Member sibling : grandparent.getChildren()) {
                //exclude parent case
                if (isParent(sibling, member2))
                    continue;

                //is the aunt/Uncle?
                if (sibling.equals(member1))
                    return true;

                //non-blood relation check
                if (isCouple(sibling, member1))
                    return true;
            }
        }
        return false;
    }
}
