package com.voyageone.service.enums.cms;

/**
 * Created by dell on 2016/12/27.
 */
public enum OperationLog_Type {
    success(1,"成功"),
    successIncludeFail(2),
    parameterException(3),
    configException(4),
    businessException(5),
    unknownException(6);
    short id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    OperationLog_Type(int id, String name) {
        this.id = (short) id;
        this.name=name;
    }
    OperationLog_Type(int id) {
        this.id = (short) id;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public OperationLog_Type get(int id)
    {
            return   null;
    }
}
