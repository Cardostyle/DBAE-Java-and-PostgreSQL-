package Functions;

import Classes.*;

import java.util.List;

public interface PersonRelatedAPI {
    String getProfile(Long personId);
    List<String> getCommonInterestsOfMyFriends(Long personId);
    List<String> getCommonFriends(Long personIdA, Long personIdB);
    String getPersonsWithMostCommonInterests(Long personId);
    List<Organisation> getJobRecommendation(Long personId);
    List<String> getShortestFriendshipPath(Long personId, Long otherPersonId);
    Integer getFractionLvl(String Gamertag, String factionName);
    List<String> getShips(String Gamertag);
}
