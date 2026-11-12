package com.itjyr.travelserver.dto;

import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class TravelRequestDTO {
    @NotNull(message = "城市不能为空")
    private String city;
    @NotNull(message = "天数不能为空")
    @Min(value = 1, message = "天数不能小于一天")
    @Max(value = 30, message = "天数不能大于三十天")
    private Integer days;
    @NotNull(message = "预算不能为空")
    @Min(value = 100, message = "预算不能小于一百元")
    private Double budget;
}
