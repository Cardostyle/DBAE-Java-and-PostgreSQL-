package Functions;

import java.util.List;
import java.util.Map;

public interface StatisticAPI {
    List<String> getTagClassHierarchy();
    List<String> getPopularComments(int minLikes);
    String getFrequentPostCommentCountry();
    List<String> getMaxedPlayers(String factionName);
}
