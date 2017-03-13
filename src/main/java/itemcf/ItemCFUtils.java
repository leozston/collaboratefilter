package itemcf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by longlonglv on 2016/12/19. function:
 */


public class ItemCFUtils {
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


    public static Float[][] getSimilarMatrix(Map<String, List<Integer>> resource, Map<Integer, List<String>> itemToUserMap, Map<String, Integer> userToIdsMap, Map<Integer, String> idToUsersMap) {
        int itemSize = itemToUserMap.size();  //获取物品的个数

        Float[][] similarMatrix = new Float[itemSize][itemSize];
        if (resource.isEmpty()) {
            return similarMatrix;
        }

        for (int i = 0; i < resource.size(); i++) {
            for (int j = 0; j < resource.size(); j++) {
                similarMatrix[i][j] = 0f;
            }
        }

        Integer[] numberList = new Integer[itemSize];
        for (int i = 0; i < itemSize; i++) {
            numberList[i] = 0;
        }

        for (Map.Entry<String, List<Integer>> entry : resource.entrySet()) {
            List<Integer> itemList = entry.getValue();

            if (CollectionUtils.isEmpty(itemList) || itemList.size() == 1) {
                continue;
            }
            for (int i = 0; i < itemList.size(); i++) {
                numberList[itemList.get(i) - 1]++;
                for (int j = i + 1; j < itemList.size(); j++) {
                    similarMatrix[itemList.get(i) - 1][itemList.get(j) - 1] += 1;
                    similarMatrix[itemList.get(j) - 1][itemList.get(i) - 1] += 1;
                }
            }
        }

        for (int i = 0; i < itemSize; i++) {
            for (int j = i + 1; j < itemSize; j++) {
                similarMatrix[i][j] = getSimilarity(similarMatrix[i][j], numberList[i], numberList[j]);
                similarMatrix[j][i] = similarMatrix[i][j];
            }
        }

        return similarMatrix;
    }

    private static float getSimilarity(float tmp, int count1, int count2) {
        if (count1 == 0 || count2 == 0) {
            return -1;
        }
        return (float) (tmp/Math.sqrt(count1 * count2));
    }
}
