package com.imooc.VO;

import lombok.Data;
import lombok.Getter;

/**
 * http 返回的最外层 VO
 *
 * @author Bruce Liu
 * @since 20190228
 */
@Data
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
}
