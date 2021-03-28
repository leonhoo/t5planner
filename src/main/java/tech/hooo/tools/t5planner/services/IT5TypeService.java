package tech.hooo.tools.t5planner.services;

import tech.hooo.tools.t5planner.models.*;

import java.util.List;

public interface IT5TypeService {
    /**
     * 根据品牌获取T5信息
     *
     * @param param
     * @return
     */
    List<T5LightTube> lightTubeList(T5TypeRequest param);

    /**
     * 根据品牌获取连接器信息
     *
     * @param param
     * @return
     */
    T5Adapter t5Adapter(T5TypeRequest param);

    /**
     * 获取品牌列表
     *
     * @param param
     * @return
     */
    List<BrandModel> brandList(PageRequest param);
}
