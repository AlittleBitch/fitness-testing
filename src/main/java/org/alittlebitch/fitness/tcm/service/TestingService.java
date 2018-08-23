package org.alittlebitch.fitness.tcm.service;

import org.alittlebitch.fitness.dto.*;
import org.alittlebitch.fitness.tcm.bean.Analysis;
import org.alittlebitch.fitness.tcm.bean.ResultRecord;
import org.alittlebitch.fitness.tcm.bean.SomatoInfo;
import org.alittlebitch.fitness.tcm.dao.TestingDao;
import org.alittlebitch.fitness.tcm.enums.Determination;
import org.alittlebitch.fitness.tcm.enums.SomatoType;
import org.shoper.commons.core.CloneUtil;
import org.shoper.commons.core.StringUtil;
import org.shoper.commons.core.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.alittlebitch.fitness.tcm.enums.SomatoType.*;

/**
 * @author ShawnShoper
 * @date 2018/8/16 17:32
 */
@Service
public class TestingService {
    @Autowired
    TestingDao testingDao;

    public long testCount() {
        return testingDao.testCount();
    }

    public TcmQuestionResp question() {
        List<TcmQuestion> tcmQuestions = TcmQuestionBuilder.create(testingDao.findQuestion());
        TcmQuestionResp tcmQuestionResp = new TcmQuestionResp();
        tcmQuestionResp.setTotal(tcmQuestions.size());
        tcmQuestionResp.setQuestions(tcmQuestions);
        return tcmQuestionResp;
    }

