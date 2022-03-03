package com.lsyf.pay.logger.service.res;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.searchbox.core.SearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Monitor2Res {
    UserIdType type;
    String userId;

    double amount;

    public static List<Monitor2Res> parse(SearchResult result, UserIdType type) {
        List<Monitor2Res> list = Lists.newArrayList();
        ESRes res;
        try {
            res = JSON.parseObject(result.getJsonObject()
                    .getAsJsonObject("aggregations")
                    .getAsJsonObject("user_id")
                    .toString(), ESRes.class);
        } catch (Exception e) {
            return list;
        }

        if (res == null || CollectionUtils.isEmpty(res.buckets)) {
            return list;
        }
        res.buckets.forEach(b -> {
            if (b.amount == null || b.amount.value == null) {
                return;
            }
            String userId = b.key;
            list.add(Monitor2Res.builder()
                    .type(type)
                    .userId(userId)
                    .amount(toDouble(b.amount.value))
                    .build());

        });
        return list;
    }

    private static double toDouble(long l) {
        return new Double(l) / 1000;
    }

    @NoArgsConstructor
    @Data
    public static class ESRes {


        private List<BucketsBean> buckets;

        @NoArgsConstructor
        @Data
        public static class BucketsBean {


            private String key;
            private int doc_count;
            private AmountBean amount;

            @NoArgsConstructor
            @Data
            public static class AmountBean {


                private Long value;
            }
        }
    }
}
