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
public class Monitor1Res {
    UserIdType type;
    String userId;
    String merchantId;

    int num;

    public static List<Monitor1Res> parse(SearchResult result, UserIdType type) {
        List<Monitor1Res> list = Lists.newArrayList();
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
            if (b.merchant_id == null || CollectionUtils.isEmpty(b.merchant_id.buckets)) {
                return;
            }
            String userId = b.key;
            b.merchant_id.buckets.forEach(bb ->
                    list.add(Monitor1Res.builder()
                            .type(type)
                            .userId(userId)
                            .merchantId(bb.key)
                            .num(bb.doc_count)
                            .build()));
        });
        return list;
    }

    @Data
    @NoArgsConstructor
    public static class ESRes {

        private List<BucketsBeanX> buckets;


        @NoArgsConstructor
        @Data
        public static class BucketsBeanX {

            private String key;
            private int doc_count;
            private WarnsMerchantIdBean merchant_id;

            @NoArgsConstructor
            @Data
            public static class WarnsMerchantIdBean {

                private List<BucketsBean> buckets;

                @NoArgsConstructor
                @Data
                public static class BucketsBean {


                    private String key;
                    private int doc_count;
                }
            }
        }
    }
}
