create or replace trigger XQJS_WBYHL_INSERT
  after insert on ck_yaohl 
  for each row 
declare
  -- local variables here
  yhlzt_yjf varchar2(2):='00';
  --yhlzt_bd varchar2(2):='01';
  yhlzt_yzz varchar2(2):='05';
begin
  if :new.yaohlzt=yhlzt_yjf then 
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
            /*if :new.muddlx='1' then
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
                                                      
            end if;*/ 
     -- 要货令状态 为 已终止       
     elsif :new.yaohlzt=yhlzt_yzz  then
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
              /*if :new.muddlx='1' then
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
                                                      
            end if;*/                 
                                               
     end if;
end XQJS_WBYHL_INSERT;