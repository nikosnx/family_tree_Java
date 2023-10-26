package com.genTree;

import java.util.Comparator;



public class SortByName implements Comparator<Member> {

    @Override
    public int compare(Member m1, Member m2) {

        return m1.getName().compareTo(m2.getName());

    }
}
