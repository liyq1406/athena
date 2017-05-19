create or replace function check_value(dingdlx in varchar2) return Integer is
acount Integer;

type curtype is ref cursor;--游标定义
  cur_Cus curtype;
  str_sql varchar2(500);--拼接sql预计
  p_table xqjs_maoxqhzpc%ROWTYPE;--pp模式行数据格式定义
  s_table xqjs_maoxqhzsc%ROWTYPE;--ps模式行数据格式定义
  j_table xqjs_maoxqhzjc%ROWTYPE;--pj模式行数据格式定义
  message varchar2(500);--详细信息字符串
  TYPE gongysfe_re IS RECORD(--供应商份额零时表数据存储格式
    lingjh     VARCHAR2(10),
    usercenter VARCHAR2(10),
    fene       number,
    jihydz     VARCHAR2(10),
    cangkdm   VARCHAR2(10),
    waibghms  VARCHAR2(10)) ;
  g_table gongysfe_re;
begin
  acount :=0;
  if dingdlx = 'PP' then--判断是否是pp模式
    str_sql := 'select *
                from (select t.lingjbh,
                             t.usercenter,
                             sum(t.gongysfe) as fene,
                             t.jihydz,
                             t.cangkdm,
                             t.waibghms
                        from xqjs_maoxqhzpc t
                       group by t.lingjbh, t.usercenter,t.jihydz,t.cangkdm,t.waibghms) a
               where a.fene <> 1 or a.fene is null' ;--查询同一用户中心下同一零件的供应商份额的和是否不等于100
    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO g_table;
      EXIT WHEN cur_Cus%NOTFOUND;
      insert into xqjs_yicbj
        (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)--存在供应商份额不为100的零件，将信息插入到异常报警表
      values
        ('31',
        g_table.lingjh,
         g_table.usercenter,
         '100',
         g_table.usercenter || '用户中心下的号码为' || g_table.lingjh ||
         '的零件的供应商份额不足100%',
         g_table.jihydz,
         to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
      delete from xqjs_maoxqhzpc t--从pp模式毛需求汇总_参考系表中剔除供应商份额不为100的数据
       where t.lingjbh = g_table.lingjh
         and t.usercenter = g_table.usercenter
         and t.cangkdm = g_table.cangkdm;
         acount := acount+1;
    end loop;

    str_sql := 'select * from xqjs_maoxqhzpc  order by id';--打开pp模式毛需求汇总_参考系表游标
    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO p_table;
    EXIT WHEN cur_Cus%NOTFOUND;
      if p_table.zhizlx is null then--当制造路线为空时
        insert into xqjs_yicbj--将信息插入到异常报警表中
          (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
        values
          ('31',
          p_table.lingjbh,
           p_table.usercenter,
           '100',
           p_table.usercenter || '用户中心下的号码为' || p_table.lingjbh ||
           '的零件制作路径为空',
           p_table.jihydz,
           to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
           delete from xqjs_maoxqhzpc t--从pp模式毛需求汇总_参考系表中剔除制造路线为空的数据
       where t.id = p_table.id;
         acount := acount+1;
      else--如果制造路线不为空则，则判断其他相关参考系的参数是否为空
        select decode(d.cangkdm, null, '仓库编号为空+') ||
               decode(d.dinghcj, null, '订货车间为空+') ||
               decode(d.anqkc, null, '安全库存为空+') ||
               decode(d.kuc, null, '库存为空+') ||
               decode(d.gongysdm, null, '供应商编号为空+') ||
               decode(d.fayzq, null, '发运周期为空+') ||
               decode(d.beihzq, null, '备货周期为空+') ||
               decode(d.gongysfe, null, '供应商份额为空+') ||
               decode(d.tiaozjsz, null, '调整计算值为空+') ||
               decode(d.uabzlx, null, 'ua包装类型为空+') ||
               decode(d.uabzuclx, null, 'ua包装中uc类型为空+') ||
               decode(d.uabzucsl, null, 'ua包装中uc包装个数为空+') ||
               decode(d.uabzucrl, null, 'ua包装中uc包装容量为空+') ||
               decode(d.ziyhqrq, null, '资源获取日期为空+') ||
               decode(d.waibghms, null, '外部供货模式为空+') ||
               decode(d.yugsfqz, null, '预告是否取整为空+') ||
               decode(d.shifylkc, null, '是否依赖库存为空+') ||
               decode(d.shifylaqkc, null, '是否依赖安全库存为空+') ||
               decode(d.shifyldjf, null, '是否依赖待交付为空+') ||
               decode(d.shifyldxh, null, '是否依赖待消耗为空+') ||
               decode(d.jihydz, null, '计划员代码为空+') ||
               decode(d.lujdm, null, '路径代码为空+') ||
               decode(d.dingdlj, null, '订单累积为空+') ||
               decode(d.jiaoflj, null, '交付累积为空+')
          into message
          from xqjs_maoxqhzpc d
         where d.id = p_table.id;
        if message is not null then--当message不为空时
          insert into xqjs_yicbj--错误信息插入到异常报警表
            (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
          values
            ('31',p_table.lingjbh,
             p_table.usercenter,
             '200',
             p_table.usercenter || '用户中心下的号码为' || p_table.lingjbh ||
             '的零件以下参数为空:' || message,
             p_table.jihydz,
             to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
              delete from xqjs_maoxqhzpc t--从pp模式毛需求汇总_参考系表中剔除参数为空的数据
       where t.id = p_table.id;
         acount := acount+1;
        end if;
      end if;
    end loop;
  elsif dingdlx = 'PS' then--判断是否是ps模式
    str_sql := 'select *
                from (select t.lingjbh,
                             t.usercenter,
                             sum(t.gongysfe) as fene,
                             t.jihyz,
                             t.cangkdm,
                             t.waibghms
                        from xqjs_maoxqhzsc t
                       group by t.lingjbh, t.usercenter, t.jihyz,t.cangkdm,t.waibghms) a
               where a.fene <> 1 or a.fene is null';--查询同一用户中心下同一零件的供应商份额的和是否不等于100

    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO g_table;

     EXIT WHEN cur_Cus%NOTFOUND;
      insert into xqjs_yicbj
        (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)--存在供应商份额不为100的零件，将信息插入到异常报警表
      values
        ('31',
        g_table.lingjh,
         g_table.usercenter,
         '100',
         g_table.usercenter || '用户中心下的号码为' || g_table.lingjh ||
         '的供应商份额不足100%',
         g_table.jihydz,
         to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
      delete from XQJS_MAOXQHZSC t--从ps模式毛需求汇总_参考系表中剔除供应商份额不为100的数据
       where t.lingjbh = g_table.lingjh
         and t.usercenter = g_table.usercenter
         and t.cangkdm = g_table.cangkdm;
         acount := acount+1;
    end loop;

    str_sql := 'select * from xqjs_maoxqhzsc order by id';--打开ps模式毛需求汇总_参考系表游标
    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO s_table;
     EXIT WHEN cur_Cus%NOTFOUND;
      if s_table.zhizlx is null then--当制造路线为空时
        insert into xqjs_yicbj--将信息插入到异常报警表中
          (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
        values
          ('31',
          s_table.lingjbh,
           s_table.usercenter,
           '100',
           s_table.usercenter || '用户中心下的号码为' || s_table.lingjbh ||
           '的零件制作路径为空',
           s_table.jihyz,
           to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
           delete from xqjs_maoxqhzsc t--从ps模式毛需求汇总_参考系表中剔除制造路线为空的数据
       where t.lingjbh = s_table.lingjbh
         and t.usercenter = s_table.usercenter
         and t.jihyz = s_table.jihyz;
         acount := acount+1;
      else--如果制造路线不为空则，则判断其他相关参考系的参数是否为空
        select decode(d.cangkdm, null, '仓库编号为空+') ||
               decode(d.dinghcj, null, '订货车间为空+') ||
               decode(d.anqkc, null, '安全库存为空+') ||
               decode(d.kuc, null, '库存为空+') ||
               decode(d.gongysdm, null, '供应商编号为空+') ||
               decode(d.fayzq, null, '发运周期为空+') ||
               decode(d.beihzq, null, '备货周期为空+') ||
               decode(d.gongysfe, null, '供应商份额为空+') ||
               decode(d.tiaozjsz, null, '调整计算值为空+') ||
               decode(d.uabzlx, null, 'ua包装类型为空+') ||
               decode(d.uabzuclx, null, 'ua包装中uc类型为空+') ||
               decode(d.uabzucsl, null, 'ua包装中uc包装个数为空+') ||
               decode(d.uabzucrl, null, 'ua包装中uc包装容量为空+') ||
               decode(d.ziyhqrq, null, '资源获取日期为空+') ||
               decode(d.waibghms, null, '外部供货模式为空+') ||
                decode(d.yugsfqz, null, '预告是否取整为空+') ||
               decode(d.shifylkc, null, '是否依赖库存为空+') ||
               decode(d.shifylaqkc, null, '是否依赖安全库存为空+') ||
               decode(d.shifyldjf, null, '是否依赖待交付为空+') ||
               decode(d.shifyldxh, null, '是否依赖待消耗为空+') ||
               decode(d.jihyz, null, '计划员代码为空+') ||
               decode(d.lujdm, null, '路径代码为空+') ||
               decode(d.dingdlj, null, '订单累积为空+') ||
               decode(d.jiaoflj, null, '交付累积为空+')
          into message
          from xqjs_maoxqhzsc d
         where d.id = s_table.id;
        if message is not null then--当message不为空时
          insert into xqjs_yicbj--错误信息插入到异常报警表
            (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
          values
            ('31',
            s_table.lingjbh,
             s_table.usercenter,
             '200',
             s_table.usercenter || '用户中心下的号码为' || s_table.lingjbh ||
             '的零件以下参数为空:' || message,
             s_table.jihyz,
             to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
             delete from xqjs_maoxqhzsc t--从ps模式毛需求汇总_参考系表中剔除参数为空的数据
       where t.id = s_table.id;
         acount := acount+1;
        end if;
      end if;
    end loop;
  elsif dingdlx = 'PJ' then--如果是pj模式
     str_sql := 'select *
                from (select t.lingjbh,
                             t.usercenter,
                             sum(t.gongysfe) as fene,
                             t.jihyz,
                             t.cangkdm,
                             t.waibghms
                        from xqjs_maoxqhzjc t
                       group by t.lingjbh, t.usercenter,t.jihyz,t.cangkdm,t.waibghms) a
               where a.fene <> 1 or a.fene is null';--查找供应商份额不为100的数据
    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO g_table;
     EXIT WHEN cur_Cus%NOTFOUND;
      insert into xqjs_yicbj--将供应商份额不为100的错误信息插入到异常报警表
        (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
      values
        ('31',
        g_table.lingjh,
         g_table.usercenter,
         '100',
         g_table.usercenter || '用户中心下的号码为' || g_table.lingjh ||
         '的供应商份额不足100%',
         g_table.jihydz,
         to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
      delete from xqjs_maoxqhzjc t--从pj模式毛需求汇总_参考系表中剔除供应商份额不为100的数据
       where t.lingjbh = g_table.lingjh
         and t.usercenter = g_table.usercenter
         and t.cangkdm = g_table.cangkdm;
         acount := acount+1;
    end loop;

    str_sql := 'select * from  xqjs_maoxqhzjc   order by id';--打开pj模式毛需求汇总_参考系表游标
    open cur_Cus for str_sql;
    loop
    FETCH cur_Cus
      INTO j_table;
     EXIT WHEN cur_Cus%NOTFOUND;
      if j_table.zhizlx is null then--如果制造路径为空
        insert into xqjs_yicbj--将错误信息插入到异常报警表
          (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
        values
          ('31',
          j_table.lingjbh,
           j_table.usercenter,
           '100',
           j_table.usercenter || '用户中心下的号码为' || j_table.lingjbh ||
           '的零件制作路径为空',
           j_table.jihyz,
            to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
           delete from xqjs_maoxqhzjc t--从pj模式毛需求汇总_参考系表中剔除制造路径为空的数据
       where t.id = j_table.id;
         acount := acount+1;
      else--如果制造路径不为空
        select decode(d.cangkdm, null, '仓库编号为空+') ||--检查其他参考系相关参数是否为空
               decode(d.dinghcj, null, '订货车间为空+') ||
               decode(d.anqkc, null, '安全库存为空+') ||
               decode(d.kuc, null, '库存为空+') ||
               decode(d.gongysdm, null, '供应商编号为空+') ||
               decode(d.fayzq, null, '发运周期为空+') ||
               decode(d.beihzq, null, '备货周期为空+') ||
               decode(d.gongysfe, null, '供应商份额为空+') ||
               decode(d.tiaozjsz, null, '调整计算值为空+') ||
               decode(d.uabzlx, null, 'ua包装类型为空+') ||
               decode(d.uabzuclx, null, 'ua包装中uc类型为空+') ||
               decode(d.uabzucsl, null, 'ua包装中uc包装个数为空+') ||
               decode(d.uabzucrl, null, 'ua包装中uc包装容量为空+') ||
               decode(d.ziyhqrq, null, '资源获取日期为空+') ||
               decode(d.waibghms, null, '外部供货模式为空+') ||
                decode(d.yugsfqz, null, '预告是否取整为空+') ||
               decode(d.shifylkc, null, '是否依赖库存为空+') ||
               decode(d.shifylaqkc, null, '是否依赖安全库存为空+') ||
               decode(d.shifyldjf, null, '是否依赖待交付为空+') ||
               decode(d.shifyldxh, null, '是否依赖待消耗为空+') ||
               decode(d.jihyz, null, '计划员代码为空+') ||
               decode(d.lujdm, null, '路径代码为空+') ||
               decode(d.dingdlj, null, '订单累积为空+') ||
               decode(d.jiaoflj, null, '交付累积为空+')
          into message
          from xqjs_maoxqhzjc d
         where d.id = j_table.id;
        if message is not null then--如果message不为空
          insert into xqjs_yicbj--将错误信息插入到异常报警表
            (jismk,lingjbh, usercenter, cuowlx, cuowxxxx, jihyz,CREATE_TIME)
          values
            ('31',
            j_table.lingjbh,
             j_table.usercenter,
             '200',
             j_table.usercenter || '用户中心下的号码为' || j_table.lingjbh ||
             '的零件以下参数为空:' || message,
             j_table.jihyz,
              to_timestamp(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff'),'yyyy-mm-dd hh24:mi:ss.ff'));
             delete from xqjs_maoxqhzjc t--从pj模式毛需求汇总_参考系表中剔参数为空的数据
       where t.id = j_table.id;
         acount := acount+1;
        end if;
      end if;
    end loop;
  end if;
return acount;
end check_value;
