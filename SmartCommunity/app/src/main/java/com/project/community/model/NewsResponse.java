package com.project.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipingfang on 17/7/13.
 */

public class NewsResponse implements Serializable {
    private static final long serialVersionUID = 6753210234564872868L;
    public List<NewsModel> newslist = new ArrayList<>();

    @Override
    public String toString() {
        return "NewsResponse{" +
                "newslist=" + newslist +
                '}';
    }
}
