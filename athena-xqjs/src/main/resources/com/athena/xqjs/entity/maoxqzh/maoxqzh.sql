--毛需求转换存储过程
--WL
--2012-05-09
create or replace procedure xqjs_maoxqzh is
       laiy varchar2(3):='';--需求来源
       mxqresult number:=0;--毛需求结果
       clztsql varchar2(100):='';--处理状态更新语句
  begin 
    begin
    --查询来源为CLV的数据,如果未报错表示有来源为CLV数据,则对来源为CLV的数据进行清除
    select ly into laiy from in_maoxqbc where ly ='CLV' and clzt='0' and rownum < 2;  
       delete xqjs_maoxqmx where xuqbc in (select xuqbc from xqjs_maoxq  where xuqly='CLV');
       delete xqjs_maoxq where xuqly ='CLV';
       commit;
    exception when no_data_found then --如果报no_data_found异常,即没有查询到来源为CLV数据,则不进行任何处理,正常转换数据
      DBMS_OUTPUT.PUT_LINE('CLV查询没有数据');
    end;
    for maoxqbc in (select banc,ly,cf_date,beiz,c_flag from in_maoxqbc where clzt = '0') loop--循环毛需求版次表,,去处理状态为0,即未处理的数据
       insert into xqjs_maoxq(xuqbc,xuqcfsj,xuqly,beiz,shengxbz,creator,create_time,editor,edit_time,active) values
       (maoxqbc.banc,to_date(maoxqbc.cf_date,'yyyyMMdd'),maoxqbc.ly,maoxqbc.beiz,'1','sys',current_timestamp,'sys',current_timestamp,'1');
       commit;
       if maoxqbc.ly = 'CLV' then --如果来源为CLV,进行时间判断
         if to_number(to_char(sysdate,'hh24')) <12 then --如果当前时间在中午12点之前,则取拆分日期为昨天的毛需求
          mxqresult := xqjs_clvmxq(maoxqbc.banc,to_char(sysdate-1,'yyyyMMdd'));--毛需求转换,返回1则表示转换成功
         else--如果在中午12点之后,则取今天的毛需求
           mxqresult := xqjs_clvmxq(maoxqbc.banc,to_char(sysdate,'yyyyMMdd'));
         end if;
       else--来源不为CLV
        mxqresult := xqjs_ddbmxq(maoxqbc.banc);
         end if;
        clztsql :='update in_maoxqbc set clzt =''1'' where banc = '''|| maoxqbc.banc ||'''';--处理完毕更新状态为1,即已处理
        EXECUTE IMMEDIATE clztsql;
    end loop;
  end xqjs_maoxqzh;
