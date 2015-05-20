package com.xmalloc.wordnote.word;

import android.content.Context;

import com.android.volley.Response;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.xmalloc.wordnote.constant.WordApi;
import com.xmalloc.wordnote.util.GsonUtils;
import com.xmalloc.wordnote.util.PreferencesUtils;
import com.xmalloc.wordnote.util.TimeUtil;
import com.xmalloc.wordnote.util.net.GsonRequest;
import com.xmalloc.wordnote.util.net.NetUtil;
import com.xmalloc.wordnote.util.net.VolleyWrapper;
import com.xmalloc.wordnote.word.vo.Record;
import com.xmalloc.wordnote.word.vo.Schedule;
import com.xmalloc.wordnote.word.vo.Word;
import com.xmalloc.wordnote.word.vo.WordListResp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhch on 2015/5/19.
 */
public class WordLogic {

    public static final String DATE_STRING = "DATE_STRING";
    public static final String SCHEDULE_STRING = "DATE_STRING";
    public static final String REVIEW_DAYS = "REVIEW_DAYS";
    public static final String REVIEW_WORDS = "REVIEW_WORDS";
    Context context;

    private String reviewDays; // 要背的单词的日子的字符串
    private Map<String, List<Word>> reviewWordMap; // 所有单词的map
    private int dayCount; // 要背的单词的天数
    private List<Word> reviewWordList; // 要背的单词列表
    private List<Word> reviewRandomList; // 要背的单词随机
    private Set<Word> forgetSet;
    private int total; // 当日复习总数量
    private int passedNum;
    public int reviewIndex; // 当前正显示的单词的 index
    private Random random = new Random();

    WordLogic(Context context) {
        this.context = context;
        reviewWordMap = new HashMap<>();
        reviewWordList = new ArrayList<>();
        forgetSet = new HashSet<>();
    }

    public List<Schedule> calculateTime(String content) {
        List<String> lines = new ArrayList<String>();
        String[] lineStrs = content.split("\\s");
        for (String s : lineStrs) {
            lines.add(s);
        }
        List<Record> records = new ArrayList<Record>();
        for (String line : lines) {
            if (Strings.isNullOrEmpty(line) || !line.contains("|") || line.startsWith("#")) {
                continue;
            }
            String[] fields = line.split("\\|");
            Record r = new Record(fields[0], fields[1], fields[2]);
            records.add(r);
        }

        Map<String, List<Record>> map = new HashMap<String, List<Record>>();

        int[] reviewTime = new int[]{1, 2, 4, 7, 15, 25, 70}; // 复习的间隔时间

        for (Record r : records) {
            Date date = TimeUtil.parseDate(r.time, "yyyy-MM-dd");
            Calendar c = Calendar.getInstance();

            for (int day : reviewTime) {
                c.setTime(date);
                c.add(Calendar.DAY_OF_YEAR, day);
                String reviewDay = TimeUtil.format(c.getTime(), "yyyy-MM-dd");

                if (map.get(reviewDay) != null) {
                    map.get(reviewDay).add(r);
                } else {
                    List<Record> tasks = new ArrayList<Record>();
                    tasks.add(r);
                    map.put(reviewDay, tasks);
                }
            }

        }

        return printResult(map);
    }

    /**
     * 打印结果
     *
     * @param map
     */
    private List<Schedule> printResult(Map<String, List<Record>> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys); // 日期排序

        // 计算所有的天数, 打印
        Date first = TimeUtil.parseDate(keys.get(0), "yyyy-MM-dd");
        Date last = TimeUtil.parseDate(keys.get(keys.size() - 1), "yyyy-MM-dd");
        long days = (last.getTime() - first.getTime()) / 1000 / 60 / 60 / 24;

        Calendar c = Calendar.getInstance();
        c.setTime(first);
        c.add(Calendar.DAY_OF_YEAR, -1);

