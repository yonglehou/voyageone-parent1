package com.voyageone.service.impl.cms.sx.rule_parser;

import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.daoext.cms.DictWordDaoExt;

import java.util.*;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public class DictValueFactory {
    private DictWordDaoExt dictWordDaoExt;
    private Map<String, Set<DictWord>> channelDictWordMap;


    public DictValueFactory() {
        this.channelDictWordMap = new HashMap<>();
        dictWordDaoExt = new DictWordDaoExt();
    }

    public void updateMapFromDatabase()
    {
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        List<DictWordBean> dictWordBeanList = dictWordDaoExt.selectDictWords();
        channelDictWordMap.clear();
        for (DictWordBean dictWordBean : dictWordBeanList)
        {
            String orderChannelId = dictWordBean.getOrder_channel_id();
            Set<DictWord> dictWordSet = channelDictWordMap.get(orderChannelId);
            if (dictWordSet == null)
            {
                dictWordSet = new HashSet<>();
                channelDictWordMap.put(orderChannelId, dictWordSet);
            }

            DictWord dictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(dictWordBean.getValue());
            dictWordSet.add(dictWord);
        }
    }


    public Set<DictWord> getDictWords(String orderChannelId)
    {
        if (channelDictWordMap.isEmpty())
        {
            updateMapFromDatabase();
        }
        return channelDictWordMap.get(orderChannelId);
        /*
        Set<DictWord> dictWords = new HashSet<>();

        //主产品图片模板
        RuleExpression mainProductImageTplExpression = new RuleExpression();
        mainProductImageTplExpression.addRuleWord(new TextWord("http://image.sneakerhead.com/is/image/sneakerhead/1200templates?$1200x1200$&$img="));
        DictWord mainProductTpl = new DictWord("主产品图片模板", mainProductImageTplExpression, false);
        dictWords.add(mainProductTpl);

        //主产品图片1
        RuleExpression mainProductExpression = new RuleExpression();
        mainProductExpression.addRuleWord(mainProductTpl);
        CustomWord itWord = new CustomWord(new CustomWordValueGetMainProductImageKey("1"));
        mainProductExpression.addRuleWord(itWord);
        DictWord mainProductImageWord = new DictWord("主产品图片-1", mainProductExpression, true);

        //主产品图片2
        RuleExpression mainProductExpression2 = new RuleExpression();
        mainProductExpression2.addRuleWord(mainProductTpl);
        CustomWord itWord2 = new CustomWord(new CustomWordValueGetMainProductImageKey("2"));
        mainProductExpression2.addRuleWord(itWord2);
        DictWord mainProductImageWord2 = new DictWord("主产品图片-2", mainProductExpression, true);

        dictWords.add(mainProductImageWord);
        dictWords.add(mainProductImageWord2);
        return dictWords;
        */
    }
}