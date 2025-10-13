package kr.co.bnk_marketproject_be.repository.projection;

import java.sql.Timestamp;

public interface DailyOpsRow {
    Timestamp getD();
    long getOrders();
    long getPayments();
    long getCancels();
}