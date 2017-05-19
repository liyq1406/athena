--CLV毛需求转换函数
--WL
--2012-05-09
create or replace function xqjs_ddbmxq(mxqbanc in varchar2) return number
       is
       zyysql varchar2(100):='';--作用域查询SQL
       zuoyy varchar2(3):='';--作用域
       cxsql varchar2(100):='';--目标产线查询SQL
       countChax number;--目标产线数量
       chax varchar2(5):='';--目标产线
       hbsql varchar2(300):='';--合并查询SQL
       mxqmxid varchar2(32):='';--毛需求明细id
       hpupsql varchar2(100):='';--合并更新SQL
       zqrow ckx_calendar_center%rowtype;--周,周期查询游标
       zqsql varchar2(200):='';--周期处理SQL
       xqdate date:=sysdate;--操作时间
       mxqresult number(1):=0;--返回值
       yyyyMMdd varchar(8):='yyyyMMdd';--日期格式
       yyyy varchar(10):='yyyy-MM-dd';--日期格式
       zzxlsql varchar2(100):='';--制造线路查询SQL
       zzxl varchar2(3):='';--制造线路
       ljdwsql varchar2(100):='';--零件表单位查询SQL
       dw varchar2(3):='';--单位
       dwsql varchar2(100):='';--转换单位查询SQL
       shul number(16,6):=0;--数量
       hsbl number(5,2):=0;--换算比例
begin
  --循环滚动毛需求表,查询来源,车间号(线号前三位),线号,用户中心,拆分日期,版次,零件编号,数量,单位,制造路线,标识,需求日期
    for ly in (select laiy,SUBSTR(c_line_code,0,3) as chejh,c_line_code,c_usercenter,t_start,t_end,banc,lingjbc_componenth,n_num,
      c_manage_unit,c_make_line,c_flag,cf_date from in_gundmxq where banc = mxqbanc) loop
       zyysql := 'select zuoyy  from ckx_xuqly where xuqly = '''|| ly.laiy || ''' and zuoyy = '''|| ly.chejh ||'''';--根据需求来源去查询作用域
       begin
       EXECUTE IMMEDIATE zyysql into zuoyy; --执行SQL获取作用域值
         if ly.chejh = zuoyy then--如果作用域跟车间号相同,则执行,不同的数据过滤
           cxsql :='select count(mubcx) from ckx_chanxhb where usercenter ='''|| ly.c_usercenter ||''' and yuancx = '''|| ly.c_line_code ||'''';
           begin
           EXECUTE IMMEDIATE cxsql into countChax; --执行SQL获取目标产线
           if countChax = 0 then
              chax := ly.c_line_code;
           else
               cxsql :='select mubcx from ckx_chanxhb where usercenter ='''|| ly.c_usercenter ||''' and yuancx = '''|| ly.c_line_code ||'''';
               EXECUTE IMMEDIATE cxsql into chax; --执行SQL获取目标产线
           end if;
           hbsql := 'select id from xqjs_maoxqmx where xuqbc ='''|| ly.banc ||''' and usercenter=''' || ly.c_usercenter ||'''
           and shiycj='''|| ly.chejh || ''' and lingjbh='''|| ly.lingjbc_componenth ||''' and danw = '''|| ly.c_manage_unit ||'''  and zhizlx ='''|| ly.c_make_line || '''
           and xuqrq = to_date('''|| ly.t_start ||''','''||yyyyMMdd||''') and chanx ='''|| chax ||'''';
           begin
           EXECUTE IMMEDIATE hbsql into mxqmxid; --执行SQL获取毛需求明细
           --查询到数据则合并数据,即为更新
           hpupsql :='update xqjs_maoxqmx set xuqsl = (xuqsl + '''|| ly.n_num ||''') where id ='''|| mxqmxid ||'''';
           EXECUTE IMMEDIATE hpupsql; --更新数据
           exception when no_data_found then --如果报no_data_found异常,即没有查询到数据则新增数据
             --周,周期查询SQL
             zqsql :='select * from ckx_calendar_center where usercenter ='''||ly.c_usercenter ||'''and riq =to_char(to_date('''|| ly.t_start || ''','''|| yyyyMMdd ||'''),'''|| yyyy ||''')';
             begin
             EXECUTE IMMEDIATE zqsql into zqrow;--赋值给游标
             --保存数据到需求计算毛需求明细表
             --制造线路转换
             zzxlsql :='select zhizlxx from ckx_zhizlxzh where usercenter = '''|| ly.c_usercenter ||''' and zhizlxy = '''|| ly.c_make_line ||'''';
             begin 
             EXECUTE IMMEDIATE zzxlsql into zzxl;
             --单位换算
             ljdwsql :='select danw from ckx_lingj where usercenter = '''|| ly.c_usercenter ||''' and lingjbh = '''|| ly.lingjbc_componenth ||'''';--查询零件表内单位
             begin 
             EXECUTE IMMEDIATE ljdwsql into dw;
             if dw != ly.c_manage_unit then--如果零件表内单位和接口管理单位不同,进行换算
               --查询换算比例
               dwsql :='select huansbl from ckx_danwhs where usercenter ='''|| ly.c_usercenter ||''' and beihdw = '''|| ly.c_manage_unit ||''' and mubdw = '''|| dw ||'''';
               begin
               EXECUTE IMMEDIATE dwsql into hsbl;
               shul := ly.n_num * hsbl;--新数量等于元数量乘以换算比例
               exception when no_data_found then --如果报no_data_found异常保存异常报警表
               mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','换算比例查询异常');
               end;
             end if;
             exception when no_data_found then --如果报no_data_found异常保存异常报警表
             mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','单位查询异常');
             end;
             insert into xqjs_maoxqmx(id,xuqbc,usercenter,shiycj,chanx,xuqz,xuqrq,lingjbh,xuqsl,danw,zhizlx,xuqsszq,creator,create_time,editor,edit_time,active,xuqksrq,xuqjsrq) values
(maoxqmx_seq.nextval,ly.banc,ly.c_usercenter,ly.chejh,chax,zqrow.nianzx,to_date(ly.t_start,'yyyyMMdd'),ly.lingjbc_componenth,ly.n_num,dw,zzxl,zqrow.nianzq,'sys',xqdate,'sys',xqdate,'1',ly.t_start,ly.t_end);
             mxqresult :=1;--为返回值赋值
             exception when no_data_found then --如果报no_data_found异常保存异常报警表
             mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','制造线路查询异常');
             end;
             exception when no_data_found then --如果报no_data_found异常保存异常报警表
             mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','周,周期查询异常');
            end;
           end;
          exception when no_data_found then --如果报no_data_found异常保存异常报警表
          mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','目标产线查询异常');
          end;
        end if;
       exception when no_data_found then --如果报no_data_found异常保存异常报警表
         mxqresult :=xqjs_ycbj(ly.lingjbc_componenth,ly.c_usercenter,'200','作用域查询异常');
       end;
    end loop;
    commit;
    return mxqresult;
end xqjs_ddbmxq;
