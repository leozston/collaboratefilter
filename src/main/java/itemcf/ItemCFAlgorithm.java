package itemcf;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by leoz on 2016/12/11.
 */
public class ItemCFAlgorithm {
    public static int TOP_N = 2;

    public Map<String, Map<Integer, Float>> recommondBasedOnItem() {
        Map<String, Map<Integer, Float>> result = Maps.newHashMap();
        //获取资源
        Map<String, List<Integer>> resource = InitSource.initSource();
        Map<String, Integer> userToIdsMap = InitSource.initUserToIdsMap();
        Map<Integer, String> idToUsersMap = InitSource.initIdToUsersMap();
        Map<Integer, List<String>> itemToUserMap = ItemCFUtils.getItemToUserMap(resource);
        //获取相似矩阵
        Float[][] similarMatrix = ItemCFUtils.getSimilarMatrix(resource, itemToUserMap, userToIdsMap, idToUsersMap);
        //实现推荐
        for (String userName : resource.keySet()) {
            List<Integer> items = resource.get(userName);
            Map<Integer, Float> recommondItem = Maps.newHashMap();
            for (int i = 0; i < idToUsersMap.size(); i++) {
                if (!items.contains(i +  1)) {
                    float score = 0;
                    for (Integer currentItem : items) {
                        score += similarMatrix[currentItem - 1][i];
                    }
                    recommondItem.put(i, score);
                }
            }
            result.put(userName, recommondItem);
        }
        return result;
    }

    public static void main(String[] args) {
        ItemCFAlgorithm itemCFAlgorithm = new ItemCFAlgorithm();
        Map<String, Map<Integer, Float>> result =  itemCFAlgorithm.recommondBasedOnItem();

        for (Map.Entry<String, Map<Integer, Float>> entry : result.entrySet()) {
            System.out.println("username:" + entry.getKey());
            Map<Integer, Float> recommond = entry.getValue();
            for (Map.Entry<Integer, Float> e : recommond.entrySet()) {
                System.out.println(e.getKey() + ":" + e.getValue());
            }
            System.out.println("-------------------");
        }
    }
}
