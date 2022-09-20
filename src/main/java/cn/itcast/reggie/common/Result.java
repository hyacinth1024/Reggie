package cn.itcast.reggie.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        Result result = new Result();
        result.code = 1;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
