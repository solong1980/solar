package com.lszyhb.basicclass;

import java.util.List;

/**
 * Created by kkk8199 on 2/11/18.
 */

public class ShowPageProject <C> extends ShowAbt  {

        private Integer start = 0;
        private Integer pageNum = 1;
        private Integer count = 1000;
        private Integer total = 0;

        private C c;// 条件
        private List<ShowProject> t;// 结果

        public ShowPageProject() {
            super();
        }

        public ShowPageProject(C c) {
            super();
            this.c = c;
        }

        public ShowPageProject(int pageNum, int count) {
            super();
            this.pageNum = pageNum;
            this.count = count;
            this.start = count * (pageNum - 1);
        }

        public Integer getStart() {
            this.start = count * (pageNum - 1);
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getPageNum() {
            return pageNum;
        }

        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }

        public int getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }

        public List<ShowProject> getT() {
            return t;
        }

        public void setT(List<ShowProject> t) {
            this.t = t;
        }

}
