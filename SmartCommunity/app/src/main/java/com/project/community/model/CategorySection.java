package com.project.community.model;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by qizfeng on 17/7/27.
 */

public class CategorySection extends SectionEntity<CategoryModel> {
    private boolean isMore;

    public CategorySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public CategorySection(CategoryModel categoryModel) {
        super(categoryModel);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }


}
