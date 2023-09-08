package com.yango.apis.wemedia;

import com.yango.model.common.dtos.ResponseResult;
import com.yango.model.wemedia.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: IWemediaClient
 * Package: com.yango.apis.wemedia
 * Description:
 *
 * @Author HuangXuSen
 * @Create 2023/9/1-20:26
 */
@FeignClient("leadnews-wemedia")
public interface IWemediaClient {

    @GetMapping("/api/v1/user/findByName/{name}")
    WmUser findWmUserByName(@PathVariable("name") String name);

    @PostMapping("/api/v1/wm_user/save")
    ResponseResult saveWmUser(@RequestBody WmUser wmUser);

    @GetMapping("/api/v1/channel/list")
    ResponseResult getChannels();

    @GetMapping("/api/v1/allSensitives")
    ResponseResult getAllSensitive();

}
