package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.ActorDTO;
import com.k365.video_base.model.po.Actor;
import com.k365.video_base.model.vo.ActorVO;
import com.k365.video_base.model.vo.BaseListVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface ActorService extends IService<Actor> {


    /**
     * 查询女优信息
     *
     * @param actorDTO
     * @return
     */
    List<Actor> findActor(ActorDTO actorDTO);

    /**
     * 新增女优
     *
     * @param actorDTO
     * @return
     */
    boolean add(ActorDTO actorDTO);

    /**
     * 修改女优信息
     *
     * @param actorDTO
     */
    void update(ActorDTO actorDTO);

    /**
     * 删除女优信息
     *
     * @param id
     */
    void remove(Integer id);

    /**
     * 查询所有女优信息
     */
    List<Actor> findAll();

    /**
     * 分页查询女优信息
     * @param ActorDTO
     * @return
     */
    BaseListVO<Actor> findPage(ActorDTO ActorDTO);


    /**
     * 查询所有女优信息
     */
    List<Actor> findAllAndHot();

    /**
     * 查询女优信息
     *
     * @param actorDTO
     * @return
     */
//    Map<String,List<Actor>> findActor(ActorDTO actorDTO);

}
