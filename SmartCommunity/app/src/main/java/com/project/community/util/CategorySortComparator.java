package com.project.community.util;

import com.project.community.model.CategorySection;

import java.util.Comparator;

/**
 * Created by qizfeng on 17/8/10.
 */

public class CategorySortComparator implements Comparator<CategorySection> {
    @Override
    public int compare(CategorySection lhs, CategorySection rhs) {
        CategorySection a = lhs;
        CategorySection b = rhs;
        if (a.isHeader || b.isHeader) {
            return 0;
        }
        int state = b.t.state - a.t.state;
//        if (state == 0) {
//            return (a.t.sort - b.t.sort);
//        } else {
//            return (a.t.state - b.t.state);
//        }
        return (a.t.state - b.t.state);
    }
}