    public Object submit(TcmRequest tcmRequest) {
        List<TcmResult> tcmResults = tcmRequest.getTcmResult();

        UserInfo userInfo = tcmRequest.getUserInfo();

        Map<SomatoType, Double> somatoTypeScoreMap = tcmResults.stream().collect(Collectors.groupingBy(TcmResult::getSomatoType, Collectors.summingDouble(TcmResult::getScore)));

        Map<SomatoType, Long> somatoTypeCountMap = tcmResults.stream().collect(Collectors.groupingBy(TcmResult::getSomatoType, Collectors.counting()));
        ResultRecord resultRecord = new ResultRecord();
        List<SomatoInfo> somatoInfos = new ArrayList<>();
        somatoTypeScoreMap.entrySet().stream().forEach(e -> {
            SomatoType key = e.getKey();
            Double value = e.getValue();
            Long count = somatoTypeCountMap.get(key);
            Double percent = (100 * ((value - count) / (count * 4)));
            somatoInfos.add(new SomatoInfo(e.getKey().getTitle(), e.getKey().name(), percent));
        });
//        Optional<SomatoInfo> first = somatoInfos.stream().filter(e -> e.getTypeValue().equals(SomatoType.MILDPHYSICAL.name())).findFirst();
//        SomatoInfo mildPhysical = first.get();
//        somatoInfos.remove(mildPhysical);
//        Map<String, Float> biasedMap = new HashMap<>();
//        somatoInfos.stream().forEach(e -> biasedMap.put(e.getTypeName(), e.getPercent()));
//        List<String> gte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 40 && e.getValue() >= 30).map(Map.Entry::getKey).collect(toList());
//        List<String> lte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 30).map(Map.Entry::getKey).collect(toList());
//        List<String> gte40 = biasedMap.entrySet().stream().filter(e -> e.getValue() >= 40).map(Map.Entry::getKey).collect(toList());
//        if (!gte40.isEmpty()) {
//            biasedMap.entrySet().stream().forEach(e -> {
//                if (e.getValue() >= 40) {
//                    String key = e.getKey();
//                    somatoInfos.stream().filter(b -> b.getTypeName().equalsIgnoreCase(key)).forEach(c -> c.setActive(true));
//                }
//            });
//        } else {
//            if (!lte30.isEmpty() && mildPhysical.getPercent() >= 60)
//                mildPhysical.setActive(true);
//            else if (!gte30.isEmpty() && mildPhysical.getPercent() >= 60) {
//                mildPhysical.setActive(true);
//                gte30.stream().forEach(e -> somatoInfos.stream().filter(s -> s.getTypeName().equals(e)).forEach(b -> b.setActive(true)));
//            } else
//                mildPhysical.setActive(false);
//            biasedMap.entrySet().stream().forEach(e -> {
//                if (e.getValue() > 30) {
//                    String key = e.getKey();
//                    somatoInfos.stream().filter(b -> b.getTypeName().equals(key)).forEach(c -> c.setActive(true));
//                }
//            });
//        }
        String id = UUID.randomUUID().toString();
//        somatoInfos.add(mildPhysical);
        Map<String, Double> score = new HashMap<>();
        somatoInfos.stream().forEach(e -> score.put(e.getTypeValue(), e.getPercent()));
        testingDao.saveUserResult(id, score.get(YANGINSUFFICIENCY.name()), score.get(YINDEFICIENCY.name()), score.get(FAINTPHYSICAL.name()), score.get(PHLEGMDAMPNESS.name()), score.get(DAMPNESSHEAT.name()), score.get(BLOODSTASIS.name()), score.get(TEBING.name()), score.get(QISTAGNATION.name()), score.get(MILDPHYSICAL.name()), userInfo.getName(), userInfo.getPhone(), userInfo.getSex(), userInfo.getAge(), userInfo.getAddress());
//        resultRecord.setTestResult(somatoInfos);
//        resultRecord.setUserInfo(userInfo);
//        resultRecord.setId();
        //先提取平和质的属性
//        Map<SomatoType, Determination> mildMap = new HashMap<>();
//        mildMap.put(SomatoType.MILDPHYSICAL, null);
//        Double mildScore = somatoTypeScoreMap.get(SomatoType.MILDPHYSICAL);
//        //去除平和质属性
//        Map<SomatoType, Double> map = CloneUtil.depthClone(somatoTypeScoreMap, Map.class);
//        map.remove(SomatoType.MILDPHYSICAL);
//        //提取出分数30-39的偏颇
//        Map<SomatoType, Determination> biasedMap = new HashMap<>();
//        map.entrySet().forEach((e) -> {
//            if (e.getValue() < 30)
//                biasedMap.put(e.getKey(), Determination.NO);
//            else if (e.getValue() >= 30 && e.getValue() < 40)
//                biasedMap.put(e.getKey(), Determination.MAYBE);
//            else
//                biasedMap.put(e.getKey(), Determination.YES);
//        });
//        if (mildScore >= 60 && biasedMap.values().contains(Determination.NO))
//            mildMap.put(SomatoType.MILDPHYSICAL, Determination.YES);
//        else if (mildScore >= 60 && biasedMap.values().contains(Determination.YES))
//            mildMap.put(SomatoType.MILDPHYSICAL, Determination.MAYBE);
//        else
//            mildMap.put(SomatoType.MILDPHYSICAL, Determination.NO);
//
//        //构建result record
//        ResultRecord resultRecord = new ResultRecord();
//        resultRecord.setScore(somatoTypeScoreMap);
//        if (mildMap.get(SomatoType.MILDPHYSICAL) != Determination.NO) {
//            //主要体征为平和质
//            resultRecord.setDetermination(mildMap.get(SomatoType.MILDPHYSICAL));
//        }
        //提取偏颇体质为
//        List<SomatoType> biaseds = biasedMap.entrySet().stream().filter(e -> e.getValue() != Determination.NO).map(e -> e.getKey()).collect(Collectors.toList());
//        if (!biaseds.isEmpty()) {
//            List<Map.Entry<SomatoType, Determination>> anyYesBiased = biasedMap.entrySet().stream()
//                    .filter(e -> e.getValue().equals(Determination.NO)
//                    )
//                    .collect(Collectors.toList());
//            if (!anyYesBiased.isEmpty()) {
//                resultRecord.setDetermination(Determination.YES);
//                resultRecord.setBiaseds(anyYesBiased.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
//            } else {
//                List<Map.Entry<SomatoType, Determination>> anyMaybeBiased = biasedMap.entrySet().stream()
//                        .filter(e -> e.getValue().equals(Determination.MAYBE)
//                        )
//                        .collect(Collectors.toList());
//                if (!anyMaybeBiased.isEmpty()) {
//                    resultRecord.setDetermination(Determination.MAYBE);
//                    resultRecord.setBiaseds(anyMaybeBiased.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
//                }
//            }
//        }


        //solution A
//        Map<SomatoType, Integer> collect = tcmResult.stream()
//                .collect(groupingBy(TcmResult::getSomatoType,
//                        mapping(TcmResult::getScore,
//                                mapping(List::stream,
//                                        mapping(e -> e.mapToInt(Integer::intValue),
//                                                summingInt(IntStream::sum))))));

        //solution B
//        Map<SomatoType, Integer> group = new HashMap<>(tcmResult.size());
//        for (TcmResult result : tcmResult) {
//            Integer totalSocre = result.getScore().stream().collect(summingInt(Integer::intValue));
//            group.put(result.getSomatoType(), totalSocre);
//        }
        //solution now
//        Map<SomatoType, Double> somatoGroupScore = tcmResult.stream().
//                collect(groupingBy(TcmResult::getSomatoType,
//                        collectingAndThen(
//                                mapping(TcmResult::getScore, toList()),
//                                list -> list
//                                        .stream()
//                                        .flatMap(Collection::stream)
//                                        .mapToDouble(e -> e).sum())));
//        Map<SomatoType, TcmResult> tcmResultMap = tcmResult.stream()
//                .collect(Collectors.toMap(TcmResult::getSomatoType, tr -> tr));
        //转化分数=[（原始分-条目数）/（条目数×4）] ×100
//        somatoGroupScore.keySet().stream().forEach(e -> {
//            TcmResult tcmResult1 = tcmResultMap.get(e);
//            double size = tcmResult1.getScore().size();
//            Double totalScore = somatoGroupScore.get(e);
//            double finalScore = 100 * ((totalScore - size) / (size * 4));
//            somatoGroupScore.put(e, finalScore);
//        });


//        resultRecord.setUserInfo(userInfo);
//        ResultRecordRespBuild.create(resultRecord);
        return id;
    }

