package com.offcn.project.service;

import com.mysql.cj.protocol.a.TextResultsetReader;
import com.offcn.project.po.*;
import io.swagger.models.auth.In;

import java.util.List;

public interface ProjectInfoService {
    /**
     * 获取项目回报列表
     * @param projectId
     * @return
     */
    List<TReturn> getReturnList(Integer projectId);
    /**
     * 获取系统中所有项目
     * @return
     */
    List<TProject> findAllProject();

    /**
     * 获取项目图片
     * @param id
     * @return
     */
    List<TProjectImages> getProjectImages(Integer id);

    /**
     * 获取项目信息
     * @param projectId
     * @return
     */
    TProject findProjectInfo(Integer projectId);
    /**
     * 获得项目标签
     * @return
     */
    List<TTag> findAllTag();
    /**
     * 获取项目分类
     * @return
     */
    List<TType> findAllType();
    /**
     * 获取回报信息
     * @param returnId
     * @return
     */
    TReturn findReturn(Integer returnId);
}
