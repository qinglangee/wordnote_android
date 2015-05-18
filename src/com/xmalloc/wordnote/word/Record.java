package com.xmalloc.wordnote.word;

/**
 * Created by zhch on 2015/5/18.
 */
public class Record implements Comparable<Record> {
    public String time;
    public String name;
    public String comment;

    public Record(String t, String n, String c) {
        time = t;
        name = n;
        comment = c;
    }

    @Override
    public int compareTo(Record o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Record [time=" + time + ", name=" + name + ", comment=" + comment + "]";
    }
}