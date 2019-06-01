package vadim99808.service;

import com.earth2me.essentials.User;
import vadim99808.TreasureHunt;
import vadim99808.entity.UserStatistics;

import java.util.Collections;
import java.util.List;

public class UserStatisticsService {
    private TreasureHunt plugin = TreasureHunt.getInstance();

    public List<UserStatistics> getListSortedByQuantity(){
        List<UserStatistics> userStatisticsList = plugin.getUserStatisticsDao().readAll();
        userStatisticsList.sort(UserStatistics.QUANTITY_COMPARATOR);
        return userStatisticsList;
    }

    public List<UserStatistics> getListSortedByValue(){
        List<UserStatistics> userStatisticsList = plugin.getUserStatisticsDao().readAll();
        userStatisticsList.sort(UserStatistics.VALUE_COMPARATOR);
        return userStatisticsList;
    }
}