        List<Schedule> result = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);

            String key = TimeUtil.format(c.getTime(), "yyyy-MM-dd");
            Schedule schedule = new Schedule();
            schedule.date = key;

            List<Record> records = map.get(key);
            List<String> wordDays = new ArrayList<>();
            if (records != null) {
                Collections.sort(records);
                schedule.wordDays = records;
            }
            result.add(schedule);
        }
        return result;
    }

    public static String printSchedule(List<Schedule> schedules, boolean showAll) {
        Calendar c = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.setTime(TimeUtil.parseDate(TimeUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        StringBuilder sb = new StringBuilder();
        for (Schedule schedule : schedules) {
            c.setTime(TimeUtil.parseDate(schedule.date, "yyyy-MM-dd"));
            if (!showAll && c.before(today)) {
                continue;
            }
            // 周一就打印个空行
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                sb.append("\n");
            }
            sb.append(schedule).append("\n");
        }
        return sb.toString();
    }

    public void setupWords() {
        String[] days = reviewDays.trim().split("\\s");
        for (String day : days) {
            String dayWordsJson = PreferencesUtils.getString(context, day);
            if (Strings.isNullOrEmpty(dayWordsJson)) {
                requestDayWords(day);
            } else {
                Type type = new TypeToken<List<Word>>() {
                }.getType();
                List<Word> words = GsonUtils.fromJson(dayWordsJson, type);
                addReviewWords(day, words);
                total = reviewWordList.size();
                setupRandomList();
            }
        }
    }

    private void setupRandomList() {
        int wordSize = reviewWordList.size();
        reviewRandomList = new ArrayList<>(wordSize);
        while (reviewWordList.size() > 0) {
            reviewRandomList.add(reviewWordList.remove(random.nextInt(reviewWordList.size())));
        }
        for (int i = 0; i < wordSize; i++) {
            reviewWordList.add(reviewRandomList.get(i));
        }
    }

    private void requestDayWords(String day) {

        Map<String, String> params = NetUtil.createParams("days", day);
        GsonRequest<WordListResp> listRequest = new GsonRequest<WordListResp>(WordApi.WORD_LIST,
                WordListResp.class, params, getListResponseListener(day),
                NetUtil.getErrorListener(context));
        VolleyWrapper.getInstance(context).addToRequestQueue(listRequest);
    }


    private Response.Listener<WordListResp> getListResponseListener(final String day) {
        return new Response.Listener<WordListResp>() {
            @Override
            public void onResponse(WordListResp res) {
                if (res.err == 0) {
                    PreferencesUtils.putString(context, day, GsonUtils.toJson(res.content));
                    addReviewWords(day, res.content);
                }
            }
        };
    }

    private void addReviewWords(String day, List<Word> words) {
        reviewWordMap.put(day, words);
        dayCount++;
        if (words != null) {
            reviewWordList.addAll(words);
        }
    }

    public void next() {
        reviewIndexAdd();
    }

    public void pre() {
        if (reviewIndex > 0)
            reviewIndex--;
    }

    public void forget() {
        forgetSet.add(reviewRandomList.get(reviewIndex));
        reviewIndexAdd();
    }

    public void pass() {
        boolean found = reviewWordList.remove(reviewRandomList.get(reviewIndex));
        if(found){
            passedNum++;
        }
        reviewIndexAdd();
    }

    /**
     * randomList 循环， 到头了就重新打乱
     */
    private void reviewIndexAdd() {
        reviewIndex++;
        if (reviewIndex >= reviewRandomList.size()) {
            setupRandomList();
        }
    }

    public String reviewInfo() {
        return "总共：" + total + "个，背了：" + passedNum + "个， 忘了：" + forgetSet.size() + "个，还剩：" + reviewWordList.size() + "个";
    }

    public void setReviewDays(String reviewDays) {
        this.reviewDays = reviewDays;
    }

    public void setReviewWordList(String reviewWordsJson) {
        Type type = new TypeToken<List<Word>>() {
        }.getType();
        this.reviewWordList = GsonUtils.fromJson(reviewWordsJson, type);
    }

    public String reviewPreInfo() {
        return "天数：" + dayCount + " 单词数：" + reviewWordList.size();
    }

    public Word getWord(int index) {
        if (index > reviewRandomList.size() - 1 || index < 0)
            return null;
        return reviewRandomList.get(index);
    }

    public Word getPre() {
        return getWord(reviewIndex - 1);
    }

    public Word getCurrent() {
        return getWord(reviewIndex);
    }

    public Word getNext() {
        return getWord(reviewIndex + 1);
    }

    public String getPreTranslate() {
        Word word = getPre();
        StringBuilder sb = new StringBuilder();
        if (word != null) {
            if (word.translate != null) {
                for (String s : word.translate) {
                    sb.append(s).append("\n");
                }
            }
        }
        return sb.toString();
    }


    public String forgetWords() {
        StringBuilder sb = new StringBuilder();
        if (forgetSet == null || forgetSet.size() <= 0)
            return sb.toString();
        for (Word word : forgetSet) {
            sb.append(word.text).append("\n");
        }
        return sb.toString();
    }
}
