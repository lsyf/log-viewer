package com.lsyf.pay.logger.service.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Monitor3Res {
    UserIdType type;
    String userId;
    String productName;

    /**
     * 日期，yyyyMM
     */
    String date;
    int num;
}
