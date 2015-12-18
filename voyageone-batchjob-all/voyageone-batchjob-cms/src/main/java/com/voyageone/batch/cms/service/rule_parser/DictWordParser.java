package com.voyageone.batch.cms.service.rule_parser;

import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.ims.rule_expression.WordType;

import java.util.Set;

/**
 * Created by Leo on 15-6-18.
 */
public class DictWordParser {
     private String order_channel_id;
     private DictValueFactory dictValueFactory;

     public DictWordParser(String order_channel_id) {
          this.order_channel_id = order_channel_id;
          dictValueFactory = new DictValueFactory();
     }

     public RuleExpression parse(RuleWord ruleWord)
     {
          if (!WordType.DICT.equals(ruleWord.getWordType()))
          {
               return null;
          }
          else
          {
               DictWord dictWord = (DictWord) ruleWord;
               Set<DictWord> dictWords = dictValueFactory.getDictWords(order_channel_id);
               RuleExpression ruleExpression = null;
               if (dictWords != null)
               {
                    for (DictWord iterDictWord : dictWords)
                    {
                         if (iterDictWord.getName().equals(dictWord.getName()))
                         {
                              ruleExpression = iterDictWord.getExpression();
                              break;
                         }
                    }
               }
               if (ruleExpression == null || dictWords == null)
               {
                    System.out.println("Can't parse ruleWord: " + ruleWord + ", for not find this dict value");
                    return  null;
               }

               return ruleExpression;
          }
     }

     public DictWord parseToDefineDict(RuleWord ruleWord)
     {
          if (!WordType.DICT.equals(ruleWord.getWordType()))
          {
               return null;
          }
          else
          {
               DictWord dictWord = (DictWord) ruleWord;
               Set<DictWord> dictWords = dictValueFactory.getDictWords(order_channel_id);
               if (dictWords != null)
               {
                    for (DictWord iterDictWord : dictWords)
                    {
                         if (iterDictWord.getName().equals(dictWord.getName()))
                         {
                              return iterDictWord;
                         }
                    }
               }
          }
          return  null;
     }

     public String getOrder_channel_id() {
          return order_channel_id;
     }

     public void setOrder_channel_id(String order_channel_id) {
          this.order_channel_id = order_channel_id;
     }
}
