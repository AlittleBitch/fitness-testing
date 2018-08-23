package org.alittlebitch.fitness.tcm.dao;

import org.alittlebitch.fitness.tcm.bean.Analysis;
import org.alittlebitch.fitness.tcm.bean.Question;
import org.alittlebitch.fitness.tcm.enums.Determination;
import org.alittlebitch.fitness.tcm.enums.SomatoType;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author ShawnShoper
 * @date 2018/8/17 0:46
 */
@Mapper
public interface TestingDao {
    @Select("select count(0) from testing_question")
    long testCount();

    @Select("select question,id,somato_type,create_time,sort_type from testing_question order by somato_type asc")
    List<Question> findQuestion();

    @Insert("insert into testing_question (question,somato_type) values (#{question},#{somatoType})")
    int saveQuestion(@Param("question") String question, @Param("somatoType") SomatoType somatoType);

    @Insert("insert into testing_score_record (id,yanginsufficiency,yindeficiency,faintphysical,phlegmdampness,dampnessheat,bloodstasis,tebing,qistagnation,mildphysical,name,phone,sex,age,address,create_time) VALUES (#{id},#{yanginsufficiency},#{yindeficiency},#{faintphysical},#{phlegmdampness},#{dampnessheat},#{bloodstasis},#{tebing},#{qistagnation},#{mildphysical},#{name},#{phone},#{sex},#{age},#{address},SYSDATE());")
    int saveUserResult(@Param("id") String id, @Param("yanginsufficiency") double yanginsufficiency, @Param("yindeficiency") double yindeficiency, @Param("faintphysical") double faintphysical, @Param("phlegmdampness") double phlegmdampness, @Param("dampnessheat") double dampnessheat, @Param("bloodstasis") double bloodstasis, @Param("tebing") double tebing, @Param("qistagnation") double qistagnation, @Param("mildphysical") double mildphysical, @Param("name") String name, @Param("phone") String phone, @Param("sex") String sex, @Param("age") int age, @Param("address") String address);

    @Select("select id,yanginsufficiency,yindeficiency,faintphysical,phlegmdampness,dampnessheat,bloodstasis,tebing,qistagnation,mildphysical,name,phone,sex,age,address from testing_score_record where id = #{id};")
    Map<String, Object> queryResult(@Param("id") String id);

    @Select("select * from testing_analysis")
    List<Analysis> queryAllAnalysis();

    @Insert("insert into (yanginsufficiency,yindeficiency,faintphysical,phlegmdampness,dampnessheat,bloodstasis,tebing,qistagnation,mildphysical) values (#{yanginsufficiency},#{yindeficiency},#{faintphysical},#{phlegmdampness},#{dampnessheat},#{bloodstasis},#{tebing},#{qistagnation},#{mildphysical})")
    int saveUnAnalysisData(@Param("yanginsufficiency") Determination yanginsufficiency, @Param("yindeficiency") Determination yindeficiency, @Param("faintphysical") Determination faintphysical, @Param("phlegmdampness") Determination phlegmdampness, @Param("dampnessheat") Determination dampnessheat, @Param("bloodstasis") Determination bloodstasis, @Param("tebing") Determination tebing, @Param("qistagnation") Determination qistagnation, @Param("mildphysical") Determination mildphysical);

    @Select("${sql}")
    List<Analysis> getAnalysis(@Param("sql") String sql);

    @Select("select count(1) from testing_analysis where name = #{unscrambleName}")
    int existsUnscramble(@Param("unscrambleName") String unscrambleName);

    @Insert("insert into (name,yanginsufficiency,yindeficiency,faintphysical,phlegmdampness,dampnessheat,bloodstasis,tebing,qistagnation,mildphysical) values (#{name},#{yanginsufficiency},#{yindeficiency},#{faintphysical},#{phlegmdampness},#{dampnessheat},#{bloodstasis},#{tebing},#{qistagnation},#{mildphysical})")
    int saveUnscramble(@Param("name") String name, @Param("yanginsufficiency") Determination yanginsufficiency, @Param("yindeficiency") Determination yindeficiency, @Param("faintphysical") Determination faintphysical, @Param("phlegmdampness") Determination phlegmdampness, @Param("dampnessheat") Determination dampnessheat, @Param("bloodstasis") Determination bloodstasis, @Param("tebing") Determination tebing, @Param("qistagnation") Determination qistagnation, @Param("mildphysical") Determination mildphysical);

    @Delete("delete from testing_analysis where id = #{id}")
    int deleteUnscramble(@Param("id") int id);

    @Select("select * from testing_unanalysis limit #{offset},#{pageSize}")
    List<Analysis> getUnanalysis(@Param("offset") int offset, @Param("pageSize") int pageSize);
}
