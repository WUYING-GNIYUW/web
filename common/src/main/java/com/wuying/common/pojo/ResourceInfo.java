package com.wuying.common.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResourceInfo {
    String filename;
    String absoluteLocation;
    String format;
}