    public void saveQuestion(TcmRequest tcmRequest) {
        if (Objects.isNull(tcmRequest))
            throw new IllegalArgumentException("参数不能为空");
        if (testingDao.saveQuestion(tcmRequest.getQuestion(), tcmRequest.getSomatoType()) != 1) {
            throw new IllegalStateException("保存失败");
        }
    }

    public Object result(String id) throws SystemException {
        Map<String, Object> resultRecordMap = testingDao.queryResult(id);
        ResultRecord resultRecord = new ResultRecord();
        resultRecord.setId((String) resultRecordMap.get("id"));
        UserInfo userInfo = new UserInfo();
        userInfo.setAddress((String) resultRecordMap.get("address"));
        userInfo.setAge((Integer) resultRecordMap.get("age"));
        userInfo.setName((String) resultRecordMap.get("name"));
        userInfo.setPhone((String) resultRecordMap.get("phone"));
        userInfo.setSex((String) resultRecordMap.get("sex"));
        Double yanginsufficiency = (Double) resultRecordMap.get("yanginsufficiency");
        Double yindeficiency = (Double) resultRecordMap.get("yindeficiency");
        Double faintphysical = (Double) resultRecordMap.get("faintphysical");
        Double phlegmdampness = (Double) resultRecordMap.get("phlegmdampness");
        Double dampnessheat = (Double) resultRecordMap.get("dampnessheat");
        Double bloodstasis = (Double) resultRecordMap.get("bloodstasis");
        Double tebing = (Double) resultRecordMap.get("tebing");
        Double qistagnation = (Double) resultRecordMap.get("qistagnation");
        Double mildphysical = (Double) resultRecordMap.get("mildphysical");
        List<SomatoInfo> somatoInfos = new ArrayList<>(9);
        somatoInfos.add(new SomatoInfo(YANGINSUFFICIENCY.getTitle(), YANGINSUFFICIENCY.name(), yanginsufficiency));
        somatoInfos.add(new SomatoInfo(YINDEFICIENCY.getTitle(), YINDEFICIENCY.name(), yindeficiency));
        somatoInfos.add(new SomatoInfo(FAINTPHYSICAL.getTitle(), FAINTPHYSICAL.name(), faintphysical));
        somatoInfos.add(new SomatoInfo(PHLEGMDAMPNESS.getTitle(), PHLEGMDAMPNESS.name(), phlegmdampness));
        somatoInfos.add(new SomatoInfo(DAMPNESSHEAT.getTitle(), DAMPNESSHEAT.name(), dampnessheat));
        somatoInfos.add(new SomatoInfo(BLOODSTASIS.getTitle(), BLOODSTASIS.name(), bloodstasis));
        somatoInfos.add(new SomatoInfo(TEBING.getTitle(), TEBING.name(), tebing));
        somatoInfos.add(new SomatoInfo(QISTAGNATION.getTitle(), QISTAGNATION.name(), qistagnation));
        somatoInfos.add(new SomatoInfo(MILDPHYSICAL.getTitle(), MILDPHYSICAL.name(), mildphysical));

        {
            Optional<SomatoInfo> first = somatoInfos.stream().filter(e -> e.getTypeValue().equals(SomatoType.MILDPHYSICAL.name())).findFirst();
            SomatoInfo mildPhysical = first.get();
            somatoInfos.remove(mildPhysical);
            Map<String, Double> biasedMap = new HashMap<>();
            somatoInfos.stream().forEach(e -> biasedMap.put(e.getTypeName(), e.getPercent()));
            List<String> gte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 40 && e.getValue() >= 30).map(Map.Entry::getKey).collect(toList());
            List<String> lte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 30).map(Map.Entry::getKey).collect(toList());
            List<String> gte40 = biasedMap.entrySet().stream().filter(e -> e.getValue() >= 40).map(Map.Entry::getKey).collect(toList());
            if (!gte40.isEmpty()) {
                biasedMap.entrySet().stream().forEach(e -> {
                    if (e.getValue() >= 40) {
                        String key = e.getKey();
                        somatoInfos.stream().filter(b -> b.getTypeName().equalsIgnoreCase(key)).forEach(c -> c.setActive(true));
                    }
                });
            } else {
                if (!lte30.isEmpty() && mildPhysical.getPercent() >= 60)
                    mildPhysical.setActive(true);
                else if (!gte30.isEmpty() && mildPhysical.getPercent() >= 60) {
                    mildPhysical.setActive(true);
                    gte30.stream().forEach(e -> somatoInfos.stream().filter(s -> s.getTypeName().equals(e)).forEach(b -> b.setActive(true)));
                } else
                    mildPhysical.setActive(false);
                biasedMap.entrySet().stream().forEach(e -> {
                    if (e.getValue() > 30) {
                        String key = e.getKey();
                        somatoInfos.stream().filter(b -> b.getTypeName().equals(key)).forEach(c -> c.setActive(true));
                    }
                });
            }
            somatoInfos.add(mildPhysical);
        }


        resultRecord.setTestResult(somatoInfos);
        resultRecord.setUserInfo(userInfo);

        Map<SomatoType, Double> somatoTypeFloatMap = new HashMap<>();
        somatoInfos.stream().forEach(e -> somatoTypeFloatMap.put(SomatoType.valueOf(e.getTypeValue()), e.getPercent()));

        //查询解读
        somatoTypeFloatMap.remove(SomatoType.MILDPHYSICAL);
        Map<SomatoType, Double> biasedMap = CloneUtil.depthClone(somatoTypeFloatMap, Map.class);

        Map<SomatoType, Determination> determinationDeter = new HashMap<>();
        List<SomatoType> gte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 40 && e.getValue() >= 30).map(Map.Entry::getKey).collect(toList());
        List<SomatoType> lte30 = biasedMap.entrySet().stream().filter(e -> e.getValue() < 30).map(Map.Entry::getKey).collect(toList());
        List<SomatoType> gte40 = biasedMap.entrySet().stream().filter(e -> e.getValue() >= 40).map(Map.Entry::getKey).collect(toList());
        if (!gte40.isEmpty()) {
            biasedMap.entrySet().stream().forEach(e -> {
                if (gte40.contains(e.getKey())) {
                    SomatoType key = e.getKey();
                    somatoInfos.stream().filter(b -> b.getTypeValue().equals(key.name())).forEach(c -> determinationDeter.put(SomatoType.valueOf(c.getTypeValue()), Determination.YES));
                }
            });
            determinationDeter.put(SomatoType.MILDPHYSICAL, Determination.NO);
        } else {
            if (!lte30.isEmpty() && somatoTypeFloatMap.get(SomatoType.MILDPHYSICAL) >= 60)
                determinationDeter.put(SomatoType.MILDPHYSICAL, Determination.YES);
            else if (!gte30.isEmpty() && somatoTypeFloatMap.get(SomatoType.MILDPHYSICAL) >= 60) {
                determinationDeter.put(SomatoType.MILDPHYSICAL, Determination.MAYBE);
                gte30.stream().forEach(e -> somatoInfos.stream().filter(s -> s.getTypeName().equals(e)).forEach(b -> determinationDeter.put(SomatoType.valueOf(b.getTypeValue()), Determination.MAYBE)));
            } else
                determinationDeter.put(SomatoType.MILDPHYSICAL, Determination.NO);
        }
        biasedMap.entrySet().stream().forEach(e -> {
            if (!determinationDeter.containsKey(e.getKey())) {
                determinationDeter.put(e.getKey(), Determination.NO);
            }
        });

        //查询数据库所有数据进行内存运算
        List<Analysis> analyses = testingDao.queryAllAnalysis();
