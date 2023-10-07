package com.wakedata.common.chatgpt;


import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.wakedata.common.chatgpt.config.ChatGptProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * @Desc
 * @Author focus
 * @Date 2021/12/13
 */
@Slf4j
public class Sample16kUnintTest {

    ChatGptStreamClient chatGptStreamClient ;

    @Before
    public void setup(){
        ChatGptProperties properties = new ChatGptProperties();
        properties.setApiKey(ChatConstants.GPT_KEY);
        properties.setProxyApiHost(ChatConstants.PROXY_URL);
        chatGptStreamClient = new ChatGptStreamClient(properties);
    }
    @Test
    public void creditGrants() {
        CreditGrantsResponse creditGrantsResponse = chatGptStreamClient.buildInstance().creditGrants();
        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
    }

    @Test
    public void completionsChat3() {
        UnintTestEventSourceListener eventSourceListener = new UnintTestEventSourceListener();
        Completion q = Completion.builder()
                .prompt(prompt(getAllClass()))
                .stream(true)
                .temperature(0.8)
                .maxTokens(2048)
                .build();
        chatGptStreamClient.buildInstance().streamCompletions(q, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completionsChat16k() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ConsoleEventSourceListenerV2 eventSourceListener = new ConsoleEventSourceListenerV2(countDownLatch);

        Message message = Message.builder().role(Message.Role.USER).content(prompt(getTest())).build();

        ChatCompletion q = ChatCompletion
                .builder()
                .messages(Arrays.asList(message))
                .model(ChatCompletion.Model.GPT_4.getName()).maxTokens(2000)
                .build();
        chatGptStreamClient.buildInstance().streamChatCompletion(q, eventSourceListener);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String prompt(String input){
        return "标题： 使用Mockito，JUnit 生成单元测试\n" +
                "背景： 为了提高编码质量，减少逻辑编码错误\n" +
                "限制：  \n" +
                " 1、使用java 语言\n" +
                " 2、移除多余的引用import" +
                " 3、覆盖函数内所有代码的逻辑,覆盖函数的所有场景\n" +
                " 4、使用testCase输出\n" +
                " 5、若是在代码中没有出现的属性请不要进行初始化赋值 \n" +
                " 6、只输出java的代码部分\n" +
                "\t\n" +
                "输入数据：\n" + input;
    }

    private String getAllClass(){
        return "package com.wakedt.visual.domain.account.account;\n" +
                "\n" +
                "import cn.hutool.core.util.ObjectUtil;\n" +
                "import com.wakedata.common.core.exception.BizException;\n" +
                "import com.wakedt.visual.domain.account.account.accountmodify.AccountModifyCmd;\n" +
                "import com.wakedt.visual.domain.account.account.accountpasswordreset.AccountPasswordResetCmd;\n" +
                "import com.wakedt.visual.domain.account.account.accountpasswordresetsendemail.AccountEmailSendCmd;\n" +
                "import com.wakedt.visual.domain.account.account.accountpasswordupdate.AccountPasswordUpdateCmd;\n" +
                "import com.wakedt.visual.domain.account.account.accountremove.AccountDeleteCmd;\n" +
                "import com.wakedt.visual.domain.account.account.login.AccountLoginCmd;\n" +
                "\n" +
                "/**\n" +
                " * 用户的唯一标识，用于平台登录-聚合根能力\n" +
                " */\n" +
                "public class Account extends AbstractAccount {\n" +
                "\n" +
                "    @Override\n" +
                "    public void checkLoginPass(String password){\n" +
                "        if (ObjectUtil.notEqual(this.getPassword(), password)) {\n" +
                "            throw new BizException(\"账号名或密码不正确,操作失败\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void checkUuidPass(String uuid){\n" +
                "        if (ObjectUtil.notEqual(this.getUuid(), uuid)){\n" +
                "            throw new BizException(\"重置密码唯一标识不正确\");\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public void accountModify(AccountModifyCmd updateCmd) {\n" +
                "        this.setId(updateCmd.getId());\n" +
                "        this.setDescription(updateCmd.getDescription());\n" +
                "        this.setUserName(updateCmd.getUserName());\n" +
                "        this.setAccountNo(updateCmd.getAccountNo());\n" +
                "        this.setPassword(updateCmd.getNewPassword());\n" +
                "        this.setIcon(updateCmd.getIcon());\n" +
                "    }\n" +
                "\n" +
                "    public void accountRemove(AccountDeleteCmd removeCmd) {\n" +
                "        this.setId(removeCmd.getId());\n" +
                "    }\n" +
                "\n" +
                "    public void accountPasswordResetSendEmail(AccountEmailSendCmd updateCmd) {\n" +
                "        this.setAccountNo(updateCmd.getAccountNo());\n" +
                "    }\n" +
                "\n" +
                "    public void login(AccountLoginCmd updateCmd) {\n" +
                "        // 验证密码是否正确\n" +
                "        checkLoginPass(updateCmd.getPassword());\n" +
                "    }\n" +
                "\n" +
                "    public void accountPasswordReset(AccountPasswordResetCmd updateCmd) {\n" +
                "        // 验证重置密码唯一标识是否正确\n" +
                "        checkUuidPass(updateCmd.getUuid());\n" +
                "        this.setUuid(updateCmd.getUuid());\n" +
                "        this.setAccountNo(updateCmd.getAccountNo());\n" +
                "        this.setPassword(updateCmd.getNewPassword());\n" +
                "    }\n" +
                "\n" +
                "    public void accountPasswordUpdate(AccountPasswordUpdateCmd updateCmd) {\n" +
                "        // 验证密码是否正确\n" +
                "        checkLoginPass(updateCmd.getOldPassword());\n" +
                "        this.setId(updateCmd.getId());\n" +
                "        this.setPassword(updateCmd.getNewPassword());\n" +
                "    }\n" +
                "    /** 用户ID */\n" +
                "    private Long id;\n" +
                "\n" +
                "    /** 用户登录的唯一标识，目前以用户邮箱作为账号 */\n" +
                "    private String accountNo;\n" +
                "\n" +
                "    /** 用户名 */\n" +
                "    private String userName;\n" +
                "\n" +
                "    /** 密码 */\n" +
                "    private String password;\n" +
                "\n" +
                "    /** 用户头像 */\n" +
                "    private String icon;\n" +
                "\n" +
                "    /** 描述 */\n" +
                "    private String description;\n" +
                "\n" +
                "    /** 用户重置密码时携带的唯一标识 */\n" +
                "    private String uuid;"+
                "}";
    }

    private String getTest(){
        return "package com.wakedata.wd.permission.auditlog.api;\n" +
                "\n" +
                "import com.alibaba.fastjson.JSONObject;\n" +
                "import com.wakedata.common.core.dto.PageResultDTO;\n" +
                "import com.wakedata.common.core.dto.ResultDTO;\n" +
                "import com.wakedata.common.core.global.GlobalCommonConfig;\n" +
                "import com.wakedata.wd.permission.auditlog.dto.AuditLogDTO;\n" +
                "import com.wakedata.wd.permission.auditlog.dto.AuditLogInfo;\n" +
                "import com.wakedata.wd.permission.auditlog.dto.AuditLogVO;\n" +
                "import com.wakedata.wd.permission.auditlog.enums.AuditLogEnum;\n" +
                "import com.wakedata.wd.permission.auditlog.query.AuditLogQuery;\n" +
                "import com.wakedata.wd.permission.auditlog.repository.mapper.AuditLogMapper;\n" +
                "import com.wakedata.wd.permission.auditlog.repository.model.AuditLogDO;\n" +
                "import com.wakedata.wd.permission.common.config.PermissionCommonConfig;\n" +
                "import com.wakedata.wd.permission.common.enums.EsRangeEnum;\n" +
                "import com.wakedata.wd.permission.es.ElasticsearchClientUtil;\n" +
                "import com.wakedata.wd.permission.menu.dto.PermissionFieldDTO;\n" +
                "import com.wakedata.wd.permission.menu.query.PermissionFieldQuery;\n" +
                "import com.wakedata.wd.permission.menu.service.PermissionFieldService;\n" +
                "import com.wakedata.wd.permission.util.ArgumentUtils;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "import org.apache.commons.collections.CollectionUtils;\n" +
                "import org.apache.commons.lang3.StringUtils;\n" +
                "import org.apache.lucene.search.TotalHits;\n" +
                "import org.elasticsearch.action.search.SearchRequest;\n" +
                "import org.elasticsearch.action.search.SearchResponse;\n" +
                "import org.elasticsearch.client.RequestOptions;\n" +
                "import org.elasticsearch.client.RestHighLevelClient;\n" +
                "import org.elasticsearch.index.query.BoolQueryBuilder;\n" +
                "import org.elasticsearch.index.query.QueryBuilder;\n" +
                "import org.elasticsearch.index.query.QueryBuilders;\n" +
                "import org.elasticsearch.index.query.RangeQueryBuilder;\n" +
                "import org.elasticsearch.search.SearchHit;\n" +
                "import org.elasticsearch.search.SearchHits;\n" +
                "import org.elasticsearch.search.builder.SearchSourceBuilder;\n" +
                "import org.elasticsearch.search.sort.SortOrder;\n" +
                "import org.springframework.beans.BeanUtils;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "import javax.annotation.Resource;\n" +
                "import java.io.IOException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "import java.util.TreeMap;\n" +
                "\n" +
                "/**\n" +
                " * @author wenyuanpeng\n" +
                " */\n" +
                "@Slf4j\n" +
                "@Component\n" +
                "@RestController\n" +
                "public class AuditLogRpcServiceImpl implements AuditLogRpcService {\n" +
                "\n" +
                "    @Resource\n" +
                "    private AuditLogMapper auditLogMapper;\n" +
                "\n" +
                "    @Autowired\n" +
                "    private PermissionFieldService permissionFieldService;\n" +
                "\n" +
                "    @Resource\n" +
                "    private GlobalCommonConfig globalCommonConfig;\n" +
                "\n" +
                "    @Override\n" +
                "    public ResultDTO<Boolean> addAuditLog(AuditLogDTO auditLogDTO) {\n" +
                "        ArgumentUtils.notNull(auditLogDTO, \"auditLogDTO\");\n" +
                "        int affectRow = auditLogMapper.insert(toDO(auditLogDTO));\n" +
                "        return ResultDTO.success(affectRow == 1);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public PageResultDTO<List<AuditLogVO>> query(AuditLogQuery query) {\n" +
                "        String auditLogType = globalCommonConfig.getAuditLogType();\n" +
                "        if (StringUtils.isEmpty(auditLogType) || AuditLogEnum.ES.getName().equalsIgnoreCase(auditLogType)) {\n" +
                "            return queryForEs(query);\n" +
                "        }\n" +
                "        return getLog(query);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 获取mysql数据库记录的审计日志\n" +
                "     *\n" +
                "     * @param query 审计日志查询实体\n" +
                "     * @return 审计日志信息\n" +
                "     */\n" +
                "    private PageResultDTO<List<AuditLogVO>> getLog(AuditLogQuery query) {\n" +
                "        PageResultDTO<List<AuditLogVO>> resultDTO = new PageResultDTO();\n" +
                "        resultDTO.setPageSize(query.getPageSize());\n" +
                "        resultDTO.setPageNo(query.getPageNo());\n" +
                "        resultDTO.setTotalCount(0L);\n" +
                "\n" +
                "        int num = auditLogMapper.countQuery(query);\n" +
                "        if (num < 1) {\n" +
                "            return resultDTO;\n" +
                "        }\n" +
                "        List<AuditLogDO> logDOList = auditLogMapper.query(query);\n" +
                "        if (CollectionUtils.isEmpty(logDOList)) {\n" +
                "            return resultDTO;\n" +
                "        }\n" +
                "        List<AuditLogVO> auditLogVOS = new ArrayList<>(logDOList.size());\n" +
                "        for (AuditLogDO auditLogDO : logDOList) {\n" +
                "            AuditLogVO auditLogVO = toVO(auditLogDO);\n" +
                "            if (auditLogVO == null) {\n" +
                "                continue;\n" +
                "            }\n" +
                "            auditLogVOS.add(auditLogVO);\n" +
                "        }\n" +
                "        resultDTO.setData(auditLogVOS);\n" +
                "        resultDTO.setTotalCount((long) num);\n" +
                "        return resultDTO;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 从es查询审计日志\n" +
                "     *\n" +
                "     * @param query 审计日志查询实体\n" +
                "     * @return 审计日志信息\n" +
                "     */\n" +
                "    @Override\n" +
                "    public PageResultDTO<List<AuditLogVO>> queryForEs(AuditLogQuery query) {\n" +
                "\n" +
                "        PageResultDTO resultDTO = new PageResultDTO();\n" +
                "        resultDTO.setPageSize(query.getPageSize());\n" +
                "        resultDTO.setPageNo(query.getPageNo());\n" +
                "        resultDTO.setTotalCount(0L);\n" +
                "        if (query.getPageSize() == 0) {\n" +
                "            return resultDTO;\n" +
                "        }\n" +
                "        RestHighLevelClient client = null;\n" +
                "        try {\n" +
                "            client = ElasticsearchClientUtil.getClient();\n" +
                "            SearchSourceBuilder searchSource = new SearchSourceBuilder();\n" +
                "            // 分页\n" +
                "            searchSource.size(query.getPageSize());\n" +
                "            if (query.getPageNo() > 0) {\n" +
                "                searchSource.from((query.getPageNo() - 1) * query.getPageSize());\n" +
                "            } else {\n" +
                "                searchSource.from(0);\n" +
                "            }\n" +
                "            // 排序\n" +
                "            searchSource.sort(\"@timestamp\", SortOrder.DESC);\n" +
                "            // 条件查询\n" +
                "            searchSource.query(queryBuilder(query));\n" +
                "            SearchRequest searchRequest = new SearchRequest(PermissionCommonConfig.AUDIT_LOG_ES_INDEX);\n" +
                "            searchRequest.source(searchSource);\n" +
                "            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);\n" +
                "\n" +
                "            SearchHits hits = response.getHits();\n" +
                "            TotalHits totalHits = hits.getTotalHits();\n" +
                "            if (totalHits.value == 0L) {\n" +
                "                return resultDTO;\n" +
                "            }\n" +
                "            List<AuditLogVO> auditLogVOList = new ArrayList<>(query.getPageSize());\n" +
                "            for (SearchHit hit : hits) {\n" +
                "                Map<String, Object> sourceAsMap = hit.getSourceAsMap();\n" +
                "                if (sourceAsMap == null) {\n" +
                "                    continue;\n" +
                "                }\n" +
                "                String message = (String) sourceAsMap.get(AuditLogVO.ES_MESSAGE);\n" +
                "                if (StringUtils.isNotEmpty(message)) {\n" +
                "                    AuditLogVO auditLogVO = getAuditLogVO(AuditLogInfo.parse(message));\n" +
                "                    if (auditLogVO != null) {\n" +
                "                        auditLogVO.setId(hit.getId());\n" +
                "                        auditLogVOList.add(auditLogVO);\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "            if (CollectionUtils.isNotEmpty(auditLogVOList)) {\n" +
                "                resultDTO.setTotalCount(Long.valueOf(String.valueOf(totalHits.value)));\n" +
                "                resultDTO.setData(auditLogVOList);\n" +
                "            }\n" +
                "        } catch (Throwable e) {\n" +
                "            log.error(e.getMessage(), e);\n" +
                "        } finally {\n" +
                "            if (client != null) {\n" +
                "                try {\n" +
                "                    client.close();\n" +
                "                } catch (IOException e) {\n" +
                "                    log.error(e.getMessage(), e);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return resultDTO;\n" +
                "    }\n" +
                "\n" +
                "    private AuditLogVO getAuditLogVO(AuditLogInfo auditLogInfo) {\n" +
                "        if (auditLogInfo == null) {\n" +
                "            return null;\n" +
                "        }\n" +
                "        AuditLogVO auditLogVO = new AuditLogVO();\n" +
                "        BeanUtils.copyProperties(auditLogInfo, auditLogVO);\n" +
                "\n" +
                "        try {\n" +
                "            // 参数翻译\n" +
                "            String param = auditLogVO.getParam();\n" +
                "            if (StringUtils.isEmpty(param)) {\n" +
                "                return auditLogVO;\n" +
                "            }\n" +
                "            Map paramMap = JSONObject.parseObject(param, Map.class);\n" +
                "            String urlId = auditLogInfo.getUrlId();\n" +
                "            if (urlId == null) {\n" +
                "                return auditLogVO;\n" +
                "            }\n" +
                "            PermissionFieldQuery query = new PermissionFieldQuery();\n" +
                "            query.setPermissionId(urlId);\n" +
                "            List<PermissionFieldDTO> permissionFieldDTOS = permissionFieldService.query(query);\n" +
                "            if (CollectionUtils.isEmpty(permissionFieldDTOS)) {\n" +
                "                return auditLogVO;\n" +
                "            }\n" +
                "            boolean changeParam = false;\n" +
                "            TreeMap<String, Object> treeMap = new TreeMap<>();\n" +
                "            for (PermissionFieldDTO permissionFieldDTO : permissionFieldDTOS) {\n" +
                "                if (permissionFieldDTO == null) {\n" +
                "                    continue;\n" +
                "                }\n" +
                "                String field = permissionFieldDTO.getField();\n" +
                "                String fieldName = permissionFieldDTO.getFieldName();\n" +
                "                if (StringUtils.isNoneBlank(field, fieldName)) {\n" +
                "                    Object value = paramMap.get(field);\n" +
                "                    if (value == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    changeParam = true;\n" +
                "                    treeMap.put(fieldName, value);\n" +
                "                }\n" +
                "            }\n" +
                "            if (changeParam) {\n" +
                "                auditLogVO.setParam(JSONObject.toJSONString(treeMap));\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(e.getMessage(), e);\n" +
                "        }\n" +
                "        return auditLogVO;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 组装es查询条件\n" +
                "     *\n" +
                "     * @param auditLogQuery\n" +
                "     * @return\n" +
                "     */\n" +
                "    private QueryBuilder queryBuilder(AuditLogQuery auditLogQuery) {\n" +
                "\n" +
                "        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();\n" +
                "\n" +
                "        try {\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getUserName())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_USER_NAME, auditLogQuery.getUserName()));\n" +
                "            }\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getUrl())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_URL, auditLogQuery.getUrl()));\n" +
                "            }\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getPermissionName())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_URL_NAME, auditLogQuery.getPermissionName()));\n" +
                "            }\n" +
                "            if (auditLogQuery.getAppBuId() != null) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_APP_BU_ID, auditLogQuery.getAppBuId()));\n" +
                "            }\n" +
                "            if (auditLogQuery.getTenantId() != null) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_TENANT_ID, auditLogQuery.getTenantId()));\n" +
                "            }\n" +
                "            if (auditLogQuery.getStatus() != null) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_STATUS, auditLogQuery.getStatus()));\n" +
                "            }\n" +
                "            if (auditLogQuery.getUrlId() != null) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_URL_ID, auditLogQuery.getUrlId()));\n" +
                "            }\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getMenuPath())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_MENU_PATH, auditLogQuery.getMenuPath()));\n" +
                "            }\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getResult())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_RESULT, auditLogQuery.getResult()));\n" +
                "            }\n" +
                "            if (StringUtils.isNotEmpty(auditLogQuery.getParam())) {\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_PARAM, auditLogQuery.getParam()));\n" +
                "            }\n" +
                "\n" +
                "            // 拼接参数字段查询\n" +
                "            List<String> paramList = auditLogQuery.getParamList();\n" +
                "            if (CollectionUtils.isNotEmpty(paramList)) {\n" +
                "                for (String param : paramList) {\n" +
                "                    if (StringUtils.isEmpty(param)) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    parseParamStr(param, boolQuery);\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            log.error(e.getMessage(), e);\n" +
                "        }\n" +
                "        return boolQuery;\n" +
                "    }\n" +
                "\n" +
                "    private void parseParamStr(String paramStr, BoolQueryBuilder boolQuery) {\n" +
                "        if (StringUtils.isEmpty(paramStr)) {\n" +
                "            return;\n" +
                "        }\n" +
                "        String[] params = paramStr.split(\",\");\n" +
                "        if (params.length != 3) {\n" +
                "            return;\n" +
                "        }\n" +
                "        String key = params[0];\n" +
                "        String operator = params[1];\n" +
                "        String value = params[2];\n" +
                "        if (StringUtils.isAnyBlank(key, operator, value)) {\n" +
                "            return;\n" +
                "        }\n" +
                "        EsRangeEnum operatorEnum = EsRangeEnum.parse(operator);\n" +
                "        if (operatorEnum == null) {\n" +
                "            boolQuery.must(QueryBuilders.matchQuery(key, value));\n" +
                "            return;\n" +
                "        }\n" +
                "        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(AuditLogVO.ES_PARAM_PREFIX + \".\" + key);\n" +
                "        switch (operatorEnum) {\n" +
                "            case eq:\n" +
                "                boolQuery.must(QueryBuilders.matchPhraseQuery(key, value));\n" +
                "                break;\n" +
                "            case gt:\n" +
                "                boolQuery.must(rangeQuery.gt(value));\n" +
                "                break;\n" +
                "            case gte:\n" +
                "                boolQuery.must(rangeQuery.gte(value));\n" +
                "                break;\n" +
                "            case lt:\n" +
                "                boolQuery.must(rangeQuery.lt(value));\n" +
                "                break;\n" +
                "            case lte:\n" +
                "                boolQuery.must(rangeQuery.lte(value));\n" +
                "                break;\n" +
                "            case like:\n" +
                "                boolQuery.must(QueryBuilders.matchQuery(AuditLogVO.ES_PARAM_PREFIX + \".\" + key, value));\n" +
                "                break;\n" +
                "            default:\n" +
                "                break;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    private AuditLogVO toVO(AuditLogDO auditLogDO) {\n" +
                "        if (auditLogDO == null) {\n" +
                "            return null;\n" +
                "        }\n" +
                "        AuditLogVO auditLogVO = null;\n" +
                "        String auditLogDOInfo = auditLogDO.getInfo();\n" +
                "        if (StringUtils.isNotEmpty(auditLogDOInfo)) {\n" +
                "            try {\n" +
                "                AuditLogInfo auditLogInfo = JSONObject.parseObject(auditLogDOInfo, AuditLogInfo.class);\n" +
                "                if (auditLogInfo == null) {\n" +
                "                    return null;\n" +
                "                }\n" +
                "                auditLogVO = new AuditLogVO();\n" +
                "                auditLogVO.setId(String.valueOf(auditLogDO.getId()));\n" +
                "                BeanUtils.copyProperties(auditLogInfo, auditLogVO);\n" +
                "                auditLogVO.setCreateTime(auditLogDO.getCreateTime());\n" +
                "                auditLogVO.setUrlName(auditLogDO.getPermissionName());\n" +
                "            } catch (Exception e) {\n" +
                "                log.error(e.getMessage(), e);\n" +
                "            }\n" +
                "        }\n" +
                "        return auditLogVO;\n" +
                "    }\n" +
                "\n" +
                "    private AuditLogDO toDO(AuditLogDTO auditLogDTO) {\n" +
                "        if (auditLogDTO == null) {\n" +
                "            return new AuditLogDO();\n" +
                "        }\n" +
                "        AuditLogDO auditLogDO = new AuditLogDO();\n" +
                "        BeanUtils.copyProperties(auditLogDTO, auditLogDO);\n" +
                "        return auditLogDO;\n" +
                "    }\n" +
                "}\n";
    }

}
