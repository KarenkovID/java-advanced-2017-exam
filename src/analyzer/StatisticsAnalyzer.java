package analyzer;

import java.rmi.Remote;
import java.util.List;

/**
 * Created by garik on 01.06.17.
 */
public interface StatisticsAnalyzer extends Remote {
    List<String> topRequests(int n);
}
