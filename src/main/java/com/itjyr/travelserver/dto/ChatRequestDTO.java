package com.itjyr.travelserver.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDTO {
    @NotBlank(message = "输入消息不能为空")
    private String message;
}
