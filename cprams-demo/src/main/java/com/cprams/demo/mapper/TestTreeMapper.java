package com.cprams.demo.mapper;

import com.cprams.common.annotation.DataColumn;
import com.cprams.common.annotation.DataPermission;
import com.cprams.common.core.mapper.BaseMapperPlus;
import com.cprams.demo.domain.TestTree;
import com.cprams.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTreeMapper, TestTree, TestTreeVo> {

}