//        analyses.get(0).getBloodstasis()
        Optional<String> anyAnalysis = analyses.stream().map(b -> {
            boolean mildphysicalFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.MILDPHYSICAL);
                mildphysicalFlag = judge(b, determination);
            }
            boolean yanginsufficiencyFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.YANGINSUFFICIENCY);
                yanginsufficiencyFlag = judge(b, determination);
            }
            boolean bloodstasisFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.BLOODSTASIS);
                bloodstasisFlag = judge(b, determination);
            }
            boolean dampnessheatFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.DAMPNESSHEAT);
                dampnessheatFlag = judge(b, determination);
            }
            boolean faintphysicalFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.FAINTPHYSICAL);
                faintphysicalFlag = judge(b, determination);
            }
            boolean phlegmdampnessFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.PHLEGMDAMPNESS);
                phlegmdampnessFlag = judge(b, determination);
            }
            boolean qistagnationFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.QISTAGNATION);
                qistagnationFlag = judge(b, determination);
            }
            boolean tebingFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.TEBING);
                tebingFlag = judge(b, determination);
            }
            boolean yindeficiencyFlag = false;
            {
                Determination determination = determinationDeter.get(SomatoType.YINDEFICIENCY);
                yindeficiencyFlag = judge(b, determination);
            }
            if (mildphysicalFlag && yanginsufficiencyFlag && bloodstasisFlag && dampnessheatFlag && faintphysicalFlag && phlegmdampnessFlag && qistagnationFlag && tebingFlag && yindeficiencyFlag)
                return b.getAnalysis();
            else return null;
        }).filter(Objects::nonNull).findAny();
        if (anyAnalysis.isPresent()) {
            //找到合适的解读
            resultRecord.setUnscrambleContent(anyAnalysis.get());
        } else {
            //未找到合适的解读
            resultRecord.setUnscrambleContent("暂无匹配的数据,请等待专家提供！");
            //储存无法匹配的相应数据
            testingDao.saveUnAnalysisData(determinationDeter.get(YANGINSUFFICIENCY), determinationDeter.get(YINDEFICIENCY), determinationDeter.get(FAINTPHYSICAL), determinationDeter.get(PHLEGMDAMPNESS), determinationDeter.get(DAMPNESSHEAT), determinationDeter.get(BLOODSTASIS), determinationDeter.get(TEBING), determinationDeter.get(QISTAGNATION), determinationDeter.get(MILDPHYSICAL));
        }

        return resultRecord;
    }

    private boolean judge(Analysis b, Determination determination) {
        boolean flag = false;
        if (Determination.YES.equals(determination)) {
            if (determination.equals(b.getBloodstasis())) {
                flag = true;
            } else if (Determination.MAYBE.equals(b.getBloodstasis())) {
                flag = true;
            }
        } else if (Determination.MAYBE.equals(determination)) {
            if (Determination.MAYBE.equals(b.getBloodstasis())) flag = true;
        } else {
            if (Determination.NO.equals(b.getBloodstasis())) flag = true;
        }
        return flag;
    }

    public List<Analysis> getUnscramble(String name, int page, int pageSize) {
        if (page == 0)
            page = 1;
        if (pageSize == 0 || pageSize > 50)
            pageSize = 10;
        StringBuilder sql = new StringBuilder();
        sql.append("select * from testing_analysis ");
        if (!StringUtil.isEmpty(name))
            sql.append("where name like '%" + name + "%' ");
        sql.append("limit " + (page - 1) * pageSize + "," + pageSize);

        List<Analysis> list = testingDao.getAnalysis(sql.toString());
        return list;
    }

    public void addUnscramble(UnscrambleRequest unscrambleRequest) {
        if (Objects.isNull(unscrambleRequest) || StringUtil.isEmpty(unscrambleRequest.getUnscrambleName()))
            throw new RuntimeException("参数不能为null");
        if (testingDao.existsUnscramble(unscrambleRequest.getUnscrambleName()) != 0)
            throw new RuntimeException("名字重复");
        Map<SomatoType, Determination> map = new HashMap();
        unscrambleRequest.getAdapterPhysique().forEach(e -> map.put(e.getSomatoType(), e.getIsAccord()));
        if (testingDao.saveUnscramble(unscrambleRequest.getUnscrambleName(),
                map.get(YANGINSUFFICIENCY),
                map.get(YINDEFICIENCY),
                map.get(FAINTPHYSICAL),
                map.get(PHLEGMDAMPNESS),
                map.get(DAMPNESSHEAT),
                map.get(BLOODSTASIS),
                map.get(TEBING),
                map.get(QISTAGNATION),
                map.get(MILDPHYSICAL)
        ) != 1) {
            throw new RuntimeException("添加解读失败");
        }
    }

    public void deleteUnscramble(int id) {
        if (testingDao.deleteUnscramble(id) != 1)
            throw new RuntimeException("删除失败");
    }

    public List<Analysis> getUnanalysis(int page, int pageSize) {
        if (page == 0)
            page = 1;
        if (pageSize == 0 || pageSize > 50)
            pageSize = 10;
        return testingDao.getUnanalysis((page - 1) * pageSize, pageSize);
    }
}
