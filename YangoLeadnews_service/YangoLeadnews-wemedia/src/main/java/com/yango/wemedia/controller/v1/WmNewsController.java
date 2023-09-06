package com.yango.wemedia.controller.v1;

import com.mysql.jdbc.log.Log;
import com.yango.common.constants.WemediaConstants;
import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.dtos.NewsAuthDto;
import com.yango.model.wemedia.dtos.WmNewDto;
import com.yango.model.wemedia.dtos.WmNewsDto;
import com.yango.model.wemedia.dtos.WmNewsPageReqDto;
import com.yango.wemedia.service.WmNewsService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: WmNewsController
 * Package: com.yango.wemedia.controller.v1
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/8/29-11:52
 */
@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {
    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.findList(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto){
        return wmNewsService.submitNews(dto);
    }

    @GetMapping("/one/{id}")
    public ResponseResult getNews(@PathVariable("id")Integer id){
        return wmNewsService.getNewsById(id);
    }

    @GetMapping("/del_news/{id}")
    public ResponseResult delNews(@PathVariable("id")Integer id){
        return wmNewsService.delNewsById(id);
    }

    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewDto dto){
        return wmNewsService.downOrUp(dto);
    }

    @PostMapping("/list_vo")
    public ResponseResult listVo(@RequestBody NewsAuthDto dto){
        return wmNewsService.listVo(dto);
    }

    @GetMapping("/one_vo/{id}")
    public ResponseResult getOneVo(@PathVariable("id")Integer id){
        return wmNewsService.getOneVo(id);
    }

    @PostMapping("/auth_fail")
    public ResponseResult authFail(@RequestBody NewsAuthDto dto){
        return wmNewsService.updateStatus(dto, WemediaConstants.WM_NEWS_AUTH_FAIL);
    }

    @PostMapping("/auth_pass")
    public ResponseResult authPass(@RequestBody NewsAuthDto dto){
        return wmNewsService.updateStatus(dto, WemediaConstants.WM_NEWS_AUTH_PASS);
    }
}
