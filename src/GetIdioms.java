import thread.IdiomsThread;
import util.DocumentUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GetIdioms {
    private static GetIdioms getIdioms;
    private long startTime;
    private long endTime;
    private List<String> letterList = new ArrayList<>();
    private static int allThreadCount = 0;

    public static GetIdioms newInstance() {
        if (getIdioms == null) {
            getIdioms = new GetIdioms();
        }
        return getIdioms;
    }

    public GetIdioms() {
        startTime = System.currentTimeMillis();
        letterList = getLetterList();
        allThreadCount = letterList.size();
        System.out.println("-------------------------线程启动！-------------------------");
        System.out.println("------------------------共" + letterList.size() + "条线程！------------------------");
        for (int i = 1; i <= letterList.size(); i++) {
            Thread thread = new Thread(new IdiomsThread(letterList.get(i - 1), new IdiomsThread.CallBack() {
                @Override
                public void callBack(String threadName, String runTime) {
                    if (allThreadCount == 1) {
                        endTime = System.currentTimeMillis();
                        System.out.println("-------------------" + "所有线程执行完毕！总耗时：" + (endTime - startTime) + "ms！------------------");
                    } else {
                        increase();
                        System.out.println("-------------------" + threadName + "执行完毕！耗时" + runTime + "-------------------");
                    }
                }
            }));
            thread.setName("字母"+letterList.get(i - 1)+"-线程" + String.format("%02d", i));
            thread.start();
            System.out.println("------------------------" + thread.getName() + "启动！------------------------");
        }
        System.out.println("------------------------所有线程启动完毕！-------------------------");
    }

    /**
     * 有一条线程完成时减一
     */
    public synchronized void increase() {
        allThreadCount--;
    }

    /**
     * 获取所有字母
     *
     * @return
     */
    private List<String> getLetterList() {
        List<String> letterList = new ArrayList<>();
        Elements letterElements;
        Document document = DocumentUtil.newInstance().getDocument(getUrl("a", 1));
        if (document != null) {
            letterElements = document.select("body > div.mainbox > div.leftbox > div:nth-child(2) > div.otitle > span > a");
            for (int i = 0; i < letterElements.size(); i++) {
                letterList.add(letterElements.get(i).text().toLowerCase());
            }
        }
        return letterList;
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
