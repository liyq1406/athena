--保存异常报警函数
--WL
--2012-05-09
create or replace function xqjs_ycbj(ljbh in varchar2,usercenter in varchar2,cwlx in varchar2,cwxxxx in varchar2) return number
       is
       ycbjresult number(1):=0;--返回值
       jhysql varchar2(100):='';--计划员查询SQL
       jhy varchar2(3):='';--计划员
begin
    --根据零件编号,用户中心查询计划员
    jhysql :='select jihy from ckx_lingj where usercenter = '''|| usercenter ||''' and lingjbh = '''|| ljbh ||'''';
    begin
    EXECUTE IMMEDIATE jhysql into jhy;
    exception when no_data_found then --如果报no_data_found异常保存异常报警表
    jhy := 'sys';--如果查询不到计划员,则给默认值sys
    end;
    --保存异常报警表
    insert into xqjs_yicbj(jismk,lingjbh,usercenter,cuowlx,cuowxxxx,jihyz,jihydm) values('10',ljbh,usercenter,cwlx,cwxxxx,jhy,'sys');
    commit;
    ycbjresult := 1;
    return ycbjresult;
end xqjs_ycbj;
