package com.yango.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yango.apis.wemedia.IWemediaClient;
import com.yango.common.constants.UserConstants;
import com.yango.model.common.dtos.PageResponseResult;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.common.enums.AppHttpCodeEnum;
import com.yango.model.user.dtos.AuthDto;
import com.yango.model.user.pojos.ApUser;
import com.yango.model.user.pojos.ApUserRealname;
import com.yango.model.wemedia.pojos.WmUser;
import com.yango.user.mapper.ApUserMapper;
import com.yango.user.mapper.ApUserRealnameMapper;
import com.yango.user.service.ApUserRealnameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * ClassName: ApUserRealnameServiceImpl
 * Package: com.yango.user.service.impl
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-20:10
 */
@Service
@Transactional
@Slf4j
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {
    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private ApUserMapper apUserMapper;
    @Override
    public ResponseResult loadListByStatus(AuthDto dto) {
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        IPage page = new Page(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealname> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(dto.getStatus() != null,ApUserRealname::getStatus,dto.getStatus());
        page(page,queryWrapper);

        ResponseResult result = new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    @Override
    public ResponseResult updateStatus(AuthDto dto, Short status) {
        if (dto == null || dto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUserRealname apUserRealname = new ApUserRealname();
        apUserRealname.setId(dto.getId());
        apUserRealname.setStatus(status);
        apUserRealname.setUpdatedTime(new Date());
        if (StringUtils.isNotBlank(dto.getMsg())){
            apUserRealname.setReason(dto.getMsg());
        }
        updateById(apUserRealname);

        if (status.equals(UserConstants.PASS_AUTH)){
            ResponseResult responseResult = createWmUserAndAuthor(dto);
            if (responseResult != null) {
                return responseResult;
            }
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private ResponseResult createWmUserAndAuthor(AuthDto dto) {
        Integer userRealnameId = dto.getId();
        ApUserRealname apUserRealname = getById(userRealnameId);
        if (apUserRealname == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //查询app端用户信息
        Integer userId = apUserRealname.getUserId();
        ApUser apUser = apUserMapper.selectById(userId);
        if(apUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //创建自媒体账户
        WmUser wmUser = wemediaClient.findWmUserByName(apUser.getName());
        //同名应该可以存在?代码逻辑有误
        if(wmUser == null){
            wmUser = new WmUser();
            wmUser.setApUserId(apUser.getId());
            wmUser.setCreatedTime(new Date());
            wmUser.setName(apUser.getName());
            wmUser.setPassword(apUser.getPassword());
            wmUser.setSalt(apUser.getSalt());
            wmUser.setPhone(apUser.getPhone());
            wmUser.setStatus(9);
            wemediaClient.saveWmUser(wmUser);
        }
        apUser.setFlag((short)1);
        apUserMapper.updateById(apUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        //return null;
    }

}
