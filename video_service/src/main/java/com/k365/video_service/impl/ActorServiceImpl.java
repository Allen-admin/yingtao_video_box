package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.ActorMapper;
import com.k365.video_base.model.dto.ActorDTO;
import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_base.model.po.Actor;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;
import com.k365.video_service.ActorService;
import com.k365.video_service.VideoActorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class ActorServiceImpl extends ServiceImpl<ActorMapper, Actor> implements ActorService {

    @Autowired
    private VideoActorService videoActorService;

    @Override
    public List<Actor> findActor(ActorDTO actorDTO) {
        return this.list(new QueryWrapper<Actor>().orderByAsc("acronym"));
    }

    @Override
    public boolean add(ActorDTO actorDTO) {
        Actor actor = this.getOne(new QueryWrapper<Actor>().eq("name", actorDTO.getName()));
        if (actor != null)
            throw new ResponsiveException("同名女优已存在！");

        Actor actor2 = Actor.builder().name(actorDTO.getName()).hot(actorDTO.getHot())
                .acronym(actorDTO.getAcronym()).actorIcon(actorDTO.getActorIcon()).build();

        return this.save(actor2);
    }


    @Override
    public void update(ActorDTO actorDTO) {

        Actor actor = this.getById(actorDTO.getId());
        if (actor == null)
            throw new RuntimeException("女优不存在或已被删除");

        Actor actor2 = Actor.builder().id(actorDTO.getId()).name(actorDTO.getName()).hot(actorDTO.getHot())
                .acronym(actorDTO.getAcronym()).actorIcon(actorDTO.getActorIcon()).build();
        this.updateById(actor2);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer id) {
        Actor actor = this.getById(id);
        if (actor == null)
            throw new ResponsiveException("女优不存在或已被删除");

        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            videoActorService.removeByActorId(id);

            if (StringUtils.isNotBlank(actor.getActorIcon())) {
                String[] dfsPath = actor.getActorIcon().split("/", 5);
                FastDFSHelper.deleteFile(dfsPath[3], dfsPath[4]);
            }
        }

    }

    @Override
    public BaseListVO<Actor> findPage(ActorDTO ActorDTO) {
        IPage<Actor> page = this.page(new Page<Actor>().setCurrent(ActorDTO.getPage()).setSize(ActorDTO.getPageSize()),
                new QueryWrapper<Actor>().orderByDesc("hot"));
        return new BaseListVO<Actor>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    @Override
    public List<Actor> findAll() {
        return this.list(new QueryWrapper<Actor>().orderByDesc("hot"));
    }

    @Override
    public List<Actor> findAllAndHot() {
        LinkedList<Actor> all = (LinkedList<Actor>) this.findAll();
        if (!ListUtils.isEmpty(all)) {
            Iterator<Actor> iterator = all.iterator();
            int i = 0;

            while (iterator.hasNext() && i < 8) {
                Actor actor = iterator.next();
                all.add(0, Actor.builder().build());
                ++i;
            }

        }

        return all;
    }

/*@Override
    public Map<String,List<Actor>> findActor(ActorDTO actorDTO) {
        List<Actor> list = this.list(new QueryWrapper<Actor>().orderByAsc("acronym"));
        Map<String,List<Actor>> mapResult = new HashMap<>();
        if(!ListUtils.isEmpty(list)){
           list.forEach(actor -> {
               if(mapResult.get(actor.getAcronym()) == null)
                   mapResult.put( actor.getAcronym(), new ArrayList<>());
               else
                   mapResult.get(actor.getAcronym()).add(actor);
           });
        }
        return mapResult;
    }*/

}
