package com.lsyf.pay.logger.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liufei
 * @Date 2019/11/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("泛化调用请求")
public class DubboGenericReq {

    @ApiModelProperty(value = "组")
    String group;

    @ApiModelProperty(value = "版本")
    String version;


    @ApiModelProperty("方法参数类型全路径，逗号分隔，如 java.lang.String,com.tester.Req")
    String paramTypes;

    @ApiModelProperty("方法参数值，JSON数组，如 [\"1\",{\"name\":\"xxx\"}]")
    String paramValues;
}
