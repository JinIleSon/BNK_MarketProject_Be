package kr.co.bnk_marketproject_be.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesDTO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand_name;
    private String biz_registration_number;
    @Transient
    private int order_count;
    @Transient
    private int completed_count;
    @Transient
    private int shipping_count;
    @Transient
    private int delivered_count;
    @Transient
    private int order_total;
    @Transient
    private int sales_total;
}
