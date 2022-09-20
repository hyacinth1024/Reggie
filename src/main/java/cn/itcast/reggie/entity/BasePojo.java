package cn.itcast.reggie.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BasePojo {
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime updateTime;
    private long createUser;
    private long updateUser;
}
