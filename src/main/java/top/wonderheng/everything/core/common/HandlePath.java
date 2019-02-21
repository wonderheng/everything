package top.wonderheng.everything.core.common;

import lombok.Data;

import java.util.Set;

/**
 * Author: wonderheng
 * Created: 2019/2/17
 */
@Data
public class HandlePath {
    private Set<String> includePath;
    private Set<String> excludePath;
}
