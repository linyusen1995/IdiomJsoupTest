package thread;

import util.DBManager;
import util.DocumentUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IdiomsThread implements Runnable {
    CallBack callBack;
    long startTime = System.currentTimeMillis();
    long endTime;
    String letter;
    int letterCount = 0;

    private synchronized void insertData(List<bean.Idioms> idiomsList) {
        String sql = "insert into idioms values";
        for (int i = 0; i < idiomsList.size(); i++) {
            if (i == 0) {
                sql = sql + "(id,\'" + idiomsList.get(i).getLetter()
                        + "\',\'" + idiomsList.get(i).getContent()
                        + "\'," + idiomsList.get(i).getNum() + ")";
            } else if (i == idiomsList.size() - 1) {
                sql = sql + ",(id,\'" + idiomsList.get(i).getLetter()
                        + "\',\'" + idiomsList.get(i).getContent()
                        + "\'," + idiomsList.get(i).getNum() + ");";
            } else {
                sql = sql + ",(id,\'" + idiomsList.get(i).getLetter()
                        + "\',\'" + idiomsList.get(i).getContent()
                        + "\'," + idiomsList.get(i).getNum() + ")";
            }
        }
        DBManager dbManager = new DBManager(sql);
        try {
            dbManager.preparedStatement.executeUpdate();
            dbManager.close();
            endTime = System.currentTimeMillis();
            callBack.callBack(Thread.currentThread().getName(), (endTime - startTime) + "ms");
        } catch (SQLException e) {
            System.out.println("》》》》》》报错信息：" + "SQL语句:" + sql + "List大小" + idiomsList.size() + "当前字母：" + letter);
            e.printStackTrace();
        }
    }

    public IdiomsThread(String lertter, CallBack callBack) {
        this.letter = lertter;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        insertData(getAllIdiomList(letter));
    }

    public interface CallBack {
        void callBack(String threadName, String runTime);
    }

    /**
     * 获取当前字母所有成语
     *
     * @param letter 字母
     * @return
     */
    private List<bean.Idioms> getAllIdiomList(String letter) {
        List<bean.Idioms> idiomBeanList = new ArrayList<>();
        for (int i = 1; i <= getTotalPages(letter); i++) {
            idiomBeanList.addAll(getIdiomList(letter, i));
        }
        return idiomBeanList;
    }

    /**
     * 获取单页成语
     *
     * @param letter 字母
     * @param page   当前页
     * @return
     */
    private List<bean.Idioms> getIdiomList(String letter, int page) {
        List<bean.Idioms> idiomBeanList = new ArrayList<>();
        Elements IdiomListElements;
        Document document = DocumentUtil.newInstance().getDocument(getUrl(letter, page));
        if (document != null) {
            IdiomListElements = document.select("body > div.mainbox > div.leftbox > div:nth-child(2) > div:nth-child(5) > ul > li");
            for (int i = 0; i < IdiomListElements.size(); i++) {
                bean.Idioms idioms = new bean.Idioms();
                idioms.setLetter(letter);
                idioms.setContent(IdiomListElements.get(i).text());
                idioms.setNum(letterCount);
                idiomBeanList.add(idioms);
                letterCount++;
            }
        }
        return idiomBeanList;
    }

    /**
     * 获取总页数
     *
     * @param letter 字母
     * @return 总页数
     */
    private int getTotalPages(String letter) {
        int count = 0;
        Elements pageElements;
        Document document = DocumentUtil.newInstance().getDocument(getUrl(letter, 1));
        if (document != null) {
            pageElements = document.select("body > div.mainbox > div.leftbox > div:nth-child(2) > div.gclear.pp.bt.center.f14 > a");
            count = pageElements.size();
        }
        if (count != 1) {
            return count - 2;
        } else {
            return count;
        }
    }

    /**
     * 获取链接
     *
     * @param letter 字母
     * @param page   当前页
     * @return url
     */
    public String getUrl(String letter, int page) {
        return "https://chengyu.911cha.com/pinyin_" + letter + "_p" + page + ".html";
    }
}
