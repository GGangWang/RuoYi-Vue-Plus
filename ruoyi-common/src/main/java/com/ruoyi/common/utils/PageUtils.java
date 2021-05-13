package com.ruoyi.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.page.PagePlus;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.sql.SqlUtil;

import java.util.List;

/**
 * 分页工具
 *
 * @author Lion Li
 */
public class PageUtils {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    public static <T, K> PagePlus<T, K> buildPagePlus() {
        Integer pageNum = ServletUtils.getParameterToInt(PAGE_NUM);
        Integer pageSize = ServletUtils.getParameterToInt(PAGE_SIZE);
        String orderByColumn = ServletUtils.getParameter(ORDER_BY_COLUMN);
        String isAsc = ServletUtils.getParameter(IS_ASC);
        PagePlus<T, K> page = new PagePlus<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            if ("asc".equals(isAsc)) {
                page.addOrder(OrderItem.asc(orderBy));
            } else if ("desc".equals(isAsc)) {
                page.addOrder(OrderItem.desc(orderBy));
            }
        }
        return page;
    }

    public static <T> Page<T> buildPage() {
        Integer pageNum = ServletUtils.getParameterToInt(PAGE_NUM);
        Integer pageSize = ServletUtils.getParameterToInt(PAGE_SIZE);
        String orderByColumn = ServletUtils.getParameter(ORDER_BY_COLUMN);
        String isAsc = ServletUtils.getParameter(IS_ASC);
        Page<T> page = new Page<>(pageNum, pageSize);
        if (StrUtil.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            if ("asc".equals(isAsc)) {
                page.addOrder(OrderItem.asc(orderBy));
            } else if ("desc".equals(isAsc)) {
                page.addOrder(OrderItem.desc(orderBy));
            }
        }
        return page;
    }

    public static <T, K> TableDataInfo<K> buildDataInfo(PagePlus<T, K> page) {
        TableDataInfo<K> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecordsVo());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    public static <T> TableDataInfo<T> buildDataInfo(Page<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    public static <T> TableDataInfo<T> buildDataInfo(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

}
