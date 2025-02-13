package com.cprams.system.service;

import com.cprams.common.core.domain.PageQuery;
import com.cprams.common.core.page.TableDataInfo;
import com.cprams.system.domain.SysOss;
import com.cprams.system.domain.bo.SysOssBo;
import com.cprams.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService {

    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    List<SysOssVo> listByIds(Collection<Long> ossIds);

    SysOssVo getById(Long ossId);

    SysOss upload(MultipartFile file);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
