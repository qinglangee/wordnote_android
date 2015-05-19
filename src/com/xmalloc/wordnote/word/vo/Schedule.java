package com.xmalloc.wordnote.word.vo;

import java.util.List;

/**
 * Created by zhch on 2015/5/19.
 */
public class Schedule {
    public String date;
    public List<Record> wordDays;

    /**
     * 打印  2015-05-26 009 036 043 050
     * @return
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(date).append(words());
        return sb.toString();
    }

    public String words(){
        if(wordDays == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for(Record r : wordDays){
            sb.append(" ").append(r.name);
        }
        return sb.toString();
    }
}
