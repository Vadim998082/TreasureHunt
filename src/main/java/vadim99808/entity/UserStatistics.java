package vadim99808.entity;


import java.util.Comparator;
import java.util.UUID;

public class UserStatistics {

    private UUID userId;
    private int totalValue;
    private int totalQuantity;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public static Comparator<UserStatistics> QUANTITY_COMPARATOR = new Comparator<UserStatistics>() {
        @Override
        public int compare(UserStatistics userStatistics1, UserStatistics userStatistics2) {
            return userStatistics2.getTotalQuantity() - userStatistics1.getTotalQuantity();
        }
    };

    public static Comparator<UserStatistics> VALUE_COMPARATOR = new Comparator<UserStatistics>() {
        @Override
        public int compare(UserStatistics userStatistics1, UserStatistics userStatistics2) {
            return userStatistics2.getTotalValue() - userStatistics1.getTotalValue();
        }
    };
}
