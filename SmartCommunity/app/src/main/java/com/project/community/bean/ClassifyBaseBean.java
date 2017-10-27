package com.project.community.bean;

import java.util.List;

/**
 * author：fangkai on 2017/10/27 11:03
 * em：617716355@qq.com
 */
public class ClassifyBaseBean {


    private List<DictListBean> dictList;

    public List<DictListBean> getDictList() {
        return dictList;
    }

    public void setDictList(List<DictListBean> dictList) {
        this.dictList = dictList;
    }

    public static class DictListBean {
        /**
         * value : 1
         * label : 二手市场
         * type : livelihood_article_category
         * icon :
         */

        private String value;
        private String label;
        private String type;
        private String icon;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
