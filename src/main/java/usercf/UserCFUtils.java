package usercf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by leoz on 2016/12/11.
 */
public class UserCFUtils {
    /**
     *获得倒排表item-->List<user>
     * */
    public static Map<Integer, List<String>> getItemToUserMap(Map<String, List<Integer>> resource){
        Map<Integer, List<String>> itemToUserMap = Maps.newHashMap();
        if (resource.isEmpty()) {
            return itemToUserMap;
        }

        for (Map.Entry<String, List<Integer>> entry : resource.entrySet()) {
            String userName = entry.getKey();
            List<Integer> itemList = entry.getValue();

            if (CollectionUtils.isEmpty(itemList)) {
                continue;
            }
            for (int i : itemList) {
                if (!itemToUserMap.containsKey(i)) {
                    itemToUserMap.put(i, Lists.newArrayList(userName));
                } else {
                    itemToUserMap.get(i).add(userName);
                }
            }
        }
        return itemToUserMap;
    }

    /**
     *获得相似矩阵
     * */
    public static Float[][] getSimilarMatrix(Map<String, List<Integer>> resource, Map<Integer, List<String>> itemToUserMap, Map<String, Integer> userToIdsMap, Map<Integer, String> idToUsersMap) {
        Float[][] similarMatrix = new Float[resource.size()][resource.size()];
        if (resource.isEmpty() || itemToUserMap.isEmpty()) {
            return similarMatrix;
        }

        for (int i = 0; i < resource.size(); i++) {
            for (int j = 0; j < resource.size(); j++) {
                similarMatrix[i][j] = 0f;
            }
        }

        for (Map.Entry<Integer, List<String>> entry : itemToUserMap.entrySet()) {
            List<String> userList = entry.getValue();

            if (CollectionUtils.isEmpty(userList) || userList.size() == 1) {
                continue;
            }
            for (int i = 0; i < userList.size(); i++) {
                for (int j = i + 1; j < userList.size(); j++) {
                    similarMatrix[userToIdsMap.get(userList.get(i))][userToIdsMap.get(userList.get(j))] += 1;
                    similarMatrix[userToIdsMap.get(userList.get(j))][userToIdsMap.get(userList.get(i))] += 1;
                }
            }
        }

        for (int i = 0; i < resource.size(); i++) {
            for (int j = i + 1; j < resource.size(); j++) {
                similarMatrix[i][j] = getSimilarity(similarMatrix[i][j], i, j, idToUsersMap, resource);
                similarMatrix[j][i] = similarMatrix[i][j];
            }
        }

        return similarMatrix;
    }

    private static float getSimilarity(float tmp, int i, int j, Map<Integer, String> idTousersMap, Map<String, List<Integer>> resource) {
        float result = 0;
        int iSize = resource.get(idTousersMap.get(i)).size();
        int jSize = resource.get(idTousersMap.get(j)).size();

        if (iSize == 0 || jSize == 0) {
            return result;
        }
        return (float) (tmp/Math.sqrt(iSize * jSize));
    }
}
