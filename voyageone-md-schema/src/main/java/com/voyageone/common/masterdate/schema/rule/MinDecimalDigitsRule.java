package com.voyageone.common.masterdate.schema.rule;

import com.voyageone.common.masterdate.schema.enums.RuleTypeEnum;

public class MinDecimalDigitsRule extends Rule implements IntervalRuleInterface {
    public MinDecimalDigitsRule(String value) {
        super(RuleTypeEnum.MIN_DECIMAL_DIGITS_RULE.value(), value, "include");
    }

    public MinDecimalDigitsRule() {
        super.name = RuleTypeEnum.MIN_DECIMAL_DIGITS_RULE.value();
        super.exProperty = "include";
    }

    public MinDecimalDigitsRule(String value, boolean isInclude) {
        super(RuleTypeEnum.MIN_DECIMAL_DIGITS_RULE.value(), value);
        if(isInclude) {
            super.exProperty = "include";
        } else {
            super.exProperty = "not include";
        }

    }

    public void setValueIntervalInclude() {
        super.exProperty = "include";
    }

    public void setValueIntervalNotInclude() {
        super.exProperty = "not include";
    }
}
