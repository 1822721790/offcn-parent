package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.contants.ProjectContant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper tReturnMapper;

    @Override
    public String initCreateProject(Integer memberId) {
        String replace = UUID.randomUUID().toString().replace("-", "");
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        //存令牌
        //存memberID
        projectRedisStorageVo.setMemberid(memberId);
        projectRedisStorageVo.setProjectToken(replace);
        String string = JSON.toJSONString(projectRedisStorageVo);
        stringRedisTemplate.opsForValue().set(ProjectContant.TEMP_PROJECT_PREFIX+replace,string);
        return replace;

    }
    /**
     * 保存项目信息
     * @param auth  项目状态信息
     * @param projectVo  项目全部信息
     */
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo projectVo) {
        System.out.println();
        //创建项目，并赋值
        TProject project = new TProject();
        BeanUtils.copyProperties(projectVo,project);
        //设置时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        project.setCreatedate(format);
        //设置项目状态
        project.setStatus(auth.getCode() +"");
        //保存到数据库
        System.out.println(projectMapper);
        projectMapper.insertSelective(project);
        //2.拿到项目ID
        Integer projectId = project.getId();
        String headerImage = projectVo.getHeaderImage();

        TProjectImages images = new TProjectImages(null,projectId,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        //保存图片--头图
        projectImagesMapper.insertSelective(images);
        List<String> detailsImage = projectVo.getDetailsImage();
        if (!CollectionUtils.isEmpty(detailsImage)) {
            for (String string : detailsImage) {
                TProjectImages img = new TProjectImages(null, projectId, string, ProjectImageTypeEnume.DETAILS.getCode());
                projectImagesMapper.insertSelective(img);
            }
        }
        //3、保存项目的标签信息
        List<Integer> tagids = projectVo.getTagids();
        if (!CollectionUtils.isEmpty(tagids)) {
            for (Integer tagid : tagids) {
                TProjectTag tProjectTag = new TProjectTag(null, projectId, tagid);
                projectTagMapper.insertSelective(tProjectTag);
            }
        }
        //4、保存项目的分类信息
        List<Integer> typeids = projectVo.getTypeids();
        if (!CollectionUtils.isEmpty(typeids)) {
            for (Integer tid : typeids) {
                TProjectType tProjectType = new TProjectType(null, projectId, tid);
                projectTypeMapper.insertSelective(tProjectType);
            }
        }
        //5、保存回报信息
        List<TReturn> returns = projectVo.getProjectReturns();
        //设置项目的id
        if (!CollectionUtils.isEmpty(returns)) {
            for (TReturn tReturn : returns) {
                tReturn.setProjectid(projectId);
                tReturnMapper.insertSelective(tReturn);
            }
        }
        //6、删除临时数据，清空redis
        stringRedisTemplate.delete(ProjectContant.TEMP_PROJECT_PREFIX + projectVo.getProjectToken());

    }

}
