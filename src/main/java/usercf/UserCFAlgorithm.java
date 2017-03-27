package usercf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by leoz on 2016/12/11.
 */
public class UserCFAlgorithm {
    public static int TOP_N = 2;

    public Map<String, List<Pair<Integer, Float>>> recommondBasedOnUser() {
        Map<String, List<Pair<Integer, Float>>> result = Maps.newHashMap();
        //获取资源
        Map<String, List<Integer>> resource = InitSource.initSource();
        Map<String, Integer> userToIdsMap = InitSource.initUserToIdsMap();
        Map<Integer, String> idToUsersMap = InitSource.initIdToUsersMap();
        Map<Integer, List<String>> itemToUserMap = UserCFUtils.getItemToUserMap(resource);
        //获取相似矩阵
        Float[][] similarMatrix = UserCFUtils.getSimilarMatrix(resource, itemToUserMap, userToIdsMap, idToUsersMap);
        //实现推荐
        for (String userName : userToIdsMap.keySet()) {
            List<Pair<Integer, Float>> currentUserResult = getCurrentUserRecommondItem(userName, userToIdsMap, idToUsersMap, similarMatrix, resource);
            if (!CollectionUtils.isEmpty(currentUserResult)) {
                result.put(userName, currentUserResult);
            }
        }
        return result;
    }

    private List<Pair<Integer, Float>> getCurrentUserRecommondItem(String userName, Map<String, Integer> userToIdsMap, Map<Integer, String> idToUsersMap, Float[][] similarMatrix, Map<String, List<Integer>> source) {
        List<Pair<Integer, Float>> recommondResult = Lists.newArrayList();
        if (!userToIdsMap.containsKey(userName)){
            return recommondResult;
        }
        int userId = userToIdsMap.get(userName);

        List<String> topNSimilarUser = getTopNSimilarUser(userId,idToUsersMap, similarMatrix);
        List<Integer> recommondItem = getRecommondItem(idToUsersMap.get(userId), topNSimilarUser, source);    //获取相似用户有，而自己没有的item

        for (int itemId : recommondItem) {
            float similar = 0f;
            for (String topNUser : topNSimilarUser) {
                if (source.get(topNUser).contains(itemId)) {  //当前用户存也对itemId感兴趣
                    if (userToIdsMap.containsKey(topNUser)) {
                        similar += similarMatrix[userId][userToIdsMap.get(topNUser)];
                    }
                }
            }
            Pair<Integer, Float> pair = new Pair<Integer, Float>(itemId, similar);

            recommondResult.add(pair);
        }

        return recommondResult;
    }

    private List<String> getTopNSimilarUser(int userId, Map<Integer, String> idToUsersMap, Float[][] similarMatrix) {
        List<String> topNSimilarUser = Lists.newArrayList();
        //获取topN
        List<Integer> topNIds = getTopNSimilarIndex(userId, similarMatrix[userId]);

        for (int tmp : topNIds) {
            topNSimilarUser.add(idToUsersMap.get(tmp));
        }
        return topNSimilarUser;
    }

    private List<Integer> getRecommondItem(String userName, List<String> topNSimilarUser, Map<String, List<Integer>> source) {
        List<Integer> recommondItem = Lists.newArrayList();
        List<Integer> userContain = source.get(userName);

        if (!source.containsKey(userName)) {
            return recommondItem;
        }

        Set<Integer> unionSet = Sets.newHashSet();
        for (String currentName : topNSimilarUser) {
            unionSet.addAll(source.get(currentName));
        }
        boolean flag = unionSet.removeAll(userContain);
        if (flag) {
            for (int i : unionSet) {
                recommondItem.add(i);
            }
        }
        return recommondItem;
    }

    private List<Integer> getTopNSimilarIndex(int userId, Float[] array) {
        List<Integer> list = Lists.newArrayList();
        if (array.length < TOP_N) {
            return list;
        }

        List<SortSimilarClass> sortSimilarClasses = Lists.newArrayList();
        for (int i = 0; i < array.length; i++) {
            SortSimilarClass sortSimilarClass = new SortSimilarClass(i, array[i]);
            sortSimilarClasses.add(sortSimilarClass);
        }

        Collections.sort(sortSimilarClasses);
        //取前topN个
        int number = 0;
        int index = 0;
        while (index < array.length && number < TOP_N){
            if (sortSimilarClasses.get(index).getIndex() == userId) {
                index++;
            } else {
                list.add(sortSimilarClasses.get(index).getIndex());
                index++;
                number++;
            }
        }
        return list;
    }

    private class SortSimilarClass implements Comparable{
        private int index;
        private float similar;
        public SortSimilarClass(int index, float similar) {
            this.index = index;
            this.similar = similar;
        }

        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public float getSimilar() {
            return similar;
        }
        public void setSimilar(float similar) {
            this.similar = similar;
        }

        public int compareTo(Object o) {
            SortSimilarClass sortSimilarClass = (SortSimilarClass)o;
            return this.similar > sortSimilarClass.similar ? -1 : 1;
        }





    }


    public static void main(String[] args) {
        UserCFAlgorithm userCFAlgorithm = new UserCFAlgorithm();
        Map<String, List<Pair<Integer, Float>>> result = userCFAlgorithm.recommondBasedOnUser();
        System.out.println("输出:");
        for (Map.Entry<String, List<Pair<Integer, Float>>> entry : result.entrySet()) {
            System.out.println("用户:" + entry.getKey());
            for (Pair<Integer, Float> pair : entry.getValue()) {
                System.out.println("推荐物品 " + pair.getKey() + " 程度是:" + pair.getValue());
            }
        }
    }
}
