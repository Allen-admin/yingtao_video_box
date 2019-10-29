package com.k365.video_base.mybatis.handler;

import com.k365.video_common.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Gavin
 * @date 2019/6/24 11:38
 * @description：继承自BaseTypeHandler<Object> 使用Object是为了让JsonUtil可以处理任意类型
 */
public class JsonTypeHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter,
                                    JdbcType jdbcType) throws SQLException {

        ps.setString(i, JsonUtil.stringify(parameter));
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName)
            throws SQLException {

        return JsonUtil.parse(rs.getString(columnName), Object.class);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

        return JsonUtil.parse(rs.getString(columnIndex), Object.class);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {

        return JsonUtil.parse(cs.getString(columnIndex), Object.class);
    }

}