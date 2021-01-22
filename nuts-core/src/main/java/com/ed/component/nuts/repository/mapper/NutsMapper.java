package com.ed.component.nuts.repository.mapper;

import com.ed.component.nuts.repository.NutsRetryRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2021/1/20 下午7:03
 */
@Mapper
public interface NutsMapper {

    String COLUMNS = "id, biz_type, biz_no, params, retry_status, retry_count, next_retry_time, create_time, update_time";

    String TABLE_NAME = "nuts_retry_record";


    @Insert("insert into " + TABLE_NAME + " (biz_type, biz_no, params, retry_status, retry_count, next_retry_time, create_time, update_time) " +
            "values (#{bizType},#{bizNo},#{params},#{retryStatus},#{retryCount},#{nextRetryTime},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(NutsRetryRecord po);


    /**
     * update
     */
    @Update("update " + TABLE_NAME + " set retry_status = #{retryStatus},retry_count = retry_count+1,next_retry_time = #{nextRetryTime},update_time=#{now} where id = #{id}")
    int update(@Param("id") Long id,
               @Param("retryStatus") Integer retryStatus,
               @Param("nextRetryTime") Long nextRetryTime,
               @Param("now") Date now);


}
