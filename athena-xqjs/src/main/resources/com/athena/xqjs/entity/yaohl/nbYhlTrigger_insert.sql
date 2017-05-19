create or replace trigger XQJS_NBYHL_INSERT
  after insert on Ck_Yaonbhl
  for each row
declare
  -- local variables here
   yhlzt_yjf varchar2(2):='00';
   --yhlzt_bd  varchar2(2):='01';
   yhlzt_yzz varchar2(2):='05';
   yhllb_ax  varchar2(2):='02';
   yhllb_db  varchar2(2):='04';
   dblzt_zxz varchar2(2):='40';
   dblzt_yzx varchar2(2):='50';
   dblzt_yzz varchar2(2):='60';
begin
  --2013-06-27 kong  修改：内部要货令为已交付状态，要货令类别为按需时，不记录 CD模式，并且不记录 MD模式中目的地为消耗点要货数量
  if :new.yaohlzt=yhlzt_yjf and :new.yaohllb=yhllb_ax and :new.yaohllx<>'CD' and (:new.yaohllx='MD'and :new.muddlx='1') =false then
         --更新订单明细
         update xqjs_dingdmx ddmx
                 set ddmx.yijfl=NVL(ddmx.yijfl,0)+:new.yaohsl,
                     ddmx.YAOHLGSYJF = NVL(ddmx.yaohlgsyzz,0)+1,
                     ddmx.editor = :new.editor,
                     ddmx.edit_time = :new.edit_time
                  where     ddmx.id = :new.dingdmxid
                        and
                            ddmx.dingdh = :new.dingdh
                        and
                            ddmx.usercenter = :new.usercenter
                        and
                            ddmx.lingjbh = :new.lingjbh;
          --更新订单零件
          update xqjs_dingdlj ddlj
                 set ddlj.jiaofljddlj = NVL(ddlj.jiaofljddlj,0)+:new.yaohsl,
                     ddlj.yaohlgsljyjf = NVL(ddlj.yaohlgsljyjf,0)+1,
                     ddlj.editor = :new.editor,
                     ddlj.edit_time =:new.edit_time
                 where   ddlj.dingdh = :new.dingdh
                      and
                         ddlj.usercenter = :new.usercenter
                      and
                         ddlj.lingjbh = :new.lingjbh;
            -- 如果目的类型 为 消耗点
           /* if :new.muddlx='1' then
               update ckx_lingjxhd xhd
                     set xhd.jiaofzl=NVL(xhd.jiaofzl,0)+:new.yaohsl,
                         xhd.editor = :new.editor,
                         xhd.edit_time = :new.edit_time
                     where  xhd.usercenter = :new.usercenter
                          and
                            xhd.lingjbh = :new.lingjbh
                          and
                            xhd.xiaohdbh = :new.mudd;
            -- 如果目的类型 为 仓库
            elsif :new.muddlx='2' then
                 update  ckx_lingjck ljck
                      set ljck.yijfzl = :new.yaohsl + NVL(ljck.yijfzl,0),
                          ljck.editor = :new.editor,
                          ljck.edit_time = :new.edit_time
                      where    ljck.usercenter = :new.usercenter
                           and
                               ljck.lingjbh = :new.lingjbh
                           and
                               ljck.cangkbh = :new.cangkbh;

            end if; */
     -- 要货令状态 为 已终止
     --2013-06-27 kong  修改：内部要货令为已终止状态，要货令类别为按需时，不记录 CD模式，并且不记录 MD模式中目的地为消耗点要货数量
     elsif :new.yaohlzt=yhlzt_yzz and :new.yaohllx<>'CD' and (:new.yaohllx='MD'and :new.muddlx='1')=false then
           if :new.yaohllb=yhllb_ax then
                 update xqjs_dingdmx ddmx
                      set ddmx.yizzl = NVL(ddmx.yizzl,0)+:new.yaohsl,
                          ddmx.yaohlgsyzz = NVL(ddmx.yaohlgsyzz,0)+1,
                          ddmx.editor = :new.editor,
                          ddmx.edit_time =:new.edit_time
                      where      ddmx.id = :new.dingdmxid
                              and
                                  ddmx.dingdh = :new.dingdh
                              and
                                  ddmx.usercenter = :new.usercenter
                              and
                                  ddmx.lingjbh = :new.lingjbh;
                   --更新订单零件
                  update xqjs_dingdlj ddlj
                         set ddlj.ZHONGZLJDDLJ = NVL(ddlj.ZHONGZLJDDLJ,0)+:new.yaohsl,
                             ddlj.YAOHLGSLJYZZ = NVL(ddlj.YAOHLGSLJYZZ,0)+1,
                             ddlj.editor = :new.editor,
                             ddlj.edit_time =:new.edit_time
                         where   ddlj.dingdh = :new.dingdh
                              and
                                 ddlj.usercenter = :new.usercenter
                              and
                                 ddlj.lingjbh = :new.lingjbh;
                    -- 如果目的类型 为 消耗点
                   /* if :new.muddlx='1' then
                       update ckx_lingjxhd xhd
                             set xhd.zhongzzl=NVL(xhd.zhongzzl,0)+:new.yaohsl,
                                 xhd.editor = :new.editor,
                                 xhd.edit_time = :new.edit_time
                             where  xhd.usercenter = :new.usercenter
                                  and
                                    xhd.lingjbh = :new.lingjbh
                                  and
                                    xhd.xiaohdbh = :new.mudd;
                    -- 如果目的类型 为 仓库
                    elsif :new.muddlx='2' then
                         update  ckx_lingjck ljck
                              set ljck.DINGDZZZL = :new.yaohsl + NVL(ljck.DINGDZZZL,0),
                                  ljck.editor = :new.editor,
                                  ljck.edit_time = :new.edit_time
                              where    ljck.usercenter = :new.usercenter
                                   and
                                       ljck.lingjbh = :new.lingjbh
                                   and
                                       ljck.cangkbh = :new.cangkbh;

                 end if;   */
            end if;
      -- 如果要货令类别 为 调拨
      elsif :new.yaohllb=yhllb_db then
             -- 判断是否 执行中
             if NVL(:new.jiaofzl,0)<:new.yaohsl and :new.yaohlzt!=yhlzt_yzz then
                update   xqjs_diaobmx dbmx
                     set dbmx.zhuangt=dblzt_zxz,
                         dbmx.zhixsl = :new.jiaofzl,
                         dbmx.editor = :new.editor,
                         dbmx.edit_time = :new.edit_time
                     where  dbmx.usercenter = :new.usercenter
                         and
                            dbmx.lingjbh = :new.lingjbh
                         and
                            dbmx.diaobdh = :new.dingdh
                         and
                            dbmx.cangkbh = :new.cangkbh;
             -- 判断是否 已执行
             elsif NVL(:new.jiaofzl,0)=:new.yaohsl and :new.yaohlzt!=yhlzt_yzz then
                   update   xqjs_diaobmx dbmx
                     set dbmx.zhuangt=dblzt_yzx,
                         dbmx.zhixsl = :new.jiaofzl,
                         dbmx.editor = :new.editor,
                         dbmx.edit_time = :new.edit_time
                     where  dbmx.usercenter = :new.usercenter
                         and
                            dbmx.lingjbh = :new.lingjbh
                         and
                            dbmx.diaobdh = :new.dingdh
                         and
                            dbmx.cangkbh = :new.cangkbh;
            -- 判断是否 已终止
            elsif :new.yaohlzt=yhlzt_yzz then
                  update   xqjs_diaobmx dbmx
                     set dbmx.zhuangt=dblzt_yzz,
                         dbmx.zhixsl = :new.jiaofzl,
                         dbmx.zhongzsl=:new.yaohsl,
                         dbmx.editor = :new.editor,
                         dbmx.edit_time = :new.edit_time
                     where  dbmx.usercenter = :new.usercenter
                         and
                            dbmx.lingjbh = :new.lingjbh
                         and
                            dbmx.diaobdh = :new.dingdh
                         and
                            dbmx.cangkbh = :new.cangkbh;
         end if;
     end if;
end XQJS_NBYHL_INSERT;
