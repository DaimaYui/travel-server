package com.itjyr.travelserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class StreamDoneVO {
    private Boolean done = true;
    public static StreamDoneVO of() {
        return new StreamDoneVO();
    }
}

