package com.lszyhb.basicclass;

/**
 * Created by kkk8199 on 12/6/17.
 */

public  enum  GenVCodeType {
        REGIEST(10),
        ACCOUNT_FIND(20);

        private int type;

        private GenVCodeType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static GenVCodeType type(int type) {
            if (type == REGIEST.type) {
                return GenVCodeType.REGIEST;
            } else
                return GenVCodeType.ACCOUNT_FIND;
        }
    }
