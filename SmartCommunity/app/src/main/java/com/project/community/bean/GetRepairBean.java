package com.project.community.bean;

/**
 * author：fangkai on 2017/10/31 19:07
 * em：617716355@qq.com
 */
public class GetRepairBean {


    /**
     * contact : 01012345678
     * member : {"userNo":"无误","name":"chulain","photo":"/upload/shequ_707m9zVm94851fyS80.crop_photo.jpg","starLevel":2,"orderTotal":2}
     */

    private String contact;
    private MemberBean member;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public static class MemberBean {
        /**
         * userNo : 无误
         * name : chulain
         * photo : /upload/shequ_707m9zVm94851fyS80.crop_photo.jpg
         * starLevel : 2
         * orderTotal : 2
         */

        private String userNo;
        private String name;
        private String photo;
        private int starLevel;
        private int orderTotal;

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getStarLevel() {
            return starLevel;
        }

        public void setStarLevel(int starLevel) {
            this.starLevel = starLevel;
        }

        public int getOrderTotal() {
            return orderTotal;
        }

        public void setOrderTotal(int orderTotal) {
            this.orderTotal = orderTotal;
        }
    }
}
