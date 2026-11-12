package com.itjyr.travelserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequestDTO {
    @NotBlank(message = "输入消息不能为空")
    private String message;
}
