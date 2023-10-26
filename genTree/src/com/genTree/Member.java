package com.genTree;

import java.util.HashSet;
import java.util.Set;

public class Member {
    private String name;
    private int gender;
    private Member mother, father, spouse;
    private final Set<Member> children = new HashSet<>(); //prevent reference from changing, basic usage is for checking if it contains elements

    //constructors
    public Member(String name, int gender) {
        setName(name);
        setGender(gender);
    }

    public Member(String name, String gender) {
        this(name, makeGenderInt(gender));  //make gender an int if input is string
    }

    //getters

    public String getName() {
        return name;
    }

    public Set<Member> getChildren() {
        return children;
    }

    public int getGender() {
        return gender;
    }

    public Member getMother() {
        return mother;
    }

    public Member getFather() {
        return father;
    }

    public Member getSpouse() {
        return spouse;
    }

    //setters

    public void setName(String name) {
        this.name = Tools.format(name);
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setGender(String gender) { //make gender an int if input is string
        setGender(makeGenderInt(gender));
    }

    public void setMother(Member mother) {
        this.mother = mother;
    }

    public void setFather(Member father) {
        this.father = father;
    }

    public void setSpouse(Member spouse) {
        this.spouse = spouse;
    }

    public void setParent(Member parent, String type) {
        switch (Tools.convertToLowerCaseTrim(type)) { //make type suitable for efficient switch
            case "mother":
                setMother(parent);
                break;

            case "father":
                setFather(parent);
                break;

            default:
                throw new IllegalArgumentException("Not mother or father!");
        }
    }
    public void setChildren(Member child) { this.children.add(child); }

    @Override
    public String toString() {
        return getName();
    }

    //implementation of method representing gender with an int value
    private static int makeGenderInt(String gender) {
        int result;

        switch (Tools.convertToLowerCaseTrim(gender)) { //make gender suitable for efficient switch
            case "man":
                result = 1;
                break;
            case "woman":
                result = 0;
                break;
            default:
                throw new IllegalArgumentException("Not man or woman!");
        }
        return result;
    }
}
