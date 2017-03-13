package usercf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by leoz on 2016/12/11.
 */
public class InitSource {
    /**
     * 初始化数据
     * */
    public static Map<String, List<Integer>> initSource() {
        Map<String, List<Integer>> source = Maps.newHashMap();
        source.put("user_1", Lists.<Integer>newArrayList(1, 3, 4));   //对编号为1,3,4的物品感兴趣
        source.put("user_2", Lists.<Integer>newArrayList(1, 3, 4, 5));
        source.put("user_3", Lists.<Integer>newArrayList(2, 3, 5));
        source.put("user_4", Lists.<Integer>newArrayList(2, 3));
        source.put("user_5", Lists.<Integer>newArrayList(3, 5));

        return source;
    }

    /**
     *初始化user-->id的map
     * */
    public static Map<String, Integer> initUserToIdsMap() {
        Map<String, Integer> userToIdsMap = Maps.newHashMap();
        userToIdsMap.put("user_1", 0);
        userToIdsMap.put("user_2", 1);
        userToIdsMap.put("user_3", 2);
        userToIdsMap.put("user_4", 3);
        userToIdsMap.put("user_5", 4);
        return userToIdsMap;
    }

    /**
     *初始化id-->user的map
     * */
    public static Map<Integer, String> initIdToUsersMap() {
        Map<Integer, String> idToUsersMap = Maps.newHashMap();
        idToUsersMap.put(0, "user_1");
        idToUsersMap.put(1, "user_2");
        idToUsersMap.put(2, "user_3");
        idToUsersMap.put(3, "user_4");
        idToUsersMap.put(4, "user_5");
        return idToUsersMap;
    }
}
