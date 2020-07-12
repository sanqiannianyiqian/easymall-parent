package cn.tedu.seckill.dao;

import com.jt.common.pojo.Seckill;
import com.jt.common.pojo.Success;

import java.util.List;

/**
 * @author Mangmang
 * @date 2020/2/29 9:17
 */
public interface SeckillDao {
    List<Seckill> selectAll();

    Seckill selectOne(String seckillId);

    int decrSeckillNum(Long seckillId);

    void insertSuccess(Success suc);

    List<Success> selectSuccess(Long seckillId);
}
